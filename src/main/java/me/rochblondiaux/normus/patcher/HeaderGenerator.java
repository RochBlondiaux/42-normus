package me.rochblondiaux.normus.patcher;

import lombok.NonNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Roch Blondiaux
 * www.roch-blondiaux.com
 */
public class HeaderGenerator {

    private final static String DEFAULT_HEADER =
            "/* ************************************************************************** */\n" +
                    "/*                                                                            */\n" +
                    "/*                                                        :::      ::::::::   */\n" +
                    "/*   {file}                                             :+:      :+:    :+:   */\n" +
                    "/*                                                    +:+ +:+         +:+     */\n" +
                    "/*   By: {author} <{email}>                         +#+  +:+       +#+        */\n" +
                    "/*                                                +#+#+#+#+#+   +#+           */\n" +
                    "/*   Created: {creation_date} by {author}              #+#    #+#             */\n" +
                    "/*   Updated: {update_date} by {author}               ###   ########.fr       */\n" +
                    "/*                                                                            */\n" +
                    "/* ************************************************************************** */\n";

    public static String generateHeader(@NonNull File file) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("{file}", file.getName());
        placeholders.put("{email}", "marvin@student.42-lyon.fr");
        placeholders.put("{update_date}", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        placeholders.put("{creation_date}", new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date()));
        placeholders.put("{author}", "marvin");
        return replacePlaceholders(placeholders);
    }

    private static String replacePlaceholders(Map<String, String> placeholders) {
        StringBuilder formatted = new StringBuilder();
        for (String line : HeaderGenerator.DEFAULT_HEADER.split("\n")) {
            for (Map.Entry<String, String> placeholder : placeholders.entrySet())
                line = replacePlaceholder(line, placeholder.getKey(), placeholder.getValue());
            formatted.append(line).append("\n");
        }
        return formatted.toString();
    }

    private static String replacePlaceholder(String header, String key, String value) {
        String i;

        if (value.length() < key.length())
            value += " ".repeat(key.length() - value.length());
        i = header.replace(key, value);
        if (value.length() > key.length())
            i = remove_spaces(i, value, value.length() - key.length());
        return i;
    }

    public static String remove_spaces(String str, String replacement, int spaces) {
        int index = str.indexOf(replacement);
        if (index == -1)
            return str;
        index += replacement.length();


        StringBuilder strBuilder = new StringBuilder();
        int founds = 0;
        char[] rmString = str.toCharArray();
        strBuilder.append(str, 0, index);
        for (int i = index; i < rmString.length; i++) {
            if (rmString[i] == ' ' && founds < spaces && (i + 1 >= rmString.length || rmString[i + 1] == ' '))
                founds++;
            else
                strBuilder.append(rmString[i]);
        }
        return strBuilder.toString();
    }
}
