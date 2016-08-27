package com.example.mrboudar.playboy.api;


import com.example.mrboudar.playboy.DataResponse.VideoCategoryResponse;
import com.example.mrboudar.playboy.common.Utils;
import com.example.mrboudar.playboy.model.ResponseBody;
import com.example.mrboudar.playboy.model.SeasonDetailBean;
import com.example.mrboudar.playboy.model.SeasonQueryBean;
import com.example.mrboudar.playboy.model.User;
import com.example.mrboudar.playboy.model.VideoUrlBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by MrBoudar on 16/8/13.
 */
public interface ApiService {

    /****************************************unused*******************************************/

    @POST("/season/detail")
    Observable<SeasonDetailBean> getSeasonDetail(@Query("seasonId") int id);

    /****************************************used*******************************************/

    @POST("/season/query")
    Observable<SeasonQueryBean> getSeasonQuery(@Query("page") int page, @Query("rows") int rows, @Query("categoryId") int id,@Query("sort") String sort,@Query("cat") String type,@Query("isFinish") boolean isfinish);

    @GET("/season/search")
    Observable<SeasonQueryBean> getSeasonList(@Query("page") int page, @Query("rows") int rows, @Query("name") String name);

    @GET("/constant/category")
    Observable<ResponseBody<VideoCategoryResponse>> getHomeCateGory();

    @POST("/video/categoryQuery")
    Observable<SeasonQueryBean> getVideoByCategoryId(@Query("page") int page, @Query("rows") int rows, @Query("categoryId") int id,@Query("sort") String sort);


    @POST("/video/detail")
    Observable<SeasonDetailBean> getVideoHomeDetail(@Query("videoId") int videoId,@Query("token") String token);

    /**
     * 获取视频播放地址
     *
     * @param quality    画质 high高清
     * @param seasonId   电视剧id
     * @param episodeSid 电视剧分集id
     * @return
     */
    @FormUrlEncoded
    @POST("video/findM3u8ByEpisodeSid")
    Observable<VideoUrlBean> getVideoUrl(@Field("quality") String quality, @Field("seasonId") String seasonId, @Field("episodeSid") String episodeSid);
    /****************************************used*******************************************/
}
