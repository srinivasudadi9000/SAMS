package srinivas.sams.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by venky on 11-Aug-17.
 */

public class Umo {
    @SerializedName("uom_id")
    String uom_id;

    @SerializedName("uom_name")
    String uom_name;

    public String getUom_id() {
        return uom_id;
    }

    public String getUom_name() {
        return uom_name;
    }
}
