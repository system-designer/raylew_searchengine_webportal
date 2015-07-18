package com.searchengine.webportal.controller;

import com.searchengine.utils.StringUtils;
import com.searchengine.webportal.dto.RoleType;
import com.searchengine.webportal.dto.WebPage;
import com.searchengine.webportal.dto.Website;
import com.searchengine.webportal.service.WebPageService;
import com.searchengine.webportal.utils.Constants;
import org.apache.log4j.Logger;
import org.apache.velocity.tools.generic.EscapeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.List;

@Controller
@RequestMapping(value = "/website")
public class WebsiteController extends ControllerBase {
    private static final Logger logger = Logger.getLogger(WebsiteController.class);
    @Autowired
    WebPageService webPageService;

    @RequestMapping(value = "list", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ModelAndView getList(@RequestParam String domain, @RequestParam int pageIndex, @RequestParam int pageSize) {
        if (getCurrentRoleType() > RoleType.USER.getValue()) {
            Long count = 0L;
            //没有输入域名时查询所有
            if (!StringUtils.isNotEmpty(domain)) {
                count = webPageService.getWebsiteCount();
            } else {
                pageIndex = 1;
            }
            List<Website> websiteList = webPageService.getWebsiteList(domain, pageIndex, pageSize);
            //输入域名时根据websiteList确定count
            if (StringUtils.isNotEmpty(domain)) {
                if (websiteList != null) {
                    count = 1L;
                }
            }
            ModelAndView view = new ModelAndView("website/list");
            long pageCount = (count % pageSize == 0) ? count / pageSize : (count / pageSize + 1);
            view.addObject("websiteList", websiteList);
            view.addObject("totalCount", count);
            view.addObject("pageCount", pageCount);
            view.addObject("pageIndex", pageIndex);
            view.addObject("pageSize", pageSize);
            view.addObject("domain", domain);
            view.addObject("esc", new EscapeTool());
            return view;
        } else {
            ModelAndView view = new ModelAndView("common/error");
            return view;
        }
    }

    /**
     * 通过域名得到收录的网页
     *
     * @param domain
     * @param pageIndex
     * @param pageSize
     * @param count
     * @return
     */
    @RequestMapping(value = "webpagelist", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public ModelAndView getWebPageList(@RequestParam String domain, @RequestParam int pageIndex,
                                       @RequestParam int pageSize, @RequestParam int count) {
        if (getCurrentRoleType() > RoleType.USER.getValue()) {
            List<WebPage> webPageList = webPageService.getListByDomain(domain, pageIndex, pageSize);
            ModelAndView view = new ModelAndView("website/webPageList");
            long pageCount = (count % pageSize == 0) ? count / pageSize : (count / pageSize + 1);
            view.addObject("webPageList", webPageList);
            view.addObject("totalCount", count);
            view.addObject("pageCount", pageCount);
            view.addObject("pageIndex", pageIndex);
            view.addObject("pageSize", pageSize);
            view.addObject("domain", domain);
            view.addObject("esc", new EscapeTool());
            return view;
        } else {
            ModelAndView view = new ModelAndView("common/error");
            return view;
        }
    }

    @RequestMapping(value = "structure", method = {RequestMethod.GET})
    @ResponseBody
    public ModelAndView getStructure(@RequestParam String domain, @RequestParam int totalCount) {
        if (getCurrentRoleType() > RoleType.USER.getValue()) {
            ModelAndView view = new ModelAndView("website/structure");
            view.addObject("domain", domain);
            view.addObject("totalCount", totalCount);
            StringBuilder sb = new StringBuilder();
            getFileStructure(Constants.WEBPAGELIB_ROOTPATH + domain, sb);
            view.addObject("root", sb.toString());
            view.addObject("esc", new EscapeTool());
            return view;
        } else {
            ModelAndView view = new ModelAndView("common/error");
            return view;
        }
    }

    private void getFileStructure(String path, StringBuilder sb) {
        File file = new File(path);
        sb.append("{");
        sb.append("\"leaf\"").append(":").append(false).append(",");
        sb.append("\"name\"").append(":").append("\"" + file.getName() + "\"");
        if (file.exists()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                sb.append(",\"children\"").append(":").append("[");
                for (int i = 0; i < files.length; i++) {
                    File child = files[i];
                    getFileStructure(child.getPath(), sb);
                    if (i < files.length - 1) {
                        sb.append(",");
                    }
                }
                sb.append("]");
            }
        }
        sb.append("}");
    }

}
