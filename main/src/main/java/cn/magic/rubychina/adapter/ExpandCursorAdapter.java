package cn.magic.rubychina.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import cn.magic.rubychina.util.HtmlImageGetter;
import cn.magic.rubychina.util.NetWorkUtil;

/**
 * Created by magic on 2014/9/13.
 */
public class ExpandCursorAdapter extends SimpleCursorAdapter {
    public ExpandCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
    }

    public ExpandCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }

    public String getID(int position){
        getCursor().moveToPosition(position);
        return getCursor().getString(getCursor().getColumnIndex("id"));
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (!mDataValid) {
            throw new IllegalStateException("this should only be called when the cursor is valid");
        }
        if (!mCursor.moveToPosition(position)) {
            throw new IllegalStateException("couldn't move cursor to position " + position);
        }
        if (convertView == null) {
            convertView = newView(mContext, mCursor, parent);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder= (ViewHolder) convertView.getTag();
        }
        bindView(convertView, mContext, mCursor);
        return convertView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {


        final ViewBinder binder = getViewBinder();
        final int count = mTo.length;
        final int[] from = mFrom;
        final int[] to = mTo;

        for (int i = 0; i < count; i++) {
            final View v = ((ViewHolder)view.getTag()).getView(to[i]);
//            final View v = view.findViewById(to[i]);
            if (v != null) {
                boolean bound = false;
                if (binder != null) {
                    bound = binder.setViewValue(v, cursor, from[i]);
                }

                if (!bound) {
                    String text = cursor.getString(from[i]);
                    if (text == null) {
                        text = "";
                    }

                    if (v instanceof TextView) {
                        setViewText((TextView) v, text);
                    } else if (v instanceof NetworkImageView) {
                        setNetworkImageView((NetworkImageView) v, text);
                    } else if (v instanceof WebView) {
                        setWebVIew((WebView) v, text);
                    }
                    else if (v instanceof ImageView) {
                        setViewImage((ImageView) v, text);
                    }
                    else {
                        throw new IllegalStateException(v.getClass().getName() + " is not a " +
                                " view that can be bounds by this SimpleCursorAdapter");
                    }
                }
            }
        }
    }

    public void setViewText(TextView v, String text) {
        Spanned parsedHtml= Html.fromHtml(text, new HtmlImageGetter(mContext, v, text), null);
        v.setText(parsedHtml);

    }
    public void setWebVIew(WebView webview, String text) {
        webview.getSettings().setDefaultTextEncodingName(NetWorkUtil.CHARSET);
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview.loadData(text, "text/html; charset=UTF-8", null);
    }

    public void setNetworkImageView(NetworkImageView v, String text) {
        v.setImageUrl(text, NetWorkUtil.getInstance(mContext).getImageLoader());
    }

}
