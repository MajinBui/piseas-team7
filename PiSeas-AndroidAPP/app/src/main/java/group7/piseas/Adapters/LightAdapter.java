package group7.piseas.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import group7.piseas.Objects.LightSchedule;
import group7.piseas.R;

/**
 * Created by Mat on 12/6/2016.
 *
 */

public class LightAdapter extends ArrayAdapter<LightSchedule>{
private Context context;

    public LightAdapter(Context context, int resource, List<LightSchedule> items) {
        super(context, resource, items);
        this.context = context;
    }

    private class ViewHolder{
        TextView txtTimeOn;
        TextView txtTimeOff;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LightSchedule row = getItem(position);


        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.days_list_items, null);

            holder = new LightAdapter.ViewHolder();

            holder.txtTimeOn = (TextView) convertView.findViewById(R.id.txtTimeOn);
            holder.txtTimeOff = (TextView) convertView.findViewById(R.id.txtTimeOff);
        }
            return convertView;
    }

}
