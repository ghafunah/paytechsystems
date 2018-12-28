
package com.paytech.paytechsystems.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.paytech.paytechsystems.getset.Permission;
import com.paytech.paytechsystems.R;

import java.util.ArrayList;
import java.util.List;
 
public class PermissionAdapter extends RecyclerView.Adapter<PermissionAdapter.BrViewHolder> implements Filterable {
    private Context context;
    private List<Permission> list;
    private List<Permission> listFiltered;
    private PermissionAdapterListener listener;
 
    public class BrViewHolder extends RecyclerView.ViewHolder {
        public TextView page, users, dateadded;
 
        public BrViewHolder(View view) {
            super(view);
            page = (TextView) view.findViewById(R.id.page);
            users = (TextView) view.findViewById(R.id.users);
            dateadded = (TextView) view.findViewById(R.id.dateadded);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onPermissionSelected(listFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
 
 
    public PermissionAdapter(Context context, List<Permission> list, PermissionAdapterListener listener) {
        // public ContactsAdapter(Context context, List<Contact> contactList, ContactsAdapterListener listener)
        this.context = context;
        this.listener = listener;
        this.list = list;
        this.listFiltered = list;
    }


    @Override
    public BrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.permission_list_row, parent, false);
 
        return new BrViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(BrViewHolder holder, int position) {
        Permission m = listFiltered.get(position);
        holder.page.setText(m.getPage() );
        holder.users.setText(m.getUsers());
        holder.dateadded.setText(m.getCreated_at());
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
                    List<Permission> filteredList = new ArrayList<>();
                    for (Permission row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getPage().toLowerCase().contains(charString.toLowerCase()) || row.getUsers().toLowerCase().contains(charString.toLowerCase())) {
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
                listFiltered = (ArrayList<Permission>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
 
    public interface PermissionAdapterListener {
        void onPermissionSelected(Permission br);
    }
}