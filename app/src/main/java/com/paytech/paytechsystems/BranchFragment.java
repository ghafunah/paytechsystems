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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.paytech.paytechsystems.adapter.BranchAdapter;
import com.paytech.paytechsystems.getset.Branch;
import com.paytech.paytechsystems.helper.Config;
import com.paytech.paytechsystems.helper.Controller;
import com.paytech.paytechsystems.helper.MyDividerItemDecoration;
import com.paytech.paytechsystems.helper.RecyclerTouchListener;
import com.paytech.paytechsystems.helper.SQLiteHandler;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class BranchFragment extends Fragment implements BranchAdapter.BranchAdapterListener{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private SQLiteHandler db;
    private static final String TAG = BranchFragment.class.getSimpleName();
    ///private List<User> usersList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private BranchAdapter mAdapter;
    private List<Branch> branchList;
    private SearchView searchView;
    private View view;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public BranchFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static BranchFragment newInstance(int columnCount) {
        BranchFragment fragment = new BranchFragment();
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
        branchList = new ArrayList<>();
        fetchBranches();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_branch, container, false);
        return view;
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        super.onCreate(savedInstanceState);
        mAdapter = new BranchAdapter(getContext(), branchList, this);
        db = new SQLiteHandler(getContext());
        Context context = view.getContext();
        mAdapter = new BranchAdapter(getContext(), branchList, this);

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
                Branch user = branchList.get(position);
                Toast.makeText(getContext(), user.getName() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        fetchBranches();
    }

    public void onRefresh() {
        fetchBranches();
    }

    private void fetchBranches() {
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";

        //swipeRefreshLayout.setRefreshing(true);
        JsonArrayRequest request = new JsonArrayRequest(Config.URL +"branch.php?offset=",
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
                                String code = j.getString("code");
                                String name = j.getString("name");
                                String bphone = j.getString("code");
                                String bemail = j.getString("code");
                                //db.saveBranch(code, name, bphone, bemail);
                                //Toast.makeText(getContext(), "Branch added to SQlite", Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                //progressDialog.dismiss();
                                Toast.makeText(getContext(), "Error inserting to local db: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                        List<Branch> items = new Gson().fromJson(response.toString(), new TypeToken<List<Branch>>() {
                        }.getType());

                        // adding contacts to users list
                        branchList.clear();
                        branchList.addAll(items);

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
                branchList.clear();
                branchList.addAll(db.getAllBranches());
                mAdapter.notifyDataSetChanged();
                Log.e(TAG, "Error: " + error.getMessage());
                //Toast.makeText(view.getContext(), "Error found : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                //swipeRefreshLayout.setRefreshing(false);
            }
        });

        Controller.getInstance().addToRequestQueue(request, tag_json_obj);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        fetchBranches();
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
        void onListFragmentInteraction(Branch user);
    }

    @Override
    public void onBranchSelected(Branch user) {
        Toast.makeText(view.getContext(), "Selected: " + user.getName() + ", " + user.getLocation(), Toast.LENGTH_LONG).show();
    }
}
