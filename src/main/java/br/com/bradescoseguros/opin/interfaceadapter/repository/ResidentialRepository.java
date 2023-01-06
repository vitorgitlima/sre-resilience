package br.com.bradescoseguros.opin.interfaceadapter.repository;

import br.com.bradescoseguros.opin.domain.residential.ResidentialDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ResidentialRepository extends MongoRepository<ResidentialDomain, String> {

	Page<ResidentialDomain> findByProductCodeIn(final Pageable pageable, final List<String> codes);

}