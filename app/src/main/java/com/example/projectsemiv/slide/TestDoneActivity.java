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
import com.example.projectsemiv.entity.QuestionHistoryVm;

import java.util.ArrayList;

public class TestDoneActivity extends AppCompatActivity {

    ArrayList<QuestionHistoryVm> arr_QuesBegin = new ArrayList<QuestionHistoryVm>();
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
        arr_QuesBegin = (ArrayList<QuestionHistoryVm>) intent.getExtras().getSerializable("arr_Ques");

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
                builder.setTitle(R.string.notification);
                builder.setMessage(R.string.notification_exit);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
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
                intent2.putExtra("test", true);
                startActivity(intent2);
            }
        });
    }

    public void begin() {
        tvFalse = findViewById(R.id.tvFalse);
        tvTrue = findViewById(R.id.tvTrue);
        tvNotAns = findViewById(R.id.tvNotAns);
        tvTotalScore = findViewById(R.id.tvTotalPoint);
        btnAgain = findViewById(R.id.btnAgain);
        btnExit = findViewById(R.id.btnExit);
    }

    public void checkResult() {
        for (int i = 0; i < arr_QuesBegin.size(); i++) {
            if (arr_QuesBegin.get(i).getAnswerChoice() == null || arr_QuesBegin.get(i).getAnswerChoice().equals("") == true) {
                numNoAns++;
            } else if (arr_QuesBegin.get(i).getAnswerCorrect().equals(arr_QuesBegin.get(i).getAnswerChoice()) == true) {
                numTrue++;
            } else numFalse++;
        }
    }
}