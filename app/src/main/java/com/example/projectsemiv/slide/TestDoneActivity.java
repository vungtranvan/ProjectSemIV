package com.example.projectsemiv.slide;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.projectsemiv.R;
import com.example.projectsemiv.entity.Question01;

import java.util.ArrayList;

public class TestDoneActivity extends AppCompatActivity {

    ArrayList<Question01> arr_QuesBegin = new ArrayList<Question01>();
    int numNoAns = 0;
    int numTrue = 0;
    int numFalse = 0;


    TextView tvTrue, tvFalse, tvNotAns, tvTotalScore;
    Button btnAgain, btnExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_done);

        final Intent intent = getIntent();
        arr_QuesBegin = (ArrayList<Question01>) intent.getExtras().getSerializable("arr_Ques");
        begin();
        checkResult();
        tvNotAns.setText("" + numNoAns);
        tvFalse.setText("" + numFalse);
        tvTrue.setText("" + numTrue);
        tvTotalScore.setText(numTrue + " / " + arr_QuesBegin.size());

        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(TestDoneActivity.this);
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn có muốn thoát hay không?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });

                builder.show();
            }
        });

        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent2 = new Intent(TestDoneActivity.this, ScreenSlideActivity.class);
                intent2.putExtra("arr_Ques", arr_QuesBegin);
                intent2.putExtra("test", "no");
                startActivity(intent2);
            }
        });
    }

    public void begin() {
        tvFalse = (TextView) findViewById(R.id.tvFalse);
        tvTrue = (TextView) findViewById(R.id.tvTrue);
        tvNotAns = (TextView) findViewById(R.id.tvNotAns);
        tvTotalScore = (TextView) findViewById(R.id.tvTotalPoint);
        btnAgain = (Button) findViewById(R.id.btnAgain);
        btnExit = (Button) findViewById(R.id.btnExit);
    }

    public void checkResult() {
        for (int i = 0; i < arr_QuesBegin.size(); i++) {
            if (arr_QuesBegin.get(i).getTraloi().equals("") == true) {
                numNoAns++;
            } else if (arr_QuesBegin.get(i).getResult().equals(arr_QuesBegin.get(i).getTraloi()) == true) {
                numTrue++;
            } else numFalse++;
        }
    }
}