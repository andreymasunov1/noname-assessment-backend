package org.dci.assecorassessmentbackend.service;

import java.util.List;
import org.dci.assecorassessmentbackend.model.Person;

public interface DataSourceReader {
  List<Person> readData();
}
