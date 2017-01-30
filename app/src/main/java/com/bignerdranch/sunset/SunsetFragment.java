package com.bignerdranch.sunset;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by rbanks on 11/14/16.
 */

public class SunsetFragment extends Fragment {

    private View m_sceneView;
    private View m_sunView;
    private View m_skyView;

    //our colors
    private int m_blueSkyColor;
    private int m_sunsetSkyColor;
    private int m_nightSkyColor;

    //check if we have a sunset...TODO find a better way to trigger this
    boolean m_sunsetTrue = false;

    public static SunsetFragment newInstance() {
        return new SunsetFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sunset, container, false);
        m_sceneView = view;
        m_sunView = view.findViewById(R.id.sun);
        m_skyView = view.findViewById(R.id.sky);

        Resources resources = getResources();
        m_blueSkyColor = resources.getColor(R.color.blue_sky);
        m_sunsetSkyColor = resources.getColor(R.color.sunset_sky);
        m_nightSkyColor = resources.getColor(R.color.night_sky);

        m_sceneView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!m_sunsetTrue)
                    startAnimation();
                else
                    reverseSunsetAnimation();
            }
        });

        return view;
    }

    private void startAnimation() {
        float sunYStart = m_sunView.getTop();
        float sunYEnd = m_skyView.getHeight();

        Log.i("Values", "start pos = " + sunYStart + ", end pos = " + sunYEnd);

        ObjectAnimator heightAnimator = ObjectAnimator
                .ofFloat(m_sunView, "y", sunYStart, sunYEnd)
                .setDuration(2000);
        heightAnimator.setInterpolator(new AccelerateInterpolator());

        //change the color when you click it
        ObjectAnimator sunsetSkyAnimator = ObjectAnimator
                .ofInt(m_skyView, "backgroundColor", m_blueSkyColor, m_sunsetSkyColor)
                .setDuration(2000);
        //without this evaluator, your sky will flicker between colors roughly causing a seizure
        sunsetSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator nightSkyAnimator = ObjectAnimator
                .ofInt(m_skyView, "backgroundColor", m_sunsetSkyColor, m_nightSkyColor)
                .setDuration(1500);
        nightSkyAnimator.setEvaluator(new ArgbEvaluator());

        //heightAnimator.start();
        //sunsetSkyAnimator.start();

        //we can use an animator set to put our animations in a certain order
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(heightAnimator)
                .with(sunsetSkyAnimator)
                .before(nightSkyAnimator);
        animatorSet.start();

        m_sunsetTrue = true;
    }

    private void reverseSunsetAnimation() {
        float sunYStart = m_sunView.getTop();
        float sunYEnd = m_skyView.getHeight();

        Log.i("Values", "Reverse...start pos = " + sunYStart + ", end pos = " + sunYEnd);

        ObjectAnimator heightAnimator = ObjectAnimator
                .ofFloat(m_sunView, "y", sunYEnd, sunYStart)
                .setDuration(2000);

        //change the color when you click it
        ObjectAnimator sunriseSkyAnimator = ObjectAnimator
                .ofInt(m_skyView, "backgroundColor", m_sunsetSkyColor, m_blueSkyColor)
                .setDuration(2000);
        //without this evaluator, your sky will flicker between colors roughly causing a seizure
        sunriseSkyAnimator.setEvaluator(new ArgbEvaluator());

        ObjectAnimator morningSkyAnimator = ObjectAnimator
                .ofInt(m_skyView, "backgroundColor", m_nightSkyColor, m_sunsetSkyColor)
                .setDuration(1500);
        morningSkyAnimator.setEvaluator(new ArgbEvaluator());

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet
                .play(heightAnimator)
                .with(sunriseSkyAnimator)
                .after(morningSkyAnimator);
        animatorSet.start();

        //heightAnimator.start();

        m_sunsetTrue = false;
    }
}
