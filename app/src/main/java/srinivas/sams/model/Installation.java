package srinivas.sams.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by venky on 11-Aug-17.
 */

public class Installation {
    @SerializedName("recce_id")
    String recce_id;

    @SerializedName("project_id")
    String project_id;

    @SerializedName("vendor_id")
    String vendor_id;

    @SerializedName("crew_person_id")
    String crew_person_id;

    @SerializedName("recce_date")
    String recce_date;

    @SerializedName("outlet_name")
    String outlet_name;

    @SerializedName("uom_name")
    String uom_name;

    @SerializedName("outlet_owner_name")
    String outlet_owner_name;

    @SerializedName("outlet_address")
    String outlet_address;

    @SerializedName("longitude")
    String longitude;

    @SerializedName("latitude")
    String latitude;

    @SerializedName("recce_image")
    String recce_image;

    @SerializedName("installation_date")
    String installation_date;

    @SerializedName("installation_image")
    String installation_image;
    @SerializedName("installation_image_1")
    String installation_image_1;
    @SerializedName("installation_image_2")
    String installation_image_2;

    @SerializedName("installation_remarks")
    String installation_remarks;

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

    @SerializedName("product_name")
    String product_name;

    @SerializedName("product0")
    String product0;

    @SerializedName("installation_image_upload_status")
    String installation_image_upload_status;

    @SerializedName("recce_image_path")
    String recce_image_path;

    public Installation(
            String recce_id,
            String project_id,
            String vendor_id,
            String crew_person_id,
            String recce_date,
            String outlet_name,String uom_name,
            String outlet_owner_name,
            String outlet_address,
            String longitude,
            String latitude,
            String recce_image,
            String installation_date,
            String installation_image,
            String installation_image_1,
            String installation_image_2,
            String installation_remarks,
            String width,
            String height,
            String width_feet,
            String height_feet,
            String width_inches,
            String height_inches,
            String product_name,
            String product0,
            String installation_image_upload_status,
            String recce_image_path
    ) {
        this.recce_id = recce_id;
        ;
        this.project_id = project_id;
        this.vendor_id = vendor_id;
        this.crew_person_id = crew_person_id;
        this.recce_date = recce_date;
        this.outlet_name = outlet_name;
        this.uom_name = uom_name;
        this.outlet_owner_name = outlet_owner_name;
        this.outlet_address = outlet_address;
        this.longitude = longitude;
        this.latitude = latitude;
        this.recce_image = recce_image;
        this.installation_date = installation_date;
        this.installation_image = installation_image;
        this.installation_image_1=installation_image_1;
        this.installation_image_2=installation_image_2;
        this.installation_remarks = installation_remarks;
        this.width = width;
        this.height = height;
        this.width_feet = width_feet;
        this.height_feet = height_feet;
        this.width_inches = width_inches;
        this.height_inches = height_inches;
        this.product_name = product_name;
        this.product0 = product0;
        this.installation_image_upload_status = installation_image_upload_status;
        this.recce_image_path = recce_image_path;

    }

    public String getUom_name() {
        return uom_name;
    }

    public String getVendor_id() {
        return vendor_id;
    }

    public String getCrew_person_id() {
        return crew_person_id;
    }

    public String getInstallation_date() {
        return installation_date;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getInstallation_image() {
        return installation_image;
    }

    public String getInstallation_image_1() {
        return installation_image_1;
    }

    public String getInstallation_image_2() {
        return installation_image_2;
    }

    public String getHeight() {
        return height;
    }

    public String getHeight_feet() {
        return height_feet;
    }

    public String getInstallation_remarks() {
        return installation_remarks;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getOutlet_address() {
        return outlet_address.toUpperCase();
    }

    public String getHeight_inches() {
        return height_inches;
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

    public String getInstallation_image_upload_status() {
        return installation_image_upload_status;
    }

    public String getProduct0() {
        return product0;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getRecce_image() {
        return recce_image;
    }

    public String getRecce_image_path() {
        return recce_image_path;
    }

    public String getWidth() {
        return width;
    }

    public String getWidth_feet() {
        return width_feet;
    }

    public String getWidth_inches() {
        return width_inches;
    }

}
