package com.jaimedediego.cubemaster.view.activities.onboarding;

import android.animation.LayoutTransition;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jaimedediego.cubemaster.R;
import com.jaimedediego.cubemaster.config.PrefsConfig;
import com.jaimedediego.cubemaster.methods.PrefsMethods;
import com.jaimedediego.cubemaster.view.customViews.CustomViewPager;

public class OnboardingActivity extends AppCompatActivity {

    private RelativeLayout text;
    private TextView mainText;
    private TextView subText;
    private ImageButton arrowRight;
    private ImageButton arrowLeft;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        PrefsConfig.getInstance().setContext(this);

        text = findViewById(R.id.text);
        mainText = findViewById(R.id.main_text);
        subText = findViewById(R.id.sub_text);
        arrowRight = findViewById(R.id.arrow_right);
        arrowLeft = findViewById(R.id.arrow_left);
        image = findViewById(R.id.image);
        final ImageView cubemasterImage = findViewById(R.id.cubemaster_image);

        final Button button = findViewById(R.id.onboarding_button);
        final CustomViewPager viewPager = findViewById(R.id.onboarding_container);
        viewPager.setScrollDuration(500);

        final TabLayout pageIndicator = findViewById(R.id.pageIndicator);
        pageIndicator.setupWithViewPager(viewPager, true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            LayoutTransition arrowsLayoutTransition = ((RelativeLayout) findViewById(R.id.onboarding_button_layout)).getLayoutTransition();
            arrowsLayoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        }

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
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
                if (normalizedPosition < 0.5) {
                    text.setAlpha(1 - normalizedPosition * 2);
                    image.setAlpha((float) 0.5 - normalizedPosition);
                } else {
                    text.setAlpha(normalizedPosition * 2 - 1);
                    image.setAlpha(normalizedPosition - (float) 0.5);
                }
                image.setRotation(360 - position * 360);
                cubemasterImage.setRotation(position * 360 - 360);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
                if (ViewPager.SCROLL_STATE_IDLE == state) {
                    arrowLeft.setEnabled(true);
                    arrowRight.setEnabled(true);
                    arrowLeft.setClickable(true);
                    arrowRight.setClickable(true);
                    viewPager.setSwipeable(true);
                } else {
                    arrowLeft.setEnabled(false);
                    arrowRight.setEnabled(false);
                    arrowLeft.setClickable(false);
                    arrowRight.setClickable(false);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset > 0.5) {
                    updateText(position + 1);
                    if (position == viewPager.getAdapter().getCount() - 2) {
                        button.setText(R.string.got_it);
                        arrowRight.setVisibility(View.GONE);
                    }
                    if (position == 0) {
                        image.setVisibility(View.VISIBLE);
                        arrowLeft.setVisibility(View.VISIBLE);
                    }
                } else {
                    updateText(position);
                    if (position == viewPager.getAdapter().getCount() - 2) {
                        arrowRight.setVisibility(View.VISIBLE);
                        button.setText(R.string.skip);
                    }
                    if (position == 0) {
                        image.setVisibility(View.GONE);
                        arrowLeft.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onPageSelected(int position) {/*Do nothing*/}
        });

        arrowRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setSwipeable(false);
                pageIndicator.getTabAt(viewPager.getCurrentItem() + 1).select();
            }
        });

        arrowLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setSwipeable(false);
                pageIndicator.getTabAt(viewPager.getCurrentItem() - 1).select();
            }
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

            ImageView sectionImage = rootView.findViewById(R.id.section_image);

            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
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
                default:
                    break;
            }
            return rootView;
        }
    }

    private void updateText(int position) {
        switch (position) {
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
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        /*Do nothing*/
    }
}
