package com.da.fo.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.da.common.AwsS3Service;
import com.da.fo.service.MemberService;
import com.da.fo.service.MyPageService;
import com.da.sample.service.CommonService;
import com.da.util.SendMailUtil;
import com.da.util.SendSmsUtil;
import com.da.vo.FileVo;

@Controller
public class MyPageController {

	@Autowired
	private AwsS3Service awsS3Service;

	@Autowired
	private MemberService memberService;

	@Autowired
	private CommonService commonService;

	@Autowired
	private SendSmsUtil sendSmsUtil;

	@Autowired
	private SendMailUtil sendMailUtil;

	@Autowired
	private MyPageService myPageService;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@RequestMapping("/withdrawal")
	public String withdrawal() {
		return "thymeleaf/fo/myPage/secession";
	}

	// 회원수정 페이지
	@RequestMapping("/memberEdit")
	public String memberEdit() {
		return "thymeleaf/fo/myPage/memberEdit";
	}

	// 소장품 등록
	@RequestMapping("/collection_reg")
	public String collectionReg() {
		return "thymeleaf/fo/myPage/collection_reg";
	}

	// 소장품 수정
	@RequestMapping("/myWork_reg")
	public String myWorkReg() {
		return "thymeleaf/fo/myPage/myWork_reg";
	}

	// 알림
	@RequestMapping("/alarm")
	public String alarm() {
		return "thymeleaf/fo/myPage/alarm";
	}

	// 쿠폰
	@RequestMapping("/coupon")
	public String coupon() {
		return "thymeleaf/fo/myPage/coupon";
	}

	// 소장품
	@RequestMapping("/myCollection")
	@ResponseBody
	public ModelAndView myCollection(@RequestParam(value = "mbrSq", required = false) String mbrSq) {
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@ mbrSq : " + mbrSq);
		ModelAndView mv = new ModelAndView("thymeleaf/fo/myPage/myGallery_myCollection");
		List result = myPageService.myCollection(mbrSq);
		mv.addObject("result", result);
		return mv;
	}

	// 스크랩
	@RequestMapping("/scrap")
	@ResponseBody
	public ModelAndView scrap(@RequestParam(value = "mbrSq", required = false) String mbrSq) {
		ModelAndView mv = new ModelAndView("thymeleaf/fo/myPage/myGallery_scrap");
		List<String> result = myPageService.scrapList(mbrSq);
		mv.addObject("result", result);
		return mv;
	}

	// 나의 작품
	@RequestMapping("/myWork")
	@ResponseBody
	public ModelAndView myWork(@RequestParam(value = "artstSq", required = false) String artstSq) {
		System.out.println("@@@@@@@@@@@@@@@@@@@@@@ artstSq : " + artstSq);
		ModelAndView mv = new ModelAndView("thymeleaf/fo/myPage/portfolio_myWork");
		List result = myPageService.myWork(artstSq);
		mv.addObject("result", result);
		return mv;
	}

	// 거래내역
	@RequestMapping("/myDeal")
	public String myDeal() {
		return "thymeleaf/fo/myPage/myDeal_list";
	}

	@RequestMapping("/myDealSearchList")
	@ResponseBody
	public ModelAndView myDealSearchList(@RequestParam Map<String, Object> param) {
		ModelAndView mv = new ModelAndView("jsonView");
		System.out.println("############# param : " + param);
		List result = myPageService.myDealSearchList(param);
		mv.addObject("result", result);
		return mv;
	}

	// 작가정보등록
	@RequestMapping("/information")
	public String information() {
		return "thymeleaf/fo/myPage/portfolio_info";
	}

	@RequestMapping("/withdrawalSubmit")
	@ResponseBody
	public int withdrawalSubmit(@RequestParam @Nullable Map<String, Object> param) {
		System.out.println("############## param : " + param);
		param.put("email", commonService.encrypt((String) param.get("email")));
		param.put("password", commonService.encrypt((String) param.get("password")));
		int chk = memberService.memberWithdrawalCheck(param);
		if (chk > 0) {
			return 2;
		} else {
			return memberService.memberWithdrawal(param);
		}
	}

	// 회원수정 이메일인증 메일보내기 테스트
	@RequestMapping("/myPage/memberUpdatEmailCertification")
	@ResponseBody
	public String emailCertification(@RequestParam Map<String, Object> param) {

		String result = "";
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		Map<String, Object> tomap = new HashMap<String, Object>();
		List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
		Map<String, Object> params = new HashMap<String, Object>();

		int serti = (int) ((Math.random() * (99999 - 10000 + 1)) + 10000);

		String title = "Dealing Art 회원수정시 필요한 인증번호 입니다.";
		String content = "[인증번호] " + serti + " 입니다. <br/> 인증번호 확인란에 기입해주십시오.";

		String memberEmail = (String) param.get("mbrId");

		tomap.put("type", "R");
		tomap.put("address", memberEmail);
		mList.add(tomap);

		params.put("title", title);
		params.put("body", content);
		params.put("recipients", mList);

		rtnMap = sendMailUtil.sendMail(params);
		if (((int) Double.parseDouble(rtnMap.get("count").toString()) < 0)) {
			result = "error";
		} else {
			String sertiString = Integer.toString(serti);
			result = sertiString;
		}
		int paramStatus = -1;

		return result;
	}
<<<<<<< HEAD
	
	//회원수정 이메일인증 메일보내기 테스트 
		@RequestMapping("/emailTest")
		@ResponseBody
		public String emailTest() {
			
			String result = "";
			Map<String, Object> rtnMap = new HashMap<String, Object>();
			Map<String, Object> tomap = new HashMap<String, Object>();
			List<Map<String, Object>> mList = new ArrayList<Map<String, Object>>();
			Map<String, Object> params = new HashMap<String, Object>();
			
			int serti = (int)((Math.random()* (99999 - 10000 + 1)) + 10000);
			
			String title = "Dealing Art HTML 테스트 메일입니다..";
			//String content = "[인증번호] "+ serti +" 입니다. <br/> 인증번호 확인란에 기입해주십시오.";
			String content = "<!DOCTYPE html>"
					+ "<html>"
					+ "    <head><meta content='width=device-width, initial-scale=1, maximum-scale=1' name='viewport'><meta charset='UTF-8'><style>"
					+ "            @media only screen and(max-width:640px) {"
					+ "                .stb-left-cell,"
					+ "                .stb-right-cell {"
					+ "                    max-width: 100% !important;"
					+ "                    width: 100% !important;"
					+ "                }"
					+ "                .stb-left-cell img,"
					+ "                .stb-right-cell img {"
					+ "                    width: 100%;"
					+ "                    height: auto;"
					+ "                }"
					+ "                .stb-left-cell > div {"
					+ "                    padding: 5px 10px !important;"
					+ "                    box-sizing: border-box;"
					+ "                }"
					+ "                .stb-right-cell > div {"
					+ "                    padding: 5px 10px !important;"
					+ "                    box-sizing: border-box;"
					+ "                }"
					+ "                .stb-cell-wrap {"
					+ "                    padding: 0 !important;"
					+ "                }"
					+ "                .stb-cell-wrap > tr > td {"
					+ "                    padding: 0 !important;"
					+ "                }"
					+ "                .stb-cell-wrap > tbody > tr > td {"
					+ "                    padding: 0 !important;"
					+ "                }"
					+ "                .stb-cell-wrap > tbody > tr > td.stb-text-box {"
					+ "                    padding: 0 10px !important;"
					+ "                }"
					+ "                .stb-left-cell > div.stb-text-box {"
					+ "                    padding: 5px 10px !important;"
					+ "                }"
					+ "                .stb-right-cell > div.stb-text-box {"
					+ "                    padding: 5px 10px !important;"
					+ "                }"
					+ "                .stb-container {"
					+ "                    width: 94% !important"
					+ "                }"
					+ "                .stb-container a {"
					+ "                    text-decoration: underline;"
					+ "                }"
					+ "                .stb-cta-only-wrap {"
					+ "                    padding: 10px 0 !important;"
					+ "                }"
					+ "            }"
					+ "        </style>"
					+ "    </head>"
					+ "    <body>"
					+ "        <div class=\"stb-container-full\" style=\"width:100%; padding:40px 0; background-color:#ffffff; margin: 0 auto;\">"
					+ "            <table class=\"stb-container\" cellpadding=\"0\" cellspacing=\"0\" align=\"center\" style=\"margin: 0px auto; width: 94%; max-width: 630px; background: rgb(255, 255, 255); border: 0px;\">"
					+ "                <tbody>"
					+ "                    <tr style=\"margin: 0;padding:0;\">"
					+ "                        <td style=\"width: 100%; max-width: 630px; margin: 0 auto; position: relative; border-spacing: 0; clear: both; border-collapse: separate;padding:0;overflow:hidden;_width:620px;\">"
					+ "                            <div style=\"height: 0px; max-height: 0px; border-width: 0px; border-color: initial; border-image: initial; visibility: hidden; line-height: 0px; font-size: 0px; overflow: hidden;display:none;\"></div>"
					+ "                            <table class=\"stb-block\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"overflow: hidden; margin: 0px auto; padding: 0px; width:100%;max-width: 630px; clear: both; background: none;border:0;\" width=\"100%\">"
					+ "                                <tbody>"
					+ "                                    <tr>"
					+ "                                        <td style=\"padding: 15px 0 15px 0; line-height: 1.8; border-width: 0px;\">"
					+ "                                            <table class=\"stb-cell-wrap\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"padding: 0px 5px;table-layout:fixed\" width=\"100%\">"
					+ "                                                <tbody>"
					+ "                                                    <tr>"
					+ "                                                        <td style=\"padding: 0px 10px;\" width=\"100%\">"
					+ "                                                            <div>"
					+ "                                                                <p style=\"text-align: justify; margin: 0px;width:100%\"><img src=\"https://s3.ap-northeast-2.amazonaws.com/img.stibee.com/54874_1650865649.png\" style=\"width: 100%; display: inline; vertical-align: bottom; max-width: 100%; border-width: 0px; border-color: initial; border-image: initial; text-align: justify;\" width=\"590\"></p>"
					+ "                                                            </div>"
					+ "                                                        </td>"
					+ "                                                    </tr>"
					+ "                                                </tbody>"
					+ "                                            </table>"
					+ "                                        </td>"
					+ "                                    </tr>"
					+ "                                </tbody>"
					+ "                            </table>"
					+ "                            <table class=\"stb-block\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"overflow: hidden; margin: 0px auto; padding: 0px; width:100%;max-width: 630px; clear: both; background: none; border:0;\" width=\"100%;\">"
					+ "                                <tbody>"
					+ "                                    <tr>"
					+ "                                        <td style=\"padding: 15px 0 15px 0; line-height: 10px; border-width: 0px;\">"
					+ "                                            <table class=\"stb-cell-wrap\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"table-layout:fixed;padding: 0px 5px;\" width=\"100%\">"
					+ "                                                <tbody>"
					+ "                                                    <tr>"
					+ "                                                        <td style=\"padding: 0px 15px; line-height: 10px;\" width=\"100%\">"
					+ "                                                            <div>"
					+ "                                                                <p style=\"text-align: justify; margin: 0px; width:100%\"><img src=\"https://s3.ap-northeast-2.amazonaws.com/img.stibee.com/11188_1599120912.png\" style=\"width: 100%; display: inline; vertical-align: bottom; max-width: 100%; border-width: 0px; border-color: initial; border-image: initial; text-align: justify;\" width=\"590\" loading=\"eager\"></p>"
					+ "                                                            </div>"
					+ "                                                        </td>"
					+ "                                                    </tr>"
					+ "                                                </tbody>"
					+ "                                            </table>"
					+ "                                        </td>"
					+ "                                    </tr>"
					+ "                                </tbody>"
					+ "                            </table>"
					+ "                            <table class=\"stb-block\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"overflow: hidden; margin: 0px auto; padding: 0px; width:100%;max-width: 630px; clear: both; background: none;border:0;\" width=\"100%\">"
					+ "                                <tbody>"
					+ "                                    <tr>"
					+ "                                        <td style=\"padding: 5px 0 5px 0; line-height: 1.8; border-width: 0px;font-size: 14px;\">"
					+ "                                            <table class=\"stb-cell-wrap\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"padding: 0px 5px;\" width=\"100%\">"
					+ "                                                <tbody>"
					+ "                                                    <tr>"
					+ "                                                        <td class=\"stb-text-box\" style=\"padding: 0px 15px;mso-line-height-rule: exactly;line-height:1.8;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;word-break: break-word;font-size: 14px;font-family: AppleSDGothic, apple sd gothic neo, noto sans korean, noto sans korean regular, noto sans cjk kr, noto sans cjk, nanum gothic, malgun gothic, dotum, arial, helvetica, MS Gothic, sans-serif!important; color: #333333\" width=\"100%\">"
					+ "                                                            <div style=\"text-align: center; font-size: 18px;\">"
					+ "                                                                <span style=\"font-weight: bold;\">안녕하세요, {아이디}님!&nbsp;</span>"
					+ "                                                            </div>"
					+ "                                                            <div style=\"text-align: center; font-size: 18px;\">"
					+ "                                                                <span style=\"font-weight: bold;\">딜링아트의 세계에 오신 걸 환영합니다.&nbsp;&nbsp;</span><br></div>"
					+ "                                                        </td>"
					+ "                                                    </tr>"
					+ "                                                </tbody>"
					+ "                                            </table>"
					+ "                                        </td>"
					+ "                                    </tr>"
					+ "                                </tbody>"
					+ "                            </table>"
					+ "                            <table class=\"stb-block\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"overflow: hidden; margin: 0px auto; padding: 0px; width:100%;max-width: 630px; clear: both; background: none;border:0;\" width=\"100%\">"
					+ "                                <tbody>"
					+ "                                    <tr>"
					+ "                                        <td style=\"padding: 20px 0 15px 0;line-height: 1.8; border-width: 0px;\">"
					+ "                                            <table class=\"stb-cell-wrap\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"padding: 0px 0;\" width=\"100%\">"
					+ "                                                <tbody>"
					+ "                                                    <tr>"
					+ "                                                        <td style=\"padding: 0 0\">"
					+ "                                                            <div style=\"height: 0; background: none; padding: 0px; border-top-width:1px;border-top-style:dotted;border-top-color:#999999;margin:0 15px\"></div>"
					+ "                                                        </td>"
					+ "                                                    </tr>"
					+ "                                                </tbody>"
					+ "                                            </table>"
					+ "                                        </td>"
					+ "                                    </tr>"
					+ "                                </tbody>"
					+ "                            </table>"
					+ "                            <table class=\"stb-block\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"overflow: hidden; margin: 0px auto; padding: 0px; width:100%;max-width: 630px; clear: both; background: none;border:0;\" width=\"100%\">"
					+ "                                <tbody>"
					+ "                                    <tr>"
					+ "                                        <td style=\"padding: 15px 0 15px 0; line-height: 1.8; border-width: 0px;font-size: 14px;\">"
					+ "                                            <table class=\"stb-cell-wrap\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"padding: 0px 5px;\" width=\"100%\">"
					+ "                                                <tbody>"
					+ "                                                    <tr>"
					+ "                                                        <td class=\"stb-text-box\" style=\"padding: 0px 15px;mso-line-height-rule: exactly;line-height:1.8;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;word-break: break-word;font-size: 14px;font-family: AppleSDGothic, apple sd gothic neo, noto sans korean, noto sans korean regular, noto sans cjk kr, noto sans cjk, nanum gothic, malgun gothic, dotum, arial, helvetica, MS Gothic, sans-serif!important; color: #333333\" width=\"100%\">"
					+ "                                                            <div style=\"text-align: center; color: rgb(51, 51, 51); font-size: 16px;\">"
					+ "                                                                <span style=\"font-weight: bold;\">딜링아트는 미술품 거래의 새로운 기준을 제시합니다.&nbsp;&nbsp;</span><br></div>"
					+ "                                                            <div style=\"text-align: center;\">"
					+ "                                                                <span style=\"color: rgb(51, 51, 51); font-family: AppleSDGothic, &quot;apple sd gothic neo&quot;, &quot;noto sans korean&quot;, &quot;noto sans korean regular&quot;, &quot;noto sans cjk kr&quot;, &quot;noto sans cjk&quot;, &quot;nanum gothic&quot;, &quot;malgun gothic&quot;, dotum, arial, helvetica, MS Gothic, sans-serif; font-style: normal;\"><br></span>"
					+ "                                                            </div>"
					+ "                                                            <div style=\"text-align: center;\">"
					+ "                                                                <span style=\"color: rgb(51, 51, 51); font-family: AppleSDGothic, &quot;apple sd gothic neo&quot;, &quot;noto sans korean&quot;, &quot;noto sans korean regular&quot;, &quot;noto sans cjk kr&quot;, &quot;noto sans cjk&quot;, &quot;nanum gothic&quot;, &quot;malgun gothic&quot;, dotum, arial, helvetica, MS Gothic, sans-serif; font-style: normal; font-weight: bold;\">□ 품격 있는 안심 서비스&nbsp;</span>"
					+ "                                                            </div>"
					+ "                                                            <div style=\"text-align: center; \">&nbsp;보증서 기반 거래, 프리미엄 운송 서비스를 제공합니다.&nbsp;</div>"
					+ "                                                            <div style=\"text-align: center; \"><br></div>"
					+ "                                                            <div style=\"text-align: center; \">"
					+ "                                                                <span style=\"font-weight: bold;\">□ 투명한 공개 경매&nbsp;</span>"
					+ "                                                            </div>"
					+ "                                                            <div style=\"text-align: center; \">실시간으로 경매 진행 현황을 확인할 수 있습니다.&nbsp;</div>"
					+ "                                                            <div style=\"text-align: center; \"><br></div>"
					+ "                                                            <div style=\"text-align: center; \">"
					+ "                                                                <span style=\"font-weight: bold;\">□ 온라인 다이렉트 결제 번거로운 과정은 이제 그만!&nbsp;</span>"
					+ "                                                            </div>"
					+ "                                                            <div style=\"text-align: center; \">온라인으로 간편하게 결제가 가능합니다.&nbsp;</div>"
					+ "                                                            <div style=\"text-align: center; \"><br></div>"
					+ "                                                            <div style=\"text-align: center; \">"
					+ "                                                                <span style=\"font-weight: bold;\">&nbsp;□ 다이어트 수수료&nbsp;</span>"
					+ "                                                            </div>"
					+ "                                                            <div style=\"text-align: center; \">거래 수수료의 거품을 빼고 합리적인 수수료를 제공합니다.&nbsp;</div>"
					+ "                                                            <div style=\"text-align: center; \"><br></div>"
					+ "                                                            <div style=\"text-align: center; \">&nbsp;<span style=\"font-weight: bold;\">□ 간편 판매 등록&nbsp;</span>"
					+ "                                                            </div>"
					+ "                                                            <div style=\"text-align: center; \">보증서만 업로드하면 딜링아트 에서 등록을 도와드립니다.&nbsp;<br></div>"
					+ "                                                        </td>"
					+ "                                                    </tr>"
					+ "                                                </tbody>"
					+ "                                            </table>"
					+ "                                        </td>"
					+ "                                    </tr>"
					+ "                                </tbody>"
					+ "                            </table>"
					+ "                            <table class=\"stb-block\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"overflow: hidden; margin: 0px auto; padding: 0px; max-width: 630px; clear: both; background: none;border:0;\" width=\"100%\">"
					+ "                                <tbody>"
					+ "                                    <tr>"
					+ "                                        <td style=\"padding: 15px 0 15px 0; text-align: center;margin:0 auto 0 auto\">"
					+ "                                            <div class=\"stb-cell-wrap-sns\" style=\"margin: 0 auto\">"
					+ "                                                <span style=\"list-style: none; padding: 0px 3px; margin: 0px;\">"
					+ "                                                    <a style=\"padding: 0px 3px; border-width: 0px; border-image: initial; text-decoration: none; display: inline-block;\" target=\"_blank\" title=\"홈페이지\"><img height=\"32px\" src=\"https://stibee.com/assets/images/sns/white/sns_icon_homepage.png\" style=\"width: 32px; height: 32px; display: block; border-width: 0px; border-color: initial; border-image: initial;\" width=\"32\"></a>"
					+ "                                                </span>"
					+ "                                                <span style=\"list-style: none; padding: 0px 3px; margin: 0px;\">"
					+ "                                                    <a style=\"padding: 0px 3px; border-width: 0px; border-image: initial; text-decoration: none; display: inline-block;\" target=\"_blank\" title=\"페이스북\"><img height=\"32px\" src=\"https://stibee.com/assets/images/sns/white/sns_icon_facebook.png\" style=\"width: 32px; height: 32px; display: block; border-width: 0px; border-color: initial; border-image: initial;\" width=\"32\"></a>"
					+ "                                                </span>"
					+ "                                                <span style=\"list-style: none; padding: 0px 3px; margin: 0px;\">"
					+ "                                                    <a style=\"padding: 0px 3px; border-width: 0px; border-image: initial; text-decoration: none; display: inline-block;\" target=\"_blank\" title=\"블로그\"><img height=\"32px\" src=\"https://stibee.com/assets/images/sns/white/sns_icon_blog.png\" style=\"width: 32px; height: 32px; display: block; border-width: 0px; border-color: initial; border-image: initial;\" width=\"32\"></a>"
					+ "                                                </span>"
					+ "                                                <span style=\"list-style: none; padding: 0px 3px; margin: 0px;\">"
					+ "                                                    <a style=\"padding: 0px 3px; border-width: 0px; border-image: initial; text-decoration: none; display: inline-block;\" target=\"_blank\" title=\"유튜브\"><img height=\"32px\" src=\"https://stibee.com/assets/images/sns/white/sns_icon_youtube.png\" style=\"width: 32px; height: 32px; display: block; border-width: 0px; border-color: initial; border-image: initial;\" width=\"32\"></a>"
					+ "                                                </span>"
					+ "                                                <span style=\"list-style: none; padding: 0px 3px; margin: 0px;\">"
					+ "                                                    <a style=\"padding: 0px 3px; border-width: 0px; border-image: initial; text-decoration: none; display: inline-block;\" target=\"_blank\" title=\"브런치\"><img height=\"32px\" src=\"https://stibee.com/assets/images/sns/white/sns_icon_brunch.png\" style=\"width: 32px; height: 32px; display: block; border-width: 0px; border-color: initial; border-image: initial;\" width=\"32\"></a>"
					+ "                                                </span>"
					+ "                                            </div>"
					+ "                                        </td>"
					+ "                                    </tr>"
					+ "                                </tbody>"
					+ "                            </table>"
					+ "                            <table class=\"stb-block\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"overflow: hidden; margin: 0px auto; padding: 0px; width:100%;max-width: 630px; clear: both; background: none;border:0;\" width=\"100%\">"
					+ "                                <tbody>"
					+ "                                    <tr>"
					+ "                                        <td style=\"padding: 20px 0 15px 0;line-height: 1.8; border-width: 0px;\">"
					+ "                                            <table class=\"stb-cell-wrap\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"padding: 0px 0;\" width=\"100%\">"
					+ "                                                <tbody>"
					+ "                                                    <tr>"
					+ "                                                        <td style=\"padding: 0 0\">"
					+ "                                                            <div style=\"height: 0; background: none; padding: 0px; border-top-width:1px;border-top-style:dotted;border-top-color:#999999;margin:0 15px\"></div>"
					+ "                                                        </td>"
					+ "                                                    </tr>"
					+ "                                                </tbody>"
					+ "                                            </table>"
					+ "                                        </td>"
					+ "                                    </tr>"
					+ "                                </tbody>"
					+ "                            </table>"
					+ "                            <table class=\"stb-block\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"overflow: hidden; margin: 0px auto; padding: 0px; width:100%;max-width: 630px; clear: both; background: none;border:0;\" width=\"100%\">"
					+ "                                <tbody>"
					+ "                                    <tr>"
					+ "                                        <td style=\"padding: 15px 0 15px 0; border-width: 0px;\">"
					+ "                                            <table class=\"stb-cell-wrap\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" style=\"padding: 0px 5px;\" width=\"100%\">"
					+ "                                                <tbody>"
					+ "                                                    <tr>"
					+ "                                                        <td class=\"stb-text-box\" width=\"100%\" style=\"padding: 0px 10px;mso-line-height-rule: exactly;line-height:1.8;-ms-text-size-adjust: 100%;-webkit-text-size-adjust: 100%;word-break: break-word;font-size: 12px;font-family: AppleSDGothic, apple sd gothic neo, noto sans korean, noto sans korean regular, noto sans cjk kr, noto sans cjk, nanum gothic, malgun gothic, dotum, arial, helvetica, MS Gothic, sans-serif!important;text-align: center; color: #606060\">"
					+ "                                                            <span style=\"color: rgb(153, 153, 153);\">맨션나인 주식회사</span><br><div>"
					+ "                                                                <span style=\"color: rgb(153, 153, 153);\">help@dealing-art.com</span>"
					+ "                                                            </div>"
					+ "                                                            <div>"
					+ "                                                                <span style=\"font-size: 12px; color: rgb(153, 153, 153);\">"
					+ "                                                                    <span style=\"font-size: 12px;\">주소:&nbsp;</span>"
					+ "                                                                    <span style=\"font-size: 12px; text-align: start;\">서울특별시 마포구 월드컵로28길 9 3층&nbsp;</span>"
					+ "                                                                </span>"
					+ "                                                                <span style=\"font-size: 14px; text-align: start;\"></span>"
					+ "                                                                <span style=\"color: rgb(153, 153, 153); font-size: inherit;\">전화번호: 070-4267-7371</span>"
					+ "                                                            </div>"
					+ "                                                            <div>"
					+ "                                                                <span style=\"color: rgb(153, 153, 153);\">"
					+ "                                                                    <a href=\"$%unsubscribe%$\" style=\"text-decoration: underline; color: rgb(153, 153, 153); display: inline;\" class=\" link-edited\" target=\"_blank\">수신거부</a>&nbsp;<a href=\"$%unsubscribe%$\" style=\"text-decoration: underline; color: rgb(153, 153, 153); display: inline;\" class=\" link-edited\" target=\"_blank\">Unsubscribe</a>"
					+ "                                                                </span>"
					+ "                                                                <b></b>"
					+ "                                                            </div>"
					+ "                                                        </td>"
					+ "                                                    </tr>"
					+ "                                                </tbody>"
					+ "                                            </table>"
					+ "                                        </td>"
					+ "                                    </tr>"
					+ "                                </tbody>"
					+ "                            </table>"
					+ "                        </td>"
					+ "                    </tr>"
					+ "                </tbody>"
					+ "            </table>"
					+ "        </div>"
					+ "    </body>"
					+ "</html>";
			
					
			//String memberEmail = (String) param.get("mbrId");
			
			String memberEmail = "swordbass.j3@gmail.com";
			
			tomap.put("type", "R");
			tomap.put("address", memberEmail);
			mList.add(tomap);
			
			params.put("title", title);
			params.put("body", content);
			//params.setContent(content, "text/html; charset=UTF-8");
			params.put("recipients", mList);
			
			rtnMap = sendMailUtil.sendMail(params);
			if(((int) Double.parseDouble(rtnMap.get("count").toString()) < 0)){
				result = "error";
			}else {
				String sertiString = Integer.toString(serti);
				result = sertiString;
			}
			int paramStatus = -1; 
			
			return result;
		}
	
	//소장품 등록
=======

	// 소장품 등록
>>>>>>> 2f632496492c734e99c8023a02aadefecc4af32e
	@PostMapping("/collectionReg")
	@ResponseBody
	public int collectionReg(@RequestPart(value = "work") Map<String, Object> param,
			@RequestPart(value = "file") List<MultipartFile> multipartFiles) {
		System.out.println("##################### collectionReg param : " + param);
		System.out.println("##################### collectionReg file : " + multipartFiles);
		System.out.println("######### file : " + multipartFiles);
		List<FileVo> fileVo = awsS3Service.uploadFiles(multipartFiles, "userCollection");
		System.out.println("############## fileVO : " + fileVo);
		param.put("workMainImgUrl", fileVo.get(0).getFileUrl());
		param.put("workImgFrtUrl", fileVo.get(1).getFileUrl());
		param.put("workImgRerUrl", fileVo.get(2).getFileUrl());
		if (fileVo.size() >= 4) {
			param.put("workGrtUrl", fileVo.get(3).getFileUrl());
		}
		if (fileVo.size() >= 5) {
			param.put("workImgLefUrl", fileVo.get(4).getFileUrl());
		}
		if (fileVo.size() >= 6) {
			param.put("workImgRitUrl", fileVo.get(5).getFileUrl());
		}
		if (fileVo.size() >= 7) {
			param.put("workImgTopUrl", fileVo.get(6).getFileUrl());
		}
		if (fileVo.size() >= 8) {
			param.put("workImgBotUrl", fileVo.get(7).getFileUrl());
		}
		int result = myPageService.collectionReg(param);
		return result;
	}

	// 나의 작품 등록
	@PostMapping("/myWorkReg")
	@ResponseBody
	public int myWorkReg(@RequestPart(value = "work") Map<String, Object> param,
			@RequestPart(value = "file") List<MultipartFile> multipartFiles) {
		System.out.println("##################### collectionReg param : " + param);
		System.out.println("##################### collectionReg file : " + multipartFiles);
		System.out.println("######### file : " + multipartFiles);
		List<FileVo> fileVo = awsS3Service.uploadFiles(multipartFiles, "artistWork");
		System.out.println("############## fileVO : " + fileVo);
		param.put("workMainImgUrl", fileVo.get(0).getFileUrl());
		param.put("workImgFrtUrl", fileVo.get(1).getFileUrl());
		param.put("workImgRerUrl", fileVo.get(2).getFileUrl());
		if (fileVo.size() >= 4) {
			param.put("workGrtUrl", fileVo.get(3).getFileUrl());
		}
		if (fileVo.size() >= 5) {
			param.put("workImgLefUrl", fileVo.get(4).getFileUrl());
		}
		if (fileVo.size() >= 6) {
			param.put("workImgRitUrl", fileVo.get(5).getFileUrl());
		}
		if (fileVo.size() >= 7) {
			param.put("workImgTopUrl", fileVo.get(6).getFileUrl());
		}
		if (fileVo.size() >= 8) {
			param.put("workImgBotUrl", fileVo.get(7).getFileUrl());
		}
		int result = myPageService.myWorkReg(param);
		return result;
	}

	// 나의 작품 수정 페이지 오픈
	@RequestMapping("/myWorkMod")
	@ResponseBody
	public ModelAndView myWorkMod(@RequestParam(value = "workSq", required = false) String workSq) {
		ModelAndView mv = new ModelAndView("thymeleaf/fo/myPage/myWork_mod");
		Map<String, Object> result = myPageService.myWorkMod(workSq);
		mv.addObject("result", result);
		return mv;
	}

	// 나의 작품 수정
	@PostMapping("/myWorkCor")
	@ResponseBody
	public int myWorkCor(@RequestPart(value = "work") Map<String, Object> param, @RequestPart(value = "file") @Nullable List<MultipartFile> multipartFiles) {
		System.out.println("##################### collectionReg param : " + param);
		System.out.println("##################### collectionReg file : " + multipartFiles);
		System.out.println("######### file : " + multipartFiles);
		if (multipartFiles != null) {
			List<FileVo> fileVo = awsS3Service.uploadFiles(multipartFiles, "artistWork");
			param.put("workMainImgUrl", fileVo.get(0).getFileUrl());
			param.put("workImgFrtUrl", fileVo.get(1).getFileUrl());
			param.put("workImgRerUrl", fileVo.get(2).getFileUrl()); 
			if(fileVo.size() >= 4){ 
				param.put("workGrtUrl", fileVo.get(3).getFileUrl()); 
			} 
			if(fileVo.size() >= 5){ 
				param.put("workImgLefUrl", fileVo.get(4).getFileUrl()); 
				}
			if(fileVo.size() >= 6){ 
				param.put("workImgRitUrl",fileVo.get(5).getFileUrl()); 
			} 
			if(fileVo.size() >= 7){
				param.put("workImgTopUrl", fileVo.get(6).getFileUrl()); 
			} 
			if(fileVo.size() >= 8){ 
				param.put("workImgBotUrl", fileVo.get(7).getFileUrl()); 
			}
			System.out.println("############## fileVO : " + fileVo);
		}
		
		int result = myPageService.myWorkCor(param);
		 
		return result;
	}
	
	// 나의 작품 수정 페이지 오픈
	@RequestMapping("/myCollectionMod")
	@ResponseBody
	public ModelAndView myCollectionMod(@RequestParam(value = "workSq", required = false) String workSq) {
		ModelAndView mv = new ModelAndView("thymeleaf/fo/myPage/collection_mod");
		Map<String, Object> result = myPageService.myCollectionMod(workSq);
		mv.addObject("result", result);
		return mv;
	}
	
	// 소장품 등록
		@PostMapping("/collectionCor")
		@ResponseBody
		public int collectionCor(@RequestPart(value = "work") Map<String, Object> param, @RequestPart(value = "file") @Nullable List<MultipartFile> multipartFiles) {
			System.out.println("##################### collectionReg param : " + param);
			System.out.println("##################### collectionReg file : " + multipartFiles);
			System.out.println("######### file : " + multipartFiles);
			if (multipartFiles != null) {
				List<FileVo> fileVo = awsS3Service.uploadFiles(multipartFiles, "userCollection");
				System.out.println("############## fileVO : " + fileVo);
				param.put("workMainImgUrl", fileVo.get(0).getFileUrl());
				param.put("workImgFrtUrl", fileVo.get(1).getFileUrl());
				param.put("workImgRerUrl", fileVo.get(2).getFileUrl());
				if (fileVo.size() >= 4) {
					param.put("workGrtUrl", fileVo.get(3).getFileUrl());
				}
				if (fileVo.size() >= 5) {
					param.put("workImgLefUrl", fileVo.get(4).getFileUrl());
				}
				if (fileVo.size() >= 6) {
					param.put("workImgRitUrl", fileVo.get(5).getFileUrl());
				}
				if (fileVo.size() >= 7) {
					param.put("workImgTopUrl", fileVo.get(6).getFileUrl());
				}
				if (fileVo.size() >= 8) {
					param.put("workImgBotUrl", fileVo.get(7).getFileUrl());
				}
			}
			int result = myPageService.collectionCor(param);
			return result;
		}

}
