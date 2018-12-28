package com.paytech.paytechsystems;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
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

import com.paytech.paytechsystems.adapter.TitleNavigationAdapter;
import com.paytech.paytechsystems.adapter.UsersAdapter;
import com.paytech.paytechsystems.getset.SpinnerNavItem;
import com.paytech.paytechsystems.getset.User;
import com.paytech.paytechsystems.helper.Config;
import com.paytech.paytechsystems.helper.Controller;
import com.paytech.paytechsystems.helper.MyDividerItemDecoration;
import com.paytech.paytechsystems.helper.RecyclerTouchListener;
import com.paytech.paytechsystems.helper.SQLiteHandler;


public class Users extends AppCompatActivity  implements UsersAdapter.UsersAdapterListener, ActionBar.OnNavigationListener, SwipeRefreshLayout.OnRefreshListener{
        private static final String TAG = Users.class.getSimpleName();
	///private List<User> usersList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private UsersAdapter mAdapter;
   // ArrayList<HashMap<String, String>> Item_List;
    private List<User> usersList; // = new ArrayList<>();
    private SearchView searchView;
    private FloatingActionButton fab;
    private CoordinatorLayout coordinatorLayout;
    public SQLiteHandler db;
    String per_page, myurl;
    Boolean delete_cache, save_to_cache, use_my_url;

    private ActionBar actionBar;
    private ImageView imgHeader;
    // Title navigation Spinner data
    private ArrayList<SpinnerNavItem> navSpinner;

    // Navigation adapter
    private TitleNavigationAdapter adapter;
//private static final String URL = "https://api.androidhive.info/json/contacts.json";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        per_page =sf.getString("key_per_page", Config.LIMIT1);
        //String url = sf.getString("key_url", "http://cybersofttechnologies.net/seniors_db/");
        delete_cache =sf.getBoolean("delete_cache_on_refresh", false);
        save_to_cache =sf.getBoolean("save_to_cache", false);
        use_my_url =sf.getBoolean("use_my_url", false);
        if (use_my_url) {
            myurl = sf.getString("key_url", Config.URL);
        }
        else{
            myurl = Config.URL+"/users.php?offset=10";
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle(R.string.toolbar_title);
        //getSupportActionBar().setIcon(R.drawable.ic_menu_camera);
        //getSupportActionBar().setTitle(R.string.toolbar_title);
        //Item_List = new ArrayList<HashMap<String, String>>();
        
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        imgHeader = (ImageView) findViewById(R.id.backdrop);
        initCollapsingToolbar();
        //recyclerView = findViewById(R.id.recycler_view);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
 		usersList = new ArrayList<>();
        mAdapter = new UsersAdapter(this, usersList, this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        db = new SQLiteHandler(getApplicationContext());
        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView.setHasFixedSize(true);
 		
 		whiteNotificationBar(recyclerView);
        // vertical RecyclerView
        // keep movie_list_row.xml width to `match_parent`
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
 
        // horizontal RecyclerView
        // keep movie_list_row.xml width to `wrap_content`
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
 
        recyclerView.setLayoutManager(mLayoutManager);
 
        // adding inbuilt divider line

        //recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
 
        // adding custom divider line with padding 16dp
        //recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 8));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
 
        recyclerView.setAdapter(mAdapter);
        if(!CheckNet(getApplicationContext())){
            Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.no_internet, Snackbar.LENGTH_LONG);
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

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT)
                        //.setAction("Action", null).show();
//                Intent login = new Intent(getApplicationContext(), Register.class);
//                startActivity(login);
//                finish();
                newUser();
            }
        });
                /**
         * Showing Swipe Refresh animation on activity create
         * As animation won't start on onCreate, post runnable is used
         */
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        fetchUsers();
            if(!CheckNet(getApplicationContext())){
            Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.no_internet, Snackbar.LENGTH_LONG);
            //.setAction("RETRY", new View.OnClickListener() {
              //  @Override
              //  public void onClick(View view) {
                //}});
            //snackbar.setActionTextColor(Color.RED);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.RED);
            textView.setAllCaps(true);
            textView.setGravity(1);
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
            showSnackbar();
            swipeRefreshLayout.setRefreshing(false);
        }
        else{
            if (delete_cache) { //check if deleting is allowed
                db.deleteUsers(); // Delete cache and create again
                fetchUsers();
                //Toast.makeText(getApplicationContext(), "Cache deleted.", Toast.LENGTH_LONG).show();
            } else {
                fetchUsers();
                //Toast.makeText(getApplicationContext(), "Cache NOT deleted.", Toast.LENGTH_LONG).show();
            }


        }
    }
    /**
     * fetches json by making http calls
     */
    private void fetchUsers() {
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";
        
        swipeRefreshLayout.setRefreshing(true);
        final JsonArrayRequest request = new JsonArrayRequest(myurl ,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Log.d(TAG, "Saving to db " + response.toString());
                            Toast.makeText(getApplicationContext(), "Couldn't fetch the contacts! Please try again.", Toast.LENGTH_LONG).show();
                            return;
                        }
                        //Integer c = response.size();
                        if (save_to_cache) {
                            Log.d(TAG, "Saving to db " + response.toString());
                            Log.d(TAG, "Saving to db ");
                            saveCache(response); // Save to local database
                        }
                        //usersList.addAll(db.getAllUsers());
                        List<User> items = new Gson().fromJson(response.toString(), new TypeToken<List<User>>(){}.getType());
 
                        // adding contacts to users list
                        usersList.clear();
                        //usersList.addAll(db.getAllUsers(per_page));
                        usersList.addAll(items);
 
                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                        Log.e(TAG, "Peter and "+ db.getUserLast() );
                        swipeRefreshLayout.setRefreshing(false);
                    }
                   // 
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Toast.makeText(getApplicationContext(), R.string.from_local, Toast.LENGTH_SHORT).show();
                if (usersList !=null) {
                    usersList.clear();
                    usersList.addAll(db.getAllUsers(per_page));
                    mAdapter.notifyDataSetChanged();
                    //db.getUserLast();
                }
                 //usersList.setOnItemClickListener(new ListiYtemClickListener());
                Log.e(TAG, "Condira Systems Error Found : " + error.getStackTrace());
                Log.e(TAG, "Peter and "+ db.getUserLast() );
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
    public void onUserSelected(User user) {
            //Intent edit_intent = new Intent(Users.this, NewCourse.class);

            //edit_intent.putExtra("fname", user.getFname() );
            //edit_intent.putExtra("uname", user.getUname() );
           // startActivity(edit_intent);
            Toast.makeText(getApplicationContext(), "Selected: " + user.getFname() + ", " + user.getSname(), Toast.LENGTH_SHORT).show();
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
                    collapsingToolbar.setTitle(getString(R.string.title_activity_users));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
                // loading toolbar header image
        Glide.with(getApplicationContext()).load(R.drawable.condira)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgHeader);
    }

    /**
     * Parsing json reponse and passing the data to feed view list adapter
     * */
    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("feed");
 
            for (int i = 0; i < feedArray.length(); i++) {
                JSONObject feedObj = (JSONObject) feedArray.get(i);
 
                User item = new User();
                item.setIdno(feedObj.getString("id"));
                item.setFname(feedObj.getString("name"));
 
                // Image might be null sometimes
                String image = feedObj.isNull("image") ? null : feedObj
                        .getString("image");
                //item.setImge(image);
                item.setStatus(feedObj.getInt("status"));
                //item.setProfilePic(feedObj.getString("profilePic"));
                //item.setTimeStamp(feedObj.getString("timeStamp"));
 
                // url might be null sometimes
                String feedUrl = feedObj.isNull("url") ? null : feedObj
                        .getString("url");
                //item.setUrl(feedUrl);

                usersList.add(item);
            }
 
            // notify data changes to list adapater
            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void newUser() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.new_user, null);
        List<String> categories = new ArrayList<String>();
        categories.add("Male");
        categories.add("Female");
        categories.add("Other");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Users.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText idno = view.findViewById(R.id.usridno);
        final TextView fname = view.findViewById(R.id.usrfname);
        final EditText sname = view.findViewById(R.id.usrsname);
        final EditText uname = view.findViewById(R.id.usruname);
        final EditText email = view.findViewById(R.id.usremail);
        final EditText password = view.findViewById(R.id.usrpassword);
        final EditText phone = view.findViewById(R.id.usrphone);
        final EditText branch = view.findViewById(R.id.usrbranch);
        final Spinner sex = view.findViewById(R.id.usrsex);
        sex.setAdapter(dataAdapter);
        //sex.setBackgroundResource(R.color.white);
        //dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_note_title) : getString(R.string.lbl_edit_note_title));

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.setTitle("New User");
        alertDialog.setIcon(R.drawable.ic_action_add_person);
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(idno.getText().toString()) || TextUtils.isEmpty(fname.getText().toString())) {
                    Toast.makeText(Users.this, "Branch code and name cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }
                //Double am = admno.getText();
                db.saveUser(idno.getText().toString(), fname.getText().toString(),
                        sname.getText().toString(), uname.getText().toString(), email.getText().toString(),
                        branch.getText().toString(), password.getText().toString(), phone.getText().toString(), sex.getSelectedItem().toString(),"Cashier", 0);

            }
        });
    }

    public void showSnackbar() {
        Snackbar snackbar = Snackbar.make( coordinatorLayout, R.string.no_internet, Snackbar.LENGTH_INDEFINITE);
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
    
    public void saveCache(JSONArray response){
        for (int i = 0; i < response.length(); i++) {
            try {
                JSONObject j = new JSONObject(response.getString(i));
                Integer id = j.getInt("uid");
                String idno = j.getString("idno");
                String uname = j.getString("uname");
                String fname = j.getString("fname");
                String sname = j.getString("sname");
                String email = j.getString("email");
                String branch = j.getString("branch");
                String password = j.getString("password");
                String phone = j.getString("phone");
                String gender = j.getString("gender");
                String role = j.getString("role");
                db.saveUser(idno, fname, sname, uname, email, branch, password, phone, gender, role, 1);
                //Toast.makeText(getApplicationContext(), "User added to SQlite" +idno + "-" + fname +"-" +sname +"-"+uname, Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                //progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), "Error inserting to local db: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }


        }
    }
}
