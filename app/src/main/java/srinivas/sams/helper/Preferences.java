package srinivas.sams.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by venky on 20-Aug-17.
 */

public class Preferences  {
    public static Context context;

    public static void setVendor(String vendor_id, String vendor_name, String crew_person_id,Context mycontext){
        context = mycontext;
        SharedPreferences.Editor vendor = mycontext.getSharedPreferences("VENDOR", mycontext.MODE_PRIVATE).edit();
        vendor.putString("vendor_id",vendor_id);
        vendor.putString("vendor_name",vendor_name);
        vendor.putString("crew_person_id",crew_person_id);
        vendor.commit();
    }
     public static String getVendorid(Context context){
        SharedPreferences vendorid = context.getSharedPreferences("VENDOR",context.MODE_PRIVATE);
        return vendorid.getString("vendor_id","");
    }
    public static String getVendorname(Context context){
        SharedPreferences vendorname = context.getSharedPreferences("VENDOR",context.MODE_PRIVATE);
        return vendorname.getString("vendor_name","");
    }
    public static String getCrewpersonid(Context context){
        SharedPreferences crewpersonid = context.getSharedPreferences("VENDOR",context.MODE_PRIVATE);
        return crewpersonid.getString("crew_person_id","");
    }
    public static void setOutlet(String outletname, String idpath1, String idpath2,String idpath3,String idpath4,Context mycontext){
        context = mycontext;
        SharedPreferences.Editor outlet = mycontext.getSharedPreferences("OUTLET", mycontext.MODE_PRIVATE).edit();
        outlet.putString("outlet_name",outletname);
        outlet.putString("idpath1",idpath1);
        outlet.putString("idpath2",idpath2);
        outlet.putString("idpath3",idpath3);
        outlet.putString("idpath4",idpath4);
        outlet.commit();
    }
    public static String getOutletname(Context context){
        SharedPreferences vendorid = context.getSharedPreferences("OUTLET",context.MODE_PRIVATE);
        return vendorid.getString("outlet_name","");
    }
    public static String getidpath1(Context context){
        SharedPreferences vendorid = context.getSharedPreferences("OUTLET",context.MODE_PRIVATE);
        return vendorid.getString("idpath1","");
    }
    public static String getidpath2(Context context){
        SharedPreferences vendorid = context.getSharedPreferences("OUTLET",context.MODE_PRIVATE);
        return vendorid.getString("idpath2","");
    }
    public static String getidpath3(Context context){
        SharedPreferences vendorid = context.getSharedPreferences("OUTLET",context.MODE_PRIVATE);
        return vendorid.getString("idpath3","");
    }
    public static String getidpath4(Context context){
        SharedPreferences vendorid = context.getSharedPreferences("OUTLET",context.MODE_PRIVATE);
        return vendorid.getString("idpath4","");
    }
    public static void setProject(String key,String user_id,String crew_person_id,String crew_person_name,Context mycontext){
        SharedPreferences.Editor project = mycontext.getSharedPreferences("PROJECT", context.MODE_PRIVATE).edit();
        project.putString("key",key);
        project.putString("user_id",user_id);
        project.putString("crew_person_id",crew_person_id);
        project.putString("crew_person_name",crew_person_name);
        project.commit();
    }
    public static String getKey(Context context){
        SharedPreferences key = context.getSharedPreferences("PROJECT",context.MODE_PRIVATE);
        return key.getString("key","");
    }
    public static String getCrewPersonname(Context context){
        SharedPreferences key = context.getSharedPreferences("PROJECT",context.MODE_PRIVATE);
        return key.getString("crew_person_name","");
    }
    public static String getUserid(Context context){
        SharedPreferences key = context.getSharedPreferences("PROJECT",context.MODE_PRIVATE);
        return key.getString("user_id","");
    }
    public static String getCrewPersonid_project(Context context){
        SharedPreferences key = context.getSharedPreferences("PROJECT",context.MODE_PRIVATE);
        return key.getString("crew_person_id","");
    }
    public static void setProjectId(String projectId,Context mycontext){
        SharedPreferences.Editor project = mycontext.getSharedPreferences("PROJECT_ID", context.MODE_PRIVATE).edit();
        project.putString("project_id",projectId);
        project.commit();
    }
    public static String getProjectId(Context context){
        SharedPreferences projectid = context.getSharedPreferences("PROJECT_ID",context.MODE_PRIVATE);
        return projectid.getString("project_id","");
    }
    public static void setreeceId_product(String recceId,Context mycontext){
        SharedPreferences.Editor project = mycontext.getSharedPreferences("RECCE_ID", context.MODE_PRIVATE).edit();
        project.putString("recce_id",recceId);
        project.commit();
    }
    public static String getreeceId_product(Context context){
        SharedPreferences projectid = context.getSharedPreferences("RECCE_ID",context.MODE_PRIVATE);
        return projectid.getString("recce_id","");
    }
  public static void setlatlong(String lat ,String longitude,Context mycontext){
      SharedPreferences.Editor type = mycontext.getSharedPreferences("LatLong", context.MODE_PRIVATE).edit();
      type.putString("lat",lat);
      type.putString("longitude",longitude);
      type.commit();
  }
  public static String getLat(Context context){
      SharedPreferences type = context.getSharedPreferences("LatLong",context.MODE_PRIVATE);
      return type.getString("lat","");
  }

    public static String getLong(Context context){
        SharedPreferences type = context.getSharedPreferences("LatLong",context.MODE_PRIVATE);
        return type.getString("longitude","");
    }

    public static void setProducts(String selection,Context mycontext){
            SharedPreferences.Editor type = mycontext.getSharedPreferences("PRODUCT_DISPLAY", context.MODE_PRIVATE).edit();
            type.putString("productdisplay",selection);
            type.commit();
       }
    public static String getProducts(Context context){
        SharedPreferences type = context.getSharedPreferences("PRODUCT_DISPLAY",context.MODE_PRIVATE);
        return type.getString("productdisplay","");
    }
    public static void setSelection(String selection,Context mycontext){
        SharedPreferences.Editor type = mycontext.getSharedPreferences("SELECCTION", context.MODE_PRIVATE).edit();
        type.putString("selection",selection);
        type.commit();
    }
    public static String getSelection(Context context){
        SharedPreferences type = context.getSharedPreferences("SELECCTION",context.MODE_PRIVATE);
        return type.getString("selection","");
    }

}
