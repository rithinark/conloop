package com.mock.conloop.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.mock.conloop.constant.ContextConstant;

public class DateTimeUtils {

    public static LocalDateTime getDate(String date) {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern(ContextConstant.TIMESTAMP_FORMAT));
    }

}
