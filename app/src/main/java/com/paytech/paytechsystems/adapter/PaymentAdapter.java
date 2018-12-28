
package com.paytech.paytechsystems.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.paytech.paytechsystems.getset.Payment;
import com.paytech.paytechsystems.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 
public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.BrViewHolder> implements Filterable {
    private Context context;
    private List<Payment> list;
    private List<Payment> listFiltered;
    private PaymentAdapterListener listener;
 
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
                    listener.onPaymentSelected(listFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
 
 
    public PaymentAdapter(Context context, List<Payment> list, PaymentAdapterListener listener) {
        // public ContactsAdapter(Context context, List<Contact> contactList, ContactsAdapterListener listener)
        this.context = context;
        this.listener = listener;
        this.list = list;
        this.listFiltered = list;
    }


    @Override
    public BrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.payment_list_row, parent, false);
 
        return new BrViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(BrViewHolder holder, int position) {
        Payment m = listFiltered.get(position);
        holder.payment.setText( m.getId() + " : " +m.getReference() + "  Ksh. " +m.getAmount() );
        holder.student.setText(m.getAdmno()+" - "+ m.getStudent());
        holder.course.setText(m.getCourse());
        holder.course.setVisibility(View.GONE);
        holder.pfor.setText(m.getPfor());
        holder.datepaid.setText(formatDate(m.getCreated_at()));
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
                    List<Payment> filteredList = new ArrayList<>();
                    for (Payment row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getReference().toLowerCase().contains(charString.toLowerCase())||
                                row.getMode().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getAmount().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getAdmno().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getStudent().toLowerCase().contains(charString.toLowerCase()) ||
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
                listFiltered = (ArrayList<Payment>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
 
    public interface PaymentAdapterListener {
        void onPaymentSelected(Payment br);
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