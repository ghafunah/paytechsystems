
package com.paytech.paytechsystems.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.paytech.paytechsystems.getset.User;
import com.paytech.paytechsystems.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 
public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.BrViewHolder> implements Filterable {
    private Context context;
    private List<User> list;
    private List<User> listFiltered;
    private UsersAdapterListener listener;
 
    public class BrViewHolder extends RecyclerView.ViewHolder {
        public TextView u_name, u_email, u_role, u_idno, u_uname;
 
        public BrViewHolder(View view) {
            super(view);
            u_name = (TextView) view.findViewById(R.id.u_name);
            u_idno = (TextView) view.findViewById(R.id.u_idno);
            u_role = (TextView) view.findViewById(R.id.u_role);
            u_uname = (TextView) view.findViewById(R.id.u_uname);
            u_email = (TextView) view.findViewById(R.id.u_email);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onUserSelected(listFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
 
 
    public UsersAdapter(Context context, List<User> list, UsersAdapterListener listener) {
        // public ContactsAdapter(Context context, List<Contact> contactList, ContactsAdapterListener listener)
        this.context = context;
        this.listener = listener;
        this.list = list;
        this.listFiltered = list;
    }


    @Override
    public BrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.users_list_row, parent, false);

        return new BrViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(BrViewHolder holder, int position) {
        User user= listFiltered.get(position);
        holder.u_name.setText(user.getFname() + " "+user.getSname());
        holder.u_idno.setText("Idno : " + user.getIdno());
        holder.u_email.setText("Email : " +user.getEmail());
        holder.u_uname.setText(user.getUname());
        holder.u_role.setText("Role : " +user.getRole());
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
                    List<User> filteredList = new ArrayList<>();
                    for (User row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getFname().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getSname().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getUname().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getRole().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getIdno().toLowerCase().contains(charString.toLowerCase())
                                ){
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
                listFiltered = (ArrayList<User>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
 
    public interface UsersAdapterListener {
        void onUserSelected(User br);
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