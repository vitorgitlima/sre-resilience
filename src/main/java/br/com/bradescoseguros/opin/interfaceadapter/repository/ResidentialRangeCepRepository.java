package br.com.bradescoseguros.opin.interfaceadapter.repository;

import br.com.bradescoseguros.opin.domain.rangecep.ResidentialRangeCep;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ResidentialRangeCepRepository extends MongoRepository<ResidentialRangeCep, String> {

    List<ResidentialRangeCep> findByPostalCodeStartLessThanEqualAndPostalCodeEndGreaterThanEqual(final String init, final String end);

}