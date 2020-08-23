package com.example.doudlearmy;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements Runnable {



        // maximum of 2 pointer
        final int MAX_POINT_CNT = 2;

        private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);//paint object
        float[] x = new float[MAX_POINT_CNT];
        float[] y = new float[MAX_POINT_CNT];
        boolean[] isTouch = new boolean[MAX_POINT_CNT];//first pointer touch

        float[] x_last = new float[MAX_POINT_CNT];
        float[] y_last = new float[MAX_POINT_CNT];
        boolean[] isTouch_last = new boolean[MAX_POINT_CNT];//2nd pointer touch


        Thread thread = null;
        SurfaceHolder surfaceHolder;
        volatile boolean running = false;

        volatile boolean touched = false;
        volatile float touched_x, touched_y;

        public GameView(Context context) {
            super(context);

            surfaceHolder = getHolder();

        }

        public void resume(){
            running = true;
            thread = new Thread(this);
            thread.start();
        }

        public void pause(){
            boolean retry = true;
            running = false;
            while(retry){
                try {
                    thread.join();
                    retry = false;
                } catch (InterruptedException e) {

                    e.printStackTrace();
                }
            }
        }

        @Override
        public void run() {

            while(running){
                     draw();
            }
        }

        public void draw(){
            if(surfaceHolder.getSurface().isValid()){
                Canvas canvas = surfaceHolder.lockCanvas();
                //drawing canvas

                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(1);

                if(isTouch[0]){
                    if(isTouch_last[0]){
                        paint.setStyle(Paint.Style.STROKE);
                        paint.setStrokeWidth(5);
                        paint.setColor(Color.RED);
                        canvas.drawLine(x_last[0], y_last[0], x[0], y[0], paint);
                    }
                }
                if(isTouch[1]){
                    if(isTouch_last[1]){
                        paint.setStyle(Paint.Style.STROKE);
                        paint.setStrokeWidth(5);
                        paint.setColor(Color.BLUE);
                        canvas.drawLine(x_last[1], y_last[1], x[1], y[1], paint);
                    }
                }

                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
        @Override
        public boolean onTouchEvent(MotionEvent motionEvent) {
            int pointerIndex = ((motionEvent.getAction() & MotionEvent.ACTION_POINTER_ID_MASK) >> MotionEvent.ACTION_POINTER_ID_SHIFT);
            int pointerId = motionEvent.getPointerId(pointerIndex);
            int action = (motionEvent.getAction() & MotionEvent.ACTION_MASK);
            int pointCnt = motionEvent.getPointerCount();//The getPointerCount() method on MotionEvent allows you
            // to determine the number of pointers on the device.
            // All events and the position of the pointers are included in the instance of MotionEvent
            // which you receive in the onTouch() method.

            if (pointCnt <= MAX_POINT_CNT){
                if (pointerIndex <= MAX_POINT_CNT - 1){

                    for (int i = 0; i < pointCnt; i++) {
                        int id = motionEvent.getPointerId(i);
                        x_last[id] = x[id];
                        y_last[id] = y[id];
                        isTouch_last[id] = isTouch[id];
                        x[id] = motionEvent.getX(i);
                        y[id] = motionEvent.getY(i);
                    }

                    switch (action){
                        case MotionEvent.ACTION_DOWN:
                            isTouch[pointerId] = true;
                            break;
                        case MotionEvent.ACTION_POINTER_DOWN:
                            isTouch[pointerId] = true;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            isTouch[pointerId] = true;
                            break;
                        case MotionEvent.ACTION_UP:
                            isTouch[pointerId] = false;
                            isTouch_last[pointerId] = false;
                            break;
                        case MotionEvent.ACTION_POINTER_UP:
                            isTouch[pointerId] = false;
                            isTouch_last[pointerId] = false;
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            isTouch[pointerId] = false;
                            isTouch_last[pointerId] = false;
                            break;
                        default:
                            isTouch[pointerId] = false;
                            isTouch_last[pointerId] = false;
                    }
                }
            }

            return true;
        }

}



