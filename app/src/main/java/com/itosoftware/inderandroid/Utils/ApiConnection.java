package com.itosoftware.inderandroid.Utils;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;

/**
 ** Custom implementation of Volley Request Queue
 **/
public class ApiConnection {

    private static ApiConnection apiConnection = null;

    //this object is the queue that uses the application
    private static ApiConnection mInstance;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private static Context mCtx;

    private ApiConnection(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();


        mImageLoader = new ImageLoader(mRequestQueue,
            new ImageLoader.ImageCache() {
                private final LruCache<String, Bitmap>
                        cache = new BitmapMemCache();
                @Override
                public Bitmap getBitmap(String url) {
                    return cache.get(url);
                }
                @Override
                public void putBitmap(String url, Bitmap bitmap) {
                    cache.put(url, bitmap);
                }
            }
        );


    }

    public static synchronized ApiConnection getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ApiConnection(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }



}
