package com.example.demo.utils;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class Helpers {
    public String formatDateString(String[] inputPatterns, String inputDateString, String outputPattern) {
        String formatedString = "";
        for (String inputPattern : inputPatterns) {
            // Định dạng của chuỗi đầu vào
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern(inputPattern);
            // Định dạng mong muốn cho đầu ra
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern(outputPattern);
            try {
                // Phân tích chuỗi đầu vào thành LocalDateTime
                LocalDateTime dateTime = LocalDateTime.parse(inputDateString, inputFormatter);
                // Định dạng LocalDateTime thành chuỗi với định dạng mong muốn
                formatedString = dateTime.format(outputFormatter);
                break;
            } catch (DateTimeParseException e) {
                System.out.printf("\n>>> ::::::::::formatDateString:::::::::::( \n");
                System.out.print(e);
                System.out.printf("\n>>> ::::::::::formatDateString:::::::::::) \n");
            }
        }
        return formatedString;
    }
}
