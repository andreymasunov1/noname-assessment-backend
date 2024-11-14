package org.dci.assecorassessmentbackend.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Objects;
import org.apache.coyote.BadRequestException;
import org.dci.assecorassessmentbackend.dto.PersonCreateDto;
import org.dci.assecorassessmentbackend.dto.PersonDto;
import org.dci.assecorassessmentbackend.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class PersonControllerTest {

  @Mock
  private PersonService personService;

  @InjectMocks
  private PersonController personController;

  private PersonDto personDto;
  private PersonCreateDto personCreateDto;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    personDto = new PersonDto();
    personDto.setId(1L);
    personDto.setFirstName("John");
    personDto.setLastName("Doe");
    personDto.setZipCode("12345");
    personDto.setCity("Sample City");
    personDto.setColor("blau");

    personCreateDto = new PersonCreateDto();
    personCreateDto.setFirstName("John");
    personCreateDto.setLastName("Doe");
    personCreateDto.setZipCode("12345");
    personCreateDto.setCity("Sample City");
    personCreateDto.setColor("blau");
  }

  @Test
  void getAllPersons_ShouldReturnListOfPersonDtoAndStatus200() {
    when(personService.getAllPersons()).thenReturn(List.of(personDto));

    ResponseEntity<List<PersonDto>> response = personController.getAllPersons();

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, Objects.requireNonNull(response.getBody()).size());
    assertEquals(personDto, response.getBody().getFirst());
  }

  @Test
  void getPersonById_ShouldReturnPersonDtoAndStatus200() {
    String personId = "1";
    when(personService.getPersonById(personId)).thenReturn(personDto);

    ResponseEntity<PersonDto> response = personController.getPersonById(personId);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(personDto, response.getBody());
  }

  @Test
  void getPersonsByColor_ShouldReturnListOfPersonDtoAndStatus200() {
    String color = "blau";
    when(personService.getAllPersonsByColor(color)).thenReturn(List.of(personDto));

    ResponseEntity<List<PersonDto>> response = personController.getPersonsByColor(color);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(1, Objects.requireNonNull(response.getBody()).size());
    assertEquals(personDto, response.getBody().getFirst());
  }

  @Test
  void createPerson_ShouldReturnCreatedPersonDtoAndStatus201() throws BadRequestException {
    when(personService.createPerson(personCreateDto)).thenReturn(personDto);

    ResponseEntity<PersonDto> response = personController.createPerson(personCreateDto);

    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertEquals(personDto, response.getBody());
  }
}
