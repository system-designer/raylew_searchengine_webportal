package com.searchengine.webportal.service.impl;

import com.google.gson.reflect.TypeToken;
import com.searchengine.utils.JsonUtils;
import com.searchengine.webportal.dto.QueryResult;
import com.searchengine.webportal.dto.ResultCode;
import com.searchengine.webportal.dto.UserQuery;
import com.searchengine.webportal.service.QueryService;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by RayLew on 2014/11/14.
 */
@Service("queryService")
class QueryServiceImpl extends ServiceBase implements QueryService {

    @Override
    public List<QueryResult> getList(String queryContent, int pageIndex, int pageSize, Long userId) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("queryContent", queryContent);
        paramMap.put("pageIndex", String.valueOf(pageIndex));
        paramMap.put("pageSize", String.valueOf(pageSize));
        paramMap.put("userId", userId.toString());
        try {
            String ret = WebApiRequest.get("query/betaList", paramMap);
            Type type = new TypeToken<WebApiResult<List<QueryResult>>>() {
            }.getType();
            WebApiResult<List<QueryResult>> result = JsonUtils.fromJson(ret, type);
            // 成功
            if (result.getCode() == ResultCode.SUCCESS) {
                return result.getData();
            }
        } catch (Exception ex) {
            logger.error(ex);
        }
        return null;
    }

    @Override
    public List<UserQuery> getList(String date) {
        Map<String, String> paramMap = new HashMap<String, String>();
        paramMap.put("date", date);
        try {
            String ret = WebApiRequest.get("query/hotQueryList", paramMap);
            Type type = new TypeToken<WebApiResult<List<UserQuery>>>() {
            }.getType();
            WebApiResult<List<UserQuery>> result = JsonUtils.fromJson(ret, type);
            // 成功
            if (result.getCode() == ResultCode.SUCCESS) {
                return result.getData();
            }
        } catch (Exception ex) {
            logger.error(ex);
        }
        return null;
    }
}
