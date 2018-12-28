
package com.paytech.paytechsystems.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.paytech.paytechsystems.getset.Branch;
import com.paytech.paytechsystems.R;

import java.util.ArrayList;
import java.util.List;
 
public class BranchAdapter extends RecyclerView.Adapter<BranchAdapter.BrViewHolder> implements Filterable {
    private Context context;
    private List<Branch> list;
    private List<Branch> listFiltered;
    private BranchAdapterListener listener;
 
    public class BrViewHolder extends RecyclerView.ViewHolder {
        public TextView name, phone, email, till;
 
        public BrViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            phone = (TextView) view.findViewById(R.id.phone);
            email = (TextView) view.findViewById(R.id.email);
            till = (TextView) view.findViewById(R.id.till);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onBranchSelected(listFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
 
 
    public BranchAdapter(Context context, List<Branch> list, BranchAdapterListener listener) {
        // public ContactsAdapter(Context context, List<Contact> contactList, ContactsAdapterListener listener)
        this.context = context;
        this.listener = listener;
        this.list = list;
        this.listFiltered = list;
    }


    @Override
    public BrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.branch_list_row, parent, false);
 
        return new BrViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(BrViewHolder holder, int position) {
        Branch m = listFiltered.get(position);
        holder.name.setText(m.getCode()+ " : " +m.getName());
        holder.email.setText("Email  : " + m.getEmail());
        holder.till.setText("Till  : " + m.getTill());
        if (m.getLocation() != null) {
             holder.phone.setText("Location : " + m.getLocation());
         }else {
             holder.phone.setVisibility(View.GONE);
         }

        // String color = bgColors[position % bgColors.length];
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
                    List<Branch> filteredList = new ArrayList<>();
                    for (Branch row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getCode().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getTill().toLowerCase().contains(charString.toLowerCase())) {
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
                listFiltered = (ArrayList<Branch>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
 
    public interface BranchAdapterListener {
        void onBranchSelected(Branch br);
    }
}