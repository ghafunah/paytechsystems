
package com.paytech.paytechsystems.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paytech.paytechsystems.getset.Userfire;
import com.paytech.paytechsystems.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FireAdapter extends RecyclerView.Adapter<FireAdapter.BrViewHolder> implements Filterable {
    private Context context;
    private List<Userfire> list;
    private List<Userfire> listFiltered;
    private FireAdapterListener listener;
 
    public class BrViewHolder extends RecyclerView.ViewHolder {
        //public TextView name, email, mgsdate;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.email)
        TextView email;
        @BindView(R.id.mgsdate)
        TextView mgsdate;
        public BrViewHolder(View view) {
            super(view);
            //name = (TextView) view.findViewById(R.id.name);
            //email = (TextView) view.findViewById(R.id.email);
           // mgsdate = (TextView) view.findViewById(R.id.mgsdate);
            ButterKnife.bind(this, view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    //listener.onFireSelected(listFiltered.get(getAdapterPosition()));
                }
            });

        }
    }
 
 
    public FireAdapter(Context context, List<Userfire> list, FireAdapterListener listener) {
        // public ContactsAdapter(Context context, List<Contact> contactList, ContactsAdapterListener listener)
        this.context = context;
        this.listener = listener;
        this.list = list;
        this.listFiltered = list;
    }

    public FireAdapter(Context context, List<Userfire> list) {
        // public ContactsAdapter(Context context, List<Contact> contactList, ContactsAdapterListener listener)
        this.context = context;
        this.listener = listener;
        this.list = list;
        this.listFiltered = list;
    }
    @Override
    public BrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mgs_list_row, parent, false);

        return new BrViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(BrViewHolder holder, int position) {
        Userfire user= listFiltered.get(position);
        String uname = user.getUname();
//        if (uname != null && uname.equals("fred")){
//            LinearLayout.LayoutParams params = new  LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
//            params.weight = 1.0f;
//            params.gravity = Gravity.RIGHT | Gravity.END;
//            holder.name.setText(user.getName());
//            holder.name.setTextColor(holder.name.getResources().getColor(R.color.colorPrimaryDark));
//            holder.email.setText(user.getMessage());
//            holder.email.setTextColor(holder.email.getResources().getColor(R.color.colorPrimaryDark));
//            holder.mgsdate.setText(formatDate(user.getDate()));
//            holder.mgsdate.setTextColor(holder.mgsdate.getResources().getColor(R.color.colorPrimaryDark));
//        }else {
            holder.name.setText(user.getName());
            holder.email.setText(user.getMessage());
            holder.mgsdate.setText(formatDate(user.getDate()));
            // Glide.with(context).load(contact.getImage()).apply(RequestOptions.circleCropTransform()).into(holder.thumbnail);
       // }
    }
 
    @Override
    public int getItemCount() {
        //return listFiltered.size();
        int arr = 0;
        try{
           if(listFiltered.size()==0){
            arr = 0;
           }else{
            arr=listFiltered.size();
           }
        }catch(Exception e){

        }
        return arr;
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
                    List<Userfire> filteredList = new ArrayList<>();
                    for (Userfire row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()
                                )){
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
                listFiltered = (ArrayList<Userfire>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
 
    public interface FireAdapterListener {
        void onFireSelected(Userfire br);
    }

    private String formatDate(String dateStr) {
        try {
            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd HHmmss");
            Date date = fmt.parse(dateStr);
            SimpleDateFormat fmtOut = new SimpleDateFormat("h:mm a d MMM yy");
            return fmtOut.format(date);
        } catch (ParseException e) {

        }

        return "";
    }
}