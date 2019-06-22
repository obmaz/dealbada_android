package com.moon.dealocean.network.dealbada;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Created by zambo on 2016-06-27.
 */
public interface DealbadaAPIService {

    @GET
    Call<ResponseBody> getDealList(@Url String url, @Query("page") int page);

    @GET
    Call<ResponseBody> getDetail(@Url String url, @Query("c_page") int commentPageNum);

    @GET("/")
    Call<ResponseBody> getUserInfo();


//    @GET("bbs/board.php?&device=mobile")
//    Call<ResponseBody> getDetail(@Query("bo_table") String boardType, @Query("wr_id") int wr_id);
}
