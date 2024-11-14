package org.dci.assecorassessmentbackend.service;

import org.apache.coyote.BadRequestException;
import org.dci.assecorassessmentbackend.dto.PersonCreateDto;
import org.dci.assecorassessmentbackend.dto.PersonDto;
import org.dci.assecorassessmentbackend.dto.PersonMapper;
import org.dci.assecorassessmentbackend.exception.ResourceNotFoundException;
import org.dci.assecorassessmentbackend.model.Color;
import org.dci.assecorassessmentbackend.model.Person;
import org.dci.assecorassessmentbackend.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonServiceTest {

  @Mock
  private PersonRepository personRepository;

  @Mock
  private PersonMapper personMapper;

  @InjectMocks
  private PersonService personService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getAllPersons_ShouldReturnListOfPersonDtos() {
    Person person = new Person(1L, "John", "Doe", "12345", "Sample City", Color.BLAU);
    PersonDto personDto = new PersonDto(1L, "John", "Doe", "12345", "Sample City", "blau");
    when(personRepository.findAll()).thenReturn(List.of(person));
    when(personMapper.toPersonDto(person)).thenReturn(personDto);

    List<PersonDto> result = personService.getAllPersons();

    assertEquals(1, result.size());
    assertEquals("John", result.getFirst().getFirstName());
  }

  @Test
  void getPersonById_ShouldReturnPersonDto_WhenPersonExists() {
    Person person = new Person(1L, "John", "Doe", "12345", "Sample City", Color.BLAU);
    PersonDto personDto = new PersonDto(1L, "John", "Doe", "12345", "Sample City", "blau");
    when(personRepository.findById(1L)).thenReturn(Optional.of(person));
    when(personMapper.toPersonDto(person)).thenReturn(personDto);

    PersonDto result = personService.getPersonById("1");

    assertNotNull(result);
    assertEquals("John", result.getFirstName());
  }

  @Test
  void getPersonById_ShouldThrowResourceNotFoundException_WhenPersonDoesNotExist() {
    when(personRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(ResourceNotFoundException.class, () -> personService.getPersonById("1"));
  }

  @Test
  void getAllPersonsByColor_ShouldReturnFilteredPersonDtos_WhenColorIsValid() {
    Person person = new Person(1L, "Jane", "Doe", "54321", "Sample City", Color.ROT);
    PersonDto personDto = new PersonDto(1L, "Jane", "Doe", "54321", "Sample City", "rot");
    when(personRepository.findByColor(Color.ROT)).thenReturn(List.of(person));
    when(personMapper.toPersonDto(person)).thenReturn(personDto);

    List<PersonDto> result = personService.getAllPersonsByColor("rot");

    assertEquals(1, result.size());
    assertEquals("rot", result.getFirst().getColor());
  }

  @Test
  void getAllPersonsByColor_ShouldThrowResourceNotFoundException_WhenColorIsInvalid() {
    assertThrows(ResourceNotFoundException.class, () -> personService.getAllPersonsByColor("invalidColor"));
  }

  @Test
  void createPerson_ShouldReturnCreatedPersonDto_WhenDataIsValid() throws BadRequestException {
    PersonCreateDto personCreateDto = new PersonCreateDto("Jane", "Doe", "54321", "Sample City", "blau");
    Person person = new Person(null, "Jane", "Doe", "54321", "Sample City", Color.BLAU);
    Person savedPerson = new Person(1L, "Jane", "Doe", "54321", "Sample City", Color.BLAU);
    PersonDto personDto = new PersonDto(1L, "Jane", "Doe", "54321", "Sample City", "blau");

    when(personMapper.toPerson(personCreateDto)).thenReturn(person);
    when(personRepository.save(person)).thenReturn(savedPerson);
    when(personMapper.toPersonDto(savedPerson)).thenReturn(personDto);

    PersonDto result = personService.createPerson(personCreateDto);

    assertNotNull(result);
    assertEquals("Jane", result.getFirstName());
    assertEquals("blau", result.getColor());
  }

  @Test
  void createPerson_ShouldThrowBadRequestException_WhenColorIsInvalid() {
    PersonCreateDto personCreateDto = new PersonCreateDto("Jane", "Doe", "54321", "Sample City", "invalidColor");

    assertThrows(ResourceNotFoundException.class, () -> personService.createPerson(personCreateDto));
  }
}
