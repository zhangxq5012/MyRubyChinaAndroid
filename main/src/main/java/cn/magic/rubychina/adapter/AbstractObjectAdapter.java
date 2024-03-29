package cn.magic.rubychina.adapter;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import cn.magic.rubychina.util.HtmlImageGetter;
import cn.magic.rubychina.util.NetWorkUtil;
import cn.magic.rubychina.vo.AbstractObject;

/**
 * Created by magic on 2014/7/28.
 */
public class AbstractObjectAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    int mLayout;
    Context mContext;
    String[] from;//属性名称数组
    int[] to;//view的ID


    List<AbstractObject> abstractObjects;

    public void setAbstractObjects(List abstractObjects) {
        this.abstractObjects = abstractObjects;
        notifyDataSetChanged();
    }


    public AbstractObjectAdapter(Context context, int layout, List abstractObjects, String[] from, int[] to) {
        super();
        mContext = context;
        mLayout = layout;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.from = from;
        this.to = to;
        this.abstractObjects = abstractObjects;
    }

    @Override
    public int getCount() {
        if (abstractObjects != null) {
            return abstractObjects.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return abstractObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v;
        if (convertView == null) {
            v = newView(mContext, parent);
        } else {
            v = convertView;
        }
        bindView(v, mContext, abstractObjects.get(position));
        return v;
    }

    private void bindView(View view, Context mContext, AbstractObject abstractObject) {

        for (int i = 0; i < to.length; i++) {
            final View v = view.findViewById(to[i]);
            String text = (String) abstractObject.getAttribute(from[i]);
            if (text == null) {
                continue;
            }
            if (v instanceof TextView) {
                setViewText((TextView) v, text);
            } else if (v instanceof NetworkImageView) {
                setNetworkImageView((NetworkImageView) v, text);
            } else if (v instanceof WebView) {
                setWebVIew((WebView) v, text);
            } else {
                throw new IllegalStateException(v.getClass().getName() + " is not a " +
                        " view that can be bounds by this SimpleCursorAdapter");
            }
        }
    }

    private void setWebVIew(WebView webview, String text) {
        webview.getSettings().setDefaultTextEncodingName(NetWorkUtil.CHARSET);
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview.loadData(text, "text/html; charset=UTF-8", null);
    }

    private void setNetworkImageView(NetworkImageView v, String text) {
        v.setImageUrl(text, NetWorkUtil.getInstance(mContext).getImageLoader());
    }

    private void setViewText(TextView v, String text) {
        Spanned parsedHtml = Html.fromHtml(text, new HtmlImageGetter(mContext, v, text), null);
        v.setText(parsedHtml);

    }


    /**
     * Inflates view(s) from the specified XML file.
     *
     * @see android.widget.CursorAdapter#newView(android.content.Context,
     * android.database.Cursor, ViewGroup)
     */
    public View newView(Context context, ViewGroup parent) {
        return mInflater.inflate(mLayout, parent, false);
    }
}

