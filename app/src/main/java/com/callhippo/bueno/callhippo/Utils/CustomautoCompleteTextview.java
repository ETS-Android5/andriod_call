package com.callhippo.bueno.callhippo.Utils;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.MultiAutoCompleteTextView;

public class CustomautoCompleteTextview extends androidx.appcompat.widget.AppCompatMultiAutoCompleteTextView {

    private int myThreshold;

    public CustomautoCompleteTextview(Context context) {
        super(context);
    }

    public CustomautoCompleteTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomautoCompleteTextview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setThreshold(int threshold) {
        if (threshold <= 0) {
            threshold = 0;
        }
        myThreshold = threshold;
    }
    //if threshold   is 0 than return true
    public boolean enoughToFilter() {
        return true;
    }
    //invoke on focus
//    protected void onFocusChanged(boolean focused, int direction,
//                                  Rect previouslyFocusedRect) {
//        //skip space and backspace
//        super.performFilteri  ng("", 67);
//        // TODO Auto-generated method stub
//        super.onFocusChanged(focused, direction, previouslyFocusedRect);
//
//    }

    protected void performFiltering(CharSequence text, int keyCode) {
        // TODO Auto-generated method stub
        super.performFiltering(text, keyCode);
    }

    public int getThreshold() {
        return myThreshold;
    }
}
