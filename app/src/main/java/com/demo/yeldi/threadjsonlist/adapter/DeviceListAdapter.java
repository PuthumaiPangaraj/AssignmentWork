package com.demo.yeldi.threadjsonlist.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.demo.yeldi.threadjsonlist.R;
import com.demo.yeldi.threadjsonlist.data.DeviceItem;

import java.util.List;


/**
 * Created by pudumai on 7/8/2016.
 */
public class DeviceListAdapter extends BaseAdapter {
    Context context;
    List<DeviceItem> rowItems;
    public DeviceListAdapter(Context context, List<DeviceItem> items) {
        this.context = context;
        this.rowItems = items;
    }

    /*private view holder class*/
    private class ViewHolder {

        TextView txtDeviceName,txtDeviceType,txtDeviceModel;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LayoutInflater mInflater = (LayoutInflater)
                context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.device_list_item, null);
            holder = new ViewHolder();
            holder.txtDeviceType = (TextView) convertView.findViewById(R.id.txt_device_type);
            holder.txtDeviceName = (TextView) convertView.findViewById(R.id.txt_device_name);
            holder.txtDeviceModel = (TextView) convertView.findViewById(R.id.txt_device_model);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        DeviceItem rowItem = (DeviceItem) getItem(position);

        holder.txtDeviceType.setText("Type:  "  +rowItem.getDevicType());
        holder.txtDeviceName.setText("Name:  "  +rowItem.getName());
        holder.txtDeviceModel.setText("Model:  " +rowItem.getModel());

        return convertView;
    }

    @Override
    public int getCount() {
        return rowItems.size();
    }

    @Override
    public Object getItem(int position) {
        return rowItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return rowItems.indexOf(getItem(position));
    }
}
