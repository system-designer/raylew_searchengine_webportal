package com.searchengine.webportal.service;

/**
 * Created by RayLew on 2015/5/7.
 * QQ:897929321
 */
public interface VoiceService {
    /**
     * 语音识别：从语音文件得到
     *
     * @param voiceFileName
     * @return
     */
    String getTextFromFile(String voiceFileName);

    /**
     * 语音识别：从base64编码的字符串得到
     *
     * @param base64Code
     * @return
     */
    String getTextFromBase64(String base64Code);

    /**
     * 语音合成：将文本转化为base64格式的声音
     * @param text
     * @return
     */
    String getBase64VoiceFromText(String text);
}
