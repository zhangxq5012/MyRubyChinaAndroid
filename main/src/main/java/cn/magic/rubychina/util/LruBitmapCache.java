package cn.magic.rubychina.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import android.util.Log;

import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.ImageLoader.ImageCache;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cn.magic.rubychina.app.RubyApplication;

public class LruBitmapCache extends LruCache<String, Bitmap>
        implements ImageCache {
    private static final String TAG = "LruBitmapCache";
    private static final int MAXIMAGEDISKNUM = 50;
    private static final int MAXIMAGEMEMORYNUM = 4;
    private DiskLruCache mDiskCache;
    public static final int MAXCACHESIZE = 1024 * 1024 * 200;

    private final Object mDiskCacheLock = new Object();
    private boolean mDiskCacheStarting = true;
    private static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.JPEG;
    private static final int DEFAULT_COMPRESS_QUALITY = 70;


    private static final String DISK_CACHE_SUBDIR = "avatar_url";

    private LruBitmapCache(int maxSize) {
        super(maxSize);
    }

    public LruBitmapCache(Context ctx) {
        this(MAXIMAGEMEMORYNUM * getCacheSize(ctx));
        initDistCache(ctx);

    }

    private void initDistCache(Context ctx) {
        try {
            File cacheDir = getCacheDir(ctx, DISK_CACHE_SUBDIR);
//            mDiskCache = DiskLruCache.open(cacheDir, RubyApplication.VERSION, 1,
//                    getCacheSize(ctx) * MAXIMAGEDISKNUM);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    protected int sizeOf(String key, Bitmap value) {
        return value.getRowBytes() * value.getHeight();
    }

    @Override
    public Bitmap getBitmap(String url) {
        Bitmap bitmap;
        bitmap = get(url);
        if (bitmap == null) {
            bitmap = getBitmapFromDiskCache(url);
        }
        return bitmap;
    }

    public Bitmap getBitmapFromDiskCache(String key) {
        Bitmap bitmap = null;

        synchronized (mDiskCacheLock) {
            while (mDiskCacheStarting) {
                try {
                    mDiskCacheLock.wait();
                } catch (InterruptedException e) {
                }
            }
            if (mDiskCache != null) {
                InputStream inputStream = null;
                try {
                    final DiskLruCache.Snapshot snapshot = mDiskCache.get(key);
                    if (snapshot != null) {
                        Log.d(TAG, "Disk cache hit");
                        inputStream = snapshot.getInputStream(0);
                        if (inputStream != null) {
                            FileDescriptor fd = ((FileInputStream) inputStream).getFD();

                            // Decode bitmap, but we don't want to sample so give
                            // MAX_VALUE as the target dimensions
                            final BitmapFactory.Options options = new BitmapFactory.Options();
                            bitmap= BitmapFactory.decodeFileDescriptor(fd, null, options);
                        }
                    }
                } catch (final IOException e) {
                    Log.e(TAG, "getBitmapFromDiskCache - " + e);
                } finally {
                    try {
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } catch (IOException e) {
                    }
                }
            }
            return bitmap;
        }
    }


    public void putBitmap(String url, Bitmap bitmap) {
        put(url, bitmap);
        addBitmapToCache(url,bitmap);

    }


    /**
     * Adds a bitmap to both memory and disk cache.
     * @param url Unique identifier for the bitmap to store
     * @param value The bitmap drawable to store
     */
    public void addBitmapToCache(String url, Bitmap value) {

        if (url == null || value == null) {
            return;
        }
        synchronized (mDiskCacheLock) {
            // Add to disk cache
            if (mDiskCache != null) {
                OutputStream out = null;
                try {
                    DiskLruCache.Snapshot snapshot = mDiskCache.get(url);
                    if (snapshot == null) {
                        final DiskLruCache.Editor editor = mDiskCache.edit(url);
                        if (editor != null) {
                            out = editor.newOutputStream(0);
                            value.compress(
                                    DEFAULT_COMPRESS_FORMAT, DEFAULT_COMPRESS_QUALITY, out);
                            editor.commit();
                            out.close();
                        }
                    } else {
                        snapshot.getInputStream(0).close();
                    }
                } catch (final IOException e) {
                    Log.e(TAG, "addBitmapToCache - " + e);
                } catch (Exception e) {
                    Log.e(TAG, "addBitmapToCache - " + e);
                } finally {
                    try {
                        if (out != null) {
                            out.close();
                        }
                    } catch (IOException e) {}
                }
            }
        }

    }

    // Returns a cache size equal to approximately three screens worth of images.

    public static int getCacheSize(Context ctx) {
        final DisplayMetrics displayMetrics = ctx.getResources().
                getDisplayMetrics();
        final int screenWidth = displayMetrics.widthPixels;
        final int screenHeight = displayMetrics.heightPixels;
        // 4 bytes per pixel
        final int screenBytes = screenWidth * screenHeight * 4;

        return screenBytes;
    }


    /**
     * 获取缓存文件路径
     */
    public static File getCacheDir(Context context, String uniqueName) {
        // Check if media is mounted or storage is built-in, if so, try and use external cache dir
        // otherwise use internal cache dir
        File inCahcePath = context.getCacheDir();
        return new File(inCahcePath.getPath() + File.separator + uniqueName);
    }
}