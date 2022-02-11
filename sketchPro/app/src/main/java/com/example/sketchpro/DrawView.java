package com.example.sketchpro;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class DrawView extends View {

    private static final float TOUCH_TOLERANCE = 4;
    private float mX, mY;
    private Path mPath;
    public int id;

    private Paint mPaint1, shadowPaint;

    private ArrayList<Stroke> paths = new ArrayList<>();
    private ArrayList<Stroke> pathcircle = new ArrayList<>();
    private ArrayList<Stroke> pathrect = new ArrayList<>();
    private ArrayList<Stroke> pathtri = new ArrayList<>();

    private int currentColor;
    private int strokeWidth;
    private int strokeSize;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    private Stroke cp;
    private Stroke rp;
    private Stroke tp;

    public DrawView(Context context) {
        this(context, null);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint1 = new Paint();

        // the below methods smoothens
        // the drawings of the user
        mPaint1.setAntiAlias(true);
        mPaint1.setDither(true);
        mPaint1.setColor(Color.GREEN);
        mPaint1.setStyle(Paint.Style.STROKE);
        mPaint1.setStrokeJoin(Paint.Join.ROUND);
        mPaint1.setStrokeCap(Paint.Cap.ROUND);

        // 0xff=255 in decimal
        mPaint1.setAlpha(0xff);

    }


//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int minw = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
//        int w = resolveSizeAndState(minw, widthMeasureSpec, 1);
//
//        // Whatever the width ends up being, ask for a height that would let the pie
//        // get as big as it can
//        int minh = MeasureSpec.getSize(w) - (int) strokeWidth + getPaddingBottom() + getPaddingTop();
//        int h = resolveSizeAndState(MeasureSpec.getSize(w) - (int) strokeWidth, heightMeasureSpec, 0);
//
//        setMeasuredDimension(w, h);
//    }

    public void init(int height, int width, int id) {

        mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);

        // set an initial color of the brush
        currentColor = Color.GREEN;

        this.id = id;

        // set an initial brush size
        strokeWidth = 20;
        strokeSize = 30;


    }



    // sets the current color of stroke
    public void setColor(int color) {
        currentColor = color;
    }

    // sets the stroke width
    public void setStrokeWidth(int width) {
        strokeWidth = width;
    }

    public void  setStrokeSize(int size){
        strokeSize = size;
    }

    public void undo() {
        // check whether the List is empty or not
        // if empty, the remove method will return an error
        if (paths.size() != 0) {
            paths.remove(paths.size() - 1);
            invalidate();
        }
    }

//    public Bitmap save() {
//        return mBitmap;
//    }


    public void drawCircles(Stroke stroke, Canvas canva){
        // Use Color.parseColor to define HTML colors
        mPaint1.setColor(stroke.color);
        mPaint1.setStrokeWidth(5);
//        canva.translate(getWidth()/2f,getHeight()/2f);
        canva.drawCircle(stroke.x, stroke.y, stroke.radius * 2, mPaint1);
    }


    public void drawRect(Stroke stroke, Canvas canva){
        mPaint1.setColor(stroke.color);
        mPaint1.setStrokeWidth(5);
        canva.drawRect(stroke.x, stroke.y, stroke.x + stroke.radius * 6, stroke.y + stroke.radius * 4, mPaint1);
    }



    public void drawTriangle(Canvas canvas, Stroke stroke, float x, float y, float width) {
        float halfWidth = width * 4 / 2;

        Path path = new Path();
        path.moveTo(x, y - halfWidth);
        path.lineTo(x - halfWidth, y + halfWidth);
        path.lineTo(x + halfWidth, y + halfWidth);
        path.lineTo(x, y - halfWidth);
        path.close();
        mPaint1.setColor(stroke.color);
        mPaint1.setStrokeWidth(5);

        canvas.drawPath(path, mPaint1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();

        int backgroundColor = Color.WHITE;



            mCanvas.drawColor(backgroundColor);
            // now, we iterate over the list of paths
            // and draw each path on the canvas

            for (Stroke fp : paths) {
                mPaint1.setColor(fp.color);
                mPaint1.setStrokeWidth(fp.strokeWidth);
                mCanvas.drawPath(fp.path, mPaint1);


            }
        for(Stroke cp: pathcircle){
            drawCircles(cp, mCanvas);
        }

        for(Stroke tp: pathtri){
            drawTriangle(mCanvas, tp, tp.x, tp.y, tp.radius);
        }

        for (Stroke rp: pathrect){
            drawRect(rp, mCanvas);
        }

        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
        canvas.restore();

    }

    private void touchStart(float x, float y) {
        mPath = new Path();
        Stroke fp = new Stroke(currentColor, strokeWidth, mPath);
        cp = new Stroke(currentColor, strokeWidth, x, y, strokeSize);
        rp = new Stroke(currentColor, strokeWidth, x, y, strokeSize);
        tp = new Stroke(currentColor, strokeWidth, x, y, strokeSize);
        if(id == 1) {
            paths.add(fp);
        }
        if(id ==2) {
            pathcircle.add(cp);
        }if(id == 3){
            pathrect.add(rp);
        }if(id == 4){
            pathtri.add(tp);
        }

        // finally remove any curve
        // or line from the path
        mPath.reset();

        // this methods sets the starting
        // point of the line being drawn
        mPath.moveTo(x, y);

        // we save the current
        // coordinates of the finger
        mX = x;
        mY = y;
    }

    private void touchMove(float x, float y) {
        switch (id){
            case 1:
                float dx = Math.abs(x - mX);
                float dy = Math.abs(y - mY);

                if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                    mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                    mX = x;
                    mY = y;
                }
                break;
            case 2:
                cp.x = x;
                cp.y = y;
                break;
            case 3:
                rp.x = x;
                rp.y = y;
                break;
            case 4:
                tp.x = x;
                tp.y = y;
                break;
        }


    }

    private void touchUp() {
        if(id ==1) {
            mPath.lineTo(mX, mY);
        }
        else if(id == 2) {

        }


    }

    private static final String TAG = "DrawView";

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "onTouchEvent: " + id);
                touchStart(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d(TAG, "onTouchEvent: " + id);
                touchMove(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                Log.d(TAG, "onTouchEvent: " + id);
                touchUp();
                invalidate();
                break;
        }
        return true;
    }

}
