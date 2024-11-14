package org.dci.assecorassessmentbackend.service;

import org.dci.assecorassessmentbackend.model.Color;
import org.dci.assecorassessmentbackend.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.io.ClassPathResource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CsvFileReaderTest {

  @InjectMocks
  private CsvFileReader csvFileReader;

  @Mock
  private ClassPathResource classPathResource;

  private static final String VALID_CSV_LINE = "Doe,John,12345 Sample City,1";
  private static final String MULTILINE_DATA_PART1 = "Doe,John";
  private static final String MULTILINE_DATA_PART2 = "12345 Sample City,1";
  private static final String INVALID_CSV_LINE = "Doe,John,Sample City";

  @BeforeEach
  void setUp() {
    csvFileReader = new CsvFileReader();
  }

  @Test
  void readData_ShouldReturnListOfPersons_WhenCSVIsValid() throws Exception {
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new ClassPathResource("sample-input.csv").getInputStream()));
    assertNotNull(bufferedReader);

    List<Person> persons = csvFileReader.readData();

    assertNotNull(persons);
    assertFalse(persons.isEmpty());
  }

  @Test
  void mergeMultiLineData_ShouldMergeLines_WhenMultiLineDataDetected() {
    Optional<String> mergedLine = csvFileReader.mergeMultiLineData(MULTILINE_DATA_PART2, MULTILINE_DATA_PART1);

    assertTrue(mergedLine.isPresent());
    assertEquals("Doe,John 12345 Sample City,1", mergedLine.get().trim());
  }

  @Test
  void mergeMultiLineData_ShouldReturnLine_WhenNoPreviousLineExists() {
    Optional<String> mergedLine = csvFileReader.mergeMultiLineData(VALID_CSV_LINE, null);

    assertTrue(mergedLine.isPresent());
    assertEquals(VALID_CSV_LINE, mergedLine.get());
  }

  @Test
  void parsePerson_ShouldReturnPerson_WhenInputIsValid() {
    String[] parts = VALID_CSV_LINE.split(",");

    Person person = csvFileReader.parsePerson(parts);

    assertNotNull(person);
    assertEquals("Doe", person.getLastName());
    assertEquals("John", person.getFirstName());
    assertEquals("12345", person.getZipCode());
    assertEquals("Sample City", person.getCity());
    assertEquals(Color.BLAU, person.getColor());  // Assumes Color 1 maps to "blau"
  }

  @Test
  void extractZipCode_ShouldReturnZipCode_WhenValidInput() {
    String zipCode = csvFileReader.extractZipCode("12345 Sample City");

    assertEquals("12345", zipCode);
  }

  @Test
  void extractZipCode_ShouldThrowException_WhenInvalidInput() {
    assertThrows(IllegalArgumentException.class, () -> csvFileReader.extractZipCode("Invalid City Format"));
  }

  @Test
  void extractCity_ShouldReturnCity_WhenValidInput() {
    String city = csvFileReader.extractCity("12345 Sample City");

    assertEquals("Sample City", city);
  }

  @Test
  void extractCity_ShouldThrowException_WhenInvalidInput() {
    assertThrows(IllegalArgumentException.class, () -> csvFileReader.extractCity("Invalid Zip Code"));
  }

  @Test
  void parseColor_ShouldReturnColor_WhenValidCode() {
    Color color = csvFileReader.parseColor("1");
    assertEquals(Color.BLAU, color);  // Assumes 1 corresponds to Color.BLAU
  }

  @Test
  void parseColor_ShouldThrowException_WhenInvalidCode() {
    assertThrows(IllegalArgumentException.class, () -> csvFileReader.parseColor("InvalidColor"));
  }
}
