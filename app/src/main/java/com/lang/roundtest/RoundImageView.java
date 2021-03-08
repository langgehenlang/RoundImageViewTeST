package com.lang.roundtest;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;


@SuppressLint("AppCompatCustomView")
public class RoundImageView extends ImageView {
    private int mLeftTopRadius = 10;
    private int mRightTopRadius = 10;
    private int mLeftBottomRadius = 10;
    private int mRightBottomRadius = 10;

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
            int radius = (int) typedArray.getDimension(R.styleable.RoundImageView_rmvRadius, 0);
            mLeftTopRadius = radius;
            mLeftBottomRadius = radius;
            mRightBottomRadius = radius;
            mRightTopRadius = radius;
            typedArray.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mLeftTopRadius > 0 || mLeftBottomRadius > 0
                || mRightTopRadius > 0 || mRightBottomRadius > 0) {
            try {
                Drawable drawable = getDrawable();
                int w = this.getWidth();
                int h = this.getHeight();
                if (null != drawable /*&& drawable instanceof BitmapDrawable*/
                        && w > 0 && h > 0) {
                    //https://www.cnblogs.com/zhujiabin/p/7403753.html
                    //https://cloud.tencent.com/developer/article/1331001
                    Path path = new Path();
                    /*圆角的半径，依次为左上角xy半径，右上角，右下角，左下角*/
                    float[] rids = {mLeftTopRadius, mLeftTopRadius, mRightTopRadius, mRightTopRadius, mRightBottomRadius, mRightBottomRadius, mLeftBottomRadius, mLeftBottomRadius};
                    path.addRoundRect(new RectF(0, 0, w, h), rids, Path.Direction.CW);
                    canvas.clipPath(path);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onDraw(canvas);
    }

//    @Override
//    public void draw(Canvas canvas) {
////        if (mLeftTopRadius > 0 || mLeftBottomRadius > 0
////                || mRightTopRadius > 0 || mRightBottomRadius > 0) {
////            try {
////                Drawable drawable = getBackground();
////                int w = this.getWidth();
////                int h = this.getHeight();
////                if (null != drawable /*&& drawable instanceof BitmapDrawable*/
////                        && w > 0 && h > 0) {
////                    //https://www.cnblogs.com/zhujiabin/p/7403753.html
////                    //https://cloud.tencent.com/developer/article/1331001
////                    Path path = new Path();
////                    /*圆角的半径，依次为左上角xy半径，右上角，右下角，左下角*/
////                    float[] rids = {mLeftTopRadius, mLeftTopRadius, mRightTopRadius, mRightTopRadius, mRightBottomRadius, mRightBottomRadius, mLeftBottomRadius, mLeftBottomRadius};
////                    path.addRoundRect(new RectF(0, 0, w, h), rids, Path.Direction.CW);
////                    canvas.clipPath(path);
////                }
////            } catch (Exception e) {
////                e.printStackTrace();
////            }
////        }
//        super.draw(canvas);
//    }


    public void setRadius(int radius) {
        this.mLeftTopRadius = radius;
        this.mLeftBottomRadius = radius;
        this.mRightTopRadius = radius;
        this.mRightBottomRadius = radius;
    }


}
