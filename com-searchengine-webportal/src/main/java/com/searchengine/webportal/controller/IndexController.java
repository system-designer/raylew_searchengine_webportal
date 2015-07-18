package com.searchengine.webportal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/")
public class IndexController extends ControllerBase {

    @RequestMapping(value = {"/", "/index", "/home"})
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("index");
    }

    @RequestMapping(value = {"/about"})
    public ModelAndView about(HttpServletRequest request, HttpServletResponse response) {
        return new ModelAndView("/common/about");
    }

}
