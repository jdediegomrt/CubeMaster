package com.dediegomrt.cubemaster.View;

import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dediegomrt.cubemaster.Config.PrefsConfig;
import com.dediegomrt.cubemaster.Methods.PrefsMethods;
import com.dediegomrt.cubemaster.R;

public class OnboardingActivity extends AppCompatActivity {

    private TextView mainText;
    private TextView subText;
    private RelativeLayout text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        PrefsConfig.getInstance().setContext(this);

        mainText = (TextView) findViewById(R.id.main_text);
        subText = (TextView) findViewById(R.id.sub_text);
        text = (RelativeLayout) findViewById(R.id.text);
        final ImageView cubemasterImage = (ImageView) findViewById(R.id.cubemaster_image);

        final Button button = (Button) findViewById(R.id.gotit_button);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.onboarding_container);

        TabLayout pageIndicator = (TabLayout) findViewById(R.id.pageIndicator);
        pageIndicator.setupWithViewPager(viewPager, true); // <- magic here

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrefsMethods.getInstance().setOnboardingShown(true);
                finish();
            }
        });

        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return PlaceholderFragment.newInstance(position);
            }

            @Override
            public int getCount() {
                return 4;
            }
        });

        viewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                final float normalizedPosition = Math.abs(Math.abs(position) - 1);
                if(normalizedPosition<0.5){
                    text.setAlpha(1-normalizedPosition*2);
                    button.setAlpha(1-normalizedPosition*2);
                }else{
                    text.setAlpha(normalizedPosition*2-1);
                    button.setAlpha(normalizedPosition*2-1);
                }
                cubemasterImage.setRotation(360-position*360);
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}

            @Override
            public void onPageScrolled( int position , float positionOffset , int positionOffsetPixels ) {
                if(positionOffset > 0.5) {
                    updateText(position + 1);
                    if(position==viewPager.getAdapter().getCount()-2){
                        button.setVisibility(View.VISIBLE);
                    }
                }else{
                    updateText(position);
                    if(position==viewPager.getAdapter().getCount()-2){
                        button.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onPageSelected( int position ) {}
        });
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            ImageView sectionImage = (ImageView) rootView.findViewById(R.id.section_image);

            switch(getArguments().getInt(ARG_SECTION_NUMBER)){
                case 0:
                    sectionImage.setBackgroundResource(R.drawable.background_onboarding_1);
                    break;
                case 1:
                    sectionImage.setBackgroundResource(R.drawable.background_onboarding_2);
                    break;
                case 2:
                    sectionImage.setBackgroundResource(R.drawable.background_onboarding_3);
                    break;
                case 3:
                    sectionImage.setBackgroundResource(R.drawable.background_onboarding_4);
                    break;
            }
            return rootView;
        }
    }

    private void updateText(int position) {
        switch(position){
            case 0:
                mainText.setText(R.string.onboarding_main_text_1);
                subText.setText(R.string.onboarding_sub_text_1);
                break;
            case 1:
                mainText.setText(R.string.onboarding_main_text_2);
                subText.setText(R.string.onboarding_sub_text_2);
                break;
            case 2:
                mainText.setText(R.string.onboarding_main_text_3);
                subText.setText(R.string.onboarding_sub_text_3);
                break;
            case 3:
                mainText.setText(R.string.onboarding_main_text_4);
                subText.setText(R.string.onboarding_sub_text_4);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        /*Do nothing*/
    }
}
