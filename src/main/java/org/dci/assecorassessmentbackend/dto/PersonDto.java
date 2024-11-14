package org.dci.assecorassessmentbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDto {

  private Long id;

  @JsonProperty("name")
  private String firstName;
  @JsonProperty("lastname")
  private String lastName;
  @JsonProperty("zipcode")
  private String zipCode;
  private String city;
  private String color;
}
