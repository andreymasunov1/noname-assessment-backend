package org.dci.assecorassessmentbackend.service;

import java.util.List;
import org.apache.coyote.BadRequestException;
import org.dci.assecorassessmentbackend.dto.PersonCreateDto;
import org.dci.assecorassessmentbackend.dto.PersonDto;
import org.dci.assecorassessmentbackend.dto.PersonMapper;
import org.dci.assecorassessmentbackend.exception.ResourceNotFoundException;
import org.dci.assecorassessmentbackend.model.Color;
import org.dci.assecorassessmentbackend.model.Person;
import org.dci.assecorassessmentbackend.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class PersonService {

  private final PersonRepository personRepository;
  private final PersonMapper personMapper;

  @Autowired
  public PersonService(PersonRepository personRepository, PersonMapper personMapper) {
    this.personRepository = personRepository;
    this.personMapper = personMapper;
  }

  /**
   * Retrieves all persons and maps them to PersonDto.
   *
   * @return List of PersonDto
   */
  @Transactional(readOnly = true)
  public List<PersonDto> getAllPersons() {
    return personRepository.findAll().stream()
        .map(this::mapToPersonDto)
        .toList();
  }

  /**
   * Retrieves a person by ID.
   *
   * @param id The ID of the person.
   * @return PersonDto of the person with the specified ID.
   * @throws ResponseStatusException if the person is not found or if mapping fails.
   */
  @Transactional(readOnly = true)
  public PersonDto getPersonById(String id) {
    Long personId = parseId(id);
    Person person = personRepository.findById(personId)
        .orElseThrow(() -> new ResourceNotFoundException("Person not found with ID: " + id));
    return mapToPersonDto(person);
  }

  /**
   * Retrieves all persons filtered by a specific color.
   *
   * @param color The color to filter by.
   * @return List of PersonDto filtered by the specified color.
   * @throws ResourceNotFoundException if the color is not recognized.
   */
  @Transactional(readOnly = true)
  public List<PersonDto> getAllPersonsByColor(String color) {
    Color colorEnum = parseColor(color);
    return personRepository.findByColor(colorEnum).stream()
        .map(this::mapToPersonDto)
        .toList();
  }

  /**
   * Creates a new person.
   *
   * @param personCreateDto The data transfer object containing person details.
   * @return The created person's PersonDto.
   * @throws ResponseStatusException if an invalid color is provided or mapping fails.
   */
  @Transactional
  public PersonDto createPerson(PersonCreateDto personCreateDto) throws BadRequestException {
    Color colorEnum = parseColor(personCreateDto.getColor());
    Person person = personMapper.toPerson(personCreateDto);
    person.setColor(colorEnum);
    Person createdPerson = personRepository.save(person);
    return mapToPersonDto(createdPerson);
  }

  /**
   * Maps a Person entity to PersonDto and handles mapping exceptions.
   *
   * @param person The person entity.
   * @return Mapped PersonDto.
   * @throws ResponseStatusException if mapping fails.
   */
  private PersonDto mapToPersonDto(Person person) {
    try {
      return personMapper.toPersonDto(person);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to map person to PersonDto",
          e);
    }
  }

  /**
   * Parses and validates a color from its string representation.
   *
   * @param color The color string.
   * @return The corresponding Color enum.
   * @throws ResourceNotFoundException if the color is invalid.
   */
  private Color parseColor(String color) {
    try {
      return Color.fromDisplayName(color);
    } catch (IllegalArgumentException e) {
      throw new ResourceNotFoundException("Invalid color: " + color);
    }
  }

  /**
   * Parses and validates an ID from its string representation.
   *
   * @param id The ID string.
   * @return The parsed Long ID.
   * @throws ResponseStatusException if the ID is invalid.
   */
  private Long parseId(String id) {
    try {
      return Long.valueOf(id);
    } catch (NumberFormatException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID format: " + id, e);
    }
  }
}
