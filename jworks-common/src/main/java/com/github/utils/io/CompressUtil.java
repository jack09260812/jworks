package com.github.utils.io;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.CRC32;
import java.util.zip.CheckedInputStream;
import java.util.zip.CheckedOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class CompressUtil {

    private static Logger logger = LoggerFactory.getLogger(CompressUtil.class);
    static final int BUFFER = 8192;

    private File zipFile;

    /**
     * 压缩
     *
     * @param destPath
     * @param srcPath
     */
    public static void compress(String destPath, String... srcPath) {
        File zipFile = null;
        ZipOutputStream out = null;
        try {
            zipFile = new File(destPath);
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,
                    new CRC32());
            out = new ZipOutputStream(cos);
            String basedir = "";
            for (int i = 0; i < srcPath.length; i++) {
                compress(new File(srcPath[i]), out, basedir);
            }
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解压
     *
     * @param srcPath
     * @param destPath
     */
    public static void decompress(String srcPath, String destPath) {
        Preconditions.checkNotNull(srcPath, "原路径不能为空");
        Preconditions.checkNotNull(destPath, "目标地址不能为空");
        File zipFile = null;
        File destFile = null;
        ZipInputStream zis = null;
        try {
            zipFile = new File(srcPath);
            //判断文件是否存在
            if (!zipFile.exists()) {
                logger.error("{}文件不存在", srcPath);
                return;
            }
            if (!destPath.endsWith("\\") && !destPath.endsWith("/")) {
                destPath += File.separator;
            }
            destFile = new File(destPath);
            //如果不存在，创建文件夹
            if (!destFile.exists() || !destFile.isDirectory())
                destFile.mkdirs();
            FileInputStream fileInputStream = new FileInputStream(zipFile);
            CheckedInputStream cis = new CheckedInputStream(fileInputStream,
                    new CRC32());
            zis = new ZipInputStream(cis);
            ZipEntry entry = null;
            while ((entry = zis.getNextEntry()) != null) {
                String fileName = entry.getName();
                String fullName = destPath + fileName;
                OutputStream os = null;
                try {
                    // 把解压出来的文件写到指定路径
                    File entryFile = new File(fullName);
                    if (fullName.endsWith("/")) {
                        if (!entryFile.exists())
                            entryFile.mkdirs();
                    } else {
                        //如果父文件夹不存在，创建父文件夹
                        if (!entryFile.getParentFile().exists()) {
                            entryFile.getParentFile().mkdirs();
                        }
                        os = new BufferedOutputStream(new FileOutputStream(
                                entryFile));
                        byte[] buffer = new byte[BUFFER];
                        int len = -1;
                        while ((len = zis.read(buffer)) != -1) {
                            os.write(buffer, 0, len);
                        }
                    }
                } catch (IOException e) {
                    throw new IOException(e);
                } finally {
                    if (os != null) {
                        os.flush();
                        os.close();
                    }
                }
            }
            zis.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * 压缩子目录
     *
     * @param destPath
     * @param srcPath
     */
    public static void compressWithOutDirect(String destPath, String srcPath) {
        File file = new File(srcPath);
        if (!file.exists())
            throw new RuntimeException(srcPath + "不存在！");
        if (!file.isDirectory()) {
            logger.error("{}不是目录", srcPath);
            return;
        }
        File[] files = file.listFiles();
        compress(destPath, files);
    }

    public static void compress(String destPath, File... files) {
        File zipFile = null;
        ZipOutputStream out = null;
        try {
            zipFile = new File(destPath);
            FileOutputStream fileOutputStream = new FileOutputStream(zipFile);
            CheckedOutputStream cos = new CheckedOutputStream(fileOutputStream,
                    new CRC32());
            out = new ZipOutputStream(cos);
            String basedir = "";
            for (int i = 0; i < files.length; i++) {
                compress(files[i], out, basedir);
            }
            out.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static void compress(File file, ZipOutputStream out, String basedir) {
            /* 判断是目录还是文件 */
        if (file.isDirectory()) {
            System.out.println("压缩：" + basedir + file.getName());
            compressDirectory(file, out, basedir);
        } else {
            System.out.println("压缩：" + basedir + file.getName());
            compressFile(file, out, basedir);
        }
    }

    /**
     * 压缩一个目录
     */
    private static void compressDirectory(File dir, ZipOutputStream out, String basedir) {
        if (!dir.exists())
            return;

        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
                /* 递归 */
            compress(files[i], out, basedir + dir.getName() + "/");
        }
    }

    /**
     * 压缩一个文件
     */
    private static void compressFile(File file, ZipOutputStream out, String basedir) {
        if (!file.exists()) {
            return;
        }
        try {
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(file));
            ZipEntry entry = new ZipEntry(basedir + file.getName());
            out.putNextEntry(entry);
            int count;
            byte data[] = new byte[BUFFER];
            while ((count = bis.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            bis.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}