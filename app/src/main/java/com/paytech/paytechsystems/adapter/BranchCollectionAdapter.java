
package com.paytech.paytechsystems.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.paytech.paytechsystems.getset.BranchCollection;
import com.paytech.paytechsystems.R;

import java.util.ArrayList;
import java.util.List;
 
public class BranchCollectionAdapter extends RecyclerView.Adapter<BranchCollectionAdapter.BrViewHolder> implements Filterable {
    private Context context;
    private List<BranchCollection> list;
    private List<BranchCollection> listFiltered;
    private BranchCollectionAdapterListener listener;
 
    public class BrViewHolder extends RecyclerView.ViewHolder {
        public TextView branch, fees, date;
 
        public BrViewHolder(View view) {
            super(view);
            branch = (TextView) view.findViewById(R.id.branch);
            fees = (TextView) view.findViewById(R.id.fees);
            date = (TextView) view.findViewById(R.id.date);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onBranchCollectionSelected(listFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
 
 
    public BranchCollectionAdapter(Context context, List<BranchCollection> list, BranchCollectionAdapterListener listener) {
        // public ContactsAdapter(Context context, List<Contact> contactList, ContactsAdapterListener listener)
        this.context = context;
        this.listener = listener;
        this.list = list;
        this.listFiltered = list;
    }


    @Override
    public BrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.branchcollection_list_row, parent, false);
 
        return new BrViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(BrViewHolder holder, int position) {
        BranchCollection m = listFiltered.get(position);
        holder.branch.setText(m.getBranch() + " - Ksh. " + m.getFees());
        //holder.fees.setText(m.getFees());
        holder.date.setText(m.getDate());
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
                    List<BranchCollection> filteredList = new ArrayList<>();
                    for (BranchCollection row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getBranch().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getDate().toLowerCase().contains(charString.toLowerCase())) {
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
                listFiltered = (ArrayList<BranchCollection>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
 
    public interface BranchCollectionAdapterListener {
        void onBranchCollectionSelected(BranchCollection br);
    }
}