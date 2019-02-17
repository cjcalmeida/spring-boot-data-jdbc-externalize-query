package com.cjcalmeida.spring.data.jdbc.externalize.query.sample;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends CrudRepository<PersonEntity, Integer> {

    @Query(value = "Person.findByFirstname", rowMapperClass = PersonMapper.class)
    List<PersonEntity> findByFirstname(@Param("firstname") String name);

    @Query(value = "Person.findAll", rowMapperClass = PersonMapper.class)
    @Override
    Iterable<PersonEntity> findAll();

    @Modifying
    @Query(value = "Person.updateLastname")
    void updateLastname(@Param("lastname") String lastname, @Param("id") Integer id);
}
