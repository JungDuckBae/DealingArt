package com.da.fo.controller;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.da.fo.service.MainService;

@Controller
public class MainController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private MainService mainService;
	
	@RequestMapping("/main")
	public String openMain() {
		return "fo/main/main";
	}
	
	@RequestMapping("/main/mainData")
	@ResponseBody
	public ModelAndView mainData(@RequestParam @Nullable Map<String, Object>  param) {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("jsonView");
		Map<String, Object> result = new HashMap<>();
		result = mainService.openMain(param);
		mv.addObject("mainData", result);
		
		return mv;
	}
}
