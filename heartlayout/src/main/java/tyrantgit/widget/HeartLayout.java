/*
 * Copyright (C) 2015 tyrantgit
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tyrantgit.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import java.util.Timer;
import java.util.TimerTask;

public class HeartLayout extends RelativeLayout implements View.OnTouchListener {

    //隐藏的时候
    public static final int STATUS_HIDE = 0;
    //准备更新
    public static final int STATUS_PRE_REFRESH = 1;
    //正在更新
    public static final int STATUS_REFRESHING  =2;
    //准备停止更新
    public static final int STATUS_PRE_STOP = 3;
    //停止更新
    public static final int STATUS_FINISH = 4;
    private AbstractPathAnimator mAnimator;
    private Timer mTimer = new Timer();
    // 手指按下时,屏幕上的高度
    private float mYDown = 0;
    // 手指移动的最大高度
    private int mTouchSlop = 0;
    // 下拉头的布局参数
    private MarginLayoutParams mHeaderLayoutParams;
    // 心形view的高度
    private HeartView mHeartView;
    //隐藏heartview部分的高度
    private int mHideHeaderHeight;
    //当前的状态
    private boolean mAbleToPull;
    private int mCurrentStatus;

    public HeartLayout(Context context) {
        super(context);
        init(null, 0);
    }

    public HeartLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public HeartLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        mHeartView  =new HeartView(getContext());
        mHideHeaderHeight = -2*mHeartView.getHeight();
        mHeaderLayoutParams = (MarginLayoutParams) mHeartView.getLayoutParams();
        mAbleToPull = true;
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.HeartLayout, defStyleAttr, 0);
        mAnimator = new PathAnimator(AbstractPathAnimator.Config.fromTypeArray(a));
        a.recycle();
    }

    public AbstractPathAnimator getAnimator() {
        return mAnimator;
    }

    public void setAnimator(AbstractPathAnimator animator) {
        clearAnimation();
        mAnimator = animator;
    }

    public void clearAnimation() {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).clearAnimation();
        }
        removeAllViews();
    }

    public void addHeart(int color) {
        HeartView heartView = new HeartView(getContext());
        heartView.setColor(color);
        mAnimator.start(heartView, this);
    }

    public void addHeart(int color, int heartResId, int heartBorderResId) {
        HeartView heartView = new HeartView(getContext());
        heartView.setColorAndDrawables(color, heartResId, heartBorderResId);
        mAnimator.start(heartView, this);
    }

    public void startUpdate(){
        mTimer.scheduleAtFixedRate(new TimerTask() {
            @Override public void run() {
                addHeart(Color.RED);
            }
        },500,1000);
    }

    private void stopAnimation(){

    }

    public void finish(){
        mTimer.cancel();
    }

    @Override public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mYDown = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float yMove = event.getRawY();
                int distance = (int) (yMove - mYDown);
                if (distance<=0||distance<mTouchSlop){
                    return false;
                }

                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return false;
    }
}
