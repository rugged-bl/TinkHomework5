package com.seriabov.fintecharch.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.TextView;

import com.seriabov.fintecharch.R;

public class TextColoringUtil {
    public static void applyPercentStyleAndText(TextView textView, double percentChange) {
        Context context = textView.getContext();

        textView.setText(context.getString(R.string.percent_format, percentChange));
        if (percentChange > 0) {
            textView.setTextColor(ContextCompat.getColor(context, R.color.green700));
        } else {
            textView.setTextColor(ContextCompat.getColor(context, R.color.red700));
        }
    }
}
