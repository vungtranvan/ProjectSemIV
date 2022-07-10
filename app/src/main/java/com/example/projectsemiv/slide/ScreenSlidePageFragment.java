package com.example.projectsemiv.slide;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.projectsemiv.R;
import com.example.projectsemiv.activity.UpdateAccountActivity;
import com.example.projectsemiv.entity.QuestionHistoryVm;

import java.util.ArrayList;
import java.util.List;


public class ScreenSlidePageFragment extends Fragment {

    List<QuestionHistoryVm> arr_Ques;
    public static final String ARG_PAGE = "page";
    public static final String ARG_CHECK_ANSWER = "checkAnswer";
    public int mPageNumber;
    public int checkAns;

    TextView tvNum, tvQuestion;
    RadioGroup radioGroup;
    RadioButton radA, radB, radC, radD;
    ImageView imgIcon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);

        tvNum = (TextView) rootView.findViewById(R.id.tvNum);
        tvQuestion = (TextView) rootView.findViewById(R.id.tvQuestion);
        radA = (RadioButton) rootView.findViewById(R.id.radA);
        radB = (RadioButton) rootView.findViewById(R.id.radB);
        radC = (RadioButton) rootView.findViewById(R.id.radC);
        radD = (RadioButton) rootView.findViewById(R.id.radD);
        imgIcon = (ImageView) rootView.findViewById(R.id.ivIcon);
        radioGroup = (RadioGroup) rootView.findViewById(R.id.radGroup);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        arr_Ques = new ArrayList<QuestionHistoryVm>();
        ScreenSlideActivity slideActivity = (ScreenSlideActivity) getActivity();
        arr_Ques = slideActivity.getData();
        mPageNumber = getArguments().getInt(ARG_PAGE);
        checkAns = getArguments().getInt(ARG_CHECK_ANSWER);
    }

    public static ScreenSlidePageFragment create(int pageNumber, int checkAnswer) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        args.putInt(ARG_CHECK_ANSWER, checkAnswer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvNum.setText(getString(R.string.question_no) + " " + (mPageNumber + 1));

        QuestionHistoryVm question = this.getItem(mPageNumber);
        tvQuestion.setText(question.getName());
        radA.setText(question.getAnswerA());
        radB.setText(question.getAnswerB());
        radC.setText(question.getAnswerC());
        radD.setText(question.getAnswerD());

        if (question.getImage() != null && !question.getImage().equals("")) {
            imgIcon.setVisibility(View.VISIBLE);
            Glide.with(ScreenSlidePageFragment.this).load(question.getImage()).into(imgIcon);
        } else {
            imgIcon.setVisibility(View.GONE);
        }

        if (checkAns != 0) {
            this.getCheckAns(question.getAnswerCorrect());
            this.setBackgroundErrandCheckedRadio(question);
            radA.setClickable(false);
            radB.setClickable(false);
            radC.setClickable(false);
            radD.setClickable(false);
        }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                question.choiceID = checkedId;
                question.setAnswerChoice(getChoiceFromID(checkedId));
            }
        });
    }

    public QuestionHistoryVm getItem(int position) {
        return arr_Ques.get(position);
    }

    private String getChoiceFromID(int ID) {
        switch (ID) {
            case R.id.radA:
                return "A";
            case R.id.radB:
                return "B";
            case R.id.radC:
                return "C";
            case R.id.radD:
                return "D";
            default:
                return "";
        }
    }

    private void setBackgroundErrandCheckedRadio(QuestionHistoryVm question) {
        if (question.getAnswerChoice() != null && !question.getAnswerChoice().equals("")) {
            switch (question.getAnswerChoice()) {
                case "A":
                    radA.setChecked(true);
                    if (!question.getAnswerCorrect().equals("A")) {
                        radA.setBackgroundColor(Color.RED);
                    }
                    break;
                case "B":
                    radB.setChecked(true);
                    if (!question.getAnswerCorrect().equals("B")) {
                        radB.setBackgroundColor(Color.RED);
                    }
                    break;
                case "C":
                    radC.setChecked(true);
                    if (!question.getAnswerCorrect().equals("C")) {
                        radC.setBackgroundColor(Color.RED);
                    }
                    break;
                case "D":
                    radD.setChecked(true);
                    if (!question.getAnswerCorrect().equals("D")) {
                        radD.setBackgroundColor(Color.RED);
                    }
                    break;
            }
        }
    }

    private void getCheckAns(String ans) {
        if (ans.equals("A")) {
            radA.setBackgroundColor(Color.GREEN);
        } else if (ans.equals("B")) {
            radB.setBackgroundColor(Color.GREEN);
        } else if (ans.equals("C")) {
            radC.setBackgroundColor(Color.GREEN);
        } else if (ans.equals("D")) {
            radD.setBackgroundColor(Color.GREEN);
        }
    }
}