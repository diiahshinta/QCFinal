package com.kosme.sjpqrcode.api;

import com.kosme.sjpqrcode.model.LevelProduct;
import com.kosme.sjpqrcode.model.Login;
import com.kosme.sjpqrcode.model.Reject;
import com.kosme.sjpqrcode.model.Replace;
import com.kosme.sjpqrcode.model.Response;
import com.kosme.sjpqrcode.model.ResponseCheckUpdate;
import com.kosme.sjpqrcode.model.ResponseReject;
import com.kosme.sjpqrcode.model.ResponseStatus;
import com.kosme.sjpqrcode.msglow.ModelData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("dataprint")
    Call<Response> getDetails(@Header ("Authorization") String token,
                              @Query("barcode") String barcode,
                              @Query("level") String level);

    @GET("dataprint")
    Call<LevelProduct> getDetailsProduk(@Header ("Authorization") String token,
                                        @Query("barcode") String barcode,
                                        @Query("level") String level);

    @GET("produksi/data/reject")
    Call<ResponseReject> getReject(@Query("barcode") String barcode);

    @GET("produksi/data/replace")
    Call<Replace> replace(@Query("old") String oldBarcode,
                          @Query("new") String newBarcode,
                          @Query("batch") String batch,
                          @Query("production_date")  String md,
                          @Query("expired_date") String ed);

    @GET("qc/check")
    Call<Replace> cekQC(@Query("barcode") String barcode,
                        @Query("name") String name,
                        @Query("department") String dept);


    @GET("gudang/sjp")
    Call<String> get_data_barcode(@Query("barcode") String barcode);

    @FormUrlEncoded
    @POST("login")
    Call<Login> loginRequest(@Field("email") String email,
                             @Field("password") String password);

    @GET("dataprint/check/{status}")
    Call<ResponseStatus> statusRequest(@Path("status") String status,
                                       @Query("level") String level,
                                       @Query("barcode") String barcode,
                                       @Query("note") String note,
                                       @Header ("Authorization") String token);

    @GET("kci")
    Call<ModelData> getmsglow(@Query("barcode") String barcode);


    @Headers("Content-Type: application/json")
    @GET("check_data_qr_pallet")
    Call<ResponseCheckUpdate> checkdata(@Query("barcode") String barcode);


}
