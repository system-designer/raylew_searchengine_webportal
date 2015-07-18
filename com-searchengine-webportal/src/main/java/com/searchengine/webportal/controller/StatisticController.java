package com.searchengine.webportal.controller;

import com.searchengine.webportal.dto.PageWord;
import com.searchengine.webportal.dto.RoleType;
import com.searchengine.webportal.dto.WebPage;
import com.searchengine.webportal.dto.Word;
import com.searchengine.webportal.service.PageWordService;
import com.searchengine.webportal.service.WebPageService;
import com.searchengine.webportal.service.WordService;
import org.apache.velocity.tools.generic.EscapeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping(value = "/statistic")
public class StatisticController extends ControllerBase {

    @Autowired
    WordService wordService;

    @Autowired
    WebPageService webPageService;

    @Autowired
    PageWordService pageWordService;

    /**
     * 词表
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "wordList", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView wordList(@RequestParam int pageIndex, @RequestParam int pageSize) {
        if (getCurrentRoleType() > RoleType.USER.getValue()) {
            Long count = wordService.getCount();
            List<Word> wordList = wordService.getList(pageIndex, pageSize);
            ModelAndView view = new ModelAndView("statistic/wordList");
            long pageCount = (count % pageSize == 0) ? count / pageSize : (count / pageSize + 1);
            view.addObject("wordList", wordList);
            view.addObject("totalCount", count);
            view.addObject("pageCount", pageCount);
            view.addObject("pageIndex", pageIndex);
            view.addObject("esc", new EscapeTool());
            return view;
        } else {
            ModelAndView view = new ModelAndView("common/error");
            return view;
        }
    }

    /**
     * 网页表
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "webPageList", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView webPageList(@RequestParam int pageIndex, @RequestParam int pageSize) {
        if (getCurrentRoleType() > RoleType.USER.getValue()) {
            Long count = webPageService.getCount();
            List<WebPage> webPageList = webPageService.getList(pageIndex, pageSize);
            ModelAndView view = new ModelAndView("statistic/webPageList");
            long pageCount = (count % pageSize == 0) ? count / pageSize : (count / pageSize + 1);
            view.addObject("webPageList", webPageList);
            view.addObject("totalCount", count);
            view.addObject("pageCount", pageCount);
            view.addObject("pageIndex", pageIndex);
            view.addObject("esc", new EscapeTool());
            return view;
        } else {
            ModelAndView view = new ModelAndView("common/error");
            return view;
        }
    }

    /**
     * 正排索引表
     *
     * @param pageIndex
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "pageWordList", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView pageWordList(@RequestParam int pageIndex, @RequestParam int pageSize) {
        if (getCurrentRoleType() > RoleType.USER.getValue()) {
            Long count = pageWordService.getCount();
            List<PageWord> pageWordList = pageWordService.getList(pageIndex, pageSize);
            ModelAndView view = new ModelAndView("statistic/pageWordList");
            long pageCount = (count % pageSize == 0) ? count / pageSize : (count / pageSize + 1);
            view.addObject("pageWordList", pageWordList);
            view.addObject("totalCount", count);
            view.addObject("pageCount", pageCount);
            view.addObject("pageIndex", pageIndex);
            view.addObject("esc", new EscapeTool());
            return view;
        } else {
            ModelAndView view = new ModelAndView("common/error");
            return view;
        }
    }

}
