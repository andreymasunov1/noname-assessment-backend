package org.dci.assecorassessmentbackend.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.dci.assecorassessmentbackend.model.Color;
import org.dci.assecorassessmentbackend.model.Person;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CsvFileReader implements DataSourceReader {

  private static final String FILE_PATH = "sample-input.csv";
  private static final String CSV_DELIMITER = ",";
  private static final int EXPECTED_COLUMNS = 4;
  private static final Pattern ZIP_CODE_PATTERN = Pattern.compile("^(\\d{5})\\s+(.+)$");

  @Override
  public List<Person> readData() {
    List<Person> persons = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(
        new InputStreamReader(new ClassPathResource(FILE_PATH).getInputStream()))) {

      String line;
      String previousLine = null;

      while ((line = reader.readLine()) != null) {
        Optional<String> mergedLine = mergeMultiLineData(line, previousLine);
        if (mergedLine.isEmpty()) {
          previousLine = line;
          log.info("There is multiline data: {}", previousLine);
          continue;
        }

        String[] parts = mergedLine.get().split(CSV_DELIMITER);
        if (parts.length != EXPECTED_COLUMNS) {
          log.warn("Invalid line format: {}", mergedLine.get());
          continue;
        }

        try {
          Person person = parsePerson(parts);
          persons.add(person);
          previousLine = null;
        } catch (IllegalArgumentException e) {
          log.warn("Failed to parse person data: {}", e.getMessage());
        }
      }

    } catch (Exception e) {
      log.error("Error reading CSV file: {}", e.getMessage(), e);
    }

    return persons;
  }

  /**
   * Merges multi-line data if the current line starts with a space.
   */
  public Optional<String> mergeMultiLineData(String line, String previousLine) {
    if (previousLine != null) {
      return Optional.of(previousLine + " " + line.trim());
    } else if (line.trim().isEmpty() || line.split(CSV_DELIMITER).length < EXPECTED_COLUMNS) {
      return Optional.empty();
    }
    return Optional.of(line);
  }

  /**
   * Parses a Person object from an array of CSV parts.
   */
  public Person parsePerson(String[] parts) {
    String lastName = parts[0].trim();
    String firstName = parts[1].trim();
    String cityZipCodePart = parts[2].trim();
    String colorNumber = parts[3].trim();

    String zipCode = extractZipCode(cityZipCodePart);
    String city = extractCity(cityZipCodePart);
    Color color = parseColor(colorNumber);

    return new Person(null, firstName, lastName, zipCode, city, color);
  }

  /**
   * Extracts the zip code using a regex pattern.
   */
  public String extractZipCode(String cityZipCodePart) {
    Matcher matcher = ZIP_CODE_PATTERN.matcher(cityZipCodePart);
    if (matcher.find()) {
      return matcher.group(1);
    }
    throw new IllegalArgumentException("Invalid zip code format: " + cityZipCodePart);
  }

  /**
   * Extracts the city name using a regex pattern.
   */
  public String extractCity(String cityZipCodePart) {
    Matcher matcher = ZIP_CODE_PATTERN.matcher(cityZipCodePart);
    if (matcher.find()) {
      return matcher.group(2).replaceAll("-\\*$", "").trim();
    }
    throw new IllegalArgumentException("Invalid city format: " + cityZipCodePart);
  }

  /**
   * Parses the color from a numeric string value.
   */
  public Color parseColor(String colorNumber) {
    try {
      int colorCode = Integer.parseInt(colorNumber);
      return Color.fromCode(colorCode);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Invalid color number: " + colorNumber);
    }
  }
}
