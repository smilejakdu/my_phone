package com.example.sh.androidregisterandlogin.SearchUtil;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;


public interface NaverShoppingSearchService {

    @Headers({
            "X-Naver-Client-Id: hRkd_0C0i4WcIm4wY_vK",
            "X-Naver-Client-Secret: sMgN4zxD2S"
    })
    @GET("shop")
    Observable<SearchDataList> getSearchDataList(@Query("query") String queryKey,
                                                 @Query("display") int displayValue,
                                                 @Query("start") int start,
                                                 @Query("sort") String sortType);
}
