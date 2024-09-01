package com.ithome._demo.common.utils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DateUtil {
    private static final ZoneId ZONE_ID = ZoneId.systemDefault();

    /**
     * LocalDate -> Date
     */
    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZONE_ID).toInstant());
    }

    /**
     * Date -> LocalDate
     */
    public static LocalDate toLocalDate(Date date) {
        return date.toInstant().atZone(ZONE_ID).toLocalDate();
    }

    public static class DatePattern {
        public static final String YEAR = "yyyy";
        public static final String MONTH = "MM";
        public static final String YEAR_MONTH_DASH = "yyyy-MM";
        public static final String YEAR_MONTH_SLASH = "yyyy/MM";
        public static final String DATE_DASH = "yyyy-MM-dd";
        public static final String DATE_SLASH = "yyyy/MM/dd";
        public static final String DATE_NO_DASH = "yyyyMMdd";
        public static final String DATE_NOTIFY = "yyy年MM月dd日";
        public static final String YEAR_MONTH_NOTIFY = "yyyy年MM月";
    }

    /**
     * LocalDate 格式化
     */
    public static String formatDate(LocalDate date, String pattern) {
        // 定義日期格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return date.format(formatter);
    }
}
