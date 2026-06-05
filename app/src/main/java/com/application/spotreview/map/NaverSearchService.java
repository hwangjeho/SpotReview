package com.application.spotreview.map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;
public interface NaverSearchService {
    @GET("v1/search/local.json") // 지역 검색 엔드포인트
    Call<SearchResponse> getLocalSearch(
            @Header("X-Naver-Client-Id") String clientId,
            @Header("X-Naver-Client-Secret") String clientSecret,
            @Query("query") String query,         // 검색어 (예: "시흥시청 맛집")
            @Query("display") int display         // 가져올 개수 (최대 50개)
    );
}
