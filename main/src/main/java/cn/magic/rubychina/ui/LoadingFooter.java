package cn.magic.rubychina.ui;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.magic.rubychina.main.R;

/**
 * Created by magic on 2014/9/25.
 */
public class LoadingFooter {
    TextView mLoadingText;
    LinearLayout loadingLayout;
    ILoader iLoader;
    protected State mState = State.Idle;

    public static enum State {
        Idle, Loading
    }

    public LoadingFooter(Context context) {
        loadingLayout = (LinearLayout) View.inflate(context, R.layout.loading_footer, null);
        loadingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mLoadingText = (TextView) loadingLayout.findViewById(R.id.load_text);
        setState(State.Idle);
    }

    public View getView() {
        return loadingLayout;
    }

    public State getState() {
        return mState;
    }

    public void setState(State mState) {
        if (this.mState == mState) {
            return;
        }
        this.mState=mState;
        loadingLayout.setVisibility(View.VISIBLE);
        if (getState().equals(State.Idle)) {
            loadingLayout.setVisibility(View.GONE);
        } else {
            loadingLayout.setVisibility(View.VISIBLE);
            if(iLoader!=null){
                iLoader.load();
            }
        }
    }

    public void setiLoader(ILoader iLoader){
        this.iLoader=iLoader;
    }

    interface ILoader{
        public void load();
    }


}
