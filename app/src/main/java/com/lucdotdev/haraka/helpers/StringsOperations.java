package com.lucdotdev.haraka.helpers;


import android.widget.EditText;
public class StringsOperations {


    public StringsOperations(){}

    public boolean isEmpty(EditText s){
        return s.getText().toString().trim().length() <= 0;
    }

    public boolean isValidEmail(EditText s){
        return s.getText().toString().contains("@");
    }
    public boolean isValidPhone(EditText s){
        return s.getText().toString().trim().length() == 9;
    }

    public boolean isValidpassword(EditText s){return s.getText().toString().trim().length()>=6;}
}
