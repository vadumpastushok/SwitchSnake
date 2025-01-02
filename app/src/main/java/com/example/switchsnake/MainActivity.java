package com.example.switchsnake;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements Runnable {

    private int width = 7;
    private int height = 20;

    private SnakeDirection snakeDirection = SnakeDirection.LEFT;

    private ArrayList<Pair<Integer, Integer>> snake = new ArrayList<>();

    public int food_x=-1, food_y=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        build();
        startGame();
        new Thread(this).start();
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
        Random random=new Random();
        int x=random.nextInt(height);
        int y=random.nextInt(width);

        snake.clear();
        snake.add(new Pair<>(x, y));
        Log.d("tagg-new", snake.toString());
    }

    private boolean isPlaceEmpty(int x, int y){
        if(x<0 || y<0 || x>=height || y>=width)return false;
        for(int i=0;i<snake.size();i++){
            if(x== snake.get(i).first && y== snake.get(i).second) {
                return false;
            }
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

    private void consumeFood() {
        food_x = -1;
        food_y = -1;
    }

    private void checkGameStatus() {
        Pair<Integer, Integer> position = snake.get(0);
        Log.d("tagg", position.first.toString() + "   " + position.second.toString());
        if (position.first < 0 ||
            position.second < 0 ||
            position.first >= height ||
            position.second >= width
        ) {
            showGameOver();
        }
    }

    public void goLeft(){
        Pair<Integer, Integer> prevPosition = snake.get(0);
        Pair<Integer, Integer> newPosition = new Pair<>(prevPosition.first, prevPosition.second - 1);
        snake.add(0, newPosition);

        if (newPosition.first == food_x && newPosition.second == food_y) {
            consumeFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    public void goRight(){
        Pair<Integer, Integer> prevPosition = snake.get(0);
        Pair<Integer, Integer> newPosition = new Pair<>(prevPosition.first, prevPosition.second + 1);
        snake.add(0, newPosition);

        if (newPosition.first == food_x && newPosition.second == food_y) {
            consumeFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    public void goUp(){
        Pair<Integer, Integer> prevPosition = snake.get(0);
        Pair<Integer, Integer> newPosition = new Pair<>(prevPosition.first - 1, prevPosition.second);
        snake.add(0, newPosition);

        if (newPosition.first == food_x && newPosition.second == food_y) {
            consumeFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    public void goDown(){
        Pair<Integer, Integer> prevPosition = snake.get(0);
        Pair<Integer, Integer> newPosition = new Pair<>(prevPosition.first + 1, prevPosition.second);
        snake.add(0, newPosition);

        if (newPosition.first == food_x && newPosition.second == food_y) {
            consumeFood();
        } else {
            snake.remove(snake.size() - 1);
        }
    }

    private void showGameOver() {
        Toast.makeText(this, "Game over! Restarting...", Toast.LENGTH_LONG).show();
        startGame();
    }

    public void draw() {
        for(int i=0;i<height;i++)for(int j=0;j<width;j++) {
            if(i==food_x && j==food_y) {
                SwitchCompat x = findViewById(i*100+j);
                x.setChecked(true);
                x.setThumbTintList(ColorStateList.valueOf(Color.RED));
                continue;
            }

            boolean isSnake = false;
            for(int k=0;k<snake.size();k++) {
                if(snake.get(k).first == i && snake.get(k).second ==j) {
                    isSnake=true;
                    break;
                }
            }
            if(isSnake) {
                SwitchCompat x = findViewById(i*100+j);
                x.setChecked(true);
                x.setThumbTintList(ColorStateList.valueOf(Color.GREEN));
                continue;
            }

            SwitchCompat x = findViewById(i*100+j);
            x.setChecked(false);
            x.setThumbTintList(ColorStateList.valueOf(Color.GRAY));
        }
    }

    public void score(){
        TextView tv=findViewById(R.id.textView);
        tv.setText("Length: "+ snake.size());
    }

    @Override
    public void run() {
        while (true) {
            if(food_x==-1 && food_y==-1) {
                spawnFood();
            }
            runOnUiThread(this::draw);
            runOnUiThread(this::score);

            switch (snakeDirection) {
                case LEFT:
                    runOnUiThread(this::goLeft);
                    break;
                case TOP:
                    runOnUiThread(this::goUp);
                    break;
                case RIGHT:
                    runOnUiThread(this::goRight);
                    break;
                case BOTTOM:
                    runOnUiThread(this::goDown);
                    break;
            }

            runOnUiThread(this::checkGameStatus);

            try {
                Thread.sleep(400L);
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

            SnakeDirection newSnakeDirection;
            if (Math.abs(offsetX) > Math.abs(offsetY)) {
                newSnakeDirection = offsetX > 0 ? SnakeDirection.RIGHT : SnakeDirection.LEFT;
            } else {
                newSnakeDirection = offsetY > 0 ? SnakeDirection.BOTTOM : SnakeDirection.TOP;
            }

            if (newSnakeDirection == SnakeDirection.LEFT && snakeDirection != SnakeDirection.RIGHT) {
                snakeDirection = newSnakeDirection;
            } else if (newSnakeDirection == SnakeDirection.TOP && snakeDirection != SnakeDirection.BOTTOM) {
                snakeDirection = newSnakeDirection;
            } else if (newSnakeDirection == SnakeDirection.RIGHT && snakeDirection != SnakeDirection.LEFT) {
                snakeDirection = newSnakeDirection;
            } else if (newSnakeDirection == SnakeDirection.BOTTOM && snakeDirection != SnakeDirection.TOP) {
                snakeDirection = newSnakeDirection;
            }
        }
        return true;
    }
}