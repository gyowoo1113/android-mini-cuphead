package kr.ac.tukorea.ge.sgp02.a2019182019.sample02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{

    private TextView subTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        subTextView = findViewById(R.id.subText);
        subTextView.setText("I am a Good Programmer");
    }

    public void onBtnPushMe(View view) {
        subTextView.setText("Clicked !!!");
    }
}