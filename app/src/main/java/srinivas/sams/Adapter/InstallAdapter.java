package srinivas.sams.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import srinivas.sams.Activity.Update_Install;
import srinivas.sams.R;
import srinivas.sams.model.Installation;
import srinivas.sams.validation.Validation;

/**
 * Created by venky on 11-Aug-17.
 */

public class InstallAdapter extends RecyclerView.Adapter<InstallAdapter.Recceholder> {
    private List<Installation> installations;
    private List<Installation> installations_filter;
    private int rowLayout;
    public Context context;
    private DisplayImageOptions options;
   // public String web = "http://128.199.131.14/sams/web/";
  // public String web = "http://128.199.131.14/samsdev/web/";
    public String web = "http://128.199.131.14/samsapp/web/";
    public InstallAdapter(List<Installation> installations, int rowLayout, Context context) {
        this.installations = installations;
        this.installations_filter = installations;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new Recceholder(view);
    }

    @Override
    public void onBindViewHolder(Recceholder holder, int position) {
        holder.outletname_tv.setText(installations.get(position).getOutlet_name().toString());
        holder.outletaddress_tv.setText(installations.get(position).getOutlet_address().toString().toLowerCase());
        holder.productname_tv.setText(installations.get(position).getProduct_name().toString());
        holder.height_width_tv.setText(installations.get(position).getWidth().toString() + "X" + installations.get(position).getHeight().toString()
        +"  ("+installations.get(position).getUom_name().toString()+")");
        holder.recce_status_tv.setText(installations.get(position).getInstallation_image_upload_status().toString());
        if (installations.get(position).getInstallation_image_upload_status().equals("Completed")) {
            holder.recce_img.setScaleType(ImageView.ScaleType.FIT_XY);
            holder.recce_status_tv.setTextColor(Color.parseColor("#00a65a"));
            holder.recce_status_tv.setText(installations.get(position).getInstallation_image_upload_status().toString());

        } else {
            holder.recce_img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }

        Bitmap bmImage = null;
        if (!Validation.internet(context)) {
            if (installations.get(position).getInstallation_image().toString().contains("storage")) {
                bmImage = BitmapFactory.decodeFile(installations.get(position).getInstallation_image().toString(), null);
                holder.recce_img.setImageBitmap(bmImage);
            } else {
               // holder.recce_img.setImageResource(R.drawable.dummy);
                ImageLoader.getInstance()
                        .displayImage(web+"image_uploads/install_uploads/" + installations.get(position).getInstallation_image().toString()
                                , holder.recce_img, options);
            }

        } else {
            DiskCacheUtils.removeFromCache(web+"image_uploads/install_uploads/" + installations.get(position).getInstallation_image().toString(), ImageLoader.getInstance().getDiskCache());
            MemoryCacheUtils.removeFromCache(web+"image_uploads/install_uploads/" + installations.get(position).getInstallation_image().toString(), ImageLoader.getInstance().getMemoryCache());
            ImageLoader.getInstance()
                    .displayImage(web+"image_uploads/recce_uploads/" +installations.get(position).getRecce_image().toString()
                            , holder.recce_img_hide, options);

            ImageLoader.getInstance()
                    .displayImage(web+"image_uploads/install_uploads/" + installations.get(position).getInstallation_image().toString()
                            , holder.recce_img, options);


            Log.d("installationpic",installations.get(position).getInstallation_image().toString());
        }

    }

    @Override
    public int getItemCount() {
        return installations.size();
    }

    public class Recceholder extends RecyclerView.ViewHolder {
        TextView outletname_tv, outletaddress_tv, productname_tv, height_width_tv, recce_status_tv, recce_edit_tv;
        ImageView recce_img,recce_img_hide;

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
            recce_edit_tv.setVisibility(View.GONE);
            recce_img = (ImageView) itemView.findViewById(R.id.recce_img);
            recce_img_hide = (ImageView)itemView.findViewById(R.id.recce_img_hide);
            recce_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //   Toast.makeText(v.getContext(), "install adapter ra babu", Toast.LENGTH_SHORT).show();
                    String installdate = "", installremark = "", recce_image = "", install_image = "",
                            install_image_1="",install_image_2="";
                    Intent updateinstall = new Intent(context, Update_Install.class);
                    updateinstall.putExtra("recce_id", installations.get(getAdapterPosition()).getRecce_id().toString());
                    if (installations.get(getAdapterPosition()).getInstallation_date().toString() != null) {
                        if (installations.get(getAdapterPosition()).getInstallation_date().toString().equals("0000-00-00")){
                            Date cDate = new Date();
                            String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
                            updateinstall.putExtra("install_date", fDate);
                        }else{
                            updateinstall.putExtra("install_date", installations.get(getAdapterPosition()).getInstallation_date().toString());
                        }
                    } else {
                        updateinstall.putExtra("install_date", installdate);
                    }
                    if (installations.get(getAdapterPosition()).getInstallation_remarks().toString() != null) {
                        updateinstall.putExtra("install_remark", installations.get(getAdapterPosition()).getInstallation_remarks().toString());

                    } else {
                        updateinstall.putExtra("install_remark", installremark);

                    }

                    if (installations.get(getAdapterPosition()).getRecce_image().toString() != null) {
                        updateinstall.putExtra("recce_image", installations.get(getAdapterPosition()).getRecce_image().toString());
                    } else {
                        updateinstall.putExtra("recce_image", recce_image);
                    }

                    if (installations.get(getAdapterPosition()).getInstallation_image().toString() != null) {
                        updateinstall.putExtra("install_image", installations.get(getAdapterPosition()).getInstallation_image().toString());
                    } else {
                        updateinstall.putExtra("install_image", install_image);
                    }
                    if (installations.get(getAdapterPosition()).getInstallation_image().toString() != null) {
                        updateinstall.putExtra("install_image_2", installations.get(getAdapterPosition()).getInstallation_image_1().toString());
                    } else {
                        updateinstall.putExtra("install_image_2", install_image);
                    }
                    if (installations.get(getAdapterPosition()).getInstallation_image().toString() != null) {
                        updateinstall.putExtra("install_image_3", installations.get(getAdapterPosition()).getInstallation_image_2().toString());
                    } else {
                        updateinstall.putExtra("install_image_3", install_image);
                    }
                    updateinstall.putExtra("status", installations.get(getAdapterPosition()).getInstallation_image_upload_status());
                    ;
                    itemView.getContext().startActivity(updateinstall);
                    ((Activity) itemView.getContext()).finish();
                }
            });

        }
    }

    public void filter(String charText) {
        charText = charText.toUpperCase(Locale.getDefault());
       // Toast.makeText(context, charText.toString(), Toast.LENGTH_SHORT).show();
        // dealerses.clear();
        if (charText.length() == 0) {
            installations = installations_filter;
        } else {

            ArrayList<Installation> filteredList = new ArrayList<>();

            for (Installation androidVersion : installations) {
                if (androidVersion.getOutlet_name().contains(charText)) {
                    filteredList.add(androidVersion);
                }
            }
            installations = filteredList;
        }
        notifyDataSetChanged();
    }

    public void filteraddress(String charText) {
        charText = charText.toUpperCase(Locale.getDefault());
       // Toast.makeText(context, charText.toString(), Toast.LENGTH_SHORT).show();
        // dealerses.clear();
        if (charText.length() == 0) {
            installations = installations_filter;
        } else {
            ArrayList<Installation> filteredList = new ArrayList<>();

            for (Installation androidVersion : installations) {
                if (androidVersion.getOutlet_address().contains(charText)) {
                    filteredList.add(androidVersion);
                }
            }
            installations = filteredList;
        }
        notifyDataSetChanged();
    }

}
