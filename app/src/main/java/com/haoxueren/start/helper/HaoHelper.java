package com.haoxueren.start.helper;

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
}
