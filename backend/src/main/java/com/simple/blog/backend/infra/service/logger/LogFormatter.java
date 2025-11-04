package com.simple.blog.backend.infra.service.logger;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class LogFormatter {

    private static final String LINE = "───────────────────────────────────────────────────────────────";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss z");

    public static String startSection(String title) {
        return String.format("%n%s%n🔧 %s%nTimestamp: %s%n%s",
                LINE,
                title.toUpperCase(),
                ZonedDateTime.now().format(formatter),
                LINE);
    }

    public static String endSection(String status) {
        return String.format("%n🚧 %s%n%s", status, LINE);
    }

    public static String listItems(String emoji, String label, List<String> items) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s %s%n", emoji, label));
        for (String item : items) {
            sb.append("   - ").append(item).append(System.lineSeparator());
        }
        return sb.toString();
    }

}
