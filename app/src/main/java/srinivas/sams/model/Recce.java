package srinivas.sams.model;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

/**
 * Created by venky on 11-Aug-17.
 */

public class Recce {

    @SerializedName("recce_id")
    String recce_id;

    @SerializedName("project_id")
    String project_id;

    @SerializedName("product_name")
    String product_name;

    @SerializedName("zone_id")
    String zone_id;

    @SerializedName("uom_id")
    String uom_id;

    @SerializedName("uom_name")
    String uom_name;



    @SerializedName("recce_date")
    String recce_date;

    @SerializedName("outlet_name")
    String outlet_name;

    @SerializedName("outlet_owner_name")
    String outlet_owner_name;

    @SerializedName("outlet_address")
    String outlet_address;

    @SerializedName("longitude")
    String longitude;

    @SerializedName("latitude")
    String latitude;

    @SerializedName("width")
    String width;

    @SerializedName("height")
    String height;

    @SerializedName("width_feet")
    String width_feet;

    @SerializedName("height_feet")
    String height_feet;

    @SerializedName("width_inches")
    String width_inches;

    @SerializedName("height_inches")
    String height_inches;

    @SerializedName("recce_image")
    String recce_image;

    @SerializedName("recce_image_1")
    String recce_image_1;

    @SerializedName("recce_image_2")
    String recce_image_2;

    @SerializedName("recce_image_3")
    String recce_image_3;

    @SerializedName("recce_image_4")
    String recce_image_4;

    @SerializedName("product0")
    String product0;

    @SerializedName("uoms")
    JsonElement uoms;

    @SerializedName("recce_image_upload_status")
    String recce_image_upload_status;
    @SerializedName("product_id")
    String product_id;

    public Recce(String recce_id,
                 String project_id,
                 String product_name,
                 String zone_id,
                 String uom_id,
                 String uom_name,
                 String recce_date,
                 String outlet_name,
                 String outlet_owner_name,
                 String outlet_address,
                 String longitude,
                 String latitude,
                 String width,
                 String height,
                 String width_feet,
                 String height_feet,
                 String width_inches,
                 String height_inches,
                 String recce_image,
                 String recce_image_1,
                 String recce_image_2,
                 String recce_image_3,
                 String recce_image_4,
                 String product0,
                 JsonElement uoms,
                 String recce_image_upload_status, String product_id
    ) {
        this.recce_id = recce_id;
        this.project_id = project_id;
        this.product_name = product_name;
        this.zone_id = zone_id;
        this.uom_id = uom_id;
        this.uom_name = uom_name;
        this.recce_date = recce_date;
        this.outlet_name = outlet_name;
        this.outlet_owner_name = outlet_owner_name;
        this.outlet_address = outlet_address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.width = width;
        this.height = height;
        this.width_feet = width_feet;
        this.height_feet = height_feet;
        this.width_inches = width_inches;
        this.height_inches = height_inches;
        this.recce_image = recce_image;
        this.recce_image_1 = recce_image_1;
        this.recce_image_2 = recce_image_2;
        this.recce_image_3 = recce_image_3;
        this.recce_image_4 = recce_image_4;
        this.product0 = product0;
        this.uoms = uoms;
        this.recce_image_upload_status = recce_image_upload_status;
        this.product_id = product_id;
    }

    public String getUom_name() {
        return uom_name;
    }

    public String getUom_id() {
        return uom_id;
    }

    public String getHeight() {
        return height;
    }

    public String getOutlet_address() {
        return outlet_address.toUpperCase();
    }

    public String getLatitude() {
        return latitude;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getHeight_feet() {
        return height_feet;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getOutlet_name() {
        return outlet_name.toUpperCase();
    }

    public String getOutlet_owner_name() {
        return outlet_owner_name;
    }

    public String getProject_id() {
        return project_id;
    }

    public String getRecce_date() {
        return recce_date;
    }

    public String getRecce_id() {
        return recce_id;
    }

    public String getHeight_inches() {
        return height_inches;
    }

    public String getWidth() {
        return width;
    }

    public String getRecce_image() {
        return recce_image;
    }

    public String getWidth_feet() {
        return width_feet;
    }

    public JsonElement getUoms() {
        return uoms;
    }

    public String getWidth_inches() {
        return width_inches;
    }

    public String getZone_id() {
        return zone_id;
    }

    public String getProduct0() {
        return product0;
    }

    public String getRecce_image_1() {
        return recce_image_1;
    }

    public String getRecce_image_2() {
        return recce_image_2;
    }

    public String getRecce_image_3() {
        return recce_image_3;
    }

    public String getRecce_image_4() {
        return recce_image_4;
    }

    public String getRecce_image_upload_status() {
        return recce_image_upload_status;
    }

    public String getProduct_id() {
        return product_id;
    }
}
