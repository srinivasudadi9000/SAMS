package srinivas.sams.model;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by venky on 11-Aug-17.
 */

public class Login_Service {
    @SerializedName("key")
    String key;

    @SerializedName("user_id")
    JsonElement user_id;

    @SerializedName("crew_person_id")
    String crew_person_id;

    @SerializedName("crew_person_name")
    String crew_person_name;

    @SerializedName("projects")
    List<Projects> projects;

    public String getCrew_person_id() {
        return crew_person_id;
    }

    public String getCrew_person_name() {
        return crew_person_name;
    }

    public String getKey() {
        return key;
    }

    public JsonElement getUser_id() {
        return user_id;
    }

    public List<Projects> getProjects() {
        return projects;
    }

}
