package com.searchengine.webportal.controller;

import com.google.gson.reflect.TypeToken;
import com.searchengine.utils.FileAction;
import com.searchengine.utils.JsonUtils;
import com.searchengine.webportal.cookie.CookieUtils;
import com.searchengine.webportal.dto.QueryResult;
import com.searchengine.webportal.dto.UserQuery;
import com.searchengine.webportal.interceptor.LoginContextInterceptor;
import com.searchengine.webportal.service.QueryService;
import com.searchengine.webportal.service.VoiceService;
import com.searchengine.webportal.utils.Constants;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.velocity.tools.generic.EscapeTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;

@Controller
@RequestMapping(value = "/query")
public class QueryController extends ControllerBase {
    private final static Logger logger = Logger.getLogger(LoginContextInterceptor.class);

    @Autowired
    private QueryService queryService;

    @Autowired
    private CookieUtils cookieUtils;

    @Value("${search.cookie.key}")
    private String searchCookieKey;

    @Autowired
    private VoiceService voiceService;

    @RequestMapping(value = "list", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ModelAndView index(@RequestParam("queryContent") String queryContent, HttpServletRequest request, HttpServletResponse response) {
        String pageIndexStr = request.getParameter("pageIndex");
        if (!StringUtils.isNotEmpty(pageIndexStr)) {
            pageIndexStr = "1";
        }
        Long beforeSearchTime = System.currentTimeMillis();
        Long userId = getCurrentUserId();
        queryContent = queryContent.trim();
        //如果搜索结果为空，跳转到首页
        if (!StringUtils.isNotEmpty(queryContent)) {
            ModelAndView view = new ModelAndView("index");
            return view;
        }
        //将中文字符编码
        try {
            queryContent = URLEncoder.encode(queryContent, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int pageIndex = Integer.parseInt(pageIndexStr);
        int pageSize = Constants.DEFAULT_PAGESIZE;
        List<QueryResult> queryList = queryService.getList(queryContent, pageIndex, pageSize, userId);
        ModelAndView view = new ModelAndView("query/list");
        view.addObject("queryList", queryList);
        int totalCount = 0;
        if (queryList != null && queryList.size() > 0) {
            totalCount = queryList.get(0).getRank();
        }
        view.addObject("totalCount", totalCount);
        long pageCount = (totalCount % pageSize == 0) ? totalCount / pageSize : (totalCount / pageSize + 1);
        view.addObject("pageCount", pageCount);
        view.addObject("pageIndex", pageIndex);
        view.addObject("esc", new EscapeTool());
        //将中文字符解码
        try {
            queryContent = URLDecoder.decode(queryContent, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        view.addObject("queryContent", queryContent);
        //设置搜索历史的cookie
        setHistoryCookie(request, queryContent, view, response);
        //显示当日热门搜索
        Date date = new Date();
        List<UserQuery> userQueryList = queryService.getList(date.toLocaleString().substring(0, 10));
        view.addObject("hotQueryList", userQueryList);
        if (getCurrentUserId() > 0 && pageIndex == 1) {
            //语音合成
            StringBuilder tts_audio_builder = new StringBuilder();
            tts_audio_builder.append("爱搜为您找到约" + totalCount + "个结果,");
            if (queryList != null && queryList.size() > 0) {
                tts_audio_builder.append("第一条结果是：" + queryList.get(0).getTitle().replaceAll("<span style='color:red;'>", "").replaceAll("</span>", ""));
            }
            String tts_audio = voiceService.getBase64VoiceFromText(tts_audio_builder.toString());
            view.addObject("tts_audio", tts_audio);
        } else {
            view.addObject("tts_audio", "");
        }
        //显示搜索时间
        Long afterSearchTime = System.currentTimeMillis();
        double seconds = (double) ((afterSearchTime - beforeSearchTime) / 1000) % 60;
        view.addObject("seconds", seconds);
        return view;
    }

    @RequestMapping(value = "voice", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String voice(HttpServletRequest request, HttpServletResponse response) {
        Long userId = getCurrentUserId();
        try {
            request.setCharacterEncoding("UTF-8");
            String data = request.getParameter("data");
            data = data.substring(data.indexOf(",") + 1);
            String json = voiceService.getTextFromBase64(data);
            if (StringUtils.isNotEmpty(json) && json.contains("[") && json.contains("]")) {
                String result = json.substring(json.indexOf("[") + 2, json.indexOf("]") - 1);
                String[] temp = result.split("，");
                if (temp != null && temp.length > 0) {
                    String queryContent = temp[0];
                    Map<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("queryContent", queryContent);
                    logger.info(queryContent);
                    Type type = new TypeToken<HashMap<String, String>>() {
                    }.getType();
                    return JsonUtils.toJson(hashMap, type);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 网页快照
     *
     * @param filePath
     * @param fileCharset
     * @return
     */
    @RequestMapping(value = "fastGraph", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getFastGraph(@RequestParam("filePath") String filePath, @RequestParam("fileCharset") String fileCharset) {
        if (!StringUtils.isNotEmpty(filePath) || !StringUtils.isNotEmpty((fileCharset))) {
            return null;
        }
        ModelAndView view = new ModelAndView("query/fastgraph");
        String htmlContent = FileAction.convertFileToString(com.searchengine.utils.Constant.WEBPAGE_BASEPATH + "/" + filePath,
                fileCharset);
        view.addObject("htmlContent", htmlContent);
        return view;
    }

    /**
     * 设置搜索历史的cookie
     *
     * @param request
     * @param queryContent
     * @param view
     * @param response
     */
    private void setHistoryCookie(HttpServletRequest request, String queryContent, ModelAndView view, HttpServletResponse response) {
        String paramCookieValue = cookieUtils.getCookieValue(request, searchCookieKey);
        LinkedList<String> searchList = new LinkedList<String>();
        if (paramCookieValue == null || paramCookieValue.length() == 0) {
            searchList.add(queryContent);
        } else {
            //解析cookie
            String cookieValue = null;
            try {
                cookieValue = URLDecoder.decode(paramCookieValue, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                logger.error("cookie param error:", e);
            }
            String[] searchHistory = cookieValue.split(",");
            if (searchHistory != null && searchHistory.length > 0) {
                for (String history : searchHistory) {
                    searchList.add(history.trim());
                }
                for (String str : searchList) {
                    if (str.equals(queryContent)) {
                        searchList.remove(str);
                        break;
                    }
                }
                searchList.addFirst(queryContent);
            }
        }
        if (searchList != null) {
            //显示搜索历史
            view.addObject("searchList", searchList);
            //设置cookie
            String cookieValue = null;
            try {
                cookieValue = toCookieValue(searchList);
                cookieValue = URLEncoder.encode(cookieValue, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                logger.error("cookie param error:", e);
            }
            cookieUtils.setCookie(response, searchCookieKey, cookieValue);
        }
    }

    /**
     * 将搜索历史列表转化为cookie串
     *
     * @param searchList
     * @return
     */
    private String toCookieValue(List<String> searchList) {
        String cookieValue = searchList.toString();
        cookieValue = cookieValue.substring(1, cookieValue.length() - 1);
        return cookieValue;
    }

}
