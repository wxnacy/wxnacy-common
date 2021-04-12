package com.wxnacy.common.logger;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class SimpleLoggerFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        return String.format("%s [%s] [%s:%s] %s\n",
                DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss.SSS").format(ZonedDateTime.now()),
                record.getLevel(),
                record.getSourceClassName(),
                record.getSourceMethodName(),
                record.getMessage()
        );
    }
}
