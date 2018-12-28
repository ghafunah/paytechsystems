package com.paytech.paytechsystems;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
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

import com.paytech.paytechsystems.adapter.VehicleAdapter;
import com.paytech.paytechsystems.adapter.TitleNavigationAdapter;
import com.paytech.paytechsystems.getset.Note;
import com.paytech.paytechsystems.getset.SpinnerNavItem;
import com.paytech.paytechsystems.getset.Vehicle;
import com.paytech.paytechsystems.helper.Config;
import com.paytech.paytechsystems.helper.Controller;
import com.paytech.paytechsystems.helper.MyDividerItemDecoration;
import com.paytech.paytechsystems.helper.RecyclerTouchListener;
import com.paytech.paytechsystems.helper.SQLiteHandler;


public class Vehicles extends AppCompatActivity  implements VehicleAdapter.VehicleAdapterListener, ActionBar.OnNavigationListener, SwipeRefreshLayout.OnRefreshListener{
        private static final String TAG = Vehicles.class.getSimpleName();
	///private List<User> usersList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private VehicleAdapter mAdapter;
    private List<Vehicle> list;// = new ArrayList<>();;
    private SearchView searchView;
    private FloatingActionButton fab;
    private CoordinatorLayout coordinatorLayout;
    public SQLiteHandler db;
    private ImageView imgHeader;
    private ActionBar actionBar;

    // Title navigation Spinner data
    private ArrayList<SpinnerNavItem> navSpinner;

    // Navigation adapter
    private TitleNavigationAdapter adapter;
//private static final String URL = "https://api.androidhive.info/json/contacts.json";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setTitle(R.string.toolbar_title);
        getSupportActionBar().setIcon(R.drawable.ic_menu_camera);
        //getSupportActionBar().setTitle(R.string.toolbar_title);
        imgHeader = (ImageView) findViewById(R.id.backdrop);
        initCollapsingToolbar();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        //recyclerView = findViewById(R.id.recycler_view);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        list = new ArrayList<>();
        mAdapter = new VehicleAdapter(this, list, this);
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
               //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
               Intent i = new Intent(getApplicationContext(), NewVehicle.class);
               startActivity(i);
               //finish();
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
            Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.no_internet, Snackbar.LENGTH_INDEFINITE);
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
        fetchUsers();
        if(!CheckNet(getApplicationContext())){
            Snackbar snackbar = Snackbar.make(coordinatorLayout, "No internet connection", Snackbar.LENGTH_INDEFINITE);
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
    /**
     * fetches json by making http calls
     */
    private void fetchUsers() {
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";
        
        swipeRefreshLayout.setRefreshing(true);
        final JsonArrayRequest request = new JsonArrayRequest(Config.URL +"vehicles.php?offset=200",
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
                                String regno = j.getString("regno");
                                String make = j.getString("make");
                                String type = j.getString("type");
                                String dateadd = j.getString("dateadd");
                                db.saveVehicle(regno, make, type, dateadd, 1);

                                //Toast.makeText(getApplicationContext(), R.string.greetings, Toast.LENGTH_LONG).show();
                            } catch (JSONException e) {
                                //progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Error inserting to local db: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }


                        }
                        //usersList.addAll(db.getAllUsers());
                        List<Vehicle> items = new Gson().fromJson(response.toString(), new TypeToken<List<Vehicle>>(){}.getType());
 
                        // adding contacts to users list
                        list.clear();
                        list.addAll(items);
 
                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                   // 
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Toast.makeText(getApplicationContext(), R.string.from_local, Toast.LENGTH_SHORT).show();
                list.clear();
                list.addAll(db.getAllVehicles());
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
    public void onVehicleSelected(Vehicle i) {
        //Toast.makeText(getApplicationContext(), "Selected: " + i.getId() + ", " + i.getType()+ ", " + i.getType(), Toast.LENGTH_SHORT).show();
        showActionsDialog(i.getRegno());
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
                    collapsingToolbar.setTitle(getString(R.string.title_activity_vehicles));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });

                        // loading toolbar header image
        Glide.with(getApplicationContext()).load("https://api.androidhive.info/webview/nougat.jpg")
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgHeader);
    }

    private void showActionsDialog(final String position) {
        CharSequence colors[] = new CharSequence[]{"Show Details", "Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Action");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    deleteNote(position);
                } else if(which == 1){
                    showNoteDialog(true, position);
                } else {
                    deleteNote(position);
                }
            }
        });
        builder.show();
    }
    private void showNoteDialog(final boolean shouldUpdate, final String position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.note_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Vehicles.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputNote = view.findViewById(R.id.note);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_note_title) : getString(R.string.lbl_edit_note_title));
        Vehicle v = db.getVehicle(position);
        if (shouldUpdate && v != null) {
            inputNote.setText(v.getRegno());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
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
        alertDialog.setTitle("New Vehicle");
        alertDialog.setIcon(R.drawable.condira);
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputNote.getText().toString())) {
                    Toast.makeText(Vehicles.this, "Vehicle details!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating note
                if (shouldUpdate && v != null) {
                    // update note by it's id
                    Toast.makeText(Vehicles.this, "Action to Update Vehicle!", Toast.LENGTH_SHORT).show();
                    //updateNote(inputNote.getText().toString(), position);
                } else {
                    // create new note
                    Toast.makeText(Vehicles.this, "aAction to Create vehicle!", Toast.LENGTH_SHORT).show();
                    //createNote(inputNote.getText().toString());
                }
            }
        });
    }

    private void deleteNote(String position) {
        // deleting the note from db
        //db.deleteNote(notesList.get(position));

        // removing the note from the list
        //notesList.remove(position);
        //mAdapter.notifyItemRemoved(position);

        //toggleEmptyNotes();
        Toast.makeText(getApplicationContext(), "Action Delete Vehicle!", Toast.LENGTH_SHORT).show();
    }
}
