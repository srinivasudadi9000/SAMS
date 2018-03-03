package srinivas.sams.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import srinivas.sams.Activity.AddRecceWeb;
import srinivas.sams.Activity.Product_Display;
import srinivas.sams.Activity.Update_Recce;
import srinivas.sams.R;
import srinivas.sams.helper.Preferences;
import srinivas.sams.model.Recce;
import srinivas.sams.validation.Validation;

/**
 * Created by venky on 11-Aug-17.
 */

public class RecceAdapter extends RecyclerView.Adapter<RecceAdapter.Recceholder> {
    private List<Recce> receelist;
    private List<Recce> receelist_filter;
    private int rowLayout;
    public Context context;
    View view = null;
    private DisplayImageOptions options;
    public String web = "http://128.199.131.14/sams/web/";

    public RecceAdapter(List<Recce> receelist, int rowLayout, Context context) {
        //this.receelist.clear();
        //receelist.addAll(receelist);
        this.receelist_filter = receelist;
        this.receelist = receelist;
        this.rowLayout = rowLayout;
        this.context = context;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.ic_launcher_round)
                .showImageForEmptyUri(R.drawable.clear)
                .showImageOnFail(R.drawable.dummy)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(20))
                .build();
    }

    @Override
    public Recceholder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new Recceholder(view);
    }


    @Override
    public void onBindViewHolder(Recceholder holder, final int position) {
        holder.outletname_tv.setText(receelist.get(position).getOutlet_name().toString());
        holder.outletaddress_tv.setText(receelist.get(position).getOutlet_address().toString().toLowerCase());
        holder.productname_tv.setText(receelist.get(position).getProduct_name().toString());
        holder.height_width_tv.setText(receelist.get(position).getWidth().toString() + "X" +
                receelist.get(position).getHeight().toString()+"  ("+receelist.get(position).getUom_name().toString()+")");
        //  holder.recce_img.setText(receelist.get(position).getOutlet_name().toString());

        Bitmap bmImage = null;
        if (!Validation.internet(context)) {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            //opt.inSampleSize = 8;
            //opt.inMutable = true;
            // bmImage = BitmapFactory.decodeFile(receelist.get(position).getRecce_image().toString(), opt);
            //holder.recce_img.setImageBitmap(bmImage);
            if (receelist.get(position).getRecce_image().toString().contains("storage")) {
                bmImage = BitmapFactory.decodeFile(receelist.get(position).getRecce_image().toString(), null);
                holder.recce_img.setImageBitmap(bmImage);

            } else {

               /* holder.recce_img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                holder.recce_img.setImageResource(R.drawable.dummy);
*/

                ImageLoader.getInstance()
                        .displayImage(web+"image_uploads/recce_uploads/" +
                                        receelist.get(position).getRecce_image().toString()
                                , holder.recce_img, options);
            }

            Log.d("imagepath", receelist.get(position).getRecce_id().toString() + " " +
                    receelist.get(position).getRecce_image().toString());
            //Toast.makeText(getBaseContext(), "local db recces", Toast.LENGTH_LONG).show();
        } else {
            if (!receelist.get(position).getRecce_image().toString().equals("")) {
                DiskCacheUtils.removeFromCache(web+"image_uploads/recce_uploads/" +
                        receelist.get(position).getRecce_image().toString(), ImageLoader.getInstance().getDiskCache());
                MemoryCacheUtils.removeFromCache(web+"image_uploads/recce_uploads/" +
                        receelist.get(position).getRecce_image().toString(), ImageLoader.getInstance().getMemoryCache());

                ImageLoader.getInstance()
                        .displayImage(web+"image_uploads/recce_uploads/" +
                                        receelist.get(position).getRecce_image().toString()
                                , holder.recce_img, options);

            }
            if (!receelist.get(position).getRecce_image_1().toString().equals("")) {

             /*   DiskCacheUtils.removeFromCache(web+"image_uploads/recce_images_1_uploads/" +
                        receelist.get(position).getRecce_image_1().toString(), ImageLoader.getInstance().getDiskCache());
                MemoryCacheUtils.removeFromCache(web+"image_uploads/recce_images_1_uploads/" +
                        receelist.get(position).getRecce_image_1().toString(), ImageLoader.getInstance().getMemoryCache());
*/
                ImageLoader.getInstance()
                        .displayImage(web+"image_uploads/recce_images_1_uploads/" +
                                        receelist.get(position).getRecce_image_1().toString()
                                , holder.recce_img_testing, options);
            }
            if (!receelist.get(position).getRecce_image_2().toString().equals("")) {

               /* DiskCacheUtils.removeFromCache(web+"image_uploads/recce_images_2_uploads/" +
                        receelist.get(position).getRecce_image_2().toString(), ImageLoader.getInstance().getDiskCache());
                MemoryCacheUtils.removeFromCache(web+"image_uploads/recce_images_2_uploads/" +
                        receelist.get(position).getRecce_image_2().toString(), ImageLoader.getInstance().getMemoryCache());
*/
                ImageLoader.getInstance()
                        .displayImage(web+"image_uploads/recce_images_2_uploads/" +
                                        receelist.get(position).getRecce_image_2().toString()
                                , holder.recce_img_testing, options);
            }
            if (!receelist.get(position).getRecce_image_3().toString().equals("")) {

              /*  DiskCacheUtils.removeFromCache(web+"image_uploads/recce_images_3_uploads/" +
                        receelist.get(position).getRecce_image_3().toString(), ImageLoader.getInstance().getDiskCache());
                MemoryCacheUtils.removeFromCache(web+"image_uploads/recce_images_3_uploads/" +
                        receelist.get(position).getRecce_image_3().toString(), ImageLoader.getInstance().getMemoryCache());
*/
                ImageLoader.getInstance()
                        .displayImage(web+"image_uploads/recce_images_3_uploads/" +
                                        receelist.get(position).getRecce_image_3().toString()
                                , holder.recce_img_testing, options);

            }
            if (!receelist.get(position).getRecce_image_4().toString().equals("")) {

               /* DiskCacheUtils.removeFromCache(web+"image_uploads/recce_images_4_uploads/" +
                        receelist.get(position).getRecce_image_4().toString(), ImageLoader.getInstance().getDiskCache());
                MemoryCacheUtils.removeFromCache(web+"image_uploads/recce_images_4_uploads/" +
                        receelist.get(position).getRecce_image_4().toString(), ImageLoader.getInstance().getMemoryCache());
*/
                ImageLoader.getInstance()
                        .displayImage(web+"image_uploads/recce_images_4_uploads/" +
                                        receelist.get(position).getRecce_image_4().toString()
                                , holder.recce_img_testing, options);
            }



/*
            Picasso.with(context)
                    .load("http://128.199.131.14/sams/web/image_uploads/recce_uploads/" + receelist.get(position).getRecce_image().toString())
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .resize(512, 512)
                    .error(R.drawable.dummy)
                    .noFade()
                    .into(holder.recce_img);
*/

        }

        if (!Validation.internet(context)) {
            if (receelist.get(position).getRecce_image_upload_status().equals("Completed")) {
                holder.recce_status_tv.setTextColor(Color.parseColor("#007EA8"));
                holder.recce_status_tv.setText(receelist.get(position).getRecce_image_upload_status().toString());
                holder.recce_img.setScaleType(ImageView.ScaleType.FIT_XY);
            } else {
                holder.recce_status_tv.setTextColor(Color.parseColor("#ff0000"));
                holder.recce_status_tv.setText(receelist.get(position).getRecce_image_upload_status().toString());
                holder.recce_img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            }

        } else {
            if (receelist.get(position).getRecce_image_upload_status().equals("Completed")) {
                holder.recce_status_tv.setTextColor(Color.parseColor("#00a65a"));
                holder.recce_status_tv.setText(receelist.get(position).getRecce_image_upload_status().toString());
                holder.recce_img.setScaleType(ImageView.ScaleType.FIT_XY);
            } else {
                holder.recce_status_tv.setTextColor(Color.parseColor("#ff0000"));
                holder.recce_status_tv.setText(receelist.get(position).getRecce_image_upload_status().toString());
                holder.recce_img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return receelist.size();
    }

    public class Recceholder extends RecyclerView.ViewHolder {
        TextView outletname_tv, outletaddress_tv, productname_tv, height_width_tv, recce_status_tv, recce_edit_tv;
        ImageView recce_img, recce_img_testing;

        //@BindView(R.id.recce_img) ImageView ;
        public Recceholder(final View itemView) {
            super(itemView);
            //ButterKnife.bind(itemView);
            outletname_tv = (TextView) itemView.findViewById(R.id.outletname_tv);
            outletaddress_tv = (TextView) itemView.findViewById(R.id.outletaddress_tv);
            productname_tv = (TextView) itemView.findViewById(R.id.productname_tv);
            height_width_tv = (TextView) itemView.findViewById(R.id.height_width_tv);
            recce_status_tv = (TextView) itemView.findViewById(R.id.recce_status_tv);
            recce_edit_tv = (TextView) itemView.findViewById(R.id.recce_edit_tv);
            recce_img = (ImageView) itemView.findViewById(R.id.recce_img);
            recce_img_testing = (ImageView) itemView.findViewById(R.id.recce_img_testing);
            recce_edit_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Validation.internet(itemView.getContext())) {
                        //showRecceEdit(itemView.getContext());
                        Preferences.setreeceId_product(receelist.get(getAdapterPosition()).getRecce_id(), itemView.getContext());
                        Intent product = new Intent(context, Product_Display.class);
                        itemView.getContext().startActivity(product);
                        ((Activity) itemView.getContext()).finish();
                        // Toast.makeText(getBaseContext(), "local db recces", Toast.LENGTH_LONG).show();
                    } else {
                        Preferences.setreeceId_product(receelist.get(getAdapterPosition()).getRecce_id(), itemView.getContext());
                        Intent edit = new Intent(context, AddRecceWeb.class);
                        edit.putExtra("url", web+"index.php?r=app-outlets/app-recce-edit&user_id=" + Preferences.getUserid(itemView.getContext()) + "&crew_person_id=" + Preferences.getCrewPersonid_project(itemView.getContext()) + "&recce_id=" + receelist.get(getAdapterPosition()).getRecce_id().toString());
                        itemView.getContext().startActivity(edit);
                        ((Activity) itemView.getContext()).finish();
                    }

                 /*   Intent product= new Intent(context,Product_Display.class);
                    itemView.getContext().startActivity(product);
                    ((Activity)itemView.getContext()).finish();*/
                }
            });

            recce_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String width = "", height = "", umoid = "", productname = "", mainpic = "", image1 = "", image2 = "", image3 = "", image4 = "", address = "",
                            latitude = "", longitude = "", uomname = "", product_id = "",outlet_name="";

                    Preferences.setreeceId_product(receelist.get(getAdapterPosition()).getRecce_id(), itemView.getContext());
                    if (!receelist.get(getAdapterPosition()).getProduct_name().equals("NA") && !height_width_tv.getText().toString().equals("0X0")) {
                        Intent i = new Intent(context, Update_Recce.class);
                        i.putExtra("recce_id", receelist.get(getAdapterPosition()).getRecce_id().toString());
                        if (receelist.get(getAdapterPosition()).getUom_name().toString() != null) {
                            i.putExtra("outlet_name", receelist.get(getAdapterPosition()).getOutlet_name().toString());
                        } else {
                            i.putExtra("outlet_name", outlet_name);
                        }
                        if (receelist.get(getAdapterPosition()).getUom_name().toString() != null) {
                            i.putExtra("uomname", receelist.get(getAdapterPosition()).getUom_name().toString());
                        } else {
                            i.putExtra("uomname", uomname);
                        }
                        if (receelist.get(getAdapterPosition()).getWidth().toString() != null) {
                            i.putExtra("width", receelist.get(getAdapterPosition()).getWidth().toString());
                        } else {
                            i.putExtra("width", width);
                        }
                        if (receelist.get(getAdapterPosition()).getProduct_id().toString() != null) {
                            i.putExtra("product_id", receelist.get(getAdapterPosition()).getProduct_id().toString());
                        } else {
                            i.putExtra("product_id", product_id);
                        }
                        if (receelist.get(getAdapterPosition()).getHeight().toString() != null) {
                            i.putExtra("height", receelist.get(getAdapterPosition()).getHeight().toString());
                        } else {
                            i.putExtra("height", height);
                        }
                        if (receelist.get(getAdapterPosition()).getUom_id().toString() != null) {
                            i.putExtra("uomid", receelist.get(getAdapterPosition()).getUom_id().toString());
                        } else {
                            i.putExtra("uomid", umoid);
                        }
                        if (receelist.get(getAdapterPosition()).getProduct_name().toString() != null) {
                            i.putExtra("productname", receelist.get(getAdapterPosition()).getProduct_name().toString());
                        } else {
                            i.putExtra("productname", productname);
                        }
                        if (receelist.get(getAdapterPosition()).getRecce_image_upload_status().toString() != null) {
                            i.putExtra("imagestatus", receelist.get(getAdapterPosition()).getRecce_image_upload_status().toString());
                        } else {
                            i.putExtra("imagestatus", productname);
                        }
                        if (receelist.get(getAdapterPosition()).getRecce_image().toString() != null) {
                            i.putExtra("mainpic", receelist.get(getAdapterPosition()).getRecce_image().toString());
                        } else {
                            i.putExtra("mainpic", mainpic);
                        }

                        if (receelist.get(getAdapterPosition()).getRecce_image_1().toString() != null) {
                            i.putExtra("image1", receelist.get(getAdapterPosition()).getRecce_image_1());
                        } else {
                            i.putExtra("image1", image1);
                        }
                        if (receelist.get(getAdapterPosition()).getRecce_image_2().toString() != null) {
                            i.putExtra("image2", receelist.get(getAdapterPosition()).getRecce_image_2());
                        } else {
                            i.putExtra("image2", image2);
                        }
                        if (receelist.get(getAdapterPosition()).getRecce_image_3().toString() != null) {
                            i.putExtra("image3", receelist.get(getAdapterPosition()).getRecce_image_3());
                        } else {
                            i.putExtra("image3", image3);
                        }
                        if (receelist.get(getAdapterPosition()).getRecce_image_4().toString() != null) {
                            i.putExtra("image4", receelist.get(getAdapterPosition()).getRecce_image_4());
                        } else {
                            i.putExtra("image4", image4);
                        }

                        if (receelist.get(getAdapterPosition()).getOutlet_address().toString() != null) {
                            i.putExtra("address", receelist.get(getAdapterPosition()).getOutlet_address());
                        } else {
                            i.putExtra("address", address);
                        }
                        if (receelist.get(getAdapterPosition()).getLatitude().toString() != null) {
                            i.putExtra("latitude", receelist.get(getAdapterPosition()).getLatitude());
                        } else {
                            i.putExtra("latitude", Preferences.getLat(itemView.getContext()));
                        }
                        if (receelist.get(getAdapterPosition()).getLongitude().toString() != null) {
                            i.putExtra("longitude", receelist.get(getAdapterPosition()).getLongitude());
                        } else {
                            i.putExtra("longitude", Preferences.getLong(itemView.getContext()));
                        }
                        itemView.getContext().startActivity(i);
                        ((Activity) itemView.getContext()).finish();
                    } else {
                        if (receelist.get(getAdapterPosition()).getProduct_name().equals("NA")) {
                            if (!Validation.internet(context)) {
                                Intent product = new Intent(context, Product_Display.class);
                                itemView.getContext().startActivity(product);
                                ((Activity) itemView.getContext()).finish();
                            } else {
                                showLocationDialog(context, receelist.get(getAdapterPosition()).getRecce_id(), "SAMS", "Please Edit Product Name");
                            }
                        } else {

                            if (!Validation.internet(context)) {
                                Intent i = new Intent(context, Update_Recce.class);
                                i.putExtra("recce_id", receelist.get(getAdapterPosition()).getRecce_id().toString());
                                if (receelist.get(getAdapterPosition()).getWidth().toString() != null) {
                                    i.putExtra("width", receelist.get(getAdapterPosition()).getWidth().toString());
                                } else {
                                    i.putExtra("width", width);
                                }
                                if (receelist.get(getAdapterPosition()).getProduct_id().toString() != null) {
                                    i.putExtra("product_id", receelist.get(getAdapterPosition()).getProduct_id().toString());
                                } else {
                                    i.putExtra("product_id", product_id);
                                }
                                if (receelist.get(getAdapterPosition()).getHeight().toString() != null) {
                                    i.putExtra("height", receelist.get(getAdapterPosition()).getHeight().toString());
                                } else {
                                    i.putExtra("height", height);
                                }
                                if (receelist.get(getAdapterPosition()).getUom_id().toString() != null) {
                                    i.putExtra("uomid", receelist.get(getAdapterPosition()).getUom_id().toString());
                                } else {
                                    i.putExtra("uomid", umoid);
                                }
                                if (receelist.get(getAdapterPosition()).getProduct_name().toString() != null) {
                                    i.putExtra("productname", receelist.get(getAdapterPosition()).getProduct_name().toString());
                                } else {
                                    i.putExtra("productname", productname);
                                }
                                if (receelist.get(getAdapterPosition()).getRecce_image().toString() != null) {
                                    i.putExtra("mainpic", receelist.get(getAdapterPosition()).getRecce_image().toString());
                                } else {
                                    i.putExtra("mainpic", mainpic);
                                }

                                if (receelist.get(getAdapterPosition()).getRecce_image_1().toString() != null) {
                                    i.putExtra("image1", receelist.get(getAdapterPosition()).getRecce_image_1());
                                } else {
                                    i.putExtra("image1", image1);
                                }
                                if (receelist.get(getAdapterPosition()).getRecce_image_2().toString() != null) {
                                    i.putExtra("image2", receelist.get(getAdapterPosition()).getRecce_image_2());
                                } else {
                                    i.putExtra("image2", image2);
                                }
                                if (receelist.get(getAdapterPosition()).getRecce_image_3().toString() != null) {
                                    i.putExtra("image3", receelist.get(getAdapterPosition()).getRecce_image_3());
                                } else {
                                    i.putExtra("image3", image3);
                                }
                                if (receelist.get(getAdapterPosition()).getRecce_image_4().toString() != null) {
                                    i.putExtra("image4", receelist.get(getAdapterPosition()).getRecce_image_4());
                                } else {
                                    i.putExtra("image4", image4);
                                }

                                if (receelist.get(getAdapterPosition()).getOutlet_address().toString() != null) {
                                    i.putExtra("address", receelist.get(getAdapterPosition()).getOutlet_address());
                                } else {
                                    i.putExtra("address", address);
                                }
                                if (receelist.get(getAdapterPosition()).getLatitude().toString() != null) {
                                    i.putExtra("latitude", receelist.get(getAdapterPosition()).getLatitude());
                                } else {
                                    i.putExtra("latitude", latitude);
                                }
                                if (receelist.get(getAdapterPosition()).getLongitude().toString() != null) {
                                    i.putExtra("longitude", receelist.get(getAdapterPosition()).getLongitude());
                                } else {
                                    i.putExtra("longitude", longitude);
                                }
                                itemView.getContext().startActivity(i);
                                ((Activity) itemView.getContext()).finish();
                            } else {
                                showLocationDialog(context, receelist.get(getAdapterPosition()).getRecce_id(), "SAMS", "Please Edit Recce Size ");
                            }

                        }
                    }
                }
            });

        }
    }

    private void showRecceEdit(Context activity) {

        AlertDialog.Builder alertbox = new AlertDialog.Builder(activity);
        alertbox.setMessage("Please Check Internet Connection before Would you like to edit product name / size of recce");
        alertbox.setTitle("Sams" +
                "");
        alertbox.setIcon(R.drawable.samslogofinal);

        alertbox.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0,
                                        int arg1) {
                    }
                });
        // String negativeText = context.getApplicationContext().getString(android.R.string.cancel);
        alertbox.show();
    }

    private void showLocationDialog(Context activity, final String recceid, String title, String message) {

        AlertDialog.Builder alertbox = new AlertDialog.Builder(view.getRootView().getContext());
        alertbox.setMessage("Would you like to edit product name / size of an recce");
        alertbox.setTitle("Sams" +
                "");
        alertbox.setIcon(R.drawable.samslogofinal);

        alertbox.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0,
                                        int arg1) {
                        if (recceid.toString() != null) {
                            Intent intent = new Intent(view.getRootView().getContext(), AddRecceWeb.class);
                            intent.putExtra("path", "edit");
                            Log.d("values", web+"index.php?r=app-outlets/app-recce-edit&user_id=" + Preferences.getUserid(view.getRootView().getContext()) + "&crew_person_id=" + Preferences.getCrewPersonid_project(view.getRootView().getContext()) + "&recce_id=" + recceid);
                            intent.putExtra("url", web+"index.php?r=app-outlets/app-recce-edit&user_id=" + Preferences.getUserid(view.getRootView().getContext()) + "&crew_person_id=" + Preferences.getCrewPersonid_project(view.getRootView().getContext()) + "&recce_id=" + recceid);
                            view.getRootView().getContext().startActivity(intent);
                            ((Activity) view.getRootView().getContext()).finish();
                        }
                    }
                });
        // String negativeText = context.getApplicationContext().getString(android.R.string.cancel);
        alertbox.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                    }
                });
        alertbox.show();
    }

    // Filter Class
    public void filteraddress(String charText) {
        charText = charText.toUpperCase(Locale.getDefault());
       // charText = charText.toUpperCase();
        // dealerses.clear();
        if (charText.length() == 0) {
            receelist = receelist_filter;
        } else {
            ArrayList<Recce> filteredList = new ArrayList<>();
            for (Recce androidVersion : receelist) {
                //  Toast.makeText(context,charText.toString(),Toast.LENGTH_SHORT).show();

                if (androidVersion.getOutlet_address().contains(charText)) {
                    filteredList.add(androidVersion);
                }

            }
            receelist = filteredList;
        }
        notifyDataSetChanged();
    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toUpperCase(Locale.getDefault());
        //charText = charText.toLowerCase();
        //  Toast.makeText(context,charText.toString(),Toast.LENGTH_SHORT).show();
        // dealerses.clear();
        if (charText.length() == 0) {
            receelist = receelist_filter;
        } else {
            ArrayList<Recce> filteredList = new ArrayList<>();
            for (Recce androidVersion : receelist) {
                //  Toast.makeText(context.getApplicationContext(),androidVersion.getOutlet_name(),Toast.LENGTH_SHORT).show();
                if (androidVersion.getOutlet_name().contains(charText)) {
                    filteredList.add(androidVersion);
                }
            }
            receelist = filteredList;
        }
        notifyDataSetChanged();
    }


}
