package com.franklyn.alc.cryptocash.helper.io;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;

import com.franklyn.alc.cryptocash.R;
import com.franklyn.alc.cryptocash.app.AppController;
import com.franklyn.alc.cryptocash.calculate.fragment.CalculateFragment;

/**
 * Created by AGBOMA franklyn on 10/16/17.
 */

public class CustomWatcher implements TextWatcher {

    private Context context;
    private TextInputEditText inputEditText;
    private Button button;
    private String selectedType = "";
    private String type = "";

    public CustomWatcher(){
    }

    public CustomWatcher(Context context, TextInputEditText inputEditText,
                         Button button, String selectedType, String type) {
        this.context = context;
        this.inputEditText = inputEditText;
        this.button = button;
        this.selectedType = selectedType;
        this.type = type;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence string, int start, int before, int count) {
        String getChar = string.toString();
        if(type.equalsIgnoreCase(CalculateFragment.EDIT_VALUE)){
            if(CalculateFragment.checked && !getChar.isEmpty()){
                button.setBackgroundResource(R.drawable.green_button);
                CalculateFragment.notEmpty = true;
            }
            else if(CalculateFragment.checked && getChar.isEmpty()){
                button.setBackgroundResource(R.drawable.not_active);
                CalculateFragment.notEmpty = false;
            }
            else if(!CalculateFragment.checked && getChar.isEmpty()){
                button.setBackgroundResource(R.drawable.not_active);
                CalculateFragment.notEmpty = false;
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
