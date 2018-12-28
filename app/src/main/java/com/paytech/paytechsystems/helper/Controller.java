package com.paytech.paytechsystems.helper;

import android.app.Application;
import android.content.Intent;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.FirebaseDatabase;
import com.paytech.paytechsystems.BuildConfig;
import com.paytech.paytechsystems.MainActivity;

import timber.log.Timber;

public class Controller extends Application {

	public static final String TAG = Controller.class.getSimpleName();

	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	private static Controller mInstance;
	LruBitmapCache mLruBitmapCache;
	private SessionManager pref;
	private FirebaseDatabase mFirebaseDatabase;
	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		//if (mFirebaseDatabase == null){
		FirebaseDatabase.getInstance().setPersistenceEnabled(true);
		//}
		if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            //Timber.plant(new ReleaseTree());
        }
	}

	public static synchronized Controller getInstance() {
		return mInstance;
	}

	public ImageLoader getImageLoader() {
		getRequestQueue();
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(this.mRequestQueue, new LruBitmapCache());
		}
		return this.mImageLoader;
	}

	public LruBitmapCache getLruBitmapCache() {
        if (mLruBitmapCache == null)
            mLruBitmapCache = new LruBitmapCache();
        return this.mLruBitmapCache;
    }

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	 public SessionManager getPrefManager() {
        if (pref == null) {
            pref = new SessionManager(this);
        }
 
        return pref;
    }

    public void logout() {
        pref.clear();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    }