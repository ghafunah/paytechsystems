
package com.paytech.paytechsystems.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.paytech.paytechsystems.getset.Mpesa;
import com.paytech.paytechsystems.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 
public class MpesaAdapter extends RecyclerView.Adapter<MpesaAdapter.BrViewHolder> implements Filterable {
    private Context context;
    private List<Mpesa> list;
    private List<Mpesa> listFiltered;
    private MpesaAdapterListener listener;
 
    public class BrViewHolder extends RecyclerView.ViewHolder {
        public TextView service, tref, phone, fname, date, amount;
 
        public BrViewHolder(View view) {
            super(view);
            service = (TextView) view.findViewById(R.id.service);
            //tref = (TextView) view.findViewById(R.id.tref);
            phone = (TextView) view.findViewById(R.id.phone);            
            fname = (TextView) view.findViewById(R.id.fname);
            date = (TextView) view.findViewById(R.id.date);
            amount = (TextView) view.findViewById(R.id.amount);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onMpesaSelected(listFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
 
 
    public MpesaAdapter(Context context, List<Mpesa> list, MpesaAdapterListener listener) {
        // public ContactsAdapter(Context context, List<Contact> contactList, ContactsAdapterListener listener)
        this.context = context;
        this.listener = listener;
        this.list = list;
        this.listFiltered = list;
    }


    @Override
    public BrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mpesa_list_row, parent, false);
 
        return new BrViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(BrViewHolder holder, int position) {
        Mpesa m = listFiltered.get(position);
        holder.service.setText(m.getTref() + " : " +m.getFname() + " " + m.getLname());
        //holder.tref.setText(br.getTref());
        holder.phone.setText(m.getPhone());
        holder.fname.setText(m.getFname() + " " + m.getLname());
        holder.fname.setVisibility(View.GONE);
        holder.date.setText(formatDate(m.getTtime()));
        //holder.date.setText(System.nanoTime());
        holder.amount.setText("KES : " + m.getAmount());
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
                    List<Mpesa> filteredList = new ArrayList<>();
                    for (Mpesa row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getFname().toLowerCase().contains(charString.toLowerCase())||
                                row.getPhone().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getTref().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getLname().toLowerCase().contains(charString.toLowerCase()) ||
                        row.getAmount().toLowerCase().contains(charString.toLowerCase())) {
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
                listFiltered = (ArrayList<Mpesa>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
 
    public interface MpesaAdapterListener {
        void onMpesaSelected(Mpesa br);
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