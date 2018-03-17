package com.dediegomrt.cubemaster.Config;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.dediegomrt.cubemaster.R;
import com.dediegomrt.cubemaster.Utils.Session;

import java.util.Arrays;
import java.util.List;

public class ThemeConfig {

    private final List<Integer> colors = Arrays.asList(
            R.drawable.spinner_of_colors_red,
            R.drawable.spinner_of_colors_pink,
            R.drawable.spinner_of_colors_purple,
            R.drawable.spinner_of_colors_deep_purple,
            R.drawable.spinner_of_colors_indigo,
            R.drawable.spinner_of_colors_blue,
            R.drawable.spinner_of_colors_light_blue,
            R.drawable.spinner_of_colors_cyan,
            R.drawable.spinner_of_colors_green,
            R.drawable.spinner_of_colors_teal,
            R.drawable.spinner_of_colors_light_green,
            R.drawable.spinner_of_colors_lime,
            R.drawable.spinner_of_colors_yellow,
            R.drawable.spinner_of_colors_orange,
            R.drawable.spinner_of_colors_deep_orange,
            R.drawable.spinner_of_colors_brown,
            R.drawable.spinner_of_colors_grey,
            R.drawable.spinner_of_colors_blue_grey
    );

    private static ThemeConfig instance;

    private ThemeConfig() {
    }

    public static ThemeConfig getInstance() {
        if (instance == null) {
            instance = new ThemeConfig();
        }
        return instance;
    }

    private AppCompatActivity activity;
    private Session session = Session.getInstance();

    public void setActivity(AppCompatActivity activity){
        this.activity = activity;
    }
    
    public void initConfig(){
        final Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        toolbar.getOverflowIcon().setTint(Color.WHITE);
        toolbar.setTitleTextColor(Color.WHITE);
        activity.setSupportActionBar(toolbar);

        switch(PrefsConfig.getInstance().prefs.getInt("colorAccent", 0)){
            case 0:
                activity.setTheme(R.style.AppTheme_CustomBarRed);
                session.darkColorTheme=R.color.md_red_900;
                session.lightColorTheme=R.color.md_red_500;
                break;
            case 1:
                activity.setTheme(R.style.AppTheme_CustomBarPink);
                session.darkColorTheme=R.color.md_pink_900;
                session.lightColorTheme=R.color.md_pink_500;
                break;
            case 2:
                activity.setTheme(R.style.AppTheme_CustomBarPurple);
                session.darkColorTheme=R.color.md_purple_900;
                session.lightColorTheme=R.color.md_purple_500;
                break;
            case 3:
                activity.setTheme(R.style.AppTheme_CustomBarDeepPurple);
                session.darkColorTheme=R.color.md_deep_purple_900;
                session.lightColorTheme=R.color.md_deep_purple_500;
                break;
            case 4:
                activity.setTheme(R.style.AppTheme_CustomBarIndigo);
                session.darkColorTheme=R.color.md_indigo_900;
                session.lightColorTheme=R.color.md_indigo_500;
                break;
            case 5:
                activity.setTheme(R.style.AppTheme_CustomBarBlue);
                session.darkColorTheme=R.color.md_blue_900;
                session.lightColorTheme=R.color.md_blue_500;
                break;
            case 6:
                activity.setTheme(R.style.AppTheme_CustomBarLightBlue);
                session.darkColorTheme=R.color.md_light_blue_900;
                session.lightColorTheme=R.color.md_light_blue_500;
                break;
            case 7:
                activity.setTheme(R.style.AppTheme_CustomBarCyan);
                session.darkColorTheme=R.color.md_cyan_900;
                session.lightColorTheme=R.color.md_cyan_500;
                break;
            case 8:
                activity.setTheme(R.style.AppTheme_CustomBarGreen);
                session.darkColorTheme=R.color.md_green_900;
                session.lightColorTheme=R.color.md_green_500;
                break;
            case 9:
                activity.setTheme(R.style.AppTheme_CustomBarTeal);
                session.darkColorTheme=R.color.md_teal_900;
                session.lightColorTheme=R.color.md_teal_500;
                break;
            case 10:
                activity.setTheme(R.style.AppTheme_CustomBarLightGreen);
                session.darkColorTheme=R.color.md_light_green_900;
                session.lightColorTheme=R.color.md_light_green_500;
                break;
            case 11:
                activity.setTheme(R.style.AppTheme_CustomBarLime);
                session.darkColorTheme=R.color.md_lime_900;
                session.lightColorTheme=R.color.md_lime_500;
                break;
            case 12:
                activity.setTheme(R.style.AppTheme_CustomBarYellow);
                session.darkColorTheme=R.color.md_amber_600;
                session.lightColorTheme=R.color.md_yellow_A700;
                break;
            case 14:
                activity.setTheme(R.style.AppTheme_CustomBarDeepOrange);
                session.darkColorTheme=R.color.md_deep_orange_900;
                session.lightColorTheme=R.color.md_deep_orange_500;
                break;
            case 15:
                activity.setTheme(R.style.AppTheme_CustomBarBrown);
                session.darkColorTheme=R.color.md_brown_900;
                session.lightColorTheme=R.color.md_brown_500;
                break;
            case 16:
                activity.setTheme(R.style.AppTheme_CustomBarGrey);
                session.darkColorTheme=R.color.md_grey_900;
                session.lightColorTheme=R.color.md_grey_500;
                break;
            case 17:
                activity.setTheme(R.style.AppTheme_CustomBarBlueGrey);
                session.darkColorTheme=R.color.md_blue_grey_900;
                session.lightColorTheme=R.color.md_blue_grey_500;
                break;
            default:
                activity.setTheme(R.style.AppTheme_CustomBarOrange);
                session.darkColorTheme=R.color.md_orange_900;
                session.lightColorTheme=R.color.md_orange_500;
                break;
        }
    }

    public List<Integer> colors(){
        return colors;
    }
}
