package com.github.utils.io;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;

/**
 * Created by jinwei.li on 2017/8/28 0028.
 */
public class IoUtil {

    /**
     * 关闭
     *
     * @param closeable
     */
    public static void close(Closeable closeable) {
        if (closeable != null)
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
