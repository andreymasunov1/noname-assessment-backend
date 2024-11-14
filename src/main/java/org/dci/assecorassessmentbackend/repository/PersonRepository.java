package org.dci.assecorassessmentbackend.repository;

import java.util.List;
import org.dci.assecorassessmentbackend.model.Color;
import org.dci.assecorassessmentbackend.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

  List<Person> findByColor(Color color);
}
