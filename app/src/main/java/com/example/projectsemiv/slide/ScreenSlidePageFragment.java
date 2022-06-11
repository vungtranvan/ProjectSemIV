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

import com.example.projectsemiv.R;
import com.example.projectsemiv.entity.Question01;

import java.util.ArrayList;
import java.util.List;


public class ScreenSlidePageFragment extends Fragment {

    List<Question01> arr_Ques;
    public static final String ARG_PAGE = "page";
    public static final String ARG_CHECKANSWER = "checkAnswer";
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

        arr_Ques = new ArrayList<Question01>();
        ScreenSlideActivity slideActivity = (ScreenSlideActivity) getActivity();
        arr_Ques = slideActivity.getData();
        mPageNumber = getArguments().getInt(ARG_PAGE);
        checkAns = getArguments().getInt(ARG_CHECKANSWER);
    }

    public static ScreenSlidePageFragment create(int pageNumber, int checkAnswer) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        args.putInt(ARG_CHECKANSWER, checkAnswer);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvNum.setText("Câu " + (mPageNumber + 1));

        Question01 question = this.getItem(mPageNumber);
        tvQuestion.setText(question.getQuestion());
        radA.setText(question.getAns_a());
        radB.setText(question.getAns_b());
        radC.setText(question.getAns_c());
        radD.setText(question.getAns_d());

        if (checkAns != 0) {
            this.getCheckAns(question.getResult().toString());
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
                question.setTraloi(getChoiceFromID(checkedId));
            }
        });
    }

    public Question01 getItem(int position) {
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

    private void setBackgroundErrandCheckedRadio(Question01 question) {
        if (question.getTraloi() != null && !question.getTraloi().equals("")) {
            switch (question.getTraloi()) {
                case "A":
                    radA.setChecked(true);
                    if (!question.getResult().equals("A")) {
                        radA.setBackgroundColor(Color.RED);
                    }
                    break;
                case "B":
                    radB.setChecked(true);
                    if (!question.getResult().equals("B")) {
                        radB.setBackgroundColor(Color.RED);
                    }
                    break;
                case "C":
                    radC.setChecked(true);
                    if (!question.getResult().equals("C")) {
                        radC.setBackgroundColor(Color.RED);
                    }
                    break;
                case "D":
                    radD.setChecked(true);
                    if (!question.getResult().equals("D")) {
                        radD.setBackgroundColor(Color.RED);
                    }
                    break;
            }
        }
    }

    private void getCheckAns(String ans) {
        if (ans.equals("A") == true) {
            radA.setBackgroundColor(Color.GREEN);
        } else if (ans.equals("B") == true) {
            radB.setBackgroundColor(Color.GREEN);
        } else if (ans.equals("C") == true) {
            radC.setBackgroundColor(Color.GREEN);
        } else if (ans.equals("D") == true) {
            radD.setBackgroundColor(Color.GREEN);
        }
    }
}