package com.da.fo.service;

import java.util.List;
import java.util.Map;

public interface DealService {
	/*
	 * 딜 페이지에서 검색 필터별로 검색 결과를 조회한다.
	 * param : searchOption
	 * return : 검색 결과
	 */
	List dealSerach(Map<String, Object> searchOptions);
}
