
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

import com.paytech.paytechsystems.getset.Lesson;
import com.paytech.paytechsystems.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.paytech.paytechsystems.helper.Controller.TAG;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.BrViewHolder> implements Filterable {
    private Context context;
    private List<Lesson> list;
    private List<Lesson> listFiltered;
    private LessonAdapterListener listener;
 
    public class BrViewHolder extends RecyclerView.ViewHolder {
        public TextView course, lesson, date;
 
        public BrViewHolder(View view) {
            super(view);
            course = (TextView) view.findViewById(R.id.course);
            lesson = (TextView) view.findViewById(R.id.lesson);
            date = (TextView) view.findViewById(R.id.date);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onLessonSelected(listFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
 
 
    public LessonAdapter(Context context, List<Lesson> list, LessonAdapterListener listener) {
        // public ContactsAdapter(Context context, List<Contact> contactList, ContactsAdapterListener listener)
        this.context = context;
        this.listener = listener;
        this.list = list;
        this.listFiltered = list;
    }


    @Override
    public BrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lesson_list_row, parent, false);
 
        return new BrViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(BrViewHolder holder, int position) {
        Lesson m = listFiltered.get(position);
        holder.course.setText(m.getCourse() );
        holder.lesson.setText(m.getLesson());
        holder.date.setText(timeAgo(m.getCreated_at()));
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
                    List<Lesson> filteredList = new ArrayList<>();
                    for (Lesson row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getLesson().toLowerCase().contains(charString.toLowerCase()) || row.getCourse().toLowerCase().contains(charString.toLowerCase())) {
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
                listFiltered = (ArrayList<Lesson>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
 
    public interface LessonAdapterListener {
        void onLessonSelected(Lesson br);

    }

public CharSequence timeAgo(String time){
        //CharSequence timeAgo = '';
    Calendar cal = Calendar.getInstance(Locale.getDefault());
    SimpleDateFormat std = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    std.setTimeZone(TimeZone.getTimeZone("GMT"));
    try {
        long now = std.parse(time).getTime();
        CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(now, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
        return  timeAgo;
    }catch (Exception e){
        Log.d(TAG,"TTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT");
    }

    //timestamp.setText(timeAgo);
   return null;
}
}