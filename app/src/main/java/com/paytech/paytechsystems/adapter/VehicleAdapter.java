
package com.paytech.paytechsystems.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.paytech.paytechsystems.getset.Vehicle;
import com.paytech.paytechsystems.R;

import java.util.ArrayList;
import java.util.List;
 
public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.BrViewHolder> implements Filterable {
    private Context context;
    private List<Vehicle> list;
    private List<Vehicle> listFiltered;
    private VehicleAdapterListener listener;
 
    public class BrViewHolder extends RecyclerView.ViewHolder {
        public TextView regno, make, type, dateadd;
 
        public BrViewHolder(View view) {
            super(view);
            regno = (TextView) view.findViewById(R.id.service);
            //tref = (TextView) view.findViewById(R.id.tref);
            make = (TextView) view.findViewById(R.id.phone);
            type = (TextView) view.findViewById(R.id.fname);
            dateadd = (TextView) view.findViewById(R.id.date);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onVehicleSelected(listFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
 
 
    public VehicleAdapter(Context context, List<Vehicle> list, VehicleAdapterListener listener) {
        // public ContactsAdapter(Context context, List<Contact> contactList, ContactsAdapterListener listener)
        this.context = context;
        this.listener = listener;
        this.list = list;
        this.listFiltered = list;
    }


    @Override
    public BrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mpesa_list_row, parent, false);
 
        return new BrViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(BrViewHolder holder, int position) {
        Vehicle m = listFiltered.get(position);
        holder.regno.setText(m.getRegno());
        holder.make.setText(m.getMake());
        holder.type.setText(m.getType());
        holder.dateadd.setText(m.getDateadd());
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
                    List<Vehicle> filteredList = new ArrayList<>();
                    for (Vehicle row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getRegno().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getType().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getMake().toLowerCase().contains(charString.toLowerCase())) {
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
                listFiltered = (ArrayList<Vehicle>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
 
    public interface VehicleAdapterListener {
        void onVehicleSelected(Vehicle br);
    }
}