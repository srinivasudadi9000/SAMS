package srinivas.sams.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by venky on 11-Aug-17.
 */

public class Appopen {
    @SerializedName("vendors_list")
    List<Vendor> vendors_list;

    @SerializedName("status")
    String status;

    public List<Vendor> getVendors_list() {
        return vendors_list;
    }

    public String getStatus() {
        return status;
    }
}
