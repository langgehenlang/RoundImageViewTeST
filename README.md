> 之前为了图方便和赶工期，所以直接使用早期的工具类去裁剪。后期的话有一些时间，所以做了一些优化，就自定义了一个圆角`ImageView`。其实大概思路都是清楚的，但是想看下网上有没有其他好的方便的，结果一查，跟我的思路基本一样，就是在`onDrawable`中做裁剪，但是代码一大推，也是醉了。后面看到一个挺有意思的思路，是通过`clipPath`实现的。

使用裁剪的代码：
```java
//RoundedBitmapDrawable是基于glide的
public static Drawable bitmapToRound(Context context, Bitmap bitmap, int resid) {
    if (bitmap != null) {
        int radius = 0;
        try {
            radius = context.getResources().getDimensionPixelOffset(resid);

            RoundedBitmapDrawable circularBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
            circularBitmapDrawable.setCornerRadius(radius); //设置圆角弧度
            return circularBitmapDrawable;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return drawableToRound(context, new BitmapDrawable(bitmap), radius);
    }

    return null;
}
```

自定义`ImageView`核心代码：
```java
@Override
protected void onDraw(Canvas canvas) {
     if (mLeftTopRadius > 0 || mLeftBottomRadius > 0
             || mRightTopRadius > 0 || mRightBottomRadius > 0) {
         try {
             Drawable drawable = getDrawable();
             int w = this.getWidth();
             int h = this.getHeight();
//                LogUtils.d(drawable + " " + w + " " + h);
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
```
上图中，我自定义了四个角，所以其实是可以实现单独设置的。
但是，这里有一个问题，就是这块只能修改`ImageView`的`src`图片，并不能让`Background`也是圆角。
这个问题是因为，当执行`onDraw`时，`Background`早已经在`draw`中绘制了（`View.draw.drawBackground`），故我们如果想让`Background`也是圆角，可以在`draw`方法中实现
```java
@Override
public void draw(Canvas canvas) {
    if (mLeftTopRadius > 0 || mLeftBottomRadius > 0
            || mRightTopRadius > 0 || mRightBottomRadius > 0) {
        try {
            Drawable drawable = getDrawable();
            int w = this.getWidth();
            int h = this.getHeight();
//                LogUtils.d(drawable + " " + w + " " + h);
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
    super.draw(canvas);
}
```
对了，切记使用`clipPath`针对的是画布`canvas`，不是针对图片。所如果你的图片（不管是`src`或`Background`），如果跟绘制面积比画布小，是不会有圆角效果的。
