
package com.paytech.paytechsystems.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.paytech.paytechsystems.getset.StudentHome;
import com.paytech.paytechsystems.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.paytech.paytechsystems.helper.Controller.TAG;

public class StudentHomeAdapter extends RecyclerView.Adapter<StudentHomeAdapter.BrViewHolder> implements Filterable {
    private Context context;
    private List<StudentHome> list;
    private List<StudentHome> listFiltered;
    private StudentHomeAdapterListener listener;
 
    public class BrViewHolder extends RecyclerView.ViewHolder {
        public TextView student, phone, fees, charges, paid, balance, date ;
 
        public BrViewHolder(View view) {
            super(view);
            student = (TextView) view.findViewById(R.id.student);
            phone = (TextView) view.findViewById(R.id.phone);
            fees = (TextView) view.findViewById(R.id.fees);
            charges = (TextView) view.findViewById(R.id.charges);
            paid = (TextView) view.findViewById(R.id.paid);
            balance = (TextView) view.findViewById(R.id.balance);
            date = (TextView) view.findViewById(R.id.date);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onStudentHomeSelected(listFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
 
 
    public StudentHomeAdapter(Context context, List<StudentHome> list, StudentHomeAdapterListener listener) {
        // public ContactsAdapter(Context context, List<Contact> contactList, ContactsAdapterListener listener)
        this.context = context;
        this.listener = listener;
        this.list = list;
        this.listFiltered = list;
    }


    @Override
    public BrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.studenthome_list_row, parent, false);
 
        return new BrViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(BrViewHolder holder, int position) {
        StudentHome m = listFiltered.get(position);
        holder.student.setText(m.getAdmno() +" : " +m.getStudent() );
        holder.phone.setText("Phone : " + m.getPhone());
        holder.fees.setText("Fees : " + m.getFees());
        holder.charges.setText("Charges : " + m.getCharges());
        holder.paid.setText("Paid : " + m.getPaid());
        holder.balance.setText("Balance : "+ m.getBalance());
        holder.date.setText(timeAgo(m.getAdmdate()));
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
                    List<StudentHome> filteredList = new ArrayList<>();
                    for (StudentHome row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getAdmno().toLowerCase().contains(charString.toLowerCase()) || row.getStudent().toLowerCase().contains(charString.toLowerCase())) {
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
                listFiltered = (ArrayList<StudentHome>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
 
    public interface StudentHomeAdapterListener {
        void onStudentHomeSelected(StudentHome br);
    }

    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("d MMM yy");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }

        return "";
    }

    public CharSequence timeAgo(String time){
        //CharSequence timeAgo = '';
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        SimpleDateFormat std = new SimpleDateFormat("yyyy-MM-dd");
        std.setTimeZone(TimeZone.getTimeZone("GMT"));
        try {
            long now = std.parse(time).getTime();
            CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(now, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
            return  timeAgo;
        }catch (Exception e){
            Log.d(TAG,"TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
        }

        //timestamp.setText(timeAgo);
        return "";
    }
}