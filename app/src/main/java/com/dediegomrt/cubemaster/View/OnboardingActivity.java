package com.dediegomrt.cubemaster.View;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dediegomrt.cubemaster.Config.PrefsConfig;
import com.dediegomrt.cubemaster.Methods.PrefsMethods;
import com.dediegomrt.cubemaster.R;

public class OnboardingActivity extends AppCompatActivity {

    private RelativeLayout text;
    private TextView mainText;
    private TextView subText;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        PrefsConfig.getInstance().setContext(this);

        text = (RelativeLayout) findViewById(R.id.text);
        mainText = (TextView) findViewById(R.id.main_text);
        subText = (TextView) findViewById(R.id.sub_text);
        image = (ImageView) findViewById(R.id.image);
        final ImageView cubemasterImage = (ImageView) findViewById(R.id.cubemaster_image);

        final Button button = (Button) findViewById(R.id.onboarding_button);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.onboarding_container);

        TabLayout pageIndicator = (TabLayout) findViewById(R.id.pageIndicator);
        pageIndicator.setupWithViewPager(viewPager, true); // <- magic here

        viewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return PlaceholderFragment.newInstance(position);
            }

            @Override
            public int getCount() {
                return 6;
            }
        });

        viewPager.setPageTransformer(true, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                final float normalizedPosition = Math.abs(Math.abs(position) - 1);
                if(normalizedPosition<0.5){
                    text.setAlpha(1-normalizedPosition*2);
                    image.setAlpha((float)0.5-normalizedPosition);
                }else{
                    text.setAlpha(normalizedPosition*2-1);
                    image.setAlpha(normalizedPosition-(float)0.5);
                }
                image.setRotation(360-position*360);
                cubemasterImage.setRotation(position*360-360);
            }
        });

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {/*Do nothing*/}

            @Override
            public void onPageScrolled( int position , float positionOffset , int positionOffsetPixels ) {
                if(positionOffset > 0.5) {
                    updateText(position + 1);
                    if(position==viewPager.getAdapter().getCount()-2){
                        button.setText(R.string.got_it);
                    }
                    if(position==0){
                        image.setVisibility(View.VISIBLE);
                    }
                }else{
                    updateText(position);
                    if(position==viewPager.getAdapter().getCount()-2){
                        button.setText(R.string.skip);
                    }
                    if(position==0){
                        image.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onPageSelected( int position ) {/*Do nothing*/}
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PrefsMethods.getInstance().setOnboardingShown(true);
                finish();
            }
        });
    }

    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {/*Do nothing*/}

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
                case 4:
                    sectionImage.setBackgroundResource(R.drawable.background_onboarding_5);
                    break;
                case 5:
                    sectionImage.setBackgroundResource(R.drawable.background_onboarding_6);
                    break;
                default: break;
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
                image.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.alarm, null));
                break;
            case 2:
                mainText.setText(R.string.onboarding_main_text_3);
                subText.setText(R.string.onboarding_sub_text_3);
                image.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.assessment, null));
                break;
            case 3:
                mainText.setText(R.string.onboarding_main_text_4);
                subText.setText(R.string.onboarding_sub_text_4);
                image.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.extension, null));
                break;
            case 4:
                mainText.setText(R.string.onboarding_main_text_5);
                subText.setText(R.string.onboarding_sub_text_5);
                image.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.settings, null));
                break;
            case 5:
                mainText.setText(R.string.onboarding_main_text_6);
                subText.setText(R.string.onboarding_sub_text_6);
                image.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.tick, null));
                break;
            default: break;
        }
    }

    @Override
    public void onBackPressed() {
        /*Do nothing*/
    }
}
