package com.searchengine.webportal.service.impl;

import com.google.gson.reflect.TypeToken;
import com.searchengine.utils.JsonUtils;
import com.searchengine.webportal.dto.ResultCode;
import com.searchengine.webportal.dto.WebPage;
import com.searchengine.webportal.dto.Website;
import com.searchengine.webportal.service.WebPageService;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("webPageService")
public class WebPageServiceImpl extends ServiceBase implements WebPageService {

    @Override
    public List<WebPage> getList(int pageIndex, int pageSize) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("pageIndex", String.valueOf(pageIndex));
        paramMap.put("pageSize", String.valueOf(pageSize));

        try {
            String ret = WebApiRequest.get("webpage/list", paramMap);
            Type type = new TypeToken<WebApiResult<List<WebPage>>>() {
            }.getType();
            WebApiResult<List<WebPage>> result = JsonUtils.fromJson(ret, type);
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
            String ret = WebApiRequest.get("webpage/count", paramMap);

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

    @Override
    public List<Website> getWebsiteList(String domain, int pageIndex, int pageSize) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("domain", domain);
        paramMap.put("pageIndex", String.valueOf(pageIndex));
        paramMap.put("pageSize", String.valueOf(pageSize));

        try {
            String ret = WebApiRequest.get("website/list", paramMap);
            Type type = new TypeToken<WebApiResult<List<Website>>>() {
            }.getType();
            WebApiResult<List<Website>> result = JsonUtils.fromJson(ret, type);
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
    public Long getWebsiteCount() {
        Map<String, String> paramMap = new HashMap<String, String>();

        try {
            String ret = WebApiRequest.get("website/count", paramMap);

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

    @Override
    public List<WebPage> getListByDomain(String domain, int pageIndex, int pageSize) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("domain", domain);
        paramMap.put("pageIndex", String.valueOf(pageIndex));
        paramMap.put("pageSize", String.valueOf(pageSize));

        try {
            String ret = WebApiRequest.get("website/webpagelist", paramMap);
            Type type = new TypeToken<WebApiResult<List<WebPage>>>() {
            }.getType();
            WebApiResult<List<WebPage>> result = JsonUtils.fromJson(ret, type);
            // 成功
            if (result.getCode() == ResultCode.USER_OK) {
                return result.getData();
            }
        } catch (Exception ex) {
            logger.error(ex);
        }
        return null;
    }
}
