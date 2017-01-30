package group7.piseas.Adapters;


import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.List;

import group7.piseas.Objects.FeedSchedule;
import group7.piseas.R;

/**
 * Created by Mat on 12/03/2016.
 *
 * Custom list adapter for directions
 */

public class FeedingAdapter extends ArrayAdapter<FeedSchedule> {

    private Context context;

    public FeedingAdapter(Context context, int resource, List<FeedSchedule> items) {
        super(context, resource, items);
        this.context = context;
    }

    private class ViewHolder{
        TextView txtTime;
        TextView txtSun, txtMon, txtTue, txtWed, txtThu, txtFri, txtSat;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder holder = null;

        FeedSchedule row = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if(convertView == null){
            convertView = mInflater.inflate(R.layout.days_list_items, null);

            holder = new ViewHolder();

            holder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
            holder.txtSun = (TextView) convertView.findViewById(R.id.dataSun);
            holder.txtMon = (TextView) convertView.findViewById(R.id.dataMon);
            holder.txtTue = (TextView) convertView.findViewById(R.id.dataTue);
            holder.txtWed = (TextView) convertView.findViewById(R.id.dataWed);
            holder.txtThu = (TextView) convertView.findViewById(R.id.dataThur);
            holder.txtFri = (TextView) convertView.findViewById(R.id.dataFri);
            holder.txtSat = (TextView) convertView.findViewById(R.id.dataSat);

            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();

        holder.txtTime.setText(row.getTime());

        if(row.getWeek(0))
            holder.txtSun.setTextColor(ContextCompat.getColor(context, R.color.lightBlue));
        else
            holder.txtSun.setTextColor(ContextCompat.getColor(context, R.color.darkBlue));
        if(row.getWeek(1))
            holder.txtMon.setTextColor(ContextCompat.getColor(context, R.color.lightBlue));
        else
            holder.txtMon.setTextColor(ContextCompat.getColor(context, R.color.darkBlue));
        if(row.getWeek(2))
            holder.txtTue.setTextColor(ContextCompat.getColor(context, R.color.lightBlue));
        else
            holder.txtTue.setTextColor(ContextCompat.getColor(context, R.color.darkBlue));
        if(row.getWeek(3))
            holder.txtWed.setTextColor(ContextCompat.getColor(context, R.color.lightBlue));
        else
            holder.txtWed.setTextColor(ContextCompat.getColor(context, R.color.darkBlue));
        if(row.getWeek(4))
            holder.txtThu.setTextColor(ContextCompat.getColor(context, R.color.lightBlue));
        else
            holder.txtThu.setTextColor(ContextCompat.getColor(context, R.color.darkBlue));
        if(row.getWeek(5))
            holder.txtFri.setTextColor(ContextCompat.getColor(context, R.color.lightBlue));
        else
            holder.txtFri.setTextColor(ContextCompat.getColor(context, R.color.darkBlue));
        if(row.getWeek(6))
            holder.txtSat.setTextColor(ContextCompat.getColor(context, R.color.lightBlue));
        else
            holder.txtSat.setTextColor(ContextCompat.getColor(context, R.color.darkBlue));

        return convertView;
    }

}
