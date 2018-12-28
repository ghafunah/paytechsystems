package com.paytech.paytechsystems;

//import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaCas;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.paytech.paytechsystems.condira.MemberActivity;
import com.paytech.paytechsystems.condira.SignInActivity2;
import com.paytech.paytechsystems.family.FamilyActivity;
import com.paytech.paytechsystems.family.SignInActivity;
import com.paytech.paytechsystems.genqr.QrGen;
import com.paytech.paytechsystems.getset.User;
import com.paytech.paytechsystems.helper.Controller;
import com.paytech.paytechsystems.helper.SQLiteHandler;
import com.paytech.paytechsystems.helper.SessionManager;
import com.paytech.paytechsystems.storage.StorageActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SQLiteHandler db;
    private SessionManager session;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private TextView userTxt, textEmail;
    // index to identify current nav menu item
    public static int navItemIndex = 0;
    SharedPreferences pref;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_BRANCH = "branch";
    private static final String TAG_STAFF = "staff";
    private static final String TAG_INSTRUCTOR = "instructor";
    private static final String TAG_MPESA = "mpesa";
    private static final String TAG_PAYMENT = "payment";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    private int[] tabIcons = {
            R.drawable.ic_menu_camera,
            R.drawable.ic_menu_camera,
            R.drawable.ic_menu_camera,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayOptions(getSupportActionBar().DISPLAY_USE_LOGO|getSupportActionBar().DISPLAY_SHOW_HOME|getSupportActionBar().DISPLAY_HOME_AS_UP);
        getSupportActionBar().setIcon(android.R.color.transparent);//ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setTitle("Condira Systems");
        //actionBar.setIcon(R.drawable.ic_menu_camera);

        //viewPager = (ViewPager) findViewById(R.id.viewpager);
        //setupViewPager(viewPager);


        //tabLayout = (TabLayout) findViewById(R.id.tabs);
        //tabLayout.setupWithViewPager(viewPager);

        //Show Icons for the tabs
        //setupTabIcons();
        mHandler = new Handler();
        // fab = (FloatingActionButton) findViewById(R.id.fab);
        // fab.setOnClickListener(new View.OnClickListener() {
        //     @Override
        //     public void onClick(View view) {
        //         Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //                 .setAction("Action", null).show();
        //     }
        // });
        // navHeader = navigationView.getHeaderView(0);
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navHeader = navigationView.getHeaderView(0);
        userTxt = (TextView) navHeader.findViewById(R.id.textTitle);
        textEmail = (TextView) navHeader.findViewById(R.id.textView);
        db = new SQLiteHandler(getApplicationContext());
                // Fetching user details from sqlite
        //HashMap<String, String> user = db.getUserDetails();

       // String fname = user.get("fname");
       //String sname = user.get("sname");
        SharedPreferences pref = getSharedPreferences("Paytech", 0);
        String uname = pref.getString("user_uname", null);
        String fname = pref.getString("user_fname", null);
        String sname = pref.getString("user_sname", null);

        userTxt.setText(fname + " " + sname + " " + uname);
        //textEmail.setText("Email");

        
        // load nav menu header data
        // loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
        

        // session manager
        session = new SessionManager(getApplicationContext());
        if (Controller.getInstance().getPrefManager().getUser() == null) {
            logoutUser();
        }
//        if (!session.isLoggedIn()) {
//            logoutUser();
//        }
        Button b = (Button)findViewById(R.id.action_logout);

//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(getFragmentRefreshListener()!=null){
//                    getFragmentRefreshListener().onRefresh();
//                }
//            }
//        });

    }


    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }
// private void loadNavHeader() {
//        // name, website
//        txtName.setText("Ravi Tamada");
//        txtWebsite.setText("www.androidhive.info");
//
//        // loading header background image
//        Glide.with(this).load(urlNavHeaderBg)
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imgNavHeaderBg);
//
//        // Loading profile image
//        Glide.with(this).load(urlProfileImg)
//                .crossFade()
//                .thumbnail(0.5f)
//                .bitmapTransform(new CircleTransform(this))
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imgProfile);
//
//        // showing dot next to notifications label
//        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
//    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();
       // userTxt.setText("Name");
       // textEmail.setText("Email");
        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            //toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        //toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }

        // when fragment is notifications, load the menu created for notifications
        if (navItemIndex == 3) {
            getMenuInflater().inflate(R.menu.menu_users, menu);
        }
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
            //Intent user = new Intent(getApplicationContext(), Users.class);
            //startActivity(user);
            //finish();]
            return true;
        }

        if (id == R.id.action_location) {
            Intent i = new Intent(getApplicationContext(), LocationActivity.class);
            startActivity(i);
            //finish();
        }

        if (id == R.id.action_logout) {

            //Toast.makeText(getApplicationContext(), "You have been logged out !!!", Toast.LENGTH_LONG).show();
            //logoutUser();
            alert();
            return true;
        }
        if (id == R.id.action_settings) {
            // launch settings activity
            startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            return true;
        }        
        if (id == R.id.action_messages) {
            // launch settings activity
            startActivity(new Intent(MainActivity.this, UserfireActivity.class));
            return true;
        }          
        if (id == R.id.action_mymessages) {
            // launch settings activity
            startActivity(new Intent(MainActivity.this, MessageBox.class));
            return true;
        }         
        if (id == R.id.action_relation) {
            // launch settings activity
            startActivity(new Intent(MainActivity.this, SignInActivity2.class));
            return true;
        }        
        if (id == R.id.action_family) {
            // launch settings activity
            startActivity(new Intent(MainActivity.this, SignInActivity.class));
            return true;
        }

        if (id == R.id.action_settings_headers) {
            // launch settings activity
            startActivity(new Intent(MainActivity.this, SettingsHeadersActivity.class));
            return true;
        }
        if (id == R.id.action_browser) {
            // launch settings activity
            startActivity(new Intent(MainActivity.this, WebActivity.class));
            return true;
        }        
        if (id == R.id.action_storage) {
            // launch settings activity
            startActivity(new Intent(MainActivity.this, StorageActivity.class));
            return true;
        }        
        if (id == R.id.action_qrgen) {
            // launch settings activity
            startActivity(new Intent(MainActivity.this, QrGen.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

//        if (id == R.id.nav_branch) {
//            // Handle the camera action
//            Toast.makeText(getApplicationContext(), "You Clicked Branch! Nothing to do", Toast.LENGTH_LONG).show();
//        } else if (id == R.id.nav_instructor) {
//            Toast.makeText(getApplicationContext(), "You Clicked Instructor! Nothing to do", Toast.LENGTH_LONG).show();
//        } else if (id == R.id.nav_staff) {
//            Toast.makeText(getApplicationContext(), "You Clicked Staff! Nothing to do", Toast.LENGTH_LONG).show();
//        }
        if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                StudentFragment studentFragment = new StudentFragment();
                return studentFragment;
            case 1:
                // branch
                BranchFragment branchFragment = new BranchFragment();
                return branchFragment;
            case 2:
                // instructor fragment
                InstructorFragment instructorFragment = new InstructorFragment();
                return instructorFragment;
            case 3:
                // staff fragment
                StaffFragment staffFragment = new StaffFragment();
                return staffFragment;
            case 4:
                // staff fragment
                MpesaFragment mpesaFragment = new MpesaFragment();
                return mpesaFragment;
            // case 5:
            //     // staff fragment
            //     PaymentFragment payFragment = new PaymentFragment();
            //     return payFragment;
            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {

        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
       // userTxt.setText("Name");
       // textEmail.setText("Email");
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
//                    case R.id.nav_branch:
//                        navItemIndex = 1;
//                        CURRENT_TAG = TAG_BRANCH;
//                        break;
//                    case R.id.nav_instructor:
//                        navItemIndex = 2;
//                        CURRENT_TAG = TAG_INSTRUCTOR;
//                        break;
//                    case R.id.nav_staff:
//                        navItemIndex = 3;
//                        CURRENT_TAG = TAG_STAFF;
//                        break;
//                    case R.id.nav_mpesa:
//                        navItemIndex = 4;
//                        CURRENT_TAG = TAG_MPESA;
//                        break;
                    // case R.id.nav_payment:
                    //     navItemIndex = 5;
                    //     CURRENT_TAG = TAG_PAYMENT;
                    //     break;
                    case R.id.nav_student:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, Students.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_payments:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, Payments.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_teacher:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, Teachers.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_course:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, Courses.class));
                        drawer.closeDrawers();
                        return true;                    
                    case R.id.nav_branches:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, Branches.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_vehicles:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, Vehicles.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_gallery:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, GalleryActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_mail:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, MailActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_note:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, NoteActivity.class));
                        drawer.closeDrawers();
                        return true;                    
                    case R.id.nav_feed:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, FeedActivity.class));
                        drawer.closeDrawers();                    
                    case R.id.nav_movie:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, MovieActivity.class));
                        drawer.closeDrawers();
                        return true;                    
                    case R.id.nav_firebase:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, FirebaseActivity.class));
                        drawer.closeDrawers();
                        return true;                       
                    case R.id.nav_chat:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, ChatActivity.class));
                        drawer.closeDrawers();
                        return true;                    
                    case R.id.nav_retrofit:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, RetrofitActivity.class));
                        drawer.closeDrawers();
                        return true;                    
                    case R.id.nav_cardview:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, CardActivity.class));
                        drawer.closeDrawers();
                        return true;                    
                    case R.id.nav_mainfire:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, MainFireActivity.class));
                        drawer.closeDrawers();
                        return true;                    
                    case R.id.nav_mpesa2:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, MpesaActivity.class));
                        drawer.closeDrawers();
                        return true;                    
                    case R.id.nav_permissions:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, Permissions.class));
                        drawer.closeDrawers();
                        return true;                    
                    case R.id.nav_studenthome:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, StudentHomeActivity.class));
                        drawer.closeDrawers();
                        return true;                    
                    case R.id.nav_lessonedone:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, LessonsDone.class));
                        drawer.closeDrawers();
                        return true;                    
                    case R.id.nav_branchcollections:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, BranchCollections.class));
                        drawer.closeDrawers();
                        return true;                    
                    case R.id.nav_users:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, Users.class));
                        drawer.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    // show or hide the fab
//    private void toggleFab() {
//        if (navItemIndex == 0)
//            fab.show();
//        else
//            fab.hide();
//    }

    private void logoutUser() {
        session.setLogin(false);

        //db.deleteUsers();
        session.clear();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, Login.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void alert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Confirm Logging out!");

        // Setting Dialog Message
        alertDialog.setMessage("Are you sure you want logout?");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.ic_search_black_24dp);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                // Write your code here to invoke YES event
                Toast.makeText(getApplicationContext(), "You have been logged out!", Toast.LENGTH_SHORT).show();
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //                 .setAction("Action", null).show();
                logoutUser();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                Toast.makeText(getApplicationContext(), "You clicked on NO", Toast.LENGTH_SHORT).show();
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BranchFragment(), "Branches");
        adapter.addFragment(new StudentFragment(), "Students");
        adapter.addFragment(new StaffFragment(), "Staff");
        adapter.addFragment(new MpesaFragment(), "Mpesa");
        adapter.addFragment(new MpesaFragment(), "Payments");
        adapter.addFragment(new MpesaFragment(), "Courses");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
