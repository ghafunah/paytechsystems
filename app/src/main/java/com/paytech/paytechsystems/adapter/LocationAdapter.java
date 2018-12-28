
package com.paytech.paytechsystems.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.paytech.paytechsystems.R;
import com.paytech.paytechsystems.getset.MyLocation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 
public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.BrViewHolder> implements Filterable {
    private Context context;
    private List<MyLocation> list;
    private List<MyLocation> listFiltered;
    private LocationAdapterListener listener;
 
    public class BrViewHolder extends RecyclerView.ViewHolder {
        public TextView lat, longt, time;
 
        public BrViewHolder(View view) {
            super(view);
            lat = (TextView) view.findViewById(R.id.latitude);
            //longt = (TextView) view.findViewById(R.id.longitude);
            //time = (TextView) view.findViewById(R.id.updatetime);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                   // listener.onLocationSelected(listFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
 
 
    public LocationAdapter(Context context, List<MyLocation> list, LocationAdapterListener listener) {
        // public ContactsAdapter(Context context, List<Contact> contactList, ContactsAdapterListener listener)
        this.context = context;
        this.listener = listener;
        this.list = list;
        this.listFiltered = list;
    }

    public LocationAdapter(Context context, List<MyLocation> list) {
        // public ContactsAdapter(Context context, List<Contact> contactList, ContactsAdapterListener listener)
        this.context = context;
        this.listener = listener;
        this.list = list;
        this.listFiltered = list;
    }
    @Override
    public BrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.location_list_row, parent, false);
 
        return new BrViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(BrViewHolder holder, int position) {
        MyLocation m = listFiltered.get(position);
        holder.lat.setText(m.getId()+ "  (" + m.getLatitude().toString() + " , "+ m.getLongitude().toString()+") at "+ m.getUpdatetime());
        //holder.longt.setText(m.getUpdatetime());
        //holder.time.setText(m.getUpdatetime());
        // Glide.with(context).load(contact.getImage()).apply(RequestOptions.circleCropTransform()).into(holder.thumbnail);
    }
 
    @Override
    public int getItemCount() {
        return listFiltered.size();
    }

     
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    listFiltered = list;
                } else {
                    List<MyLocation> filteredList = new ArrayList<>();
                    for (MyLocation row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getLatitude().toString().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    listFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = listFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                listFiltered = (ArrayList<MyLocation>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
 
    public interface LocationAdapterListener {
        void onLocationSelected(MyLocation br);
    }

    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("h:mm a d MMM yy");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }

        return "";
    }
}