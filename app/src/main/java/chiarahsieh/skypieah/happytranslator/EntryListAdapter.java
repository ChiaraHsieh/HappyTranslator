package chiarahsieh.skypieah.happytranslator;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ChiaraHsieh on 2015/12/15.
 */

public class EntryListAdapter extends BaseAdapter implements Filterable {

    private ArrayList<EntryClause> mOriginalValues; // Original Values
    private ArrayList<EntryClause> mDisplayedValues;    // Values to be displayed
    LayoutInflater inflater;

    public EntryListAdapter(Context context, ArrayList<EntryClause> entryClausArrayList) {
        this.mOriginalValues = entryClausArrayList;
        this.mDisplayedValues = entryClausArrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDisplayedValues.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        LinearLayout llContainer;
        TextView tvIdx,tvEng,tvChi,tvTwn;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.row, null);
            holder.llContainer = (LinearLayout)convertView.findViewById(R.id.llContainer);
            holder.tvIdx = (TextView) convertView.findViewById(R.id.tvIdx);
            holder.tvEng = (TextView) convertView.findViewById(R.id.tvEng);
            holder.tvChi = (TextView) convertView.findViewById(R.id.tvChi);
            holder.tvTwn = (TextView) convertView.findViewById(R.id.tvTwn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvEng.setText(mDisplayedValues.get(position).eng);
        holder.tvChi.setText(mDisplayedValues.get(position).chi);
        holder.tvTwn.setText(mDisplayedValues.get(position).twn);

        holder.llContainer.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Log.d("HappyAdapter", mDisplayedValues.get(position).eng + " clicked pos " + position);
            }
        });

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                mDisplayedValues = (ArrayList<EntryClause>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                ArrayList<EntryClause> FilteredArrList = new ArrayList<EntryClause>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<EntryClause>(mDisplayedValues); // saves the original data in mOriginalValues
                }

                /********
                 *
                 *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
                 *  else does the Filtering and returns FilteredArrList(Filtered)
                 *
                 ********/
                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        String dataEng = mOriginalValues.get(i).eng;
                        String dataChi = mOriginalValues.get(i).chi;
                        String dataTwn = mOriginalValues.get(i).twn;
                        if (dataEng.toLowerCase().startsWith(constraint.toString()) ||
                            dataChi.toLowerCase().startsWith(constraint.toString()) ||
                                dataTwn.toLowerCase().startsWith(constraint.toString())    ) {
                            FilteredArrList.add(new EntryClause(dataEng, dataChi,dataTwn));
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}
