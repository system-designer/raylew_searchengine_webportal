package com.searchengine.webportal.service;

import com.searchengine.webportal.dto.PageWord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PageWordService {

    /**
     * 得到所有索引
     *
     * @return
     */
    List<PageWord> getList(int pageIndex, int pageSize);

    /**
     * 得到索引总数
     *
     * @return
     */
    Long getCount();
}
