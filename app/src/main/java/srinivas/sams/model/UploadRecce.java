package srinivas.sams.model;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

/**
 * Created by venky on 14-Aug-17.
 */

public class UploadRecce {

    @SerializedName("key")
    JsonElement key;

    @SerializedName("user_id")
    JsonElement user_id;

    @SerializedName("recce_image1_path")
    String recce_image1_path;

    @SerializedName("crew_person_name")
    JsonElement crew_person_name;

    @SerializedName("crew_person_id")
    JsonElement crew_person_id;

    @SerializedName("recce_image_path")
    JsonElement recce_image_path;

    @SerializedName("message")
    JsonElement message;

    public JsonElement getCrew_person_name() {
        return crew_person_name;
    }
}
