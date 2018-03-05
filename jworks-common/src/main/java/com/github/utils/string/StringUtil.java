/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.utils.string;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.hash.Hashing;
import com.google.common.io.CharStreams;
import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.CharsetMatch;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.SecureRandom;
import java.util.Map;

public class StringUtil {

    private static final String DES = "DES";
    private static final String KEY = "password";

    /**
     * md5
     *
     * @param source
     * @return
     */
    public static String md5(String source) {
        Preconditions.checkNotNull(source);
        String md5 = Hashing.md5().newHasher().putString(source, Charsets.UTF_8).hash().toString();
        return md5;
    }

    /**
     * des加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encrypt(String data) throws Exception {
        byte[] bt = encrypt(data.getBytes(), KEY.getBytes());
        return new BASE64Encoder().encode(bt);
    }

    /**
     * des解密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String decrypt(String data) throws Exception {
        if (data == null)
            return null;
        BASE64Decoder decoder = new BASE64Decoder();
        byte[] buf = decoder.decodeBuffer(data);
        byte[] bt = decrypt(buf, KEY.getBytes());
        return new String(bt);
    }

    /**
     * des加密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(byte[] data, byte[] key) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance(DES);
        cipher.init(1, securekey, sr);
        return cipher.doFinal(data);
    }

    /**
     * des解密
     *
     * @param data
     * @param key
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] data, byte[] key) throws Exception {
        SecureRandom sr = new SecureRandom();
        DESKeySpec dks = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
        SecretKey securekey = keyFactory.generateSecret(dks);
        Cipher cipher = Cipher.getInstance(DES);
        cipher.init(2, securekey, sr);
        return cipher.doFinal(data);
    }

    /**
     * 分割转化为驼峰形式
     *
     * @param str
     * @param split
     * @return
     */
    public static String humpString(String str, String split) {
        Preconditions.checkNotNull(str);
        if (split == null)
            split = "";
        String[] arr = str.split(split);
        String result = "";
        for (int i = 0; i < arr.length; i++) {
            if (i == 0)
                result = arr[i].toLowerCase();
            else
                result += (arr[i].substring(0, 1).toUpperCase() + arr[i].substring(1).toLowerCase());
        }
        return result;
    }

    /**
     * 分割转化为首字母大写驼峰形式
     *
     * @param str
     * @param split
     * @return
     */
    @SuppressWarnings("all")
    public static String _humpString(String str, String split) {
        Preconditions.checkNotNull(str);
        if (split == null)
            split = "";
        String[] arr = str.split(split);
        String result = "";
        for (int i = 0; i < arr.length; i++) {
            result += (arr[i].substring(0, 1).toUpperCase() + arr[i].substring(1).toLowerCase());
        }
        return result;
    }

    /**
     * 首字母大写
     *
     * @param str
     * @return
     */
    public static String capitalize(String str) {
        if (isNull(str))
            return null;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 判断是否为null和空值
     *
     * @param str
     * @return
     */
    public static boolean isNull(String str) {
        return str == null || str.equals("");
    }

    /**
     * 对象转化为字符串
     *
     * @param object
     * @return
     */
    public static String toString(Object object) {
        return toString(object, null);
    }

    /**
     * 对象转化为字符串
     *
     * @param object
     * @param defaultValue 转换失败默认返回值
     * @return
     */
    public static String toString(Object object, String defaultValue) {
        if (object == null)
            return defaultValue;
        if (object instanceof InputStream)
            try {
                return CharStreams.toString(new InputStreamReader((InputStream) object, "UTF-8"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        if (object instanceof Readable)
            try {
                return CharStreams.toString((Readable) object);
            } catch (IOException e) {
                e.printStackTrace();
            }
        if (object instanceof Map)
            return Joiner.on(",").withKeyValueSeparator(" = ")
                    .join((Map) object);
        if (object instanceof Iterable || object instanceof Object[])
            return Joiner.on(",").skipNulls().join((Iterable) object);
//            return Joiner.on(",").useForNull(defaultValue).join((Iterable) object);
        return JSON.toJSONString(object);
    }


    /**
     * 字符串转16进制
     *
     * @param str
     * @return
     */
    public static String toHexStr(String str) {
        if (isNull(str))
            return null;
        StringBuilder sb = new StringBuilder();
        byte[] src = str.getBytes();
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                sb.append(0);
            }
            sb.append(hv);
        }
        return sb.toString();
    }

    /**
     * 16进制字符串转换为字符串
     *
     * @param str
     * @return
     */
    public static String hexToStr(String str) {
        if (isNull(str)) {
            return null;
        }
        str = str.toUpperCase();
        int length = str.length() / 2;
        char[] hexChars = str.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return new String(d, Charset.forName("utf-8"));
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 整数转二进制
     *
     * @param i
     * @return
     */
    public static String toBinary(int i) {
        return Integer.toBinaryString(i);
    }

    /**
     * 解决乱码问题
     *
     * @param str
     * @return
     */
    public static String convertCharset(String str) throws UnsupportedEncodingException {
        CharsetDetector detector = new CharsetDetector();
        detector.setText(str.getBytes());
        CharsetMatch match = detector.detect();
        String charset = match.getName();
        return convertCharset(str, charset);
    }

    /**
     * 根据给定字符集转码为UTF-8
     *
     * @param str
     * @param charset
     * @return
     */
    public static String convertCharset(String str, String charset) throws UnsupportedEncodingException {
        return new String(str.getBytes(charset), "utf-8");
    }

    /**
     * 截取前缀
     *
     * @param str
     * @param prefix
     * @return
     */
    public static String removePrefix(String str, String prefix) {
        if (isNull(str) || isNull(prefix))
            return null;
        return str.substring(str.indexOf(prefix));
    }


}
