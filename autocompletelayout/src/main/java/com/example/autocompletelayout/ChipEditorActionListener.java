package com.example.autocompletelayout;

import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;


public class ChipEditorActionListener implements EditText.OnEditorActionListener {

    private EditText editText;

    public ChipEditorActionListener(EditText editText) {
        this.editText = editText;
    }

    public boolean onEditorAction(TextView exampleView, int actionId, KeyEvent event) {
        Log.e("Test", "onEditorAction: Called finally " +actionId );
        Log.e("Test", "onEditorAction: Called finally " +EditorInfo.IME_ACTION_DONE );
        Log.e("Test", "onEditorAction: Called finally " +event );
        try{
            if (actionId == EditorInfo.IME_ACTION_DONE ) {
                if (editText.getText() != null && editText.getText().toString().length() > 0){
                    editText.setText(editText.getText().toString()+",");

                }
            }
        }catch (Exception e){}
        return true;
    }
}
