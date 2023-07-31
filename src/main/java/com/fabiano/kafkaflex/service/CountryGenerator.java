package com.fabiano.kafkaflex.service;

import java.util.Locale;
import java.util.SplittableRandom;

public class CountryGenerator {

  private CountryGenerator() {
    throw new IllegalStateException("Utility class");
  }

  public static String generate() {
    String[] countryCodes = Locale.getISOCountries();
    int randomIndex = new SplittableRandom().nextInt(0, countryCodes.length - 1);
    return new Locale("", countryCodes[randomIndex]).getDisplayCountry();
  }
}
