package com.example.decrypt.demos.web;



import cn.hutool.core.codec.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * 加密解码工具类.
 *
 * @author szw
 * @date 2024/07/01
 */
public class SecretUtils {
    /**
     * key和iv值可以随机生成.
     */
    private static final String KEY = "abcdefghijklmnop";

    private static final String IV = "abcdefghijklmnop";

    private static final String UREPORT2 = "ureport2";

    private static final String CORE_PUBLIC = "core/public";

    private static final String WX_GZH_EVENT =  "/oauth/user/wxGzhEvent";



    /**
     * param data 需要解密的数据.
     * 调用desEncrypt（）方法
     */
    public static String desEncrypt(String data) {
        return desEncrypt(data, KEY, IV);
    }

    public static byte[] desEncrypt(byte[] data) {
        return desEncrypt(data, KEY, IV);
    }

    /**
     * 解密方法.
     *
     * @param data 要解密的数据
     * @param key  解密key
     * @param iv   解密iv
     * @return 解密的结果
     */
    private static String desEncrypt(String data, String key, String iv) {
        try {
            byte[] bytes = desEncrypt(Base64.decode(data), key, iv);
            if (bytes == null) {
                return null;
            }
            return new String(bytes, StandardCharsets.UTF_8).trim();
        } catch (Exception e) {
            return data;
        }
    }

    private static byte[] desEncrypt(byte[] encrypted1, String key, String iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] original = cipher.doFinal(encrypted1);
            return original;
        } catch (Exception e) {
            return encrypted1;
        }
    }
}
