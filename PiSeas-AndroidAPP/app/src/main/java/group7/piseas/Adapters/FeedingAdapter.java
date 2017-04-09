package group7.piseas.Adapters;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.List;

import group7.piseas.FeedingManagementActivity;
import group7.piseas.Objects.FeedSchedule;
import group7.piseas.R;
import group7.piseas.TankListActivity;
import piseas.network.FishyClient;

import static group7.piseas.FeedingManagementActivity.feeds;

/**
 * Created by Mat on 12/03/2016.
 *
 * Custom list adapter for directions
 */

public class FeedingAdapter extends ArrayAdapter<FeedSchedule> {

    private Context context;
    private List<FeedSchedule> fSchedules;
    private int index;

    public FeedingAdapter(Context context, int resource, List<FeedSchedule> items, int index) {
        super(context, resource, items);
        this.context = context;
        fSchedules = items;
        this.index = index;
    }

    private class ViewHolder{
        TextView txtTime;
        TextView txtSun, txtMon, txtTue, txtWed, txtThu, txtFri, txtSat;
        ImageView deleteButton;
    }

    public View getView(final int position, View convertView, ViewGroup parent){
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
            holder.deleteButton = (ImageView) convertView.findViewById(R.id.xFeedButton);
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog popUp = new AlertDialog.Builder(getContext()).create();
                popUp.setTitle("Are you sure you want to delete this time?");
                popUp.setButton(AlertDialog.BUTTON_NEUTRAL, "Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                popUp.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                feeds.remove(position);
                                dialogInterface.dismiss();
                                boolean[][] weekArray = new boolean[feeds.size()][7];
                                int[] hour = new int[feeds.size()];
                                int[] min = new int[feeds.size()];
                                for (int j =0; j< feeds.size(); j++){
                                    FeedSchedule temp = feeds.get(j);
                                    for (int k = 0; k< 7; k++)
                                        weekArray[j][k] = temp.getWeek(k);
                                    hour[j] = temp.getHour();
                                    min[j] = temp.getMin();
                                }
                                FishyClient.setFeeding(TankListActivity.tankList.get(index).getId(), weekArray,hour, min, FeedingManagementActivity.autoFeed.isChecked(), false);
                                FishyClient.retrieveMobileXmlData(TankListActivity.tankList.get(index).getId(), context.getFilesDir().getAbsolutePath().toString());
                                feeds = TankListActivity.tankList.get(index).getPiSeasXmlHandler().getFeedSchedules();
                                notifyDataSetChanged();
                            }
                        });
                popUp.show();
            }
        });

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
