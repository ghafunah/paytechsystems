
package com.paytech.paytechsystems.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.paytech.paytechsystems.getset.Paybill;
import com.paytech.paytechsystems.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 
public class PaybillAdapter extends RecyclerView.Adapter<PaybillAdapter.BrViewHolder> implements Filterable {
    private Context context;
    private List<Paybill> list;
    private List<Paybill> listFiltered;
    private PaybillAdapterListener listener;
 
    public class BrViewHolder extends RecyclerView.ViewHolder {
        public TextView payment, student, pfor, datepaid, course;
 
        public BrViewHolder(View view) {
            super(view);
            payment = (TextView) view.findViewById(R.id.payment);
            student = (TextView) view.findViewById(R.id.student);
            pfor = (TextView) view.findViewById(R.id.pfor);
            course = (TextView) view.findViewById(R.id.course);
            datepaid = (TextView) view.findViewById(R.id.datepaid);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onPaybillSelected(listFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
 
 
    public PaybillAdapter(Context context, List<Paybill> list, PaybillAdapterListener listener) {
        // public ContactsAdapter(Context context, List<Contact> contactList, ContactsAdapterListener listener)
        this.context = context;
        this.listener = listener;
        this.list = list;
        this.listFiltered = list;
    }


    @Override
    public BrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.paybill_list_row, parent, false);
 
        return new BrViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(BrViewHolder holder, int position) {
        Paybill m = listFiltered.get(position);
        holder.payment.setText( m.getId() + " : " +m.getTransID() + "  Ksh. " +m.getTransAmount() );
        holder.student.setText(m.getBillRefNumber()+" - "+ m.getFirstName()+" "+ m.getMiddleName()+" "+ m.getLastName());
        holder.course.setText(m.getTransTime());
        holder.course.setVisibility(View.GONE);
        holder.pfor.setText(m.getmSISDN());
        holder.datepaid.setText(formatDate(m.getTransTime()));
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
                    List<Paybill> filteredList = new ArrayList<>();
                    for (Paybill row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTransID().toLowerCase().contains(charString.toLowerCase())||
                                row.getmSISDN().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getTransAmount().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getLastName().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getFirstName().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getCreated_at().toLowerCase().contains(charString.toLowerCase())) {
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
                listFiltered = (ArrayList<Paybill>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
 
    public interface PaybillAdapterListener {
        void onPaybillSelected(Paybill br);
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
}