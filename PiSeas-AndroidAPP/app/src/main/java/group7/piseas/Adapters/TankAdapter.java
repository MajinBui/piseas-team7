package group7.piseas.Adapters;

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


import java.util.HashMap;
import java.util.List;

import group7.piseas.R;
import group7.piseas.Objects.Tank;
import group7.piseas.TankListActivity;

/**
 * Created by Sallie on 06/12/2016.
 */
public class TankAdapter extends ArrayAdapter<Tank> {
    private int layout;
    private List<Tank> itemList;

    public TankAdapter(Context context,
                       int resourceId,
                       List<Tank> items){
        super( context, resourceId, items );
        layout = resourceId;
        itemList = items;
    }

    private class ViewHolder {
        TextView textViewName;
        ImageView deleteButton;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        final Tank rowItem = getItem(position); // retrieve from the List (data set)

        if (convertView == null) { // the old/recycled view does NOT exist
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);

            holder = new ViewHolder();

            holder.textViewName  = (TextView) convertView.findViewById( R.id.name );
            holder.deleteButton  = (ImageView) convertView.findViewById( R.id.deleteButton );
            convertView.setTag(holder); // associate the view holder with the new View
        }
        else
            holder = (ViewHolder) convertView.getTag(); // retrieve the view holder of
        // TODO: OLD SERVER SET UP
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("DELETE", "1");

                AlertDialog popUp = new AlertDialog.Builder(getContext()).create();
                popUp.setTitle("Are you sure you want to delete the " + rowItem.getName() +" tank?");
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
                                HashMap<String, String> dataList =new HashMap<String, String>();
                                dataList.put("name", "");
                                dataList.put("type", 0+"");
                                dataList.put("size", 0+"");
                                dataList.put("desc", "");
                                //FishyClient.writeToServerData(TankListActivity.tankList.get(position).getId()+"", dataList);

                                TankListActivity.tankList.remove(position);
                                TankListActivity.update();
                                dialogInterface.dismiss();
                            }
                        });
                popUp.show();
            }
        });

        holder.textViewName.setText(rowItem.getName());

        return convertView;
    }
}
