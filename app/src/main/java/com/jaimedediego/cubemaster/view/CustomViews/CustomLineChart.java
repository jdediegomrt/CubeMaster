package com.jaimedediego.cubemaster.view.CustomViews;

import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;

public class CustomLineChart extends LineChart {

    public CustomLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDoubleTapToZoomEnabled(false);
        setScaleYEnabled(false);

        getLegend().setEnabled(false);
        getAxisLeft().setDrawLabels(false);
        getAxisLeft().setDrawAxisLine(false);
        getAxisLeft().setDrawGridLines(false);
        getXAxis().setDrawLabels(false);
        getXAxis().setDrawAxisLine(false);
        getXAxis().setDrawGridLines(false);

        Description description = new Description();
        description.setText("Times chart");
        setDescription(description);
    }

    public CustomLineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
}
