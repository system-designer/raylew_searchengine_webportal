package com.searchengine.webportal.service.impl;

import com.google.gson.reflect.TypeToken;
import com.searchengine.utils.JsonUtils;
import com.searchengine.webportal.dto.ResultCode;
import com.searchengine.webportal.dto.Word;
import com.searchengine.webportal.service.WordService;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("wordService")
public class WordServiceImpl extends ServiceBase implements WordService {

    @Override
    public List<Word> getList(int pageIndex, int pageSize) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("pageIndex", String.valueOf(pageIndex));
        paramMap.put("pageSize", String.valueOf(pageSize));

        try {
            String ret = WebApiRequest.get("word/list", paramMap);
            Type type = new TypeToken<WebApiResult<List<Word>>>() {
            }.getType();
            WebApiResult<List<Word>> result = JsonUtils.fromJson(ret, type);
            // 成功
            if (result.getCode() == ResultCode.USER_OK) {
                return result.getData();
            }
        } catch (Exception ex) {
            logger.error(ex);
        }
        return null;
    }

    @Override
    public Long getCount() {
        Map<String, String> paramMap = new HashMap<String, String>();

        try {
            String ret = WebApiRequest.get("word/count", paramMap);

            Type type = new TypeToken<WebApiResult<Long>>() {
            }.getType();
            WebApiResult<Long> result = JsonUtils.fromJson(ret, type);

            // 成功
            if (result.getCode() == ResultCode.USER_OK) {
                return result.getData();
            }
        } catch (Exception ex) {
            logger.error(ex);
        }

        return 0L;
    }
}
