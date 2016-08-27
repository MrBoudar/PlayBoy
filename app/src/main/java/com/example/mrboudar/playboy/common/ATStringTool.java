package com.example.mrboudar.playboy.common;

/**
 * Created by jsion on 16/7/4.
 */

public class ATStringTool {
    private ATStringTool() {
    }
    /**
     * Get target String
     *
     * @param mParams the String need to append
     * @return String
     */
    public static String concatString(String... mParams) {
        StringBuilder builder = new StringBuilder();
        if (mParams.length > 0) {
            for (String mParam : mParams) {
                if (!isEmpty(mParam))
                    builder.append(mParam);
                else
                    builder.append("");
            }
        }
        return builder.toString();
    }

    public static boolean isEmpty(String s) {
        return s == null || s.isEmpty();
    }
}
