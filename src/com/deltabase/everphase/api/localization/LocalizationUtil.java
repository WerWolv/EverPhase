package com.deltabase.everphase.api.localization;

import com.deltabase.everphase.api.Log;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalizationUtil {

    private static String currentLanguage = "en_US";
    private static Map<String, List<LocalizationString>> localizations = new HashMap<>();

    public static void loadFiles(String path) {
        File folder = new File(path);
        File[] langFiles = folder.listFiles();

        for (File langFile : langFiles) {
            if (!langFile.isFile()) return;

            List<LocalizationString> localizationStrings = new ArrayList<>();

            try {
                List<String> lines = Files.readAllLines(langFile.toPath());

                lines.forEach(line -> {
                    String[] tokens = line.split("=");
                    localizationStrings.add(new LocalizationString(tokens[0], tokens[1]));
                });
                localizations.put(langFile.getName().replace(".lang", ""), localizationStrings);
                Log.i("Localization", "Localization file \"" + langFile.getName() + "\" loaded!");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean setLanguage(String language) {
        if (localizations.keySet().contains(language)) {
            LocalizationUtil.currentLanguage = language;
            Log.i("Localization", "Language set to " + language + "!");
            return true;
        }

        return false;
    }

    public static String getLocalizedString(String localizationString) {
        for (LocalizationString loc : localizations.get(currentLanguage)) {
            if (loc.getLocalizationString().equals(localizationString))
                return loc.getLocalizedString();
        }

        return localizationString;

    }

}
