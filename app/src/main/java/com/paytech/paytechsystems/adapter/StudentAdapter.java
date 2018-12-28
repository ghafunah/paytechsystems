
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
import com.paytech.paytechsystems.getset.Student;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 
public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.BrViewHolder> implements Filterable {
    private Context context;
    private List<Student> studentList;
    private List<Student> studentListFiltered;
    private StudentAdapterListener listener;
 
    public class BrViewHolder extends RecyclerView.ViewHolder {
        public TextView bname, bphone, bemail, admdate;
 
        public BrViewHolder(View view) {
            super(view);
            bname = (TextView) view.findViewById(R.id.stdfname);
            bphone = (TextView) view.findViewById(R.id.stdsname);
            bemail = (TextView) view.findViewById(R.id.stdemail);
            admdate = (TextView) view.findViewById(R.id.admdate);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onStudentSelected(studentListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
 
 
    public StudentAdapter(Context context, List<Student> studentList, StudentAdapterListener listener) {
        // public ContactsAdapter(Context context, List<Contact> contactList, ContactsAdapterListener listener)
        this.context = context;
        this.listener = listener;
        this.studentList = studentList;
        this.studentListFiltered = studentList;
    }


    @Override
    public BrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_list_row, parent, false);
 
        return new BrViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(BrViewHolder holder, int position) {
        Student br = studentListFiltered.get(position);
        holder.bname.setText(br.getAdmno()+ " : " + br.getFirstname() + " " +br.getSurname());
        holder.bphone.setText(br.getTelephone());
        holder.admdate.setText(br.getAdmdate());//holder.admdate.setText(formatDate(br.getAdmdate()));
        holder.bemail.setText(br.getIdno());
        if (br.getEmail() == null) {
            //holder.bemail.setText("Location : " + br.getEmail());
        }else {
            //holder.bemail.setVisibility(View.GONE);
        }
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
                    List<Student> filteredList = new ArrayList<>();
                    for (Student row : studentList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getFirstname().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getSurname().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getAdmno().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getIdno().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getTelephone().toLowerCase().contains(charString.toLowerCase())) {
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
                studentListFiltered = (ArrayList<Student>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
 
    public interface StudentAdapterListener {
        void onStudentSelected(Student br);
    }

    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd MMM, yyyy");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }

        return "";
    }
}