package com.bluehomestudio.luckywheeldemo;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bluehomestudio.luckywheel.LuckyWheel;
import com.bluehomestudio.luckywheel.OnLuckyWheelReachTheTarget;
import com.bluehomestudio.luckywheel.WheelItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity{

    private LuckyWheel lw;
    List<WheelItem> wheelItems ;
    String result = "Làm gì bây giờ";
    final int SWIPE_DISTANCE_THRESHOLD = 100;
    float x1, x2, y1, y2, dx, dy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        generateWheelItems();
        lw = findViewById(R.id.lwv);
        lw.addWheelItems(wheelItems);


        lw.setLuckyWheelReachTheTarget(new OnLuckyWheelReachTheTarget() {
            @Override
            public void onReachTarget() {
                Toast.makeText(MainActivity.this, result, Toast.LENGTH_LONG).show();
            }
        });

        Button start = findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Random random = new Random();
                int selectedIndex = random.nextInt(wheelItems.size()-1);
                lw.setTarget(selectedIndex+1);
                lw.setTextSize(60);
                lw.setTextColor(Color.YELLOW);
                result = wheelItems.get(selectedIndex).text;
                lw.rotateWheelTo(selectedIndex+1);
            }
        });

        lw.setOnTouchListener(
                new View.OnTouchListener() {
                    @SuppressLint("ClickableViewAccessibility")
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {

                        switch (event.getAction()) {
                            case (MotionEvent.ACTION_DOWN):
                                x1 = event.getX();
                                y1 = event.getY();
                                break;
                            case MotionEvent.ACTION_UP:
                                x2 = event.getX();
                                y2 = event.getY();
                                dx = x2 - x1;
                                dy = y2 - y1;
                                if ( Math.abs(dx) > Math.abs(dy) ) {
                                    if ( dx < 0 && Math.abs(dx) > SWIPE_DISTANCE_THRESHOLD )
                                    {
                                        Random random = new Random();
                                        int selectedIndex = random.nextInt(wheelItems.size()-1);
                                        lw.setTarget(selectedIndex+1);
                                        result = wheelItems.get(selectedIndex).text;
                                        lw.rotateWheelTo(selectedIndex+1);
                                    }
                                } else {
                                    if ( dy > 0 && Math.abs(dy) > SWIPE_DISTANCE_THRESHOLD )
                                    {
                                        Random random = new Random();
                                        int selectedIndex = random.nextInt(wheelItems.size()-1);
                                        lw.setTarget(selectedIndex+1);
                                        result = wheelItems.get(selectedIndex).text;
                                        lw.rotateWheelTo(selectedIndex+1);
                                    }
                                }
                                break;
                            default:
                                return true;
                        }
                        return true;
                    }
                }
        );

        Button add = findViewById(R.id.add);
        add.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        System.out.println("add");
                        wheelItems.add(new WheelItem(Color.parseColor("#fc6c6c"), BitmapFactory.decodeResource(getResources(),
                                R.drawable.chat) , "100 $"));
                        lw.addWheelItems(wheelItems);
                        List<Integer> listColor = new ArrayList<Integer>();
                        listColor.add(Color.parseColor("#FF0000"));
                        listColor.add(Color.parseColor("#00FF00"));
                        listColor.add(Color.parseColor("#0000FF"));
                        listColor.add(Color.parseColor("#FFFF00"));
                        listColor.add(Color.parseColor("#FF00FF"));
                        lw.setItemsColor(listColor);
                    }
                }
        );
    }

    private void generateWheelItems() {
        wheelItems = new ArrayList<>();
        wheelItems.add(new WheelItem(Color.parseColor("#fc6c6c"), BitmapFactory.decodeResource(getResources(),
                R.drawable.chat) , "100 $", 0xffffffff));
        wheelItems.add(new WheelItem(Color.parseColor("#00E6FF"), BitmapFactory.decodeResource(getResources(),
                R.drawable.coupon) , "0 $"));
        wheelItems.add(new WheelItem(Color.parseColor("#F00E6F"), BitmapFactory.decodeResource(getResources(),
                R.drawable.ice_cream), "30 $"));
        wheelItems.add(new WheelItem(Color.parseColor("#00E6FF"), BitmapFactory.decodeResource(getResources(),
                R.drawable.lemonade), "6000 $"));
        wheelItems.add(new WheelItem(Color.parseColor("#fc6c6c"), BitmapFactory.decodeResource(getResources(),
                R.drawable.orange), "9 $"));
        wheelItems.add(new WheelItem(Color.parseColor("#00E6FF"), BitmapFactory.decodeResource(getResources(),
                R.drawable.shop), "20 $"));
    }
}
