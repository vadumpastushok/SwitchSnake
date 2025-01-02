package com.example.switchsnake;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements Runnable {

    private final int FOOD=-1;
    private final int EMPTY=0;
    private final int START_SNAKE=1;
    private final int BODY_SNAKE=2;
    private final int END_SNAKE=3;

    private int width = 10;
    private int height = 20;


    private int[] snake_x= new int[1001];
    private int[] snake_y= new int[1001];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        build();
    }


    private void build() {
        LinearLayout mainLayout = findViewById(R.id.main_layout);

        for (int i = 0; i < height; i++) {
            LinearLayout layout = new LinearLayout(getApplicationContext());
            layout.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.weight = 1;
            layout.setLayoutParams(layoutParams);


            for (int j = 0; j < width; j++) {

                Switch aSwitch = new Switch(getApplicationContext());
                LinearLayout.LayoutParams switchParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                switchParams.weight = 1;
                aSwitch.setLayoutParams(layoutParams);
                aSwitch.setId(i * 100 + j);
                aSwitch.setClickable(false);

                layout.addView(aSwitch);
            }

            mainLayout.addView(layout);

        }


        Random random=new Random();
        int x=random.nextInt(height);
        int y=random.nextInt(width);

        snake_x[0]=x;
        snake_y[0]=y;

        new Thread(this).start();

    }

    public int len=1;
    public int food_x=-1, food_y=-1;


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

        Switch z=(Switch) findViewById(100*x+y);
        z.setChecked(true);

        food_x=x;
        food_y=y;

    }


    public void goLeft(){
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

        }else{

            for(int i=len-1;i>0;i--){
                snake_x[i]=snake_x[i-1];
                snake_y[i]=snake_y[i-1];
            }

            snake_x[0]--;


        }
    }
    public void goRight(){
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
        }else{

            for(int i=len-1;i>0;i--){
                snake_x[i]=snake_x[i-1];
                snake_y[i]=snake_y[i-1];
            }

            snake_x[0]++;


        }



    }
    public void goUp(){

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
        }else{

            for(int i=len-1;i>0;i--){
                snake_x[i]=snake_x[i-1];
                snake_y[i]=snake_y[i-1];
            }

            snake_y[0]++;


        }




    }
    public void goDown(){
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
        }else{

            for(int i=len-1;i>0;i--){
                snake_x[i]=snake_x[i-1];
                snake_y[i]=snake_y[i-1];
            }

            snake_y[0]--;


        }



    }

    public void draw(){
        for(int i=0;i<height;i++)for(int j=0;j<width;j++) {
            if(i==food_x && j==food_y)continue;

            boolean f=false;
            for(int k=0;k<len;k++) if(snake_x[k]==i && snake_y[k]==j){f=true;break;}
            if(f)continue;

            Switch x=findViewById(i*100+j);
            x.setChecked(false);
        }

        if(food_x!=-1 && food_y!=-1){Switch x=findViewById(food_x*100+food_y);x.setChecked(true);}


        for(int i=0;i<len;i++){
            Switch x=findViewById(snake_x[i]*100+snake_y[i]);
            x.setChecked(true);
        }


    }
    public void score(){
        TextView tv=findViewById(R.id.textView);
        tv.setText("Length: "+ len);
    }
    @Override
    public void run() {
        while (true) {
            if(food_x==-1 && food_y==-1)runOnUiThread(this::spawnFood);

            runOnUiThread(this::draw);
            runOnUiThread(this::score);

            if(food_x<snake_x[0]){
                if(isPlaceEmpty(snake_x[0]-1,snake_y[0]))runOnUiThread(this::goLeft);
                else if(isPlaceEmpty(snake_x[0],snake_y[0]-1))runOnUiThread(this::goDown);
                else if(isPlaceEmpty(snake_x[0],snake_y[0]+1))runOnUiThread(this::goUp);
                else if(isPlaceEmpty(snake_x[0]+1,snake_y[0]))runOnUiThread(this::goRight);
            }
            else if(food_x>snake_x[0]){
                if(isPlaceEmpty(snake_x[0]+1,snake_y[0]))runOnUiThread(this::goRight);
                else if(isPlaceEmpty(snake_x[0],snake_y[0]-1))runOnUiThread(this::goDown);
                else if(isPlaceEmpty(snake_x[0],snake_y[0]+1))runOnUiThread(this::goUp);
                else if(isPlaceEmpty(snake_x[0]-1,snake_y[0]))runOnUiThread(this::goLeft);
            }

            else if(food_y>snake_y[0]){
                if(isPlaceEmpty(snake_x[0],snake_y[0]+1))runOnUiThread(this::goUp);
                else if(isPlaceEmpty(snake_x[0]+1,snake_y[0]))runOnUiThread(this::goRight);
                else if(isPlaceEmpty(snake_x[0]-1,snake_y[0]))runOnUiThread(this::goLeft);
                else if(isPlaceEmpty(snake_x[0],snake_y[0]-1))runOnUiThread(this::goDown);
            }
            else if(food_y<snake_y[0]){
                if(isPlaceEmpty(snake_x[0],snake_y[0]-1))runOnUiThread(this::goDown);
                else if(isPlaceEmpty(snake_x[0]+1,snake_y[0]))runOnUiThread(this::goRight);
                else if(isPlaceEmpty(snake_x[0]-1,snake_y[0]))runOnUiThread(this::goLeft);
                else if(isPlaceEmpty(snake_x[0],snake_y[0]+1))runOnUiThread(this::goUp);
            }









            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}