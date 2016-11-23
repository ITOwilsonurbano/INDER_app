package com.itosoftware.inderandroid.Utils;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;

public class BitmapMemCache extends LruCache<String, Bitmap> implements ImageLoader.ImageCache {

    public BitmapMemCache() {
        this((int) (Runtime.getRuntime().maxMemory() / 1024) / 8);
    }

    public BitmapMemCache(int sizeInKiloBytes) {
        super(sizeInKiloBytes);
    }

    @Override
    protected int sizeOf(String key, Bitmap bitmap) {
        int size = bitmap.getByteCount() / 1024;
        return size;
    }

    public boolean contains(String key) {
        return get(key) != null;
    }

    public Bitmap getBitmap(String key) {
        Bitmap bitmap = get(key);
        return bitmap;
    }

    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
    }
}