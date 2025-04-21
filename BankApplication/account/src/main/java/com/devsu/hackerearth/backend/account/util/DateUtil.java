package com.devsu.hackerearth.backend.account.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.commons.lang3.StringUtils;

public class DateUtil {
    DateUtil() {
    }

    public static final String YYYY_MM_DD = "yyyy-MM-dd";

    public static String formatDate(LocalDate date, String format) {
        String dateFormat = null;

        if (date != null && StringUtils.isNotBlank(format)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            dateFormat = formatter.format(date);

        }

        return dateFormat;

    }

}