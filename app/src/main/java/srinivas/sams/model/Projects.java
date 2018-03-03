package srinivas.sams.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by venky on 11-Aug-17.
 */

public class Projects {
    @SerializedName("project_name")
    String project_name;

    @SerializedName("project_id")
    String project_id;

    public Projects(String project_name, String project_id){
        this.project_name = project_name;this.project_id = project_id;
    }

    public String getProject_name() {
        return project_name;
    }

    public String getProject_id() {
        return project_id;
    }

}
