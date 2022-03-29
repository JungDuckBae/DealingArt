package com.da.fo.dao;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.da.mapper.MemberMapper;
import com.da.sample.service.CommonService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.da.sample.service.CommonService;



@Repository
public class MemberDao {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	JPAQueryFactory queryFactory; //QueryDsl을 사용하기 위해
	
	@Autowired
	MemberMapper memberMapper;
	
	//회원정보 입력
	public void memberInsert(Map<String, Object> param){
		
		System.out.println("FO_MEMBER_INSERT_REQUEST = " + param);
		memberMapper.memberInsert(param);
		
	}
	
	/*
	 * 로그인 기능
	 * param : loginId, loginPw
	 * return : int
	 */
	public Map<String, Object> login(Map<String, Object> param) {
		return memberMapper.login(param);
	}
	
	/*
	 * 회원탈퇴
	 * param : name, email, password
	 * return : int
	 */
	public int memberWithdrawal(Map<String, Object> param) {
		return memberMapper.memberWithdrawal(param);
	}
	
	/*
	 * 회원탈퇴 체크
	 * param : name, email, password
	 * return : Map
	 */
	public int memberWithdrawalCheck(Map<String, Object> param){
		return memberMapper.memberWithdrawalCheck(param);
	}

}
