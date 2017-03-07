package group7.piseas.Adapters;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Van on 2017-03-07.
 * Created to 'fix' the strange index out of bounds bug.  Bug was caused by:
 * 1.  Outdated android libraries
 * 2.  Requirement to use an extended array adapter as the default spinner implementation accesses the same list causing outofbounds
 */

public class SpinnerAdapter extends ArrayAdapter<CharSequence> {

    private final List<CharSequence> items;

    public SpinnerAdapter(final Context context, final int _resource, final List<CharSequence> items) {
        super(context, _resource, items);
        this.items = items;
        Log.d("SpinnerAdapter", "Construct");
    }

    // IMPORTANT: either override both getCount and getItem or none as they have to access the same list
    @Override
    public int getCount() {
        return this.items.size();
    };

    @Override
    public CharSequence getItem(final int position) {
        return this.items.get(position);
    }

}
