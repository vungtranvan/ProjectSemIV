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
import android.widget.Toast;

import com.example.projectsemiv.R;
import com.example.projectsemiv.adapter.CheckAnswerAdapter;
import com.example.projectsemiv.entity.HistoryUpdateVm;
import com.example.projectsemiv.entity.QuestionHistoryVm;
import com.example.projectsemiv.helper.CommonData;
import com.example.projectsemiv.services.ApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScreenSlideActivity extends FragmentActivity {
    /**
     * The number of pages (wizard steps) to show.
     */
    private int NUM_PAGES = 0;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter pagerAdapter;

    TextView tvKiemTra, tvTimer, tvXemDiem, tv_back_question, tv_current_question, tv_total_question, tv_next_question;
    public int checkAns = 0;
    List<QuestionHistoryVm> arr_Ques;
    CounterClass timer;
    int _idH;
    int totalTimer = CommonData.TIME_TOTAL_TEST;
    boolean test = false;
    Dialog dialogCheckAnswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_slide);

        Intent intent = getIntent();
        test = intent.getBooleanExtra("test", false);
        _idH = intent.getIntExtra("_idH", 0);
        arr_Ques = (ArrayList<QuestionHistoryVm>) intent.getExtras().getSerializable("arr_Ques");

        NUM_PAGES = arr_Ques.size();

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);
        mPager.setPageTransformer(true, new DepthPageTransformer());


        timer = new CounterClass(totalTimer * 60 * 1000, 1000);
        tvKiemTra = (TextView) findViewById(R.id.tvKiemTra);
        tvTimer = (TextView) findViewById(R.id.tvTimer);
        tvXemDiem = (TextView) findViewById(R.id.tvScore);

        tv_back_question = (TextView) findViewById(R.id.tv_back_question);
        tv_current_question = (TextView) findViewById(R.id.tv_current_question);
        tv_total_question = (TextView) findViewById(R.id.tv_total_question);
        tv_next_question = (TextView) findViewById(R.id.tv_next_question);

        if (!test) {
            // Fake Data
            //arr_Ques = new FakeData().getQuestionFake();
            timer.start();
        } else {
            timer.cancel();
            tvTimer.setText("00:00");
            checkAns = 1;
            tvKiemTra.setVisibility(View.GONE);
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

        tvKiemTra.setOnClickListener(new View.OnClickListener() {
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

    private void submitDataExam() {
        int numTrue = 0;
        for (int i = 0; i < arr_Ques.size(); i++) {
            if (arr_Ques.get(i).getAnswerCorrect().equals(arr_Ques.get(i).getAnswerChoice()) == true) {
                numTrue++;
            }
        }

        HistoryUpdateVm historyUpdateVm = new HistoryUpdateVm();
        historyUpdateVm.setId(_idH);
        historyUpdateVm.setCorrectMark(numTrue);
        historyUpdateVm.setTotalMark(arr_Ques.size());
        historyUpdateVm.setItems((List) arr_Ques);

        ApiService.apiService.editHistory(historyUpdateVm).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Toast.makeText(ScreenSlideActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
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

    public List<QuestionHistoryVm> getData() {
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
        tvKiemTra.setVisibility(View.GONE);
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
                if (!test) {
                    submitDataExam();
                }
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
                if (!test) {
                    submitDataExam();
                }
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