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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.paytech.paytechsystems.adapter.StudentAdapter;
import com.paytech.paytechsystems.adapter.TitleNavigationAdapter;
import com.paytech.paytechsystems.adapter.UsersAdapter;
import com.paytech.paytechsystems.getset.Course;
import com.paytech.paytechsystems.getset.SpinnerNavItem;
import com.paytech.paytechsystems.getset.Student;
import com.paytech.paytechsystems.helper.Config;
import com.paytech.paytechsystems.helper.Controller;
import com.paytech.paytechsystems.helper.MyDividerItemDecoration;
import com.paytech.paytechsystems.helper.RecyclerTouchListener;
import com.paytech.paytechsystems.helper.SQLiteHandler;


public class Students extends AppCompatActivity  implements StudentAdapter.StudentAdapterListener, ActionBar.OnNavigationListener, SwipeRefreshLayout.OnRefreshListener{
        private static final String TAG = Students.class.getSimpleName();
	///private List<User> usersList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private StudentAdapter mAdapter;
    private List<Student> stdList; // = new ArrayList<>();
    private SearchView searchView;
    private FloatingActionButton fab;
    private CoordinatorLayout coordinatorLayout;
    public SQLiteHandler db;
    private ImageView imgHeader;
    private ActionBar actionBar;
    String lastid, per_page;
    Boolean delete_cache, save_to_cache;

    // Title navigation Spinner data
    private ArrayList<SpinnerNavItem> navSpinner;

    // Navigation adapter
    private TitleNavigationAdapter adapter;
//private static final String URL = "https://api.androidhive.info/json/contacts.json";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        per_page =sf.getString("key_per_page", Config.LIMIT1);
        String url = sf.getString("key_url", "http://cybersofttechnologies.net/seniors_db/");
        delete_cache =sf.getBoolean("delete_cache_on_refresh", false);
        save_to_cache =sf.getBoolean("save_to_cache", false);

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
        stdList = new ArrayList<>();
        mAdapter = new StudentAdapter(this, stdList, this);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        db = new SQLiteHandler(getApplicationContext());
        lastid = db.getStudentLast();// get last record in local db.
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
       // recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 8));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
 
        recyclerView.setAdapter(mAdapter);
        if(!CheckNet(getApplicationContext())){
            Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.greetings, Snackbar.LENGTH_LONG);
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
                //Intent login = new Intent(getApplicationContext(), NewStudent.class);
                //startActivity(login);
                //finish();
                newStudent(false, null);
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
                db.deleteStd(); // Delete cache and create again
                fetchUsers();
                //Toast.makeText(getApplicationContext(), "Cache deleted.", Toast.LENGTH_LONG).show();
            } else {
                fetchUsers();// Fetch online records and add
                //Toast.makeText(getApplicationContext(), "Cache NOT deleted.", Toast.LENGTH_LONG).show();
            }


        }
    }

    @Override
    public void onResume(){
        super.onResume();
        fetchUsers();
    }
    /**
     * fetches json by making http calls
     */
    private void fetchUsers() {
        // Tag used to cancel the request
        String tag_json_obj = "json_obj_req";
        
        swipeRefreshLayout.setRefreshing(true);
        final JsonArrayRequest request = new JsonArrayRequest(Config.URL +"students.php?offset="+per_page+"&lastid="+lastid,
                new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(getApplicationContext(), "Couldn't fetch the records! Please try again.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        //Integer c = response.size();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject j = new JSONObject(response.getString(i));
                                String idno1 = j.getString("idno");
                                String fname = j.getString("firstname");
                                String sname = j.getString("surname");
                                String yob = j.getString("yob");
                                String email = j.getString("email");
                                String branch = j.getString("admdate");
                                String gender = j.getString("gender");
                                String phone = j.getString("telephone");
                                String admno = j.getString("admno");
                                String admdate = j.getString("admdate");
                                db.saveStudent(idno1, fname, sname, yob, email, branch, gender, phone, admno, admdate,1);
                                //Toast.makeText(getApplicationContext(),R.string.greetings ,Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                //progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Error inserting to local db: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }


                        }
                        //usersList.addAll(db.getAllStudents());
                        List<Student> items = new Gson().fromJson(response.toString(), new TypeToken<List<Student>>(){}.getType());
 
                        // adding contacts to users list
                        stdList.clear();
                        //stdList.addAll(db.getAllStudents());
                        stdList.addAll(items);
 
                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                   // 
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json

                Log.e(TAG, "Error:+++++++++++++++++++++++++++++++++++++++ " + db.getAllStudents());
                Toast.makeText(getApplicationContext(), R.string.from_local, Toast.LENGTH_SHORT).show();
                stdList.clear();
                stdList.addAll(db.getAllStudents());
                mAdapter.notifyDataSetChanged();
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), "Records : " + db.getStudentCount(), Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("offset", per_page);
                params.put("lastid", lastid);

                return params;
            }

        };
 
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
    public void onStudentSelected(Student i) {
        showActionsDialog(i.getAdmno());
        Toast.makeText(getApplicationContext(), "Selected: " + i.getFirstname() + ", " + i.getSurname(), Toast.LENGTH_SHORT).show();
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
        appBarLayout.setExpanded(true);
 
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
                    collapsingToolbar.setTitle(getString(R.string.title_activity_students));
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


    private void showActionsDialog(final String position) {
        CharSequence colors[] = new CharSequence[]{"Show Details", "Update Student", "New Payment", "Add PDL","Add Lesson", "Add Picture"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Action \n("+ position +")");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showDetails(position);
                } else if(which == 1){
                    newStudent(true, position);
                    //return;
                } else if(which == 2){
                    newPayment(position);
                } else if(which == 3){
                    viewEditions(position);
                }else if(which == 4){
                    viewLessons(position);
                }else {
                    takePicture(position);
                }
            }
        });
        // builder.setIcon(R.drawable.common_google_signin_btn_icon_dark);
        builder.setCancelable(false);
        builder.show();
    }
    private void showDetails(final String position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.student_details, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Students.this);
        alertDialogBuilderUserInput.setView(view);

        final TextView title = view.findViewById(R.id.title);
        final TextView name = view.findViewById(R.id.name);
        final TextView email = view.findViewById(R.id.email);
        final TextView admdate = view.findViewById(R.id.admdate);
        final TextView phone = view.findViewById(R.id.phone);
        final TextView sex = view.findViewById(R.id.sex);
        final TextView dob = view.findViewById(R.id.dob);
        final TextView admno = view.findViewById(R.id.admno);
        final TextView idno = view.findViewById(R.id.idno);
        final TextView status = view.findViewById(R.id.status);
        Student v = db.getStudent(position);
        title.setText("Details For : " +v.getFirstname() +" " +v.getSurname());
        //dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_note_title) : getString(R.string.lbl_edit_note_title));

        if (v != null) {
            name.setText("Name : " + v.getFirstname() +" " +v.getSurname());
            admdate.setText("Adm Date : " +v.getAdmdate());
            email.setText("Email : " +v.getEmail());
            phone.setText("Phone : " +v.getTelephone());
            sex.setText("Sex : " +v.getGender());
            dob.setText("DOB : " +v.getYob());
            admno.setText("Admno : " +v.getAdmno());
            idno.setText("Idno : " +v.getIdno());
            status.setText("Status : " +v.getStatus());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
//                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialogBox, int id) {
//
//                    }
//                })
                .setNegativeButton("Close",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(title.getText().toString())) {
                    Toast.makeText(Students.this, "Enter note!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating note
                if (v != null) {
                    // update note by it's id
                    Toast.makeText(Students.this, "Update Note!", Toast.LENGTH_SHORT).show();
                    //updateNote(inputNote.getText().toString(), position);
                } else {
                    // create new note
                    Toast.makeText(Students.this, "Create note!", Toast.LENGTH_SHORT).show();
                    //createNote(inputNote.getText().toString());
                }
            }
        });
    }

    private void newEdition(String position) {
        Toast.makeText(getApplicationContext(), "New Edition!", Toast.LENGTH_SHORT).show();
    }
    private void newLesson(String position) {
        Toast.makeText(getApplicationContext(), "New Lesson!", Toast.LENGTH_SHORT).show();
    }
    private void viewEditions(String position) {
        Toast.makeText(getApplicationContext(), "View Editions!", Toast.LENGTH_SHORT).show();
    }
    private void viewLessons(String position) {
        Toast.makeText(getApplicationContext(), "View Lessons!", Toast.LENGTH_SHORT).show();
    }

    private void newStudent(final boolean shouldUpdate, final String position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.new_student, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Students.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText fname = view.findViewById(R.id.fname);
        //final TextView title = view.findViewById(R.id.title);
        final EditText sname = view.findViewById(R.id.sname);
        final EditText email = view.findViewById(R.id.email);
        //final EditText admdate = view.findViewById(R.id.admdate);
        final EditText phone = view.findViewById(R.id.phone);
        final EditText sex = view.findViewById(R.id.gender);
        final EditText dob = view.findViewById(R.id.yob);
        //final EditText admno = view.findViewById(R.id.admno);
        final EditText idno = view.findViewById(R.id.idno);
        //final EditText status = view.findViewById(R.id.status);
        //TextView dialogTitle = view.findViewById(R.id.dialog_title);
        //dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_note_title) : getString(R.string.lbl_edit_note_title));
        Student v;
        if(position != null){  v = db.getStudent(position);} else{v = null;}
        //title.setText(!shouldUpdate ? "New Student" : "Update Student");
        if (shouldUpdate && v != null) {
            fname.setText(v.getFirstname());
            sname.setText(v.getSurname());
            //admdate.setText(v.getAdmdate());
            email.setText(v.getEmail());
            phone.setText(v.getTelephone());
            sex.setText(v.getGender());
            dob.setText(v.getYob());
            //admno.setText(v.getAdmno());
            idno.setText(v.getIdno());
            //status.setText("Status : " +v.getStatus());
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
        alertDialog.setTitle("New Student");
        alertDialog.setIcon(R.drawable.condira);
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(fname.getText().toString())) {
                    Toast.makeText(Students.this, "Enter First Name!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating note
                if (shouldUpdate && v != null) {
                    // update note by it's id
                    //updateNote(inputNote.getText().toString(), position);
                } else {
                    // save new student
                    //String idno, String fname, String sname, String yob, String email, String branch, String gender, String phone, String admno,String admdate, Integer status
                    db.saveStudent(idno.getText().toString(), fname.getText().toString(),sname.getText().toString(),dob.getText().toString(), email.getText().toString(), null, sex.getText().toString(), phone.getText().toString(), null, null, 0);
                }
            }
        });
    }


    private void newPayment(final String position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.new_payment, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Students.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText amount = view.findViewById(R.id.amount);
        final TextView title = view.findViewById(R.id.title);
        final EditText admno = view.findViewById(R.id.admno);
        final EditText mode = view.findViewById(R.id.mode);
        //TextView dialogTitle = view.findViewById(R.id.dialog_title);
        //dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_note_title) : getString(R.string.lbl_edit_note_title));
        Student v;
        if(position != null){  v = db.getStudent(position);} else{v = null;}
        title.setText("New Payment for : "+ v.getSurname());
        if (v != null) {
            admno.setText(v.getAdmno());
            admno.setKeyListener(null);
        }
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
        alertDialog.setTitle("New Payment");
        alertDialog.setIcon(R.drawable.condira);
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(amount.getText().toString())) {
                    Toast.makeText(Students.this, "Amount cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }
                    //Double am = admno.getText();
                    db.savePayment(admno.getText().toString(), amount.getText().toString(), mode.getText().toString(), 0);

            }
        });
    }

    public void takePicture(String position){
        //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        Intent i = new Intent(getApplicationContext(), CameraActivity.class);
        i.putExtra("idno", position);
        startActivity(i);
        //finish();
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
}
