package com.mammon.merchant.utils;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class CodeGen {

    private static final char[] array1 = {
            'P', 'm', 'A', 'b', 'I', 'c', 'J', 'z', 'H', 'd',
            'a', '6', 'p', 'W', 'O', 'e', 'Q', 'f', '7', 'K',
            'h', 'C', 'i', '5', 'j', 'R', 'g', 'D', 'k', 'Z',
            'M', '8', 'n', '4', 'F', 'L', 'E', 'o', '9', 'X',
            'r', 'T', 's', 'B', '3', 't', 'V', 'G', 'u', 'Y',
            'v', '2', 'w', 'x', '1', 'y', 'U', '0', 'N', 'l',
            'S', 'q'
    };

    private static final char[] array2 = {
            'q', 'w', 'e', 'r', 't', 'Y', 'U', 'I', 'O', 'P',
            'a', 's', 'd', 'f', 'g', 'H', 'J', 'K', 'L',
            'z', 'x', 'c', 'v', 'B', 'N', 'M',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'Q', 'W', 'E', 'R', 'T', 'y', 'u', 'i', 'o', 'p',
            'A', 'S', 'D', 'F', 'G', 'h', 'j', 'k', 'l',
            'Z', 'X', 'C', 'V', 'b', 'n', 'm'
    };

    private static final char[] array3 = {
            '0', '6', '9', '8', '3', '7', '4', '2', '5', '1',
    };

    public static String convert(long number) {
        long rest = number;
        Stack<Character> stack = new Stack<>();
        StringBuilder result = new StringBuilder(10);
        do {
            stack.add(array3[new Long((rest - (rest / array3.length) * array3.length)).intValue()]);
            rest = rest / array3.length;
        } while (rest != 0);

        while (!stack.isEmpty()) {
            result.append(stack.pop());
        }

        return result.toString();
    }

    public static void main(String[] args) {
        LocalDate staticNow = LocalDate.of(2023, 1, 1);
        LocalDate now = LocalDate.now();
        String year = String.valueOf(staticNow.getYear() - now.getYear());
        String nowStr = LocalDate.now().format(DateTimeFormatter.ofPattern("MMdd"));
        Set<String> s = new HashSet<>();
        for (int i = 1; i <= 1000000; i++) {
            String tempStr = StringUtils.leftPad(String.valueOf(i), 5, "0");
            String code = convert(Long.parseLong(year + nowStr + tempStr));
            System.out.println(code);
            s.add(code);
        }
        System.out.println(s.size());
    }
}
