package com.github.utils.bean;

import com.github.utils.Filter;
import com.github.utils.io.FileUtil;
import com.github.utils.io.IoUtil;
import com.github.utils.string.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 类工具类 <br>
 */
public class ClassUtil {

    private static Logger logger = LoggerFactory.getLogger(ClassUtil.class);

    /**
     * {@code null}安全的获取对象类型
     *
     * @param <T> 对象类型
     * @param obj 对象，如果为{@code null} 返回{@code null}
     * @return 对象类型，提供对象如果为{@code null} 返回{@code null}
     */
    public static <T> Class<T> getClass(T obj) {
        return ((null == obj) ? null : (Class<T>) obj.getClass());
    }

    /**
     * 获取类名
     *
     * @param obj      获取类名对象
     * @param isSimple 是否简单类名，如果为true，返回不带包名的类名
     * @return 类名
     */
    public static String getClassName(Object obj, boolean isSimple) {
        if (null == obj) {
            return null;
        }
        final Class<?> clazz = obj.getClass();
        return isSimple ? clazz.getSimpleName() : clazz.getName();
    }

    /**
     * 扫面包路径下满足class过滤器条件的所有class文件，<br>
     *
     * @param packageName
     * @param classFilter class过滤器，过滤掉不需要的class
     * @return 类集合
     */
    public static Set<Class<?>> scanPackage(String packageName, Filter<Class<?>> classFilter) {
        if (StringUtil.isNull(packageName)) {
            return null;
        }
        packageName = packageName.lastIndexOf(".") != packageName.length() - 1 ? packageName + "." : packageName;

        final Set<Class<?>> classes = new HashSet<Class<?>>();
        final Set<String> classPaths = ClassUtil.getClassPaths(packageName);
        for (String classPath : classPaths) {
            try {
                classPath = URLDecoder.decode(classPath, Charset.defaultCharset().name());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            fillClasses(classPath, packageName, classFilter, classes);
        }
        return classes;
    }

    /**
     * 获得ClassPath
     *
     * @param packageName 包名称
     * @return ClassPath路径字符串集合
     */
    public static Set<String> getClassPaths(String packageName) {
        String packagePath = packageName.replace(".", "/");
        Enumeration<URL> resources;
        try {
            resources = Thread.currentThread().getContextClassLoader().getResources(packagePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Set<String> paths = new HashSet<String>();
        while (resources.hasMoreElements()) {
            paths.add(resources.nextElement().getPath());
        }
        return paths;
    }

    private static void fillClasses(String path, String packageName, Filter<Class<?>> classFilter, Set<Class<?>> classes) {
        // 判定给定的路径是否为Jar
        int index = path.lastIndexOf(FileUtil.JAR_PATH_EXT);
        // Jar文件
        path = path.substring(0, index + FileUtil.JAR_FILE_EXT.length()); // 截取jar路径
        path = StringUtil.removePrefix(path, FileUtil.PATH_FILE_PRE); // 去掉文件前缀
        processJarFile(new File(path), packageName, classFilter, classes);
    }

    private static void processJarFile(File file, String packageName, Filter<Class<?>> classFilter, Set<Class<?>> classes) {
        JarFile jarFile = null;
        try {
            jarFile = new JarFile(file);
            for (JarEntry entry : Collections.list(jarFile.entries())) {
                if (isClass(entry.getName())) {
                    final String className = entry.getName().replace("/", ".").replace(FileUtil.CLASS_EXT, "");
                    fillClass(className, packageName, classes, classFilter);
                }
            }
        } catch (Exception e) {
            logger.error("获取失败", e);
        } finally {
            IoUtil.close(jarFile);
        }
    }

    private static void fillClass(String className, String packageName, Set<Class<?>> classes, Filter<Class<?>> classFilter) {
        if (className.startsWith(packageName)) {
            try {
                final Class<?> clazz = Class.forName(className, false, Thread.currentThread().getContextClassLoader());
                if (classFilter == null || classFilter.accept(clazz)) {
                    classes.add(clazz);
                }
            } catch (Exception ex) {
                // Pass Load Error.
            }
        }
    }

    private static boolean isClass(String fileName) {
        return fileName.endsWith(FileUtil.CLASS_EXT);
    }

    /**
     * 扫描指定包路径下所有包含指定注解的类
     *
     * @param packageName     包路径
     * @param annotationClass 注解类
     * @return 类集合
     */
    public static Set<Class<?>> scanPackageByAnnotation(String packageName, final Class<? extends Annotation> annotationClass) {
        return scanPackage(packageName, new Filter<Class<?>>() {
            @Override
            public boolean accept(Class<?> clazz) {
                return clazz.isAnnotationPresent(annotationClass);
            }
        });
    }

    /**
     * 扫描指定包路径下所有指定类或接口的子类或实现类
     *
     * @param packageName 包路径
     * @param superClass  父类或接口
     * @return 类集合
     */
    public static Set<Class<?>> scanPackageBySuper(String packageName, final Class<?> superClass) {
        return scanPackageBySuper(packageName, superClass);
    }

    /**
     * 扫面该包路径下所有class文件
     *
     * @param packageName
     * @return 类集合
     */
    public static Set<Class<?>> scanPackage(String packageName) {
        return scanPackage(packageName, null);
    }

    /**
     * 加载类
     *
     * @param path
     * @param className
     * @return
     */
    public static Class<?> loadClass(String path, String className) {
        try {
            URL url = new URL(path);
            URLClassLoader classLoader = new URLClassLoader(new URL[]{url}, Thread.currentThread()
                    .getContextClassLoader());
            Class<?> cls = (Class<?>) classLoader.loadClass(className);
            return cls;
        } catch (MalformedURLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 加载类并初始化
     *
     * @param <T>       对象类型
     * @param className 类名
     * @return 类
     */
    public static <T> T loadInstance(String path, String className) {
        try {
            return (T) loadClass(path, className).newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}