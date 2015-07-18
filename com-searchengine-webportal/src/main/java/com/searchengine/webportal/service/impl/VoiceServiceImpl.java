package com.searchengine.webportal.service.impl;

import com.searchengine.webportal.common.HttpWebRequest;
import com.searchengine.webportal.interceptor.LoginContextInterceptor;
import com.searchengine.webportal.service.VoiceService;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import com.searchengine.webportal.utils.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by RayLew on 2015/5/7.
 * QQ:897929321
 */
@Service("voiceService")
public class VoiceServiceImpl extends ServiceBase implements VoiceService {
    private final static Logger logger = Logger.getLogger(LoginContextInterceptor.class);

    //语音识别
    private static final String asrServerURL = "http://vop.baidu.com/server_api";
    //语音合成
    private static final String ttsServerURL = "http://tsn.baidu.com/text2audio";
    //put your own params here
    private static final String apiKey = "HYfqn6GPv0mVD2RSUOHmeAbO";
    private static final String secretKey = "cPWhZ4iKYqAFj7GdbSvOd03Z3AasVIGF";
    private static final String cuid = "1071682780741632";
    private static String token = "24.7bd58bf4ec181b6a3d52fafcf28a7797.2592000.1433296052.282335-5076367";

    @Override
    public String getTextFromFile(String voiceFileName) {
        String res = "";
        try {
            res = method1(voiceFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public String getTextFromBase64(String base64Code) {
        String res = "";
        try {
            res = method2(base64Code);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public String getBase64VoiceFromText(String text) {
        String res = "";
        try {
            res = method3(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    private void getToken() throws Exception {
        String getTokenURL = "https://openapi.baidu.com/oauth/2.0/token?grant_type=client_credentials" +
                "&client_id=" + apiKey + "&client_secret=" + secretKey;
        HttpURLConnection conn = (HttpURLConnection) new URL(getTokenURL).openConnection();
        token = new JSONObject(printResponse(conn)).getString("access_token");
    }

    private String method1(String testFileName) throws Exception {
        File pcmFile = new File(testFileName);
        HttpURLConnection conn = (HttpURLConnection) new URL(asrServerURL).openConnection();

        // construct params
        JSONObject params = new JSONObject();
        params.put("format", "wav");
        params.put("rate", 16000);
        params.put("channel", "1");
        params.put("token", token);
        params.put("cuid", cuid);
        params.put("len", pcmFile.length());
        params.put("speech", DatatypeConverter.printBase64Binary(loadFile(pcmFile)));

        // add request header
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");

        conn.setDoInput(true);
        conn.setDoOutput(true);

        // send request
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(params.toString());
        wr.flush();
        wr.close();

        return printResponse(conn);
    }

    private String method2(String base64Code) {
        byte[] bytes = Base64.decodeBase64(base64Code);
        JSONObject params = new JSONObject();
        params.put("format", "wav");
        params.put("rate", 16000);
        params.put("channel", "1");
        params.put("token", token);
        params.put("cuid", cuid);
        params.put("len", bytes.length);

        params.put("speech", base64Code);
        String res = "";
        try {
            res = HttpWebRequest.post(asrServerURL, null, params.toString(), "application/json; charset=utf-8");
            logger.info(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    private String method3(String text) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("tex", text);
        params.put("lan", "zh");
        params.put("tok", token);
        params.put("ctp", "1");
        params.put("cuid", cuid);
        params.put("spd", "5");
        params.put("pit", "5");
        params.put("vol", "5");
        params.put("per", "0");
        byte[] res = null;
        try {
            res = HttpWebRequest.getByte(ttsServerURL, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (res != null) {
            return Base64.encodeBase64String(res);
        }
        return "";
    }

    private String printResponse(HttpURLConnection conn) throws Exception {
        if (conn.getResponseCode() != 200) {
            // request error
            return "";
        }
        InputStream is = conn.getInputStream();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));
        String line;
        StringBuffer response = new StringBuffer();
        while ((line = rd.readLine()) != null) {
            response.append(line);
            response.append('\r');
        }
        rd.close();
        System.out.println(new JSONObject(response.toString()).toString(4));
        return response.toString();
    }

    private byte[] loadFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        long length = file.length();
        byte[] bytes = new byte[(int) length];

        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            is.close();
            throw new IOException("Could not completely read file " + file.getName());
        }

        is.close();
        return bytes;
    }
}
