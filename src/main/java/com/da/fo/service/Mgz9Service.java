package com.da.fo.service;

import java.util.List;
import java.util.Map;

public interface Mgz9Service {
	
	/*
	 * MGZ9 목록 조회
	 * PARAM : 
		#MGZ_TYP_CD
			IST - INSIGHTS
			MDA - MEDIA
			EBI - EXHIBITION
	 * RETURN : MGZ9 목록
	 */
	List selectMgz9List(String param);
	
	/*
	 * MGZ9 상세 조회
	 * PARAM : MGZ_SQ
	 * RETURN : MGZ9 상세
	 */
	Map<String, Object> selectMgz9Dtl(String param);
	
}
