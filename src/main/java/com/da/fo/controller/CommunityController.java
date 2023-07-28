package com.da.fo.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.da.fo.service.CommunityService;
import com.da.fo.service.DealService;

@Controller
public class CommunityController {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	CommunityService communityService;
	
	@Autowired
	DealService dealService;
	
	// 커뮤니티 홈 페이지
	@RequestMapping("/community/main")
	public String communityHome() {
		return "thymeleaf/fo/community/community_home";
	}
	
	// 커뮤니티 자랑하기 상세 페이지
	@RequestMapping("/community/showingOffDetail")
	public ModelAndView showingOffDetail(@RequestParam Map<String, Object> param) {
		ModelAndView mv = new ModelAndView("thymeleaf/fo/myPage/othermem_mypage_showingoff_detailpage");
		Map<String, Object> result = communityService.showingOffDetail(param);
		Map<String, Object> workDtl = dealService.workDetail((String)param.get("workSq")); // 작품 상세 정보
		mv.addObject("result", result);
		mv.addObject("workDtl", workDtl);
		return mv;
	}
	
	// 커뮤니티 홈 목록 조회
	@RequestMapping("/community/searchHomeList")
	@ResponseBody
	public ModelAndView searchHomeList() {
		ModelAndView mv = new ModelAndView("jsonView");
		Map<String, Object> result = communityService.searchHomeList();
		mv.addObject("result", result);
		return mv;
	}
	
	// 커뮤니티 이벤트 메인 페이지
	@RequestMapping("/community/event")
	public String communityEvent() {
		return "thymeleaf/fo/community/community_event";
	}
	
	// 커뮤니티 이벤트 목록 조회
	@RequestMapping("/community/searchEventList")
	@ResponseBody
	public ModelAndView searchEventList(@RequestParam Map<String, Object> param) {
		ModelAndView mv = new ModelAndView("jsonView");
		Map<String, Object> result = communityService.searchEventList(param);
		mv.addObject("result", result);
		return mv;
	}
	
	// 자랑하기 댓글, 대댓글 조회
	@RequestMapping("/community/searchCmtsList")
	@ResponseBody
	public ModelAndView searchCmtsList(@RequestParam Map<String, Object> param) {
		ModelAndView mv = new ModelAndView("jsonView");
		Map<String, Object> result = communityService.searchCmtsList(param);
		mv.addObject("result", result);
		return mv;
	}
	
	// 작품자랑 리스트 페이지
	@RequestMapping("/community/worksList")
	public String communityWorksListPage() {
		return "thymeleaf/fo/community/community_works";
	}
		
	// 커뮤니티 리스트 데이터 조회 
	@RequestMapping("/community/communityListData")
	@ResponseBody
	public ModelAndView communityListData(@RequestParam Map<String, Object> param) {
		ModelAndView mv = new ModelAndView("jsonView");
		
		if(null != param.get("offset")) {
			param.put("offset", Integer.parseInt(param.get("offset").toString()));
			param.put("limit", Integer.parseInt(param.get("limit").toString()));
		}
		
		List<Map<String, Object>> result = communityService.communityListData(param);
		
		// 커뮤니티 총개수 들고오기 
		Map<String, Object> totalCount = communityService.getCommunityTotalCount(param);

		mv.addObject("result", result);
		mv.addObject("totalCount", totalCount);

		return mv;
	}
	
	// 전시후기/소개 리스트 페이지
	@RequestMapping("/community/exhintList")
	public String communityExhintListPage() {
		return "thymeleaf/fo/community/community_exhint";
	}
	
	// 커뮤니티 전시후기/소개 지역구분 코드 들고오기
	@RequestMapping("/community/getExhibitRegionCdNm")
	public ModelAndView getExhibitRegionCdNm(@RequestParam String cdSq) {
		ModelAndView mv = new ModelAndView("jsonView");
		List<Map<String, Object>> result = communityService.getDtlCdNm(cdSq);
		mv.addObject("result", result);
		return mv;
	}
	
	// 커뮤니티 전시후기/소개 전시구분 코드 들고오기
	@RequestMapping("/community/getExhibitTypCdNm")
	public ModelAndView getExhibitTypCdNm(@RequestParam String cdSq) {
		ModelAndView mv = new ModelAndView("jsonView");
		List<Map<String, Object>> result = communityService.getDtlCdNm(cdSq);
		mv.addObject("result", result);
		return mv;
	}
	
	// 커뮤니티 노하우 페이지
	@RequestMapping("/community/knowhowList")
	public String communityKnowhowListPage() {
		return "thymeleaf/fo/community/community_issue";
	}
	
	// 작품/ 자랑하기 상세페이지
	@RequestMapping("/community/workDetail")
	@ResponseBody
	public ModelAndView communityWorkDetailPage(@RequestParam String comtSq) {
		ModelAndView mv = new ModelAndView("thymeleaf/fo/myPage/othermem_mypage_showingoff_detailpage");
		
		// 조회해서 결과값 리턴해주기
		
		return mv;
	}
	
	// 전시후기/소개 상세페이지
	@RequestMapping("/community/exhibitDetail")
	@ResponseBody
	public ModelAndView communityExhibitDetailPage(@RequestParam String comtSq) {
		ModelAndView mv = new ModelAndView("thymeleaf/fo/myPage/othermem_mypage_exhint_detailpage");

		Map<String, Object> exhibit = communityService.communityExhKnoDetail(comtSq);
		mv.addObject("exhibit", exhibit);
		
		System.out.println(exhibit);
		
		return mv;
	}
	
	// 노하우 상세페이지
	@RequestMapping("/community/knowhowDetail")
	@ResponseBody
	public ModelAndView communityKnowhowDetailPage(@RequestParam String comtSq) {
		ModelAndView mv = new ModelAndView("thymeleaf/fo/myPage/othermem_mypage_knowhow_detailpage");
		
		Map<String, Object> knowhow = communityService.communityExhKnoDetail(comtSq);
		mv.addObject("knowhow", knowhow);
		
		return mv;
	}
	
}
