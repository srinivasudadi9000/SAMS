package srinivas.sams.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by venky on 11-Aug-17.
 */

public class Vendor {
    @SerializedName("vendor_id")
    String vendor_id;

    @SerializedName("vendor_code")
    String vendor_code;

    @SerializedName("vendor_name")
    String vendor_name;

    @SerializedName("crew_person_id")
    String crew_person_id;

     Vendor(String vendor_id,String vendor_code,String vendor_name,String crew_person_id){
            this.vendor_id= vendor_id;this.vendor_code=vendor_code;this.vendor_name= vendor_name;this.crew_person_id=crew_person_id;
     }

    public String getCrew_person_id() {
        return crew_person_id;
    }

    public String getVendor_code() {
        return vendor_code;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public String getVendor_name() {
        return vendor_name;
    }
}
