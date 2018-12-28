
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
import com.paytech.paytechsystems.getset.Teacher;

import java.util.ArrayList;
import java.util.List;
 
public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.BrViewHolder> implements Filterable {
    private Context context;
    private List<Teacher> list;
    private List<Teacher> listFiltered;
    private TeacherAdapterListener listener;
 
    public class BrViewHolder extends RecyclerView.ViewHolder {
        public TextView tname, tphone, temail, tdate;
 
        public BrViewHolder(View view) {
            super(view);
            tname = (TextView) view.findViewById(R.id.tname);
            tphone = (TextView) view.findViewById(R.id.tphone);
            temail = (TextView) view.findViewById(R.id.temail);
            tdate = (TextView) view.findViewById(R.id.tdate);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onTeacherSelected(listFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
 
 
    public TeacherAdapter(Context context, List<Teacher> list, TeacherAdapterListener listener) {
        // public ContactsAdapter(Context context, List<Contact> contactList, ContactsAdapterListener listener)
        this.context = context;
        this.listener = listener;
        this.list = list;
        this.listFiltered = list;
    }


    @Override
    public BrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.teacher_list_row, parent, false);
 
        return new BrViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(BrViewHolder holder, int position) {
        Teacher br = listFiltered.get(position);
        holder.tname.setText(br.getFname() + " " +br.getSname());
        holder.tphone.setText(br.getPhone());
        holder.temail.setText(br.getEmail());
        holder.tdate.setText(br.getCreatedat());
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
                    List<Teacher> filteredList = new ArrayList<>();
                    for (Teacher row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getFname().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getSname().toLowerCase().contains(charString.toLowerCase())) {
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
                listFiltered = (ArrayList<Teacher>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
 
    public interface TeacherAdapterListener {
        void onTeacherSelected(Teacher i);
    }
}