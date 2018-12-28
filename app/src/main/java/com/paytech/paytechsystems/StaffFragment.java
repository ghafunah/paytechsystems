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
import com.paytech.paytechsystems.adapter.UsersAdapter;
import com.paytech.paytechsystems.getset.User;
import com.paytech.paytechsystems.helper.Config;
import com.paytech.paytechsystems.helper.Controller;
import com.paytech.paytechsystems.helper.MyDividerItemDecoration;
import com.paytech.paytechsystems.helper.RecyclerTouchListener;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class StaffFragment extends Fragment implements UsersAdapter.UsersAdapterListener{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    
    private static final String TAG = StaffFragment.class.getSimpleName();
	///private List<User> usersList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private UsersAdapter mAdapter;
    private List<User> usersList;
    private SearchView searchView;
    private View view;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StaffFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static StaffFragment newInstance(int columnCount) {
        StaffFragment fragment = new StaffFragment();
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
 		//usersList = new ArrayList<>();
        usersList = new ArrayList<>();
        //mAdapter = new UsersAdapter(usersList, this);
        //mAdapter = new UsersAdapter(usersList, this);
        //swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        
       // swipeRefreshLayout.setOnRefreshListener(this);
        fetchUsers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_staff, container, false);
        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.onCreate(savedInstanceState);
        Context context = view.getContext();
        mAdapter = new UsersAdapter(context,usersList, this);


            //mAdapter = new UsersAdapter(getContext(), usersList, this);

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
                User user = usersList.get(position);
                Toast.makeText(getContext(), user.getFname() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
            fetchUsers();
    }

    public void onRefresh() {
        fetchUsers();
    }

    private void fetchUsers() {
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        //swipeRefreshLayout.setRefreshing(true);
        JsonArrayRequest request = new JsonArrayRequest(Config.URL +"users.php?offset="+Config.LIMIT,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(view.getContext(), "Couldn't fetch the contacts! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<User> items = new Gson().fromJson(response.toString(), new TypeToken<List<User>>() {
                        }.getType());

                        // adding contacts to users list
                        usersList.clear();
                        usersList.addAll(items);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                       // swipeRefreshLayout.setRefreshing(false);
                    }
                    //
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(view.getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                //swipeRefreshLayout.setRefreshing(false);
            }
        });

        Controller.getInstance().addToRequestQueue(request, tag_json_obj);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fetchUsers();
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
        void onListFragmentInteraction(User user);
    }

    @Override
    public void onUserSelected(User user) {
        Toast.makeText(view.getContext(), "Selected: " + user.getFname() + ", " + user.getSname(), Toast.LENGTH_LONG).show();
    }
}
