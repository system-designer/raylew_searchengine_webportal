package com.searchengine.webportal.service;

import com.searchengine.webportal.dto.WebPage;
import com.searchengine.webportal.dto.Website;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WebPageService {
    /**
     * 分页得到网页
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    List<WebPage> getList(int pageIndex, int pageSize);

    /**
     * 得到网页总数
     *
     * @return
     */
    Long getCount();

    /**
     * 分页得到网站
     *
     * @param domain
     * @param pageIndex
     * @param pageSize
     * @return
     */
    List<Website> getWebsiteList(String domain, int pageIndex, int pageSize);

    /**
     * 得到网站总数
     *
     * @return
     */
    Long getWebsiteCount();

    /**
     * 通过域名分页得到网页
     *
     * @param domain
     * @param pageIndex
     * @param pageSize
     * @return
     */
    List<WebPage> getListByDomain(String domain, int pageIndex, int pageSize);
}
