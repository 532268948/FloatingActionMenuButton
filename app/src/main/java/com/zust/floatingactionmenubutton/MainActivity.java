package com.zust.floatingactionmenubutton;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zust.floatingactionmenubutton.custom.FloatingActionMenuButton;

public class MainActivity extends AppCompatActivity {

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView=(TextView)findViewById(R.id.text1);

        TextView mTextView1=new TextView(this);
        mTextView1.setText("111");
        mTextView1.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        mTextView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"11111111",Toast.LENGTH_SHORT).show();
            }
        });
        TextView mTextView2=new TextView(this);
        mTextView2.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        mTextView2.setText("222");
        TextView mTextView3=new TextView(this);
        mTextView3.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        mTextView3.setText("333");

//        ((ViewGroup)getWindow().getDecorView().findViewById(android.R.id.content)).addView(mTextView1);

        FloatingActionMenuButton menuButton=new FloatingActionMenuButton.Builder(this)
                .setStartAngle(0)
                .setEndAngle(-90)
                .addSubActionItem(mTextView1,100,100)
                .addSubActionItem(mTextView2,100,100)
                .addSubActionItem(mTextView3,100,100)
                .attachView(mTextView)
                .setRadius(200)
                .build();
    }
}
