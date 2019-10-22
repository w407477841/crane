package com.xingyun.equipment.admin.core.util;

import java.util.Random;

/**
 *
 * 随机生成验证码
 * @author hjy
 */
public class AuthCode {

    public static String getRandNum(int charCount) {
        String charValue = "";
        for (int i = 0; i < charCount; i++) {
            char c = (char) (randomInt(0, 10) + '0');
            charValue += String.valueOf(c);
        }
        return charValue;
    }
    private static int randomInt(int from, int to) {
        Random r = new Random();
        return from + r.nextInt(to - from);
    }


    /*public static void main(String[] args) {
        System.out.println(getRandNum(6));
    }*/

}
