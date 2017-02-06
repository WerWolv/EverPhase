package com.deltabase.everphase.localization;

public class LocalizationString {
    private String localizationString, localizedString;

    public LocalizationString(String localizationString, String localizedString) {
        this.localizationString = localizationString;
        this.localizedString = localizedString;
    }

    public String getLocalizationString() {
        return localizationString;
    }

    public String getLocalizedString() {
        return localizedString;
    }
}
