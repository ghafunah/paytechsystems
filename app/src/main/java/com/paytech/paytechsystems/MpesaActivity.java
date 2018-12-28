package com.paytech.paytechsystems;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import com.paytech.paytechsystems.adapter.MpesaAdapter;
import com.paytech.paytechsystems.adapter.TitleNavigationAdapter;
import com.paytech.paytechsystems.getset.SpinnerNavItem;
import com.paytech.paytechsystems.getset.Mpesa;
import com.paytech.paytechsystems.helper.Config;
import com.paytech.paytechsystems.helper.Controller;
import com.paytech.paytechsystems.helper.EndlessRecyclerViewScrollListener;
import com.paytech.paytechsystems.helper.MyDividerItemDecoration;
import com.paytech.paytechsystems.helper.RecyclerTouchListener;
import com.paytech.paytechsystems.helper.SQLiteHandler;


public class MpesaActivity extends AppCompatActivity  implements MpesaAdapter.MpesaAdapterListener, ActionBar.OnNavigationListener, SwipeRefreshLayout.OnRefreshListener{
        private static final String TAG = MpesaActivity.class.getSimpleName();
	///private List<User> usersList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private MpesaAdapter mAdapter;
    private List<Mpesa> list;// = new ArrayList<>();;
    private SearchView searchView;
    private FloatingActionButton fab;
    private CoordinatorLayout coordinatorLayout;
    public SQLiteHandler db;
    private ImageView imgHeader;
    private ActionBar actionBar;
    private EndlessRecyclerViewScrollListener scrollListener;

    // Title navigation Spinner data
    private ArrayList<SpinnerNavItem> navSpinner;

    // Navigation adapter
    private TitleNavigationAdapter adapter;
    int page = 0;
//private static final String URL = "https://api.androidhive.info/json/contacts.json";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mpesa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle(R.string.toolbar_title);
        getSupportActionBar().setIcon(R.drawable.ic_menu_camera);
        //getSupportActionBar().setTitle(R.string.toolbar_title);
        imgHeader = (ImageView) findViewById(R.id.backdrop);
        initCollapsingToolbar();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        //fab = (FloatingActionButton) findViewById(R.id.fab);

        //recyclerView = findViewById(R.id.recycler_view);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        list = new ArrayList<>();
        mAdapter = new MpesaAdapter(this, list, this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        db = new SQLiteHandler(getApplicationContext());
        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView.setHasFixedSize(true);
 		
 		whiteNotificationBar(recyclerView);
        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        //RecyclerView.LayoutManager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());

        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
 
        recyclerView.setLayoutManager(mLayoutManager);
 
        // adding inbuilt divider line
        scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "No internet connection", Snackbar.LENGTH_LONG);
                fetchUsers(page);
            }
        };
        //recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
 
        // adding custom divider line with padding 16dp
        //recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 8));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
 
        recyclerView.setAdapter(mAdapter);
        //recyclerView.addOnScrollListener(scrollListener);
        if(!CheckNet(getApplicationContext())){
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "Welcome to Paytech", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
        // row click listener
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                //User user = usersList.get(position);
                //Toast.makeText(getApplicationContext(), user.getFname() + " is selected!", Toast.LENGTH_SHORT).show();

            }
 
            @Override
            public void onLongClick(View view, int position) {
 
            }
        }));

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//                Intent login = new Intent(getApplicationContext(), NewTeacher.class);
//                startActivity(login);
//                //finish();
//            }
//        });
                /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        fetchUsers(page);
            if(!CheckNet(getApplicationContext())){
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "No internet connection", Snackbar.LENGTH_LONG);
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
                                }
        );

        //prepareMovieData();
        //fetchUsers();
    }

   /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {

        if(!CheckNet(getApplicationContext())){
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "No internet connection", Snackbar.LENGTH_LONG);
            //.setAction("RETRY", new View.OnClickListener() {
              //  @Override
              //  public void onClick(View view) {
                //}});
            //snackbar.setActionTextColor(Color.RED);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            snackbar.show();
            swipeRefreshLayout.setRefreshing(false);
        }else {
            db.deleteMpesa();
            fetchUsers(page);
        }
    }
    /**
     * fetches json by making http calls
     */
    private void fetchUsers(int next) {
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";
        
        swipeRefreshLayout.setRefreshing(true);
        final JsonArrayRequest request = new JsonArrayRequest(Config.URL +"kopokopo.php?offset="+Config.LIMIT,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(getApplicationContext(), "Couldn't fetch the contacts! Please try again.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        //Integer c = response.size();
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

                                //Toast.makeText(getApplicationContext(), R.string.greetings, Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                //progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Error inserting to local db: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }


                        }
                        //usersList.addAll(db.getAllUsers());
                        List<Mpesa> items = new Gson().fromJson(response.toString(), new TypeToken<List<Mpesa>>(){}.getType());
 
                        // adding contacts to users list
                        list.clear();
                        list.addAll(items);
 
                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                        scrollListener.resetState();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                   // 
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Toast.makeText(getApplicationContext(), "Displaying from cache!", Toast.LENGTH_SHORT).show();
                list.clear();
                list.addAll(db.getAllMpesa());
                mAdapter.notifyDataSetChanged();
                Log.e(TAG, "Error: " + error.getMessage());
                //Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
 
        Controller.getInstance().addToRequestQueue(request, tag_json_obj);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_users, menu);
 
        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_searchs).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
 
        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }
 
            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }
 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
 
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }
 
        return super.onOptionsItemSelected(item);
    }
 
    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
 
    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }
 
    @Override
    public void onMpesaSelected(Mpesa i) {
        Toast.makeText(getApplicationContext(), "Selected: " + i.getTref() + ", " + i.getFname()+ ", " + i.getLname(), Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        // Action to be taken after selecting a spinner item
        return false;
    }

    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =  (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.setExpanded(false);
 
        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;
 
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });

                        // loading toolbar header image"https://api.androidhive.info/webview/nougat.jpg"
        Glide.with(getApplicationContext()).load(R.drawable.condira)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgHeader);
    }
}
