package com.deltabase.everphase.localization;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class LocalizationUtil {

    private static String currentLanguage = "en_US";
    private static Map<String, List<LocalizationString>> localizations = new HashMap<>();

    public static void loadFiles(String path) {
        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            paths.forEach(filePath -> {
                List<LocalizationString> strings = new ArrayList<>();
                List<String> lines = null;

                try {
                    lines = Files.readAllLines(filePath);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                lines.forEach(t -> {
                    strings.add(new LocalizationString(t.split("=")[0], t.split("=")[1]));
                    localizations.put(String.valueOf(filePath.getFileName()), strings);
                });
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getLocalizedString(String localizationString) {
        final String[] result = new String[1];
        localizations.get(currentLanguage).forEach(s -> result[0] = localizationString.equals(s.getLocalizationString()) ? s.getLocalizedString() : localizationString);
        return result[0];

    }

}
