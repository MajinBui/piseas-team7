package group7.piseas.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import group7.piseas.Objects.LogItem;
import group7.piseas.R;

/**
 * Created by Salli on 2017-02-22.
 */

public class LogAdapter extends ArrayAdapter<LogItem> {
    private int layout;
    private List<LogItem> itemList;

    public LogAdapter(Context context,
                       int resourceId,
                       List<LogItem> items){
        super( context, resourceId, items );
        layout = resourceId;
        itemList = items;
    }

    private class ViewHolder {
        TextView textViewTime;
        TextView textViewdesc;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        final LogItem rowItem = getItem(position); // retrieve from the List (data set)

        if (convertView == null) { // the old/recycled view does NOT exist
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(layout, parent, false);

            holder = new ViewHolder();

            holder.textViewTime  = (TextView) convertView.findViewById( R.id.time );
            holder.textViewdesc  = (TextView) convertView.findViewById( R.id.desc );
            convertView.setTag(holder); // associate the view holder with the new View
        }
        else
            holder = (ViewHolder) convertView.getTag(); // retrieve the view holder of

        holder.textViewTime.setText(rowItem.getTimestamp());
        holder.textViewdesc.setText(rowItem.getDescription());

        return convertView;
    }
}
