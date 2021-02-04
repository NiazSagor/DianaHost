package com.angik.dianahost;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.ramotion.paperonboarding.PaperOnboardingFragment;
import com.ramotion.paperonboarding.PaperOnboardingPage;
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnChangeListener;
import com.ramotion.paperonboarding.listeners.PaperOnboardingOnRightOutListener;

import java.util.ArrayList;

public class OnBoardingActivity extends AppCompatActivity {

    private static final String TAG = "OnBoardingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);

        PaperOnboardingPage scr1 = new PaperOnboardingPage("Web Hosting",
                "Engage customer, users with the best domain",
                Color.parseColor("#52e5ff"), R.drawable.ic_web_main, R.drawable.ic_web_outline);
        PaperOnboardingPage scr2 = new PaperOnboardingPage("Easy Setup",
                "Easy setup with instant activation",
                Color.parseColor("#ffd699"), R.drawable.ic_setup_main, R.drawable.ic_setup_outline);
        PaperOnboardingPage scr3 = new PaperOnboardingPage("Low Cost",
                "Cost friendly service with no hidden cost",
                Color.parseColor("#c499ff"), R.drawable.ic_cost_main, R.drawable.ic_cost_outline);

        ArrayList<PaperOnboardingPage> elements = new ArrayList<>();
        elements.add(scr1);
        elements.add(scr2);
        elements.add(scr3);

        PaperOnboardingFragment onBoardingFragment = PaperOnboardingFragment.newInstance(elements);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, onBoardingFragment);
        fragmentTransaction.commit();

        onBoardingFragment.setOnRightOutListener(new PaperOnboardingOnRightOutListener() {
            @Override
            public void onRightOut() {
                startActivity(new Intent(OnBoardingActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}