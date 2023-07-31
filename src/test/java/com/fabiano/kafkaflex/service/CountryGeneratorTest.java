package com.fabiano.kafkaflex.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class CountryGeneratorTest {
  @Mock
  private Locale mockedLocale;

  @InjectMocks
  private CountryGenerator countryGenerator;

  @Test
  void countryGenerator_generateCountryName() {
    String generatedCountry = CountryGenerator.generate();

    assertNotNull(generatedCountry);
    assertFalse(generatedCountry.isEmpty());
  }

  @Test
  void countryGenerator_generatedCountryIsInValidSet() {
    String countryName = CountryGenerator.generate();

    Set<String> validCountryNames = generateValidCountryNamesSet();

    assertTrue(validCountryNames.contains(countryName));
  }

  @Test
  void countryGenerator_testRandomness() {
    Set<String> generatedCountryNames = new HashSet<>();
    for (int i = 0; i < 1000; i++) {
      generatedCountryNames.add(CountryGenerator.generate());
    }
    assertTrue(generatedCountryNames.size() > 1, "Multiple unique country names should be generated.");
  }

  private Set<String> generateValidCountryNamesSet() {
    String[] countryCodes = Locale.getISOCountries();
    Set<String> validCountryNames = new HashSet<>();
    for (String countryCode : countryCodes) {
      validCountryNames.add(new Locale("", countryCode).getDisplayCountry());
    }
    return validCountryNames;
  }

}