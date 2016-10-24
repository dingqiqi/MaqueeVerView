package com.dingqiqi.testmarqueever;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private MaqueeVerView mMaqueeVerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMaqueeVerView = (MaqueeVerView) findViewById(R.id.maqueeVerView);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMaqueeVerView.setMode(MaqueeVerView.MODE.RIGHT);
                mMaqueeVerView.setTextCount(3);
                mMaqueeVerView.setColor(Color.WHITE);
                mMaqueeVerView.setStrs(new String[]{"你好滴答滴答滴答滴答滴答的1111",
                        "你好滴答滴答滴答滴答滴答的2222",
                        "你好滴答滴答滴答滴答滴答的3333",
                        "你好滴答滴答滴答滴答滴答的4444",
                        "你好滴答滴答滴答滴答滴答的5555"})
                ;
            }
        });

    }
}
