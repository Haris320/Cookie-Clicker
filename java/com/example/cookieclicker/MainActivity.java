package com.example.cookieclicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;


public class MainActivity extends AppCompatActivity implements Animation.AnimationListener {
    ImageView cookie;
    ImageView grandma;
    ImageView grandmaupgrade;
    ConstraintLayout root;
    TextView oneplus;
    TextView cps;
    TextView dollar;
    TextView grandmacounttxt;
    Button upgrade;
    Boolean enabled = false;
    //Boolean restartgame = false;
    final String dollarkey = "yoink my code and ill find u";
    final String numberkey = "hello";
    final String grandcostkey = "pog";

    AtomicInteger money = new AtomicInteger(0);
    AtomicInteger grandmanum = new AtomicInteger(0);
    AtomicInteger grandmacost = new AtomicInteger(0);
    AtomicBoolean restartgame = new AtomicBoolean(false);

    private Animation fadeInAnimation;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            Objects.requireNonNull(getSupportActionBar()).hide();
        }
        root = findViewById(R.id.id_layout);
        dollar = findViewById(R.id.dollars);
        cookie = findViewById(R.id.cookie);
        upgrade = findViewById(R.id.upgrade);
        cps = findViewById(R.id.cps);
        grandmacounttxt = findViewById(R.id.grandmaTextCount);
        grandmaupgrade = findViewById(R.id.grandmaupgrade);
        grandmaupgrade.setImageResource(R.drawable.grandma);

        //getScore();

        if(grandmanum.get()==0&&money.get()==0){
            grandmaupgrade.setVisibility(View.INVISIBLE);
            grandmacost.set(20);
            upgrade.setEnabled(false);
            dollar.setText("Cookies: "+money.get());
        }

        if(money.get()<grandmacost.get()-1){
            upgrade.setEnabled(false);
        }


        AnimationDrawable animationDrawable = (AnimationDrawable) root.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(1500);

        animationDrawable.start();



        cookie.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            public void onClick(View v)
            {
                restartgame.set(false);
                oneplus = new TextView(MainActivity.this);
                oneplus.setTextSize(30);

                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(root);
                constraintSet.connect(oneplus.getId(), ConstraintSet.BOTTOM, cookie.getId(), ConstraintSet.BOTTOM);
                constraintSet.connect(oneplus.getId(), ConstraintSet.LEFT, cookie.getId(), ConstraintSet.LEFT);
                constraintSet.connect(oneplus.getId(), ConstraintSet.TOP, root.getId(), ConstraintSet.TOP);
                constraintSet.connect(oneplus.getId(), ConstraintSet.RIGHT, cookie.getId(), ConstraintSet.RIGHT);

                float horizontalBias = (float)Math.random();
                float verticalBias = (float)((Math.random()* 0.5) + 0.5);

                constraintSet.setHorizontalBias(oneplus.getId(), horizontalBias);
                constraintSet.setVerticalBias(oneplus.getId(), verticalBias);

                constraintSet.applyTo(root);

                AlphaAnimation fade = new AlphaAnimation(1f, 0f);
                fade.setDuration(1000);
                oneplus.setId(View.generateViewId());
                oneplus.setText("+1");
                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
                oneplus.setLayoutParams(params);
                root.addView(oneplus);
                Log.d("TAG_CHILDS", String.valueOf(root.getChildCount()));

                fade.setFillAfter(true);
                float xval = (float)((Math.random()* 600) + 200);
                float yval = (float)((Math.random()* 700) + 400);
                TranslateAnimation translate = new TranslateAnimation(xval, xval, yval, 0);
                translate.setDuration(1000);
                translate.setFillAfter(true);

                AnimationSet animations = new AnimationSet(false);
                animations.addAnimation(fade);
                animations.addAnimation(translate);
                animations.setDuration(1000);

                ScaleAnimation scaleAnimation = new ScaleAnimation(1.0f,1.05f,1.0f,1.05f, Animation.RELATIVE_TO_SELF,.5f,Animation.RELATIVE_TO_SELF, .5f);
                scaleAnimation.setDuration(100);
                cookie.startAnimation(scaleAnimation);
                oneplus.startAnimation(animations);
                onAnimationEnd(animations);

                money.incrementAndGet();
                dollar.setText("Cookies: "+money.get());
                if(money.get()>grandmacost.get()-1){
                    upgrade.setEnabled(true);
                }
            }//onClick

        });



        upgrade.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                enabled = false;
                money.set(money.get()-grandmacost.get());
                dollar.setText("Cookies: "+money.get());
                int cost = (int) (grandmacost.get()*.5);
                grandmacost.addAndGet(cost);
                grandmanum.incrementAndGet();
                grandma = new ImageView(MainActivity.this);
                grandma.setId(View.generateViewId());
                grandma.setImageResource(R.drawable.grandma);

                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT);
                grandma.setLayoutParams(params);

                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(root);
                root.addView(grandma);
                constraintSet.connect(grandma.getId(), ConstraintSet.TOP, root.getId(), ConstraintSet.TOP);
                constraintSet.connect(grandma.getId(), ConstraintSet.RIGHT, root.getId(), ConstraintSet.RIGHT);
                constraintSet.connect(grandma.getId(), ConstraintSet.BOTTOM, root.getId(), ConstraintSet.BOTTOM);
                constraintSet.connect(grandma.getId(), ConstraintSet.LEFT, root.getId(), ConstraintSet.LEFT);

                AlphaAnimation fade = new AlphaAnimation(1f, 0f);
                fade.setDuration(1000);


                constraintSet.applyTo(root);
                AnimationSet animations = new AnimationSet(false);
                animations.addAnimation(fade);
                TranslateAnimation translate = new TranslateAnimation(0, 0, 200, -200);
                translate.setDuration(1000);
                translate.setFillAfter(true);
                animations.addAnimation(translate);
                animations.setDuration(1000);
                grandma.startAnimation(animations);
                grandma.animate().rotationBy(720).setDuration(300);
                onAnimationEnd(animations);

                upgrade.setText("Buy Grandma ("+grandmacost.get()+" Cookies)");
                cps.setText("CPS: "+grandmanum.get()*3);
                upgrade.setEnabled(false);
                grandmaupgrade.setVisibility(View.VISIBLE);
                grandmacounttxt.setText("x"+grandmanum.get());

                grandmatimer();

            }
        });

    }

    public void grandmatimer(){
        new Thread(){
            public void run() {
                    TimerTask task = new TimerTask() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            money.addAndGet(3);
                            dollar.setText("Cookies: " + money.get());
                            saveScore();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (money.get() > grandmacost.get() - 1) {
                                        upgrade.setEnabled(true);
                                    }
                                }
                            });
                        }
                    };
                    Timer timer = new Timer();

                    timer.scheduleAtFixedRate(task, 1000, 1000);


            }
        }.start();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        root.post(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        root.removeView(oneplus);
                        root.removeView(grandma);
                    }
                });
            }
        });
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }


    public void saveScore(){
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(dollarkey, money.get());
        editor.putInt(grandcostkey, grandmacost.get());
        editor.putInt(numberkey, grandmanum.get());
        editor.apply();
    }

    @SuppressLint("SetTextI18n")
    public void getScore(){
        int score = this.getPreferences(Context.MODE_PRIVATE).getInt(dollarkey, 0);
        int num = this.getPreferences(Context.MODE_PRIVATE).getInt(numberkey, 0);
        int cost = this.getPreferences(Context.MODE_PRIVATE).getInt(grandcostkey, 20);

        money.set(score);

        grandmacost.set(20);
        grandmanum.set(0);
        Log.d("TAG_INFO_M", String.valueOf(money.get()));
        Log.d("TAG_INFO_C", String.valueOf(grandmacost.get()));
        Log.d("TAG_INFO_N", String.valueOf(grandmanum.get()));
        this.dollar.setText("Cookies: " + money.get());
        this.cps.setText("CPS: 0");
        this.grandmacounttxt.setText("");
        grandmaupgrade.setVisibility(View.INVISIBLE);
        if(grandmanum.get()>0){
            this.grandmacounttxt.setText("");
            grandmaupgrade.setVisibility(View.INVISIBLE);
            upgrade.setText("Buy Grandma ("+grandmacost.get()+" Cookies)");
        }

    }


}