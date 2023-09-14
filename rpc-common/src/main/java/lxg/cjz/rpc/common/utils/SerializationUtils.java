package lxg.cjz.rpc.common.utils;

import java.util.stream.IntStream;

/**
 * @author russel
 * @version 1.0.0
 * @date 2023/9/14
 * @description
 */
public class SerializationUtils {
    private static final String PADDING_STRING = "0";

    /**
     * 约定序列化类型的最大长度为16
     */
    public static final int MAX_SERIALIZATION_TYPE_COUNT = 16;

    public static String getPaddingString(String str) {
        str = transNullToEmpty(str);
        if (str.length() >= MAX_SERIALIZATION_TYPE_COUNT) {
            return str;
        }
        int paddingCount = MAX_SERIALIZATION_TYPE_COUNT - str.length();
        StringBuilder paddingString = new StringBuilder(str);
        IntStream.range(0, paddingCount)
                .forEach((i)->{
                    paddingString.append(PADDING_STRING);
                });
        return paddingString.toString();
    }

    public static String getSubString(String str) {
        str = transNullToEmpty(str);
        return str.replace(PADDING_STRING, "");
    }


    public static String transNullToEmpty(String str) {
        return str == null ? "" : str;
    }
}
