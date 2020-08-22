package com.example.doudlearmy;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.graphics.Point;

import android.view.Display;
import android.view.MotionEvent;
import android.view.View;


public class Activity2 extends AppCompatActivity implements View.OnTouchListener {
    private GameView gameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Getting display object
        super.onCreate(savedInstanceState);
     //  gameView.setOnTouchListener(this);
//        Display display = getWindowManager().getDefaultDisplay();
//
//        //Getting the screen resolution into point object
//        Point size = new Point();
//        display.getSize(size);

        //Initializing game view object
        //this time we are also passing the screen size to the GameView constructor
        // gameView = new GameView(this, size.x, size.y);

        //adding it to contentview
        gameView=new GameView(this);
        setContentView(gameView);
    }
    //pausing the game when activity is paused


    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }

    @Override
    public boolean onTouch(View view, MotionEvent me) {
        return false;
    }
}