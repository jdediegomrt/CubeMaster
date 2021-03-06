package com.jaimedediego.cubemaster.view.customViews;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;

public class FilteredEditText extends android.support.v7.widget.AppCompatEditText {

    public FilteredEditText(Context context) {
        super(context);
        setFilters(new InputFilter[]{new InputFilterMinMax(1, 1000)});
    }

    public FilteredEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFilters(new InputFilter[]{new InputFilterMinMax(1, 1000)});
    }

    public FilteredEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFilters(new InputFilter[]{new InputFilterMinMax(1, 1000)});
    }

    public class InputFilterMinMax implements InputFilter {
        private int min, max;

        InputFilterMinMax(int min, int max) {
            this.min = min;
            this.max = max;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            try {
                int input = Integer.parseInt(dest.toString() + source.toString());
                if (isInRange(min, max, input))
                    return null;
            } catch (NumberFormatException ignored) {

            }
            return "";
        }

        private boolean isInRange(int a, int b, int c) {
            return b > a ? c >= a && c <= b : c >= b && c <= a;
        }
    }
}

