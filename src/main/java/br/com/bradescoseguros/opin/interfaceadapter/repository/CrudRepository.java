package br.com.bradescoseguros.opin.interfaceadapter.repository;

import br.com.bradescoseguros.opin.domain.DemoSRE;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CrudRepository extends MongoRepository<DemoSRE, Integer> {
}
