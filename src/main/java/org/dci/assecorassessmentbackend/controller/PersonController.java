package org.dci.assecorassessmentbackend.controller;

import java.util.List;
import org.apache.coyote.BadRequestException;
import org.dci.assecorassessmentbackend.dto.PersonCreateDto;
import org.dci.assecorassessmentbackend.dto.PersonDto;
import org.dci.assecorassessmentbackend.service.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/persons")
public class PersonController {

  private final PersonService personService;

  /**
   * Constructor-based dependency injection for PersonController.
   *
   * @param personService Service responsible for handling person-related operations.
   */
  public PersonController(PersonService personService) {
    this.personService = personService;
  }

  /**
   * Retrieves all persons.
   *
   * @return ResponseEntity containing a list of all PersonDto objects and HTTP status 200.
   */
  @GetMapping
  public ResponseEntity<List<PersonDto>> getAllPersons() {
    List<PersonDto> results = personService.getAllPersons();
    return ResponseEntity.ok(results);
  }

  /**
   * Retrieves a person by their ID.
   *
   * @param id The ID of the person to retrieve.
   * @return ResponseEntity containing the PersonDto and HTTP status 200.
   */
  @GetMapping("/{id}")
  public ResponseEntity<PersonDto> getPersonById(@PathVariable("id") String id) {
    PersonDto personDto = personService.getPersonById(id);
    return ResponseEntity.ok(personDto);
  }

  /**
   * Retrieves all persons filtered by a specific color.
   *
   * @param color The color to filter by.
   * @return ResponseEntity containing a list of PersonDto objects filtered by color and HTTP status
   * 200.
   */
  @GetMapping("/color/{color}")
  public ResponseEntity<List<PersonDto>> getPersonsByColor(@PathVariable("color") String color) {
    List<PersonDto> personsByColor = personService.getAllPersonsByColor(color);
    return ResponseEntity.ok(personsByColor);
  }

  /**
   * Creates a new person.
   *
   * @param personCreateDto The data transfer object containing person details.
   * @return ResponseEntity containing the created PersonDto and HTTP status 201.
   */
  @PostMapping
  public ResponseEntity<PersonDto> createPerson(@RequestBody PersonCreateDto personCreateDto)
      throws BadRequestException {
    PersonDto personDto = personService.createPerson(personCreateDto);
    return ResponseEntity.status(HttpStatus.CREATED).body(personDto);
  }
}
