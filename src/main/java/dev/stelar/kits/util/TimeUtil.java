package dev.stelar.kits.util;

import lombok.experimental.UtilityClass;

import java.time.Duration;

@UtilityClass
public class TimeUtil {

    public Duration parse(String time) {
        if (time == null || time.isBlank()) {
            throw new IllegalArgumentException("Time cannot be null or empty");
        }

        time = time.toLowerCase().trim();

        long value;
        char unit;

        try {
            value = Long.parseLong(time.substring(0, time.length() - 1));
            unit = time.charAt(time.length() - 1);
        } catch (NumberFormatException | StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("Invalid time format: " + time, e);
        }

        return switch (unit) {
            case 's' -> Duration.ofSeconds(value);
            case 'm' -> Duration.ofMinutes(value);
            case 'h' -> Duration.ofHours(value);
            case 'd' -> Duration.ofDays(value);
            case 'w' -> Duration.ofDays(value * 7);
            default -> throw new IllegalArgumentException("Unknown time unit: " + unit);
        };
    }

}
