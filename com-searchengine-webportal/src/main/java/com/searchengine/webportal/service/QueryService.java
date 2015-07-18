package com.searchengine.webportal.service;

import com.searchengine.webportal.dto.QueryResult;
import com.searchengine.webportal.dto.UserQuery;

import java.util.Date;
import java.util.List;

/**
 * Created by RayLew on 2015/2/7.
 */
public interface QueryService {


    /**
     * 返回用户搜索结果
     * @param queryContent
     * @return
     */
    List<QueryResult> getList(String queryContent,int pageIndex,int pageSize,Long userId);

    /**
     * 得到某个日期下的最热门搜索
     * @param date
     * @return
     */
    List<UserQuery> getList(String date);
}
