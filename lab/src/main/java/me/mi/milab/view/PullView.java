package me.mi.milab.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;

/**
 * Created by mi.gao on 2018/4/17.
 */

public class PullView extends LinearLayout {
    /**移动点的保护范围值*/
    private int mTouchSlop;
    private float mLastMotionY = -1;
    private int mHeight = 1;
    public PullView(Context context) {
        super(context, null);
    }
    public PullView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        setBackgroundColor(0xff000000);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getMeasuredHeight();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:{
                mLastMotionY = event.getY();
                Log.e("jimwind", "down: Y "+event.getY() + " "+mTouchSlop);

                break;
            }
            case MotionEvent.ACTION_MOVE:{
                float deltaY = event.getY() - mLastMotionY;
                Log.e("jimwind", "move: Y"+event.getY() +"/"+mHeight);
                mLastMotionY = event.getY();
                if(mHeight > 0) {
                    setAlpha(event.getY() / (float) mHeight);
                }


                break;
            }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:{
                break;
            }
        }

        return true;
    }
}
