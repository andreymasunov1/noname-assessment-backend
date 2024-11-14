package org.dci.assecorassessmentbackend.config;

import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.dci.assecorassessmentbackend.model.Person;
import org.dci.assecorassessmentbackend.repository.PersonRepository;
import org.dci.assecorassessmentbackend.service.DataSourceReader;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DataLoader {

  private final DataSourceReader dataSourceReader;
  private final PersonRepository personRepository;

  /**
   * Constructor-based dependency injection for DataLoader.
   * @param dataSourceReader Service responsible for reading data source.
   * @param personRepository Repository for Person entities.
   */
  public DataLoader(DataSourceReader dataSourceReader, PersonRepository personRepository) {
    this.dataSourceReader = dataSourceReader;
    this.personRepository = personRepository;
  }

  /**
   * Initializes data loading process after bean construction.
   * Reads data from the data source and saves it to the repository.
   * Logs errors if the data loading process encounters issues.
   */
  @PostConstruct
  public void loadData() {
    try {
      List<Person> personList = dataSourceReader.readData();
      savePersons(personList);
      log.info("Data successfully loaded. Total records: {}", personList.size());
    } catch (DataAccessException e) {
      log.error("Database error occurred while saving data: {}", e.getMessage(), e);
    } catch (Exception e) {
      log.error("Unexpected error occurred during data loading: {}", e.getMessage(), e);
    }
  }

  /**
   * Persists a list of Person entities in the database.
   * @param personList List of Person entities to save.
   */
  private void savePersons(List<Person> personList) {
    if (personList == null || personList.isEmpty()) {
      log.warn("No data to load. The person list is empty or null.");
      return;
    }
    personRepository.saveAll(personList);
  }
}
