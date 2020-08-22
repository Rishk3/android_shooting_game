package com.example.doudlearmy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.inline.InlineContentView;

import androidx.annotation.NonNull;

public class GameView extends SurfaceView implements Runnable {

    Thread gamethread= null;
    SurfaceHolder holder;
    boolean playing=false;
    Bitmap bb;
    Canvas cc;
    Paint paint;
    //frame rate
    long fps;
    Player player;
    // This is used to help calculate the fps
    private long timeThisFrame;

    boolean isMoving = false;

    float walkSpeedPerSecond = 150;

    float bobXPosition = 10;
    float facePosition_X=150;
    float facePosition_Y=80;
    float stomachPosition_X=100;
    float stomachPosition_Y=200;
    float leftArmPosition_X=50;
    float leftArmPosition_Y=110;
    float rightArmPosition_X=200;
    float rightArmPosition_Y=110;
    float leftLegPosition_X=120;
    float leftLegPosition_Y=200;
    float rightLegPosition_X=160;
    float rightLegPosition_Y=200;
    public GameView(Context context)
    {
        super(context);
        holder=getHolder();
        paint = new Paint();
        player=new Player();
        bb= BitmapFactory.decodeResource(getResources(),R.drawable.burger);
    }


    @Override
    public void run() {
        while (playing==true){
            long startFrameTime = System.currentTimeMillis();
            update();
            draw();

            timeThisFrame = System.currentTimeMillis() - startFrameTime;
            if (timeThisFrame > 0) {
                fps = 1000 / timeThisFrame;
            }

        }

    }
    public void update() {

        // If bob is moving (the player is touching the screen)
        // then move him to the right based on his target speed and the current fps.
        if(isMoving){
            bobXPosition = bobXPosition + (walkSpeedPerSecond / fps);
            facePosition_X=facePosition_X+(walkSpeedPerSecond / fps);
            stomachPosition_X=stomachPosition_X+(walkSpeedPerSecond / fps);
            leftArmPosition_X=leftArmPosition_X+(walkSpeedPerSecond / fps);
            rightArmPosition_X=rightArmPosition_X+(walkSpeedPerSecond / fps);
            leftLegPosition_X=leftLegPosition_X+(walkSpeedPerSecond / fps);
            rightLegPosition_X=rightLegPosition_X+(walkSpeedPerSecond / fps);
        }

    }
    public void draw() {

        // Make sure our drawing surface is valid or we crash
        if (holder.getSurface().isValid()) {
            // Lock the canvas ready to draw
            // Make the drawing surface our canvas object
            cc = holder.lockCanvas();

            // Draw the background color
            cc.drawColor(Color.argb(255,  26, 128, 182));

            // Choose the brush color for drawing
            paint.setColor(Color.argb(255,  249, 129, 0));

            // Make the text a bit bigger
            paint.setTextSize(45);

            // Display the current fps on the screen
            cc.drawText("FPS:" + fps, 20, 800, paint);

            // Draw bob at bobXPosition, 200 pixels
            // cc.drawBitmap(bb, bobXPosition, 200, paint);
            paint.setColor(Color.argb(200,  109, 229, 106));

            cc.drawCircle(facePosition_X,facePosition_Y,30,paint);//face
            paint.setColor(Color.argb(200,  209, 9, 5));
            cc.drawRect(stomachPosition_X,stomachPosition_Y-100,stomachPosition_X+100,stomachPosition_Y,paint);//stomach
            paint.setColor(Color.argb(200,  255, 9, 16));
            cc.drawRect(leftArmPosition_X,leftArmPosition_Y,leftArmPosition_X+50,leftArmPosition_Y+40,paint);//left arm
            cc.drawRect(rightArmPosition_X,rightArmPosition_Y,rightArmPosition_X+50,rightArmPosition_Y+40,paint);//right arm
            paint.setColor(Color.argb(250,  255, 255, 255));
            cc.drawRect(leftLegPosition_X,leftLegPosition_Y,leftLegPosition_X+20,leftLegPosition_Y+50,paint);//left leg
            cc.drawRect(rightLegPosition_X,rightLegPosition_Y,rightLegPosition_X+20,rightLegPosition_Y+50,paint);//right leg
            // Draw everything to the screen
            // and unlock the drawing surface
            holder.unlockCanvasAndPost(cc);
        }

    }
    public  void pause(){
        playing=false;
        while(true){
            try {
                gamethread.join();
            }
            catch (InterruptedException e)
            {
                Log.e("Error:", "joining thread"); }
            break;}
        gamethread=null;
    }
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {

            // Player has touched the screen
            case MotionEvent.ACTION_DOWN:
                isMoving = true;
                break;

            // Player has removed finger from screen
            case MotionEvent.ACTION_UP:
                // Set isMoving so Bob does not move
                isMoving = false;
                break;
        }
        return true;
    }



    public void resume(){
        playing=true;
        gamethread=new Thread(this);
        gamethread.start();
    }
}
