package srinivas.sams.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import srinivas.sams.Activity.Recces_display;
import srinivas.sams.R;
import srinivas.sams.helper.Preferences;
import srinivas.sams.model.Products;

/**
 * Created by venky on 11-Aug-17.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.Recceholder> {
    private List<Products> productses;
    private List<Products> productses_filter;
    private int rowLayout;
    public Context context;
    static SQLiteDatabase db;
    public ProductAdapter(List<Products> productses, int rowLayout, Context context) {
        this.productses = productses;
        this.productses_filter = productses;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public Recceholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new Recceholder(view);
    }


    @Override
    public void onBindViewHolder(Recceholder holder, int position) {
      //  Toast.makeText(context,productses.get(position).getProduct_name().toString(),Toast.LENGTH_SHORT).show();
        holder.product_name_tv.setText(productses.get(position).getProduct_name().toString());


    }

    @Override
    public int getItemCount() {
        return productses.size();
    }

    public class Recceholder extends RecyclerView.ViewHolder {
        TextView product_name_tv;

        //@BindView(R.id.recce_img) ImageView ;
        public Recceholder(final View itemView) {
            super(itemView);
            //ButterKnife.bind(itemView);
            product_name_tv = (TextView) itemView.findViewById(R.id.product_name);
            product_name_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ;
                    db = context.openOrCreateDatabase("SAMS", Context.MODE_PRIVATE, null);
                    db.execSQL("UPDATE recce SET product_name='" + productses.get(getAdapterPosition()).getProduct_name() +
                            "',product_id='"+productses.get(getAdapterPosition()).getProduct_id().toString()+"'"
                            + "  WHERE recce_id=" + Preferences.getreeceId_product(context));
                   // Toast.makeText(context,productses.get(getAdapterPosition()).getProduct_id().toString(),Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context,Recces_display.class);
                    itemView.getContext().startActivity(i);
                    ((Activity)itemView.getContext()).finish();
                }
            });
        }
    }
    // Filter Class
    public void filter(String charText) {
        charText = charText.toUpperCase(Locale.getDefault());
       // Toast.makeText(context,charText.toString(),Toast.LENGTH_SHORT).show();
        // dealerses.clear();
        if (charText.length() == 0) {
            productses = productses_filter;
        } else {
          /*  String xx = String.valueOf(dealersesfilter.size());
            Toast.makeText(displayRecylerview.getApplication(),"texdtentered "+xx,Toast.LENGTH_SHORT).show();
          */
            ArrayList<Products> filteredList = new ArrayList<>();

            for (Products androidVersion : productses) {
                if (androidVersion.getProduct_name().contains(charText)){
                    filteredList.add(androidVersion);
                }
            }
            productses = filteredList;
        }
        notifyDataSetChanged();
    }

}
