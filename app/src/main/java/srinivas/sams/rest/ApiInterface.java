package srinivas.sams.rest;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import srinivas.sams.model.Appopen;
import srinivas.sams.model.GetInstall;
import srinivas.sams.model.GetProducts;
import srinivas.sams.model.GetRecce;
import srinivas.sams.model.Login_Service;
import srinivas.sams.model.UploadInstall;
import srinivas.sams.model.UploadRecce;

/**
 * Created by USER on 25-07-2017.
 */

public interface ApiInterface {
    @FormUrlEncoded
    @POST("index.php?r=restapi/user/app-open")
    Call<Appopen> getVendors(@Field("imei_number_1") String imei_number_1, @Field("imei_number_2") String imei_number_2);

    @FormUrlEncoded
    @POST("index.php?r=restapi/user/login")
    Call<Login_Service> getProjects(@Field("vendor_id") String vendor_id, @Field("crew_person_id") String crew_person_id);

    @FormUrlEncoded
    @POST("index.php?r=restapi/user/get-recce-image-has-to-be-uploaded-recces")
    Call<GetRecce> getRecces(@Field("key") String key, @Field("user_id") String user_id,
                             @Field("project_id") String project_id, @Field("crew_person_id") String crew_person_id
    );

    @FormUrlEncoded
    @POST("index.php?r=restapi/user/get-installation-image-has-to-be-uploaded-recces")
    Call<GetInstall> getInstall(@Field("key") String key, @Field("user_id") String user_id,
                                @Field("project_id") String project_id, @Field("crew_person_id") String crew_person_id
    );

    @FormUrlEncoded
    @POST("index.php?r=restapi/user/get-products-list")
    Call<GetProducts> getProduct(@Field("key") String key, @Field("user_id") String user_id,
                                 @Field("project_id") String project_id, @Field("crew_person_id") String crew_person_id
    );

    @Multipart
    @POST("index.php?r=restapi/user/upload-recce-image")
    Call<UploadRecce> getUploadRecce(@Query("uom_id") String uom_id, @Query("width") String width,
                                     @Query("height") String Query, @Query("w_f") String w_f, @Query("h_f") String h_f,
                                     @Query("w_i") String w_i, @Query("h_i") String h_i, @Query("product_id") String product_id
            , @Part("key") RequestBody key,
                                     @Part("user_id") RequestBody user_id, @Part("crew_person_id") RequestBody crew_person_id,
                                     @Part("recce_id") RequestBody recce_id,
                                     @Part MultipartBody.Part recce_image,
                                     @Part MultipartBody.Part recce_image_1,
                                     @Part MultipartBody.Part recce_image_2,
                                     @Part MultipartBody.Part recce_image_3,
                                     @Part MultipartBody.Part recce_image_4,
                                     @Part("latitude") RequestBody latitude,
                                     @Part("longitude") RequestBody longitude, @Part("outlet_address") RequestBody outlet_address
    );

    @Multipart
    @POST("index.php?r=restapi/user/upload-installation-image")
    Call<UploadInstall> getUploadInstall(@Query("installation_date") String installation_date,
                                         @Query("installation_remarks") String installation_remarks,
                                         @Part("key") RequestBody key,
                                         @Part("user_id") RequestBody user_id, @Part("crew_person_id") RequestBody crew_person_id,
                                         @Part("recce_id") RequestBody recce_id, @Part("project_id") RequestBody project_id,
                                         @Part MultipartBody.Part installation_image, @Part MultipartBody.Part installation_image_1,
                                         @Part MultipartBody.Part installation_image_2
    );


}
