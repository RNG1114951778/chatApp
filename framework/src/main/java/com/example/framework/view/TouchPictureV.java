package com.example.framework.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.framework.R;

/**
 * FileName : TouchPictureV
 * Founder  : jyt
 * Create Date : 2020/9/3 11:47 AM
 * Profile :图片验证
 */
public class TouchPictureV extends View {

    //背景 和 背景画笔
    private Bitmap bgBitmap;
    private Paint mPaintbg;

    //空白块 和 空白块画笔
    private Bitmap  mNullBitmap;
    private Paint mPaintNull;

    //移动方块 和 移动画笔
    private Bitmap mMoveBitmap;
    private Paint mPaintMove;

    //View的宽高
    private int mWidth;
    private int mHeight;

    //方块大小
    private int CARD_SIZE = 200;
    //方块坐标
    private int LINE_W,LINE_H = 0;

    //移动的方块的坐标
    private int moveX = 200;
    private int errorValues = 10;

    private OnViewResultListener viewResultListener;

    boolean ifinit = false;

    public void setViewResultListener(OnViewResultListener viewResultListener) {
        this.viewResultListener = viewResultListener;
    }

    public TouchPictureV(Context context) {
        super(context);
        init();
    }

    public TouchPictureV(Context context,  AttributeSet attrs) {
        super(context,attrs);
        init();
    }

    public TouchPictureV(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs,defStyleAttr);
        init();

    }

    private void init(){
        mPaintbg = new Paint();
        mPaintNull = new Paint();
        mPaintMove = new Paint();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mHeight = h;
        mWidth = w;
    }


    //绘制
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBg(canvas);
        drawNullCard(canvas);
        drawMovedCard(canvas);
        
    }

    private void drawMovedCard(Canvas canvas) {
        //截取空白块位置坐标的Bitmap图像
        mMoveBitmap = Bitmap.createBitmap(bgBitmap,LINE_W,LINE_H,CARD_SIZE,CARD_SIZE);
        canvas.drawBitmap(mMoveBitmap,moveX,LINE_H,mPaintMove);


    }


    //绘制空白块
    private void drawNullCard(Canvas canvas) {


        //获取图片
        mNullBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.img_null_card);
        //2.计算大小
        CARD_SIZE =  mNullBitmap.getWidth();
        LINE_W = mWidth/3 * 2 ;
        LINE_H = mHeight/2 - (CARD_SIZE/2);

        //3.draw
        canvas.drawBitmap(mNullBitmap,LINE_W,LINE_H,mPaintNull);


    }


    //绘制验证码背景
    private void drawBg(Canvas canvas) {
        Log.d("111","aaa");
        //1.获取图片
        Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.img_bg);
        //2.创建一个bitmap
        bgBitmap = Bitmap.createBitmap(mWidth,mHeight, Bitmap.Config.ARGB_8888);
        //3.将图片绘制到空的bitmap
        Canvas bgCanvas = new Canvas(bgBitmap);
        bgCanvas.drawBitmap(mBitmap,null,new Rect(0,0,mWidth,mHeight),mPaintbg);
        //4.将bgBitmap绘制到View上
        canvas.drawBitmap(bgBitmap,null,new Rect(0,0,mWidth,mHeight),mPaintbg);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                if(event.getX() > 200 && event.getX() <( 200 + CARD_SIZE ) &&
                        event.getY() > (mHeight - CARD_SIZE) / 2 && event.getY() < (mHeight + CARD_SIZE) / 2){
                        ifinit = true;


                }
                break;
            case MotionEvent.ACTION_MOVE:

                //防止越界
                if (event.getX() > 0 && event.getX() < (mWidth - CARD_SIZE) && ifinit == true){
                    moveX = (int) event.getX();


                    invalidate();
                }
                break;

            case MotionEvent.ACTION_UP:

                if(moveX > (LINE_W - errorValues) && moveX < (LINE_W + errorValues)){

                    if(viewResultListener != null){
                        viewResultListener.onResult();
                    }
                }

                ifinit = false;

                moveX = 200;
                invalidate();
                break;


        }

        return true;
    }

    public interface OnViewResultListener{
        void onResult();
    }


}
