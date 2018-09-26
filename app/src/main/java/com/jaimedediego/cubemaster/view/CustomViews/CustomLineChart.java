package com.jaimedediego.cubemaster.view.CustomViews;

import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.jaimedediego.cubemaster.methods.StatsMethods;
import com.jaimedediego.cubemaster.utils.Constants;

public class CustomLineChart extends LineChart {

    public CustomLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDoubleTapToZoomEnabled(false);
        setScaleYEnabled(false);

        getLegend().setEnabled(false);
        getAxisLeft().setDrawLabels(false);
        getAxisLeft().setDrawGridLines(false);
        getXAxis().setDrawLabels(false);
        getXAxis().setDrawGridLines(false);

        getAxisRight().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return StatsMethods.getInstance().formatMillis(value, Constants.getInstance().SECS_FORMATTING);
            }
        });

        setDescription(null);
    }
}
