
package com.paytech.paytechsystems.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.paytech.paytechsystems.getset.LessonDone;
import com.paytech.paytechsystems.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
 
public class LessonDoneAdapter extends RecyclerView.Adapter<LessonDoneAdapter.BrViewHolder> implements Filterable {
    private Context context;
    private List<LessonDone> list;
    private List<LessonDone> listFiltered;
    private LessonDoneAdapterListener listener;
 
    public class BrViewHolder extends RecyclerView.ViewHolder {
        public TextView student, lesson, course, teacher, date;
 
        public BrViewHolder(View view) {
            super(view);
            student = (TextView) view.findViewById(R.id.student);
            lesson = (TextView) view.findViewById(R.id.lesson);
            course = (TextView) view.findViewById(R.id.course);
            teacher = (TextView) view.findViewById(R.id.teacher);
            date = (TextView) view.findViewById(R.id.date);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onLessonDoneSelected(listFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
 
 
    public LessonDoneAdapter(Context context, List<LessonDone> list, LessonDoneAdapterListener listener) {
        // public ContactsAdapter(Context context, List<Contact> contactList, ContactsAdapterListener listener)
        this.context = context;
        this.listener = listener;
        this.list = list;
        this.listFiltered = list;
    }


    @Override
    public BrViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lessondone_list_row, parent, false);
 
        return new BrViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(BrViewHolder holder, int position) {
        LessonDone m = listFiltered.get(position);
        holder.student.setText(m.getAdmno() +" "+ m.getStudent() );
        holder.lesson.setText(m.getLesson());
        holder.course.setText(m.getCourse());
        holder.teacher.setText(m.getTeacher() +" - "+ m.getVehicle());
        holder.date.setText(formatDate(m.getDate()));

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
                    List<LessonDone> filteredList = new ArrayList<>();
                    for (LessonDone row : list) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getLesson().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getCourse().toLowerCase().contains(charString.toLowerCase()) ||
                                        row.getStudent().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getVehicle().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getDate().toLowerCase().contains(charString.toLowerCase()) ||
                                        row.getTeacher().toLowerCase().contains(charString.toLowerCase())) {
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
                listFiltered = (ArrayList<LessonDone>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
 
    public interface LessonDoneAdapterListener {
        void onLessonDoneSelected(LessonDone br);
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