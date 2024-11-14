package org.dci.assecorassessmentbackend.dto;

import org.dci.assecorassessmentbackend.model.Color;
import org.dci.assecorassessmentbackend.model.Person;
import org.mapstruct.Mapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Mapper(componentModel = "spring")
public class PersonMapper {

  /**
   * Converts a PersonCreateDto to a Person entity.
   *
   * @param dto The PersonCreateDto containing the data for the new Person entity.
   * @return The converted Person entity.
   * @throws ResponseStatusException if the color in the DTO is invalid.
   */
  public Person toPerson(PersonCreateDto dto) {
    Person person = new Person();
    person.setFirstName(dto.getFirstName());
    person.setLastName(dto.getLastName());
    person.setZipCode(dto.getZipCode());
    person.setCity(dto.getCity());

    person.setColor(parseColor(dto.getColor()));

    return person;
  }

  /**
   * Converts a Person entity to a PersonDto.
   *
   * @param person The Person entity to convert.
   * @return The converted PersonDto.
   * @throws ResponseStatusException if the color in the Person entity is invalid.
   */
  public PersonDto toPersonDto(Person person) {
    PersonDto dto = new PersonDto();
    dto.setId(person.getId());
    dto.setFirstName(person.getFirstName());
    dto.setLastName(person.getLastName());
    dto.setZipCode(person.getZipCode());
    dto.setCity(person.getCity());

    dto.setColor(parseColorDisplayName(person.getColor()));

    return dto;
  }

  /**
   * Parses a color from its display name and handles invalid values.
   *
   * @param color The color display name.
   * @return The Color enum.
   * @throws ResponseStatusException if the color is invalid.
   */
  private Color parseColor(String color) {
    try {
      return Color.fromDisplayName(color);
    } catch (IllegalArgumentException ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid color: " + color, ex);
    }
  }

  /**
   * Retrieves the display name of a Color, handling any invalid enum state.
   *
   * @param color The Color enum.
   * @return The color's display name.
   * @throws ResponseStatusException if the color is invalid.
   */
  private String parseColorDisplayName(Color color) {
    try {
      return color.getDisplayName();
    } catch (IllegalArgumentException ex) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid color: " + color, ex);
    }
  }
}
