package com.paytech.paytechsystems;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.paytech.paytechsystems.adapter.StudentHomeAdapter;
import com.paytech.paytechsystems.getset.StudentHome;
import com.paytech.paytechsystems.helper.Config;
import com.paytech.paytechsystems.helper.Controller;
import com.paytech.paytechsystems.helper.MyDividerItemDecoration;
import com.paytech.paytechsystems.helper.RecyclerTouchListener;
import com.paytech.paytechsystems.helper.SQLiteHandler;

public class StudentFragment extends Fragment implements StudentHomeAdapter.StudentHomeAdapterListener{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    private static final String TAG = StudentFragment.class.getSimpleName();
    ///private List<User> usersList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private StudentHomeAdapter mAdapter;
    private List<StudentHome> studentList;
    private SearchView searchView;
    private View view;
    public SQLiteHandler db;
    String per_page, lastid;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StudentFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static StudentFragment newInstance(int columnCount) {
        StudentFragment fragment = new StudentFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        studentList = new ArrayList<>();
        db = new SQLiteHandler(getContext());
        fetchStudents();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_students, container, false);

        return view;
    }



    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.onCreate(savedInstanceState);
        mAdapter = new StudentHomeAdapter(getContext(), studentList, this);

        Context context = view.getContext();
        mAdapter = new StudentHomeAdapter(getContext(), studentList, this);

        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        // adding custom divider line with padding 16dp
        //recyclerView.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 8));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //Student user = studentList.get(position);
                //Toast.makeText(getContext(), user.getFirstname() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

                //Adding an scroll change listener to recyclerview
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (isLastItemDisplaying(recyclerView)) {
                    //Calling the method getdata again
                    //fetchStudents();
                    //progressBar.setVisibility(View.GONE);
                }
            }
        });
        //fetchStudents();
    }

    public void onRefresh() {
        if(!CheckNet(getContext())){
            showSnackbar();
            //swipeRefreshLayout.setRefreshing(false);
        }else {
            //db.deleteLd();
           // fetchStudents();

        }
    }

    private void fetchStudents() {
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        //swipeRefreshLayout.setRefreshing(true);
        final JsonArrayRequest request = new JsonArrayRequest(Config.URL +"student_home.php?offset="+Config.LIMIT,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(getContext(), "Couldn't fetch the contacts! Please try again.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        //Integer c = response.size();
                        Log.d(TAG, response.toString());
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject j = new JSONObject(response.getString(i));
                                String admno = j.getString("admno");
                                String student = j.getString("student");
                                String phone = j.getString("phone");
                                Double fees = j.getDouble("fees");
                                Double balance = j.getDouble("balance");
                                Double paid = j.getDouble("paid");
                                Double charges = j.getDouble("charge");
                                String admdate = j.getString("admdate");
                                db.saveStudentHome(admno, student, phone, fees, charges, paid, balance, admdate);

                                //Toast.makeText(getApplicationContext(), R.string.greetings, Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                //progressDialog.dismiss();
                                Toast.makeText(getContext(), "Error inserting to local db: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }


                        }

                        List<StudentHome> items = new Gson().fromJson(response.toString(), new TypeToken<List<StudentHome>>(){}.getType());

                        // adding contacts to users list
                       // studentList.clear();
                        //studentList.addAll(items);
                        //studentList.addAll(db.getAllStudentHome(per_page));

                        // refreshing recycler view
                       // mAdapter.notifyDataSetChanged();
                        //Log.e(TAG, "Peter and : " + db.getStudentLast());
                        //swipeRefreshLayout.setRefreshing(false);
                    }
                    //
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                //Toast.makeText(getContext(), R.string.from_local, Toast.LENGTH_SHORT).show();
//                if (studentList != null){
//                studentList.clear();
//                studentList.addAll(db.getAllStudentHome(Config.LIMIT1));
//                mAdapter.notifyDataSetChanged();
//                    }

                Log.e(TAG, "Error: " + error.getMessage());
                Log.e(TAG, "Peter and : " + db.getStudentLast());
                //Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                //swipeRefreshLayout.setRefreshing(false);
            }
        });

        Controller.getInstance().addToRequestQueue(request, tag_json_obj);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        //SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(context);
        //per_page =sf.getInt("key_per_page", 10);
       // lastid = db.getStudentLast();
        fetchStudents();
//        if (context instanceof OnListFragmentInteractionListener) {
//            mListener = (OnListFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(StudentHome user);
    }

    @Override
    public void onStudentHomeSelected(StudentHome std) {
        Toast.makeText(view.getContext(), "Selected: " + std.getStudent(), Toast.LENGTH_LONG).show();
    }

        //This method would check that the recyclerview scroll has reached the bottom or not
    private boolean isLastItemDisplaying(RecyclerView recyclerView) {
        if (recyclerView.getAdapter().getItemCount() != 0) {
            int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
            if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition == recyclerView.getAdapter().getItemCount() - 1)
                Toast.makeText(getActivity(), "The end has been reached!", Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }
    private void CreateBarcode(String name){
//        EAN13 barcode = new EAN13();
//        BarcodeEAN codeEAN = new BarcodeEAN();
//        codeEAN.setCodeType(codeEAN.EAN13);
//        codeEAN.setCode("9780201615883");
//        Image imageEAN = codeEAN.createImageWithBarcode(cb, null, null);
    }
    public  boolean CheckNet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        Boolean connected = false;
        //if there is a network
        if (activeNetwork != null)

        {
            //if connected to wifi or mobile data plan
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                connected = true;
            }
        }
        return connected;
    }
    public void showSnackbar() {
        Snackbar snackbar = Snackbar.make( recyclerView, R.string.no_internet, Snackbar.LENGTH_INDEFINITE);
        //.setAction("RETRY", new View.OnClickListener() {
        //  @Override
        //  public void onClick(View view) {
        //}});
        //snackbar.setActionTextColor(Color.RED);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.RED);
        snackbar.show();
    }
}
