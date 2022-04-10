package com.dsltyyz.bundle.common.util;

import java.util.Random;

/**
 * Description:
 * 验证码工具类
 *
 * @author: dsltyyz
 * @date: 2019-3-2
 */
public class CodeUtils {
    //大写
    public static final int UPPER = 2;
    //小写
    public static final int LOWER = 1;
    //原生
    public static final int ORIGIN = 0;

    private static char[] CODES = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'j', 'k', 'm',
            'n', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M',
            'N', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    private static Random RANDOM = new Random();

    /**
     * 获取指定长度的验证码
     *
     * @param length 验证码长度
     * @param flag   验证码 0 原数据 1大写 2小写
     * @return String
     */
    public static String getCode(int length, int flag) {
        StringBuffer code = new StringBuffer();
        for (int i = 0; i < length; i++) {
            code.append(CODES[RANDOM.nextInt(CODES.length)]);
        }

        return flag == CodeUtils.ORIGIN ? code.toString()
                : (flag == CodeUtils.UPPER ? code.toString().toUpperCase()
                : code.toString().toLowerCase());
    }

    /**
     * 获取指定长度的数字码
     * @param length
     * @return
     */
    public static String getIntegerCode(int length) {
        StringBuffer code = new StringBuffer();
        for (int i = 0; i < length; i++) {
            code.append(CODES[RANDOM.nextInt(10)]);
        }
        return code.toString();
    }

}
