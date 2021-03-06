package kr.ac.tukorea.ge.sgp02.a2019182019.imageswitcher;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    SharedPreferences pageShaderPref;
    SharedPreferences.Editor pageEditor;

    public static final String TAG = MainActivity.class.getSimpleName();

    private static final int[] RES_IDS = new int[]{
            R.mipmap.cat_1,
            R.mipmap.cat_2,
            R.mipmap.cat_3,
            R.mipmap.cat_4,
            R.mipmap.cat_5,
    };
    private int pageNumber;
    private TextView pageTextView;
    private ImageView contentImageView;

    public android.widget.Button prevButton;
    public android.widget.Button nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        pageShaderPref = getSharedPreferences("pagePreferences", MODE_PRIVATE);
        pageEditor = pageShaderPref.edit();

        pageTextView = findViewById(R.id.pageTextView);
        contentImageView = findViewById(R.id.contentImageView);
        prevButton = findViewById(R.id.prevButton);
        nextButton = findViewById(R.id.nextButton);

        setPage(pageShaderPref.getInt("pageNumber",1));
    }

    public void onBtnPrev(View view) {
        Log.d(TAG,"Prev pressed");
        setPage(pageNumber - 1);
    }
    public void onBtnNext(View view) {
        Log.d(TAG,"Next pressed");
        setPage(pageNumber + 1);
    }

    private void setPage(int page) {
        setEnabled(page);
        if (page < 1 || page > 5){
            return;
        }
        pageNumber = page;

        String text = pageNumber + " / " + 5;
        pageTextView.setText(text);

        int resId = RES_IDS[pageNumber-1];
        contentImageView.setImageResource(resId);

        pageEditor.putInt("pageNumber",pageNumber);
        pageEditor.apply();
    }

    public void setEnabled(int page){
        if (page == 1){
            prevButton.setEnabled(false);
        }
        else if (page == 5) {
            nextButton.setEnabled(false);
        }
        else{
            prevButton.setEnabled(true);
            nextButton.setEnabled(true);
        }
    }
}