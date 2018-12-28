
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
import com.paytech.paytechsystems.getset.Course;

import java.util.ArrayList;
import java.util.List;
 
public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.BrViewHolder> implements Filterable {
    private Context context;
    private List<Course> studentList;
    private List<Course> studentListFiltered;
    private CourseAdapterListener listener;
 
    public class BrViewHolder extends RecyclerView.ViewHolder {
        public TextView cid, cname;
 
        public BrViewHolder(View view) {
            super(view);
            cname = (TextView) view.findViewById(R.id.cname);
            cid = (TextView) view.findViewById(R.id.cid);
            //temail = (TextView) view.findViewById(R.id.temail);
            //tdate = (TextView) view.findViewById(R.id.tdate);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onCourseSelected(studentListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
 
 
    public CourseAdapter(Context context, List<Course> studentList, CourseAdapterListener listener) {
        // public ContactsAdapter(Context context, List<Contact> contactList, ContactsAdapterListener listener)
        this.context = context;
        this.listener = listener;
        this.studentList = studentList;
        this.studentListFiltered = studentList;
    }


    @Override
    public BrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_list_row, parent, false);
 
        return new BrViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(BrViewHolder holder, int position) {
        Course br = studentListFiltered.get(position);
        holder.cname.setText(br.getCname());
        //holder.cid.setText(br.getCreatedat());
        //holder.temail.setText(br.getEmail());
       // holder.tdate.setText("Add Date");
        // Glide.with(context).load(contact.getImage()).apply(RequestOptions.circleCropTransform()).into(holder.thumbnail);
    }
 
    @Override
    public int getItemCount() {
        return studentListFiltered.size();
    }

     
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    studentListFiltered = studentList;
                } else {
                    List<Course> filteredList = new ArrayList<>();
                    for (Course row : studentList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getCname().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    studentListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = studentListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                studentListFiltered = (ArrayList<Course>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
 
    public interface CourseAdapterListener {
        void onCourseSelected(Course i);
    }
}