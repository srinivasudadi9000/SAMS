package srinivas.sams.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.List;

import srinivas.sams.model.Installation;
import srinivas.sams.model.Products;
import srinivas.sams.model.Projects;
import srinivas.sams.model.Recce;


/**
 * Created by venky on 20-Aug-17.
 */

public class DBHelper {
    static SQLiteDatabase db;
    static Context context;

    public DBHelper(String x, String y, String z, String a, String b, Context context) {
        db = context.openOrCreateDatabase("SAMS", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS project(project_id VARCHAR unique,project_name VARCHAR,vendor_id VARCHAR);");

        db.execSQL("CREATE TABLE IF NOT EXISTS product(product_id VARCHAR unique,product_name VARCHAR,vendor_id VARCHAR);");

        db.execSQL("CREATE TABLE IF NOT EXISTS recce(recce_id VARCHAR unique,project_id VARCHAR,key VARCHAR,userid VARCHAR,crewpersonid VARCHAR," +
                "product_name VARCHAR,zone_id VARCHAR,uom_id VARCHAR,uom_name VARCHAR," +
                "recce_date VARCHAR,outlet_name VARCHAR,outlet_owner_name VARCHAR," +
                "outlet_address VARCHAR,longitude VARCHAR,latitude VARCHAR,width VARCHAR," +
                "height VARCHAR,width_feet VARCHAR,height_feet VARCHAR,width_inches VARCHAR," +
                "height_inches VARCHAR,recce_image VARCHAR,recce_image_1 VARCHAR,recce_image_2 VARCHAR," +
                "recce_image_3 VARCHAR,recce_image_4 VARCHAR," +
                "product0 VARCHAR,uoms VARCHAR,recce_image_upload_status VARCHAR,product_id VARCHAR);");


        db.execSQL("CREATE TABLE IF NOT EXISTS install(recce_id VARCHAR unique,project_id VARCHAR," +
                "vendor_id VARCHAR,crew_person_id VARCHAR,recce_date VARCHAR," +
                "outlet_name VARCHAR,outlet_owner_name VARCHAR," +
                "outlet_address VARCHAR,longitude VARCHAR,latitude VARCHAR,recce_image VARCHAR,installation_date VARCHAR" +
                ",installation_image VARCHAR,installation_remarks VARCHAR ,width VARCHAR," +
                "height VARCHAR,width_feet VARCHAR,height_feet VARCHAR,width_inches VARCHAR," +
                "height_inches VARCHAR,product_name VARCHAR,product0 VARCHAR,installation_image_upload_status VARCHAR," +
                "recce_image_path VARCHAR,key VARCHAR,userid VARCHAR,uom_name VARCHAR,imagepath1 VARCHAR,imagepath2 VARCHAR);");


    }

    public DBHelper(List<Projects> projects, Context context, String project, String constru_one) {
        this.context = context;
        db = context.openOrCreateDatabase("SAMS", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS project(project_id VARCHAR unique,project_name VARCHAR,vendor_id VARCHAR);");
//        String size = String.valueOf(projects.size());
        // Toast.makeText(context.getApplicationContext(), size, Toast.LENGTH_SHORT).show();
        for (int i = 0; i < projects.size(); i++) {
            if (validaterecord(projects.get(i).getProject_id(), "project").equals("notvalidate")) {
                //  Log.d("projects",projects.get(i).getProject_id().toString());
                insertProject(projects.get(i).getProject_id(), projects.get(i).getProject_name(), Preferences.getVendorid(context));
            } else {
                //  Log.d("projects","no ra");
            }
        }
        //viewmydb();
    }


    private void insertProject(String project_id, String project_name, String vendor_id) {
        db.execSQL("INSERT INTO project VALUES('" + project_id + "','" + project_name + "','" + vendor_id + "');");

    }

    public DBHelper(List<Products> productses, Context context, String produts, String constru_one, String products) {
        this.context = context;
        db = context.openOrCreateDatabase("SAMS", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS product(product_id VARCHAR unique,product_name VARCHAR,vendor_id VARCHAR);");
        // Toast.makeText(context.getApplicationContext(), size, Toast.LENGTH_SHORT).show();
        for (int i = 0; i < productses.size(); i++) {
            if (validaterecord(productses.get(i).getProduct_id(), "product").equals("notvalidate")) {
                //  Log.d("projects",productses.get(i).getProduct_id().toString());

                insertProduct(productses.get(i).getProduct_id(), productses.get(i).getProduct_name(), Preferences.getVendorid(context));
            } else {
                //  Log.d("projects","no ra");
            }
        }
        //viewmydb();
    }

    private void insertProduct(String product_id, String product_name, String vendor_id) {
        db.execSQL("INSERT INTO product VALUES('" + product_id + "','" + product_name + "','" + vendor_id + "');");
    }


    public DBHelper(List<Recce> recces, Context context) {
        this.context = context;
        db = context.openOrCreateDatabase("SAMS", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS recce(recce_id VARCHAR unique,project_id VARCHAR,key VARCHAR,userid VARCHAR,crewpersonid VARCHAR," +
                "product_name VARCHAR,zone_id VARCHAR,uom_id VARCHAR,uom_name VARCHAR," +
                "recce_date VARCHAR,outlet_name VARCHAR,outlet_owner_name VARCHAR," +
                "outlet_address VARCHAR,longitude VARCHAR,latitude VARCHAR,width VARCHAR," +
                "height VARCHAR,width_feet VARCHAR,height_feet VARCHAR,width_inches VARCHAR," +
                "height_inches VARCHAR,recce_image VARCHAR,recce_image_1 VARCHAR,recce_image_2 VARCHAR," +
                "recce_image_3 VARCHAR,recce_image_4 VARCHAR," +
                "product0 VARCHAR,uoms VARCHAR,recce_image_upload_status VARCHAR,product_id VARCHAR);");
        String size = String.valueOf(recces.size());
        // Toast.makeText(context.getApplicationContext(), size, Toast.LENGTH_SHORT).show();
        for (int i = 0; i < recces.size(); i++) {
            ;
            String productname = "", outletname = "", outletaddress = "";
            if (!recces.get(i).getProduct_name().toString().equals(null) || !recces.get(i).getOutlet_name().equals(null) ||
                    !recces.get(i).getOutlet_address().equals(null)) {
                productname = recces.get(i).getProduct_name().replaceAll("'", "");
                outletaddress = recces.get(i).getOutlet_address().replaceAll("'", "");
                outletname = recces.get(i).getOutlet_name().replaceAll("'", "");
            }
            if (validaterecord(recces.get(i).getRecce_id(), "recce").equals("notvalidate")) {
                String index = String.valueOf(i);
                insertRecce(recces.get(i).getRecce_id(), recces.get(i).getProject_id(), Preferences.getKey(context), Preferences.getUserid(context),
                        Preferences.getCrewPersonid_project(context), productname
                        , recces.get(i).getZone_id()
                        , recces.get(i).getUom_id(), recces.get(i).getUom_name(), recces.get(i).getRecce_date(), outletname
                        , recces.get(i).getOutlet_owner_name(), outletaddress, recces.get(i).getLongitude()
                        , recces.get(i).getLatitude(), recces.get(i).getWidth(), recces.get(i).getHeight(), recces.get(i).getWidth_feet()
                        , recces.get(i).getHeight_feet(), recces.get(i).getWidth_inches(), recces.get(i).getHeight_inches(), recces.get(i).getRecce_image()
                        , recces.get(i).getRecce_image_1(), recces.get(i).getRecce_image_2(), recces.get(i).getRecce_image_3()
                        , recces.get(i).getRecce_image_4(), recces.get(i).getProduct0(), recces.get(i).getHeight()
                        , recces.get(i).getRecce_image_upload_status(), recces.get(i).getProduct_id());
                // Toast.makeText(getApplicationContext(), index.toString() + "  r=  " + recces.get(i).getRecce_id(), Toast.LENGTH_SHORT).show();
                // Log.d("product_id_helper",recces.get(i).getProduct_id());
            } else {
                if (recces.get(i).getRecce_image_upload_status().equals("Completed")){

                    db.execSQL("UPDATE recce SET uom_id='" + recces.get(i).getUom_id() + "',width='" + recces.get(i).getWidth()
                            + "',key='" + Preferences.getKey(context) + "',userid='" + Preferences.getUserid(context)
                            + "',crewpersonid='" + Preferences.getCrewPersonid_project(context) +
                            "',height='" + recces.get(i).getHeight() + "',width_feet='" + recces.get(i).getWidth_feet()
                            + "',uoms='" + recces.get(i).getUom_name() +
                            "',height_feet='" + recces.get(i).getHeight_feet() + "',width_inches='" + recces.get(i).getWidth_inches() +
                            "',height_inches='" + recces.get(i).getHeight_inches()
                            + "',product_id='" + recces.get(i).getProduct_id() +
                            "',latitude='" + recces.get(i).getLatitude()
                            + "',recce_image_upload_status='" + recces.get(i).getRecce_image_upload_status() + "',longitude='" + recces.get(i).getLongitude()
                            + "',outlet_address='" + outletaddress + "'" + "WHERE recce_id=" + recces.get(i).getRecce_id());
                }

            }
        }
        //viewmydb();
    }

    public DBHelper(List<Installation> installations, Context context, String install) {
        this.context = context;
        db = context.openOrCreateDatabase("SAMS", Context.MODE_PRIVATE, null);


        db.execSQL("CREATE TABLE IF NOT EXISTS install(recce_id VARCHAR unique,project_id VARCHAR," +
                "vendor_id VARCHAR,crew_person_id VARCHAR,recce_date VARCHAR," +
                "outlet_name VARCHAR,outlet_owner_name VARCHAR," +
                "outlet_address VARCHAR,longitude VARCHAR,latitude VARCHAR,recce_image VARCHAR,installation_date VARCHAR" +
                ",installation_image VARCHAR,installation_remarks VARCHAR ,width VARCHAR," +
                "height VARCHAR,width_feet VARCHAR,height_feet VARCHAR,width_inches VARCHAR," +
                "height_inches VARCHAR,product_name VARCHAR,product0 VARCHAR,installation_image_upload_status VARCHAR," +
                "recce_image_path VARCHAR,key VARCHAR,userid VARCHAR,uom_name VARCHAR,imagepath1 VARCHAR,imagepath2 VARCHAR);");
        String size = String.valueOf(installations.size());
        // Toast.makeText(context.getApplicationContext(), size, Toast.LENGTH_SHORT).show();
        for (int i = 0; i < installations.size(); i++) {
            ;
            if (validaterecord(installations.get(i).getRecce_id(), "install").equals("notvalidate")) {
                String index = String.valueOf(i);
                // Toast.makeText(getApplicationContext(), index.toString() + "  r=  " + recces.get(i).getRecce_id(), Toast.LENGTH_SHORT).show();
                insertInstall(installations.get(i).getRecce_id(), installations.get(i).getProject_id(),
                        installations.get(i).getVendor_id(), installations.get(i).getCrew_person_id()
                        , installations.get(i).getRecce_date(), installations.get(i).getOutlet_name(),
                        installations.get(i).getOutlet_owner_name(), installations.get(i).getOutlet_address().replaceAll("'", "")
                        , installations.get(i).getLongitude(), installations.get(i).getLatitude().replaceAll("'", ""),
                        installations.get(i).getRecce_image(), installations.get(i).getInstallation_date(),
                        installations.get(i).getInstallation_image(), installations.get(i).getInstallation_remarks(),
                        installations.get(i).getWidth(), installations.get(i).getHeight(), installations.get(i).getWidth_feet(),
                        installations.get(i).getHeight_feet(), installations.get(i).getWidth_inches(),
                        installations.get(i).getHeight_inches(), installations.get(i).getProduct_name(),
                        installations.get(i).getProduct0(), installations.get(i).getInstallation_image_upload_status(),
                        installations.get(i).getRecce_image(), Preferences.getKey(context), Preferences.getUserid(context)
                        ,installations.get(i).getUom_name(),installations.get(i).getInstallation_image_1()
                        ,installations.get(i).getInstallation_image_2());
            }else {
                if (installations.get(i).getInstallation_image_upload_status().equals("Completed")){
                    String instal_remarks;
                    if (installations.get(i).getInstallation_remarks().length() > 0) {
                        instal_remarks = installations.get(i).getInstallation_remarks().replaceAll("'", "");
                    } else {
                        instal_remarks = installations.get(i).getInstallation_remarks();
                    }
                    db.execSQL("UPDATE install SET installation_date='" + installations.get(i).getInstallation_date()
                            + "',installation_remarks='" + instal_remarks + "',key='" +  Preferences.getKey(context)
                            + "',userid='" +  Preferences.getUserid(context)
                            + "',crew_person_id='" + installations.get(i).getCrew_person_id() +
                            "',project_id='" + installations.get(i).getProject_id()
                            + "'" +" WHERE recce_id=" + installations.get(i).getRecce_id());

                }
            }
        }
        //viewmydb();
    }

    public void insertRecce(String recce_id, String project_id, String key, String userid, String crewpersonid, String product_name, String zone_id, String uom_id, String uom_name,
                            String recce_date, String outlet_name, String outlet_owner_name, String outlet_address, String longitude,
                            String latitude, String width, String height, String width_feet, String height_feet, String width_inches,
                            String height_inches, String recce_image, String recce_image_1, String recce_image_2, String recce_image_3,
                            String recce_image_4, String product0, String uoms, String recce_image_upload_status, String productid) {
        db.execSQL("INSERT INTO recce VALUES('" + recce_id + "','" + project_id + "','" + key + "','" + userid + "','" + crewpersonid + "','" + product_name + "','" + zone_id + "','" +
                uom_id + "','" + uom_name + "','" + recce_date + "','" + outlet_name + "','" + outlet_owner_name + "','" + outlet_address
                + "','" + longitude + "','" + latitude + "','" + width + "','" + height + "','" + width_feet + "','" + height_feet
                + "','" + width_inches + "','" + height_inches + "','" + recce_image + "','" + recce_image_1 + "','" + recce_image_2 + "','" +
                recce_image_3 + "','" + recce_image_4 + "','" + product0 + "','" + uoms + "','" + recce_image_upload_status + "','" + productid + "');");

    }

    public void insertInstall(String recce_id, String project_id, String vendor_id, String crew_person_id, String recce_date,
                              String outlet_name, String outlet_owner_name, String outlet_address, String longitude, String latitude,
                              String recce_image, String installation_date, String installation_image, String installation_remarks,
                              String width, String height, String width_feet, String height_feet, String width_inches,
                              String height_inches, String product_name, String product0, String installation_image_upload_status,
                              String recce_image_path, String key, String userid,String uom_name,
                              String install_image_path1,String install_image_path2) {
        String outletname, outletaddress, prod_name, instal_remarks;
        if (outlet_name.length() > 0) {
            outletname = outlet_name.replaceAll("'", "");
        } else {
            outletname = outlet_name;
        }
        if (outlet_address.length() > 0) {
            outletaddress = outlet_address.replaceAll("'", "");
        } else {
            outletaddress = outlet_address;
        }
        if (product_name.length() > 0) {
            prod_name = product_name.replaceAll("'", "");
        } else {
            prod_name = product_name;
        }
        if (installation_remarks.length() > 0) {
            instal_remarks = installation_remarks.replaceAll("'", "");
        } else {
            instal_remarks = installation_remarks;
        }

        db.execSQL("INSERT INTO install VALUES('" + recce_id + "','" + project_id + "','" + vendor_id + "','" + crew_person_id + "','" +
                recce_date + "','" + outletname + "','" + outlet_owner_name + "','" + outletaddress + "','" + longitude
                + "','" + latitude + "','" + recce_image + "','" + installation_date + "','" + installation_image + "','" + instal_remarks
                + "','" + width + "','" + height + "','" + width_feet + "','" + height_feet + "','" + width_inches + "','" +
                height_inches + "','" + prod_name + "','" + product0 + "','" + installation_image_upload_status + "','" + recce_image_path
                + "','" + key + "','" + userid +"','"+uom_name+"','"+install_image_path1+"','"+install_image_path2+
                "');");

    }

    public String validaterecord(String recceid, String instal) {
        if (instal.equals("install")) {
            Cursor c = db.rawQuery("SELECT * FROM install WHERE recce_id='" + recceid + "'", null);
            if (c.moveToFirst()) {
                return "validate";
            } else {
                return "notvalidate";
            }
        } else if (instal.equals("project")) {
            Cursor c = db.rawQuery("SELECT * FROM project WHERE project_id='" + recceid + "'", null);
            if (c.moveToFirst()) {
                return "validate";
            } else {
                return "notvalidate";
            }
        } else if (instal.equals("product")) {
            Cursor c = db.rawQuery("SELECT * FROM product WHERE product_id='" + recceid + "'", null);
            if (c.moveToFirst()) {
                return "validate";
            } else {
                return "notvalidate";
            }
        } else {
            Cursor c = db.rawQuery("SELECT * FROM recce WHERE recce_id='" + recceid + "'", null);
            if (c.moveToFirst()) {
                return "validate";
            } else {
                return "notvalidate";
            }
        }
    }


    public static void updateRecce_Localdb(String uom_id, String width, String key, String userid, String crewpersonid, String height, String width_feet, String height_feet,
                                           String width_inches, String height_inches, String product_id, String recce_id, String recce_image,
                                           String recce_image_1, String recce_image_2, String recce_image_3, String recce_image_4,
                                           String latitude, String longitude, String outlet_address, String project_id, String uoms, String status, Context mycontext) {

        db = mycontext.openOrCreateDatabase("SAMS", Context.MODE_PRIVATE, null);
        //  Log.d("width",width);Log.d("width",height);
        db.execSQL("UPDATE recce SET uom_id='" + uom_id + "',width='" + width + "',key='" + key + "',userid='" + userid
                + "',crewpersonid='" + crewpersonid +
                "',height='" + height + "',width_feet='" + width_feet + "',uoms='" + uoms +
                "',height_feet='" + height_feet + "',width_inches='" + width_inches +
                "',height_inches='" + height_inches +
                "',recce_image='" + recce_image + "',recce_image_1='" + recce_image_1 + "',product_id='" + product_id +
                "',recce_image_2='" + recce_image_2 + "',recce_image_3='" + recce_image_3 +
                "',recce_image_4='" + recce_image_4 + "',latitude='" + latitude
                + "',recce_image_upload_status='" + status + "',longitude='" + longitude + "',outlet_address='" + outlet_address + "'" + "WHERE recce_id=" + recce_id);

        //  Log.d("success", "successfully updated recce");
    }


    public static void updateInstall_Localdb(String install_date, String install_remark, String key, String userid, String crewpersonid,
                                             String recce_id, String project_id, String imagefilepart1,
                                             String imagefile2 ,String imagefile3 ,String mode, Context mycontext) {
        String instal_remarks;
        if (install_remark.length() > 0) {
            instal_remarks = install_remark.replaceAll("'", "");
        } else {
            instal_remarks = install_remark;
        }

        db = mycontext.openOrCreateDatabase("SAMS", Context.MODE_PRIVATE, null);

        db.execSQL("UPDATE install SET installation_date='" + install_date + "',installation_remarks='" + instal_remarks + "',key='" + key + "',userid='" + userid
                + "',crew_person_id='" + crewpersonid +"',imagepath1='"+imagefile2+"',imagepath2='"+imagefile3+
                "',project_id='" + project_id + "',installation_image='" + imagefilepart1 + "',product0='" + mode + "'" +
                " WHERE recce_id=" + recce_id);


        db.close();
        //  Log.d("success", "successfully updated recce");
    }

}
