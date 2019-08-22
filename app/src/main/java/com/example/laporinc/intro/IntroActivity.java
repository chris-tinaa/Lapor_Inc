package com.example.laporinc.intro;

import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.laporinc.R;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;
import com.github.paolorotolo.appintro.model.SliderPage;

public class IntroActivity extends AppIntro{

//    FirstIntroFragment firstFragment = new FirstIntroFragment();
//    SecondIntroFragment secondFragment = new SecondIntroFragment();
//    ThirdIntroFragment thirdFragment = new ThirdIntroFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Note here that we DO NOT use setContentView();

        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
//        addSlide(firstFragment);
//        addSlide(secondFragment);
//        addSlide(thirdFragment);

        // Instead of fragments, you can also use our default slide.
        // Just create a `SliderPage` and provide title, description, background and image.
        // AppIntro will do the rest.
        SliderPage sliderPage = new SliderPage();
        sliderPage.setTitle("Recent");
        sliderPage.setDescription("Halaman \"Recent\" berisi laporan yang telah diverifikasi.");
        sliderPage.setImageDrawable( R.drawable.ic_home_white_150dp);
        sliderPage.setBgColor(Color.parseColor("#4593EE"));
        addSlide( AppIntroFragment.newInstance(sliderPage));


        SliderPage sliderPage2 = new SliderPage();
        sliderPage2.setTitle("Lapor!");
        sliderPage2.setDescription("Tekan tombol \"Lapor!\" untuk membuat laporan.");
        sliderPage2.setImageDrawable( R.drawable.ic_add_circle_white_150dp);
        sliderPage2.setBgColor(Color.parseColor("#4593EE"));
        addSlide( AppIntroFragment.newInstance(sliderPage2));



        SliderPage sliderPage3 = new SliderPage();
        sliderPage3.setTitle("Akun");
        sliderPage3.setDescription("Halaman informasi mengenai akun kamu.");
        sliderPage3.setImageDrawable( R.drawable.ic_person_white_150dp);
        sliderPage3.setBgColor(Color.parseColor("#4593EE"));
        addSlide( AppIntroFragment.newInstance(sliderPage3));


//
//        // OPTIONAL METHODS
//        // Override bar/separator color.
//        setBarColor(Color.parseColor("#3F51B5"));
//        setSeparatorColor(Color.parseColor("#2196F3"));
//
//        // Hide Skip/Done button.
//        showSkipButton(false);
//        setProgressButtonEnabled(false);
//
//        // Turn vibration on and set intensity.
//        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
//        setVibrate(true);
//        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.

        finish();

    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.

        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }

//    @Override
//    public int getDefaultBackgroundColor() {
//        return Color.parseColor("#4593EE");
//    }
//
//    @Override
//    public void setBackgroundColor(int backgroundColor) {
//    // Set the background color of the view within your slide to which the transition should be applied.
//        if (this != null) {
//            this.setBackgroundColor(backgroundColor);
//        }
//    }
}
