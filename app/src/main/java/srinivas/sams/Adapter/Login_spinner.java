package srinivas.sams.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import srinivas.sams.Activity.Login;
import srinivas.sams.R;
import srinivas.sams.model.Vendor;

/**
 * Created by venky on 11-Aug-17.
 */

public class Login_spinner extends BaseAdapter {
    Context context;
    List<Vendor> vendors;

    public Login_spinner(Login login, List<Vendor> vendors) {
        this.vendors = vendors;
        this.context = login;
    }

    @Override
    public int getCount() {
        return vendors.size();
    }

    @Override
    public Object getItem(int position) {
        return vendors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.loginspinner, parent, false);

        }
        TextView vendorid_tv = (TextView) convertView.findViewById(R.id.vendorid_tv);
        vendorid_tv.setText(vendors.get(position).getVendor_name());
        return convertView;
    }
}
