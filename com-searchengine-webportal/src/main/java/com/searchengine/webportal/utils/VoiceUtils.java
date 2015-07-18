package com.searchengine.webportal.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Created by RayLew on 2015/5/3.
 * QQ:897929321
 */
public class VoiceUtils {

    public static void main(String[] args) throws IOException {
        File file = new File("D:\\Workspaces\\idea\\raylew_searchengine_webportal\\com-searchengine-webportal\\src\\main\\resources\\test.wav");
        byte[] bytes = FileUtils.readFileToByteArray(file);
        System.out.println(bytes.toString());
        String encoded = Base64.encodeBase64String(bytes);
        System.out.println(encoded);
    }

}
