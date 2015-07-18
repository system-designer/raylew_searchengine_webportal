package com.searchengine.webportal.service.impl;

import com.google.gson.reflect.TypeToken;
import com.searchengine.utils.JsonUtils;
import com.searchengine.webportal.dto.PageWord;
import com.searchengine.webportal.dto.ResultCode;
import com.searchengine.webportal.service.PageWordService;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("pageWordService")
public class PageWordServiceImpl extends ServiceBase implements PageWordService {

    @Override
    public List<PageWord> getList(int pageIndex, int pageSize) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("pageIndex", String.valueOf(pageIndex));
        paramMap.put("pageSize", String.valueOf(pageSize));

        try {
            String ret = WebApiRequest.get("pageword/list", paramMap);
            Type type = new TypeToken<WebApiResult<List<PageWord>>>() {
            }.getType();
            WebApiResult<List<PageWord>> result = JsonUtils.fromJson(ret, type);
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
            String ret = WebApiRequest.get("pageword/count", paramMap);

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
