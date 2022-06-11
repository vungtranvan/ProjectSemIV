package com.example.projectsemiv.slide;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.projectsemiv.R;
import com.example.projectsemiv.adapter.CheckAnswerAdapter;
import com.example.projectsemiv.entity.FakeData;
import com.example.projectsemiv.entity.Question01;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ScreenSlideActivity extends FragmentActivity {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 15;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter pagerAdapter;

    TextView tvKiemtra, tvTimer, tvXemDiem, tv_back_question, tv_current_question, tv_total_question, tv_next_question;
    public int checkAns = 0;
    List<Question01> arr_Ques;
    CounterClass timer;
    String subject;
    int num_exam;
    int totalTimer = 10;
    String test = "";
    Dialog dialogCheckAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);
        mPager.setPageTransformer(true, new DepthPageTransformer());

        Intent intent = getIntent();
        subject = intent.getStringExtra("subject");
        num_exam = intent.getIntExtra("categoryExamId", 0);
        test = intent.getStringExtra("test");

        timer = new CounterClass(totalTimer * 60 * 1000, 1000);
        tvKiemtra = (TextView) findViewById(R.id.tvKiemTra);
        tvTimer = (TextView) findViewById(R.id.tvTimer);
        tvXemDiem = (TextView) findViewById(R.id.tvScore);

        tv_back_question = (TextView) findViewById(R.id.tv_back_question);
        tv_current_question = (TextView) findViewById(R.id.tv_current_question);
        tv_total_question = (TextView) findViewById(R.id.tv_total_question);
        tv_next_question = (TextView) findViewById(R.id.tv_next_question);

        if (test.equals("yes")) {
            // Fake Data
            arr_Ques = new FakeData().getQuestionFake();
            timer.start();
        } else {
            arr_Ques = (ArrayList<Question01>) intent.getExtras().getSerializable("arr_Ques");
            timer.cancel();
            tvTimer.setText("00:00");
            checkAns = 1;
            tvKiemtra.setVisibility(View.GONE);
            tvXemDiem.setVisibility(View.VISIBLE);
        }

        tv_total_question.setText("" + NUM_PAGES);

        this.setTextShowPageCurrent();

        if (mPager.getCurrentItem() == 0) {
            tv_next_question.setVisibility(View.VISIBLE);
        }

        tv_next_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(mPager.getCurrentItem() + 1);
                changPageCurrent();
            }
        });

        tv_back_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPager.setCurrentItem(mPager.getCurrentItem() - 1);
                changPageCurrent();
            }
        });

        tvKiemtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer();
            }
        });

        tvXemDiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent intent1 = new Intent(ScreenSlideActivity.this, TestDoneActivity.class);
                intent1.putExtra("arr_Ques", (ArrayList) arr_Ques);
                startActivity(intent1);
            }
        });
    }

    private void changPageCurrent() {
        setTextShowPageCurrent();
        if (mPager.getCurrentItem() + 1 == NUM_PAGES) {
            tv_next_question.setVisibility(View.GONE);
        } else if (mPager.getCurrentItem() == 0) {
            tv_back_question.setVisibility(View.GONE);
        } else {
            if (mPager.getCurrentItem() > 0) {
                tv_back_question.setVisibility(View.VISIBLE);
                if (mPager.getCurrentItem() < NUM_PAGES - 1) {
                    tv_next_question.setVisibility(View.VISIBLE);
                } else {
                    tv_next_question.setVisibility(View.GONE);
                }
            } else {
                tv_back_question.setVisibility(View.GONE);
            }
        }
    }

    private void setTextShowPageCurrent() {
        tv_current_question.setText("" + (mPager.getCurrentItem() + 1));
    }

    public List<Question01> getData() {
        return arr_Ques;
    }

    private void checkAnswer() {
        dialogCheckAnswer = new Dialog(this);
        dialogCheckAnswer.setContentView(R.layout.check_answer_dialog);
        dialogCheckAnswer.setTitle(R.string.list_of_answers);

        CheckAnswerAdapter answerAdapter = new CheckAnswerAdapter(arr_Ques, this);
        GridView gvLsQuestion = (GridView) dialogCheckAnswer.findViewById(R.id.gvLsQuestion);
        gvLsQuestion.setAdapter(answerAdapter);

        gvLsQuestion.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPager.setCurrentItem(position);
                dialogCheckAnswer.dismiss();
            }
        });

        Button btnCancel, btnFinish;
        btnCancel = (Button) dialogCheckAnswer.findViewById(R.id.btnCancel);
        btnFinish = (Button) dialogCheckAnswer.findViewById(R.id.btnFinish);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogCheckAnswer.dismiss();
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogFinish();
            }
        });

        dialogCheckAnswer.show();
    }

    private void dismissDialogCheckAnswer() {
        dialogCheckAnswer.dismiss();
    }

    public void result() {
        checkAns = 1;
        if (mPager.getCurrentItem() >= 4) {
            mPager.setCurrentItem(0);
            tv_next_question.setVisibility(View.VISIBLE);
        } else if (mPager.getCurrentItem() <= 4) {
            mPager.setCurrentItem(NUM_PAGES);
            tv_back_question.setVisibility(View.VISIBLE);
        }
        tvXemDiem.setVisibility(View.VISIBLE);
        tvKiemtra.setVisibility(View.GONE);
    }

    private void dialogFinish() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ScreenSlideActivity.this);
        builder.setTitle(R.string.notification);
        builder.setMessage(R.string.notification_end_test);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                timer.cancel();
                result();
                dismissDialogCheckAnswer();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        builder.show();
    }

    private void dialogExit() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ScreenSlideActivity.this);
        builder.setTitle(R.string.notification);
        builder.setMessage(R.string.notification_exit);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                timer.cancel();
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

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            dialogExit();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ScreenSlidePageFragment.create(position, checkAns);
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    public class DepthPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            changPageCurrent();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1f);
                view.setTranslationX(0f);
                view.setScaleX(1f);
                view.setScaleY(1f);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);

                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0f);
            }
        }
    }

    public class CounterClass extends CountDownTimer {
        //millisInFuture: 60*1000
        //countDownInterval:  1000
        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            String countTime = String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
            tvTimer.setText(countTime);
        }

        @Override
        public void onFinish() {
            tvTimer.setText("00:00");
            result();
        }
    }
}