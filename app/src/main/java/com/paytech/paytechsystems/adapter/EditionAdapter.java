
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
import com.paytech.paytechsystems.getset.Edition;

import java.util.ArrayList;
import java.util.List;
 
public class EditionAdapter extends RecyclerView.Adapter<EditionAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<Edition> usersList;
    private List<Edition> usersListFiltered;
    private EditionAdapterListener listener;
 
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView course, edition, fees;
 
        public MyViewHolder(View view) {
            super(view);
            course = (TextView) view.findViewById(R.id.course);
            edition = (TextView) view.findViewById(R.id.edition);
            fees = (TextView) view.findViewById(R.id.fees);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onEditionSelected(usersListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
 
 
    public EditionAdapter(Context context, List<Edition> usersList, EditionAdapterListener listener) {
        // public ContactsAdapter(Context context, List<Contact> contactList, ContactsAdapterListener listener)
        this.context = context;
        this.listener = listener;
        this.usersList = usersList;
        this.usersListFiltered = usersList;
    }

//
//    public UsersAdapter(List<User> usersList, UsersAdapterListener listener) {
//        // public ContactsAdapter(Context context, List<Contact> contactList, ContactsAdapterListener listener)
//        this.context = context;
//        this.listener = listener;
//        this.usersList = usersList;
//        this.usersListFiltered = usersList;
//    }
 
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.edition_list_row, parent, false);
 
        return new MyViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Edition i = usersListFiltered.get(position);
        holder.course.setText("Course : " +i.getCourse());
        holder.edition.setText("Edition : " + i.getEdition());
        holder.fees.setText("Fees : " +i.getFees());
        // Glide.with(context).load(contact.getImage()).apply(RequestOptions.circleCropTransform()).into(holder.thumbnail);
    }
 
    @Override
    public int getItemCount() {
        return usersListFiltered.size();
    }

     
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    usersListFiltered = usersList;
                } else {
                    List<Edition> filteredList = new ArrayList<>();
                    for (Edition row : usersList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        // || row.getSname().contains(charString.toLowerCase()))
                        if (row.getEdition().toLowerCase().contains(charString.toLowerCase()))

                        {
                            filteredList.add(row);
                        }
                    }

                    usersListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = usersListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                usersListFiltered = (ArrayList<Edition>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
 
    public interface EditionAdapterListener {
        void onEditionSelected(Edition user);
    }
}