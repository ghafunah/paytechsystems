//package com.paytech.paytechsystems;
//
//import android.content.Context;
//import android.content.res.Resources;
//import android.databinding.DataBindingUtil;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.GridLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
//import android.util.TypedValue;
//import android.view.View;
//import android.widget.Toast;
//
//import com.paytech.paytechsystems.R;
//import com.paytech.paytechsystems.adapter.PostsAdapter;
//import com.paytech.paytechsystems.getset.Bind;
//import com.paytech.paytechsystems.getset.Post;
//import com.paytech.paytechsystems.helper.GridSpacingItemDecoration;
//import com.paytech.paytechsystems.databinding.ActivityBindBinding;
//
//import java.util.ArrayList;
//
//
//public class BindActivity extends AppCompatActivity implements PostsAdapter.PostsAdapterListener {
//
//    private MyClickHandlers handlers;
//    private PostsAdapter mAdapter;
//    private RecyclerView recyclerView;
//    private ActivityBindBinding binding;
//    private Bind user;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        binding = DataBindingUtil.setContentView(this, R.layout.activity_bind);
//
//        Toolbar toolbar = binding.toolbar;
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle(R.string.toolbar_profile);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//
//        handlers = new MyClickHandlers(this);
//
//        renderProfile();
//
//        initRecyclerView();
//    }
//
//    /**
//     * Renders RecyclerView with Grid Images in 3 columns
//     */
//    private void initRecyclerView() {
//        recyclerView = binding.content.recyclerView;
//        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
//        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(4), true));
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setNestedScrollingEnabled(false);
//        mAdapter = new PostsAdapter(getPosts(), this);
//        recyclerView.setAdapter(mAdapter);
//    }
//
//    /**
//     * Renders user profile data
//     */
//    private void renderProfile() {
//        user = new Bind();
//        user.setName("David Attenborough");
//        user.setEmail("david@natgeo.com");
//        user.setProfileImage("https://api.androidhive.info/images/nature/david.jpg");
//        user.setAbout("Naturalist");
//
//        // ObservableField doesn't have setter method, instead will
//        // be called using set() method
//        user.numberOfPosts.set(3400L);
//        user.numberOfFollowers.set(3050890L);
//        user.numberOfFollowing.set(150L);
//
//
//        // display user
//        //binding.setUser(user);
//
//        // assign click handlers
//        //binding.content.setHandlers(handlers);
//    }
//
//    private ArrayList<Post> getPosts() {
//        ArrayList<Post> posts = new ArrayList<>();
//        for (int i = 1; i < 10; i++) {
//            Post post = new Post();
//            post.setImageUrl("https://api.androidhive.info/images/nature/" + i + ".jpg");
//
//            posts.add(post);
//        }
//
//        return posts;
//    }
//
//    @Override
//    public void onPostClicked(Post post) {
//        Toast.makeText(getApplicationContext(), "Post clicked! " + post.getImageUrl(), Toast.LENGTH_SHORT).show();
//    }
//
//    public class MyClickHandlers {
//
//        Context context;
//
//        public MyClickHandlers(Context context) {
//            this.context = context;
//        }
//
//        /**
//         * Demonstrating updating bind data
//         * Profile name, number of posts and profile image
//         * will be updated on Fab click
//         */
//        public void onProfileFabClicked(View view) {
//            user.setName("Sir Fredrick Kimayos");
//            user.setProfileImage("https://api.androidhive.info/images/nature/david1.jpg");
//
//            // updating ObservableField
//            user.numberOfPosts.set(5500L);
//            user.numberOfFollowers.set(5050890L);
//            user.numberOfFollowing.set(180L);
//        }
//
//        public boolean onProfileImageLongPressed(View view) {
//            Toast.makeText(getApplicationContext(), "Profile image long pressed!", Toast.LENGTH_LONG).show();
//            return false;
//        }
//
//
//        public void onFollowersClicked(View view) {
//            Toast.makeText(context, "Followers is clicked!", Toast.LENGTH_SHORT).show();
//        }
//
//        public void onFollowingClicked(View view) {
//            Toast.makeText(context, "Following is clicked!", Toast.LENGTH_SHORT).show();
//        }
//
//        public void onPostsClicked(View view) {
//            Toast.makeText(context, "Posts is clicked!", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    /**
//     * Converting dp to pixel
//     */
//    private int dpToPx(int dp) {
//        Resources r = getResources();
//        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
//    }
//}