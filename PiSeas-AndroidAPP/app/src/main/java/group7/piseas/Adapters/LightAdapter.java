package group7.piseas.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import group7.piseas.LightManagementActivity;
import group7.piseas.Objects.LightSchedule;
import group7.piseas.R;
import group7.piseas.TankListActivity;
import piseas.network.FishyClient;

/**
 * Created by Mat on 12/6/2016.
 *
 */

public class LightAdapter extends ArrayAdapter<LightSchedule>{
    private Context context;
    private List<LightSchedule> lSchedules;
    private int index;


    public LightAdapter(Context context, int resource, List<LightSchedule> items, int index_) {
        super(context, resource, items);
        this.context = context;
        lSchedules = items;
        index = index_;
    }

    private class ViewHolder{
        TextView txtTime;
        ImageView deleteButton;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        LightSchedule row = getItem(position);
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if(convertView == null) {
            convertView = mInflater.inflate(R.layout.lights_item_list, null);

            holder = new LightAdapter.ViewHolder();

            holder.txtTime = (TextView) convertView.findViewById(R.id.txtTime);
            holder.deleteButton  = (ImageView) convertView.findViewById( R.id.xLightButton );
            convertView.setTag(holder);
        }
        else
            holder = (ViewHolder) convertView.getTag();

        holder.deleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.i("DELETE LIGHT", "1");

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
                                LightManagementActivity.lights.remove(position);
                                LightManagementActivity.update();

//                                TankListActivity.tankList.get(index).getPiSeasXmlHandler()
//                                        .setLight(LightManagementActivity.lights, false, false);

                                dialogInterface.dismiss();

                                int size = LightManagementActivity.lights.size();
                                int[] hrOn = new int[size];
                                int[] minOn = new int[size];
                                int[] hrOff = new int[size];
                                int[] minOff = new int[size];

                                for(int van = 0; van<LightManagementActivity.lights.size();van++){
                                    hrOn[van] = LightManagementActivity.lights.get(van).getOnHour();
                                    minOn[van] = LightManagementActivity.lights.get(van).getOnMin();
                                    hrOff[van] = LightManagementActivity.lights.get(van).getOffHour();
                                    minOff[van] = LightManagementActivity.lights.get(van).getOffMin();
                                }

                                // int[] hrOn, int[]minOn, int[] hrOff, int[] minOff, bool auto, bool manual
                                FishyClient.setLighting(TankListActivity.tankList.get(index).getId(), hrOn, minOn, hrOff, minOff,
                                        LightManagementActivity.autoLight.isChecked(),
                                        LightManagementActivity.manual.isChecked());

//                                FishyClient.sendMobileXmlData(TankListActivity.tankList.get(index).getId(),
//                                        context.getFilesDir().getAbsolutePath().toString());
                            }
                        }
                );
                popUp.show();
            }
        });


        holder.txtTime.setText(row.getTime());
        return convertView;
    }

}
