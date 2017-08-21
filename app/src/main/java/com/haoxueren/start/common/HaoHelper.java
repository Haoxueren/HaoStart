package com.haoxueren.start.common;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * 当前项目的帮助类
 */
public class HaoHelper {

    /**
     * 获取汉字词组/句子的首字母缩写
     */
    public static String getFirstLetter(String chinese) {
        StringBuilder builder = new StringBuilder();
        char[] charArray = chinese.toCharArray();
        for (char c : charArray) {
            String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(c);
            char firstLetter = (pinyin == null) ? c : pinyin[0].charAt(0);
            builder.append(firstLetter);
        }
        return builder.toString();
    }


    /** 隐藏软键盘 */
    public static void hiddenSoftKeyboard(View view) {
        String serviceName = Context.INPUT_METHOD_SERVICE;
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(serviceName);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /** 获取系统服务 */
    public static <T> T getSystemService(Context context, String serviceName) {
        return (T) context.getSystemService(serviceName);
    }


}
