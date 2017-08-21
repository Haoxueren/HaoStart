package com.haoxueren.start.common;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * TextWatcher的默认实现类
 */
public abstract class HaoTextWatcher implements TextWatcher {

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
