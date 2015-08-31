package com.imrub.shoulder.widget;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

public class TextWatcherImp implements TextWatcher{

	public static final int MaxNumber = 50;
	
	private CharSequence temp;
    private int selectionEnd ;

    private EditText mEditText;
    private TextView mNumberLimit;
    
    private int maxNumber = MaxNumber;
    
    public TextWatcherImp(EditText text, TextView number, int maxNumber){
    	this.mEditText = text;
    	this.mNumberLimit = number;
    	this.maxNumber = maxNumber;
    }
    
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
	}
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		temp = s;
	}
	
	@Override
	public void afterTextChanged(Editable s) {
		selectionEnd = mEditText.getSelectionEnd();
        if(temp.length() > maxNumber){
        	s.delete(maxNumber, temp.length());
            mEditText.setSelection(maxNumber);
            mNumberLimit.setText("0");
        } else {
        	if(selectionEnd < maxNumber){
        		mEditText.setSelection(selectionEnd);
        	}
        	int n = maxNumber - s.length();
        	mNumberLimit.setText(n+"");
        }		
	}
	
}
