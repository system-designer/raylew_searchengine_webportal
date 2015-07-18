package com.searchengine.webportal.service;

import com.searchengine.webportal.dto.Word;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface WordService {

    /**
     * 得到所有词语
     *
     * @return
     */
    List<Word> getList(int pageIndex, int pageSize);

    /**
     * 得到词语总数
     *
     * @return
     */
    Long getCount();
}
