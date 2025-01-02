package com.example.switchsnake;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements Runnable {

    private int width = 7;
    private int height = 20;

    private SnakeDirection snakeDirection = SnakeDirection.LEFT;
    private boolean isGamePlaying = false;

    private int[] snake_x= new int[1001];
    private int[] snake_y= new int[1001];

    public int len=1;
    public int food_x=-1, food_y=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        build();
        startGame();
    }

    private void build() {
        LinearLayout mainLayout = findViewById(R.id.main_layout);

        for (int i = 0; i < height; i++) {
            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.weight = 1;
            layout.setLayoutParams(layoutParams);

            for (int j = 0; j < width; j++) {
                SwitchCompat singleSwitch = new SwitchCompat(this);
                LinearLayout.LayoutParams switchParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                switchParams.weight = 1;
                singleSwitch.setLayoutParams(layoutParams);
                singleSwitch.setId(i * 100 + j);
                singleSwitch.setClickable(false);

                layout.addView(singleSwitch);
            }
            mainLayout.addView(layout);
        }
    }

    private void startGame() {
        if (isGamePlaying) {
            return;
        }
        Random random=new Random();
        int x=random.nextInt(height);
        int y=random.nextInt(width);

        snake_x[0]=x;
        snake_y[0]=y;
        len = 1;

        isGamePlaying = true;
        new Thread(this).start();
    }

    private boolean isPlaceEmpty(int x, int y){
        if(x<0 || y<0 || x>=height || y>=width)return false;
        for(int i=0;i<len;i++){
            if(x==snake_x[i] && y==snake_y[i])return false;
        }
        return true;
    }

    public void spawnFood(){
        Random random=new Random();
        int x=random.nextInt(height);
        int y=random.nextInt(width);

        if(!isPlaceEmpty(x,y)){spawnFood();return;}
        food_x=x;
        food_y=y;
    }

    public void goLeft(){
        if(!isPlaceEmpty(snake_x[0],snake_y[0]-1)) {
            showGameOver();
            return;
        }

        if(food_x==snake_x[0]-1 && food_y==snake_y[0]){
            for(int i=len;i>0;i--){
                snake_x[i]=snake_x[i-1];
                snake_y[i]=snake_y[i-1];
            }
            snake_x[0]=food_x;
            snake_y[0]=food_y;
            len++;
            food_x=-1;
            food_y=-1;

        } else {
            for(int i=len-1;i>0;i--){
                snake_x[i]=snake_x[i-1];
                snake_y[i]=snake_y[i-1];
            }
            snake_x[0]--;
        }
    }

    public void goRight(){
        if (!isPlaceEmpty(snake_x[0],snake_y[0]+1)) {
            showGameOver();
            return;
        }

        if(food_x==snake_x[0]+1 && food_y==snake_y[0]){
            for(int i=len;i>0;i--){
                snake_x[i]=snake_x[i-1];
                snake_y[i]=snake_y[i-1];
            }
            snake_x[0]=food_x;
            snake_y[0]=food_y;
            len++;
            food_x=-1;
            food_y=-1;
        } else {

            for(int i=len-1;i>0;i--){
                snake_x[i]=snake_x[i-1];
                snake_y[i]=snake_y[i-1];
            }
            snake_x[0]++;
        }
    }

    public void goUp(){
        if(!isPlaceEmpty(snake_x[0]-1,snake_y[0])) {
            showGameOver();
            return;
        }

        if(food_x==snake_x[0] && food_y==snake_y[0]+1){
            for(int i=len;i>0;i--){
                snake_x[i]=snake_x[i-1];
                snake_y[i]=snake_y[i-1];
            }
            snake_x[0]=food_x;
            snake_y[0]=food_y;
            len++;
            food_x=-1;
            food_y=-1;
        } else {
            for(int i=len-1;i>0;i--){
                snake_x[i]=snake_x[i-1];
                snake_y[i]=snake_y[i-1];
            }
            snake_y[0]++;
        }
    }

    public void goDown(){
        if(!isPlaceEmpty(snake_x[0]+1,snake_y[0])) {
            showGameOver();
            return;
        }

        if(food_x==snake_x[0] && food_y==snake_y[0]-1){
            for(int i=len;i>0;i--){
                snake_x[i]=snake_x[i-1];
                snake_y[i]=snake_y[i-1];
            }
            snake_x[0]=food_x;
            snake_y[0]=food_y;
            len++;
            food_x=-1;
            food_y=-1;
        } else {

            for(int i=len-1;i>0;i--){
                snake_x[i]=snake_x[i-1];
                snake_y[i]=snake_y[i-1];
            }
            snake_y[0]--;
        }
    }

    private void showGameOver() {
        isGamePlaying = false;
        Toast.makeText(this, "AAAA", Toast.LENGTH_SHORT).show();
        try {
            Thread.sleep(2500L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        startGame();
    }

    public void draw(){
        for(int i=0;i<height;i++)for(int j=0;j<width;j++) {
            if(i==food_x && j==food_y)continue;

            boolean f=false;
            for(int k=0;k<len;k++) if(snake_x[k]==i && snake_y[k]==j){f=true;break;}
            if(f)continue;

            SwitchCompat x = findViewById(i*100+j);
            x.setChecked(false);
            x.setThumbTintList(ColorStateList.valueOf(Color.GRAY));
        }

        if(food_x!=-1 && food_y!=-1) {
            SwitchCompat x = findViewById(food_x*100+food_y);
            x.setChecked(true);
            x.setThumbTintList(ColorStateList.valueOf(Color.RED));
        }


        for(int i=0;i<len;i++){
            SwitchCompat x = findViewById(snake_x[i]*100+snake_y[i]);
            x.setChecked(true);
            x.setThumbTintList(ColorStateList.valueOf(Color.GREEN));
        }


    }
    public void score(){
        TextView tv=findViewById(R.id.textView);
        tv.setText("Length: "+ len);
    }

    @Override
    public void run() {
        while (isGamePlaying) {
            if(food_x==-1 && food_y==-1)runOnUiThread(this::spawnFood);
            runOnUiThread(this::draw);
            runOnUiThread(this::score);

            switch (snakeDirection) {
                case LEFT:
                    runOnUiThread(this::goDown);
                    break;
                case TOP:
                    runOnUiThread(this::goRight);
                    break;
                case RIGHT:
                    runOnUiThread(this::goUp);
                    break;
                case BOTTOM:
                    runOnUiThread(this::goLeft);
                    break;
            }

//            if(food_x<snake_x[0]){
//                if(isPlaceEmpty(snake_x[0]-1,snake_y[0]))runOnUiThread(this::goLeft);
//                else if(isPlaceEmpty(snake_x[0],snake_y[0]-1))runOnUiThread(this::goDown);
//                else if(isPlaceEmpty(snake_x[0],snake_y[0]+1))runOnUiThread(this::goUp);
//                else if(isPlaceEmpty(snake_x[0]+1,snake_y[0]))runOnUiThread(this::goRight);
//            }
//            else if(food_x>snake_x[0]){
//                if(isPlaceEmpty(snake_x[0]+1,snake_y[0]))runOnUiThread(this::goRight);
//                else if(isPlaceEmpty(snake_x[0],snake_y[0]-1))runOnUiThread(this::goDown);
//                else if(isPlaceEmpty(snake_x[0],snake_y[0]+1))runOnUiThread(this::goUp);
//                else if(isPlaceEmpty(snake_x[0]-1,snake_y[0]))runOnUiThread(this::goLeft);
//            }
//
//            else if(food_y>snake_y[0]){
//                if(isPlaceEmpty(snake_x[0],snake_y[0]+1))runOnUiThread(this::goUp);
//                else if(isPlaceEmpty(snake_x[0]+1,snake_y[0]))runOnUiThread(this::goRight);
//                else if(isPlaceEmpty(snake_x[0]-1,snake_y[0]))runOnUiThread(this::goLeft);
//                else if(isPlaceEmpty(snake_x[0],snake_y[0]-1))runOnUiThread(this::goDown);
//            }
//            else if(food_y<snake_y[0]){
//                if(isPlaceEmpty(snake_x[0],snake_y[0]-1))runOnUiThread(this::goDown);
//                else if(isPlaceEmpty(snake_x[0]+1,snake_y[0]))runOnUiThread(this::goRight);
//                else if(isPlaceEmpty(snake_x[0]-1,snake_y[0]))runOnUiThread(this::goLeft);
//                else if(isPlaceEmpty(snake_x[0],snake_y[0]+1))runOnUiThread(this::goUp);
//            }

            try {
                Thread.sleep(333);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private float startX = 0f;
    private float startY = 0f;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startX = event.getX();
            startY = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_UP) {
            float offsetX = event.getX() - startX;
            float offsetY = event.getY() - startY;

            if (Math.abs(offsetX) > Math.abs(offsetY)) {
                snakeDirection = offsetX > 0 ? SnakeDirection.RIGHT : SnakeDirection.LEFT;
            } else {
                snakeDirection = offsetY > 0 ? SnakeDirection.BOTTOM : SnakeDirection.TOP;
            }
        }
        return true;
    }
}