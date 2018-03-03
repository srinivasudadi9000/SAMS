package srinivas.sams.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by venky on 11-Aug-17.
 */

public class Products {
    @SerializedName("product_name")
    String product_name;

    @SerializedName("product_id")
    String product_id;

    public Products(String product_name, String product_id){
        this.product_name = product_name;this.product_id = product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_id() {
        return product_id;
    }

}
