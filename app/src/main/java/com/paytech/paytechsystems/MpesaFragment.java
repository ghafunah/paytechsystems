package com.paytech.paytechsystems;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.paytech.paytechsystems.adapter.MpesaAdapter;
import com.paytech.paytechsystems.adapter.MpesaAdapter;
import com.paytech.paytechsystems.getset.Mpesa;
import com.paytech.paytechsystems.getset.User;
import com.paytech.paytechsystems.helper.Config;
import com.paytech.paytechsystems.helper.Controller;
import com.paytech.paytechsystems.helper.MyDividerItemDecoration;
import com.paytech.paytechsystems.helper.RecyclerTouchListener;
import com.paytech.paytechsystems.helper.SQLiteHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MpesaFragment extends Fragment implements MpesaAdapter.MpesaAdapterListener{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private SQLiteHandler db;
    private static final String TAG = MpesaFragment.class.getSimpleName();
	///private List<User> mpesaList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private MpesaAdapter mAdapter;
    private List<Mpesa> mpesaList;
    private SearchView searchView;
    private View view;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MpesaFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static MpesaFragment newInstance(int columnCount) {
        MpesaFragment fragment = new MpesaFragment();
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
//        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
 		//mpesaList = new ArrayList<>();
        mpesaList = new ArrayList<>();
        //mAdapter = new mpesaAdapter(mpesaList, this);
        //mAdapter = new mpesaAdapter(mpesaList, this);
        //swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        
       // swipeRefreshLayout.setOnRefreshListener(this);
        fetchMpesa();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mpesa, container, false);
        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.onCreate(savedInstanceState);
        mAdapter = new MpesaAdapter(getContext(), mpesaList, this);

            Context context = view.getContext();
            mAdapter = new MpesaAdapter(getContext(), mpesaList, this);
            db = new SQLiteHandler(getContext());
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        // adding custom divider line with padding 16dp
            recyclerView.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 8));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            //swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
            recyclerView.setAdapter(mAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Mpesa user = mpesaList.get(position);
                Toast.makeText(getContext(), user.getFname() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
            fetchMpesa();
    }

    public void onRefresh() {
        fetchMpesa();
    }

    private void fetchMpesa() {
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        //swipeRefreshLayout.setRefreshing(true);
        JsonArrayRequest request = new JsonArrayRequest(Config.URL +"kopokopo.php?offset=10",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(view.getContext(), "Couldn't fetch the contacts! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject j = new JSONObject(response.getString(i));
                                String tref = j.getString("tref");
                                String fname = j.getString("fname");
                                String lname = j.getString("lname");
                                String ttime = j.getString("ttime");
                                String amount = j.getString("amount");
                                String phone = j.getString("phone");
                                String service = j.getString("service");
                                db.saveMpesa(service, fname, lname, ttime, phone, tref, amount, 1);
                                //Toast.makeText(getContext(), "Mpesa added to SQlite", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                //progressDialog.dismiss();
                                Toast.makeText(getContext(), "Error inserting to local db: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                        List<Mpesa> items = new Gson().fromJson(response.toString(), new TypeToken<List<Mpesa>>() {
                        }.getType());

                        // adding contacts to mpesa list
                        mpesaList.clear();
                        mpesaList.addAll(items);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                       // swipeRefreshLayout.setRefreshing(false);
                    }
                    //
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Toast.makeText(getContext(), "Displaying from cache!", Toast.LENGTH_SHORT).show();
                mpesaList.clear();
                mpesaList.addAll(db.getAllMpesa());
                mAdapter.notifyDataSetChanged();
                Log.e(TAG, "Error: " + error.getMessage());
                //Toast.makeText(view.getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                //swipeRefreshLayout.setRefreshing(false);
            }
        });

        Controller.getInstance().addToRequestQueue(request, tag_json_obj);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fetchMpesa();
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Mpesa user);
    }

    @Override
    public void onMpesaSelected(Mpesa user) {
        Toast.makeText(view.getContext(), "Selected: " + user.getFname() + ", " + user.getLname(), Toast.LENGTH_LONG).show();
    }
}
