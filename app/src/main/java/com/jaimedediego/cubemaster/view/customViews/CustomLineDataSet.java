package com.jaimedediego.cubemaster.view.customViews;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.jaimedediego.cubemaster.methods.StatsMethods;
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

        setValueFormatter(new MillisecondsFormatter());
    }
}

class MillisecondsFormatter implements IValueFormatter {

    MillisecondsFormatter() {
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return StatsMethods.getInstance().formatMillis(value);
    }
}