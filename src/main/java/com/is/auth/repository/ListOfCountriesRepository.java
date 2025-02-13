package com.is.auth.repository;

import com.is.auth.model.locations.Countries;
import com.is.auth.model.locations.CountriesDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ListOfCountriesRepository extends JpaRepository<Countries, Long> {

    @Query("SELECT new com.is.auth.model.locations.CountriesDTO(s.id, " +
            "CASE WHEN :language = 'name_ru' THEN s.name_ru " +
            "WHEN :language = 'name_en' THEN s.name_en " +
            "WHEN :language = 'name_uz' THEN s.name_uz END) " +
            "FROM Countries s")
    List<CountriesDTO> findByLanguage(@Param("language") String language);
}



