package com.jaimedediego.cubemaster.view.customViews;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.jaimedediego.cubemaster.utils.Session;

import java.util.List;

public class CustomLineDataSet extends LineDataSet {
    public CustomLineDataSet(List<Entry> yVals, String label) {
        super(yVals, label);
        setHighLightColor(Session.getInstance().getLighterColorTheme());
        setColor(Session.getInstance().getDarkColorTheme());
        setCircleColor(Session.getInstance().getDarkColorTheme());
        setCircleRadius(2f);
        setDrawValues(false);
    }
}