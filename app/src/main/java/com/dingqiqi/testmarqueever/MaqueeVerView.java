package com.dingqiqi.testmarqueever;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.logging.MemoryHandler;

/**
 * Created by Administrator on 2016/9/18.
 */
public class MaqueeVerView extends View {
    /**
     * 上下文
     */
    private Context mContext;
    /**
     * 广告内容
     */
    private String[] mStrs = new String[]{
            "啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦11111111111",
            "啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦22222222222",
            "啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦33333333333",
            "啦啦啦啦啦啦啦啦啦啦啦啦啦啦啦44444444444"
    };
    /**
     * 文字画笔
     */
    private Paint mPaint;
    /**
     * 文字大小
     */
    private int mTextSize = 16;
    /**
     * 上下文字距离
     */
    private float mPadding = 5;
    /**
     * 文字高度
     */
    private float mTextHeight;
    /**
     * 文字相关属性集合
     */
    private Rect mTextBonds;
    /**
     * 文字位置
     */
    private MODE mMode = MODE.LEFT;

    public enum MODE {
        LEFT, CENTER, RIGHT;
    }

    /**
     * 下标
     */
    private int mIndex;
    /**
     * 每次移动高度
     */
    private float mMoveHeigh;
    /**
     * 每次移动的最大距离
     */
    private float mMaxModeHeigh;
    /**
     * 移动计数器
     */
    private int mMoveIndex;
    /**
     * 移动次数
     */
    private int mMoveCount = 5;
    /**
     * 是否在移动
     */
    private boolean mIsMove = true;
    private Handler mHandler = new Handler();
    /**
     * 文字条数(广告条数)
     */
    private int mTextCount = 4;
    /**
     * 字体颜色
     */
    private int mColor = Color.BLACK;
    /**
     * 上下左右间距
     */
    private float mPaddingMini = 2f;

    /**
     * 设置字体颜色
     *
     * @param color
     */
    public void setColor(int color) {
        this.mColor = color;
        mPaint.setColor(mColor);
    }

    /**
     * 设置文字数组
     *
     * @param strs
     */
    public void setStrs(String[] strs) {
        if (strs == null || strs.length < mTextCount) {
            throw new IllegalArgumentException("广告文字数组不合规则");
        }
        this.mStrs = strs;
    }

    /**
     * 设置显示广告个数
     *
     * @param count
     */
    public void setTextCount(int count) {
        if (count < 1) {
            throw new IllegalArgumentException("显示广告条数不合规则");
        }
        this.mTextCount = count;
        mPadding = (getMeasuredHeight() - dp2px(mPaddingMini) - mTextCount * mTextHeight) / (mTextCount - 1);
        mMaxModeHeigh = mPadding + mTextHeight;
    }

    public void setMode(MODE mode) {
        this.mMode = mode;
    }

    public MaqueeVerView(Context context) {
        super(context, null);
    }

    public MaqueeVerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    private void initView() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mColor);
        mPaint.setTextSize(dp2px(mTextSize));
        mPaint.setStyle(Paint.Style.FILL);

        mTextBonds = new Rect();

        mPadding = dp2px(mPadding);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());

        mPaint.getTextBounds(mStrs[0], 0, mStrs[0].length(), mTextBonds);
        mTextHeight = mTextBonds.height();
        //总高度减去n个文字高度,在除去n-1个间隔，得出间隔高度
        mPadding = (getMeasuredHeight() - dp2px(mPaddingMini) - mTextCount * mTextHeight) / (mTextCount - 1);

        mMaxModeHeigh = mPadding + mTextHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //为了移动效果，要多画一条
        for (int i = 0; i <= mTextCount; i++) {
            String str = getStr(i + mIndex);
            mPaint.getTextBounds(str, 0, str.length(), mTextBonds);
            int x;
            if (mMode == MODE.LEFT) {
                x = 0 + dp2px(mPaddingMini);
            } else if (mMode == MODE.CENTER) {
                x = getMeasuredWidth() / 2 - mTextBonds.width() / 2;
            } else {
                x = getMeasuredWidth() - mTextBonds.width() - dp2px(mPaddingMini);
            }

            canvas.drawText(str, x, (i + 1) * mTextHeight + i * mPadding - mMoveHeigh, mPaint);
        }
        //开启移动
        mHandler.postDelayed(mRunnable, 120);
    }

    /**
     * 获取文字
     *
     * @param index
     * @return
     */
    private String getStr(int index) {
        //循环拿文字
        return mStrs[index % mStrs.length];
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            //当移动次数等于最大移动次数
            if (mMoveIndex == mMoveCount) {
                //次数归零
                mMoveIndex = 0;
                //不是刚开移动
                if (mMoveHeigh != 0) {
                    //下标加1
                    mIndex++;
                    //到达数组最大长度，重新开始
                    if (mIndex == mStrs.length) {
                        mIndex = 0;
                    }
                    mMoveHeigh = 0;
                }
                //移动距离不在增加
                mIsMove = false;
                //问了等下判断不再进入当前条件
                mMoveHeigh += mMaxModeHeigh / mMoveCount;

                mHandler.postDelayed(mRunnable, 600);
            } else {
                //移动次数加1
                mMoveIndex++;
                //是否要增加移动距离
                if (mIsMove) {
                    mMoveHeigh += mMaxModeHeigh / mMoveCount;
                    postInvalidate();
                } else {
                    mIsMove = true;
                    postInvalidate();
                }
            }
        }
    };

    private int dp2px(float value) {
        return (int) (mContext.getResources().getDisplayMetrics().density * value + 0.5);
    }
}
