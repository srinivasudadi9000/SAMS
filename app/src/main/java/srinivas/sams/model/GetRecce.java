package srinivas.sams.model;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by venky on 11-Aug-17.
 */

public class GetRecce {
    @SerializedName("key")
    JsonElement key;

    @SerializedName("user_id")
    JsonElement user_id;

    @SerializedName("crew_person_name")
    JsonElement crew_person_name;

    @SerializedName("crew_person_id")
    JsonElement crew_person_id;

    @SerializedName("uoms_list")
    List<Umo> uoms_list;

    @SerializedName("recces")
    List<Recce> recces;

    @SerializedName("recce_image_path")
    JsonElement recce_image_path;

    public GetRecce(JsonElement key,JsonElement user_id,JsonElement crew_person_name,JsonElement crew_person_id,List<Umo> uoms_list
    ,List<Recce> recces,JsonElement recce_image_path){
        this.key=key;this.user_id=user_id;this.crew_person_id=crew_person_id;this.crew_person_name=crew_person_name;
        this.uoms_list=uoms_list;this.recces=recces;this.recce_image_path= recce_image_path;
    }

    public JsonElement getRecce_image_path() {
        return recce_image_path;
    }

    public JsonElement getCrew_person_id() {
        return crew_person_id;
    }

    public JsonElement getCrew_person_name() {
        return crew_person_name;
    }

    public JsonElement getKey() {
        return key;
    }

    public JsonElement getUser_id() {
        return user_id;
    }

    public List<Umo> getUoms_list() {
        return uoms_list;
    }

    public List<Recce> getRecces() {
        return recces;
    }
}
