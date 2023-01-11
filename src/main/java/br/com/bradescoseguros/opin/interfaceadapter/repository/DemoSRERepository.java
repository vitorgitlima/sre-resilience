package br.com.bradescoseguros.opin.interfaceadapter.repository;

import br.com.bradescoseguros.opin.domain.demosre.DemoSRE;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DemoSRERepository extends MongoRepository<DemoSRE, Integer> {
}
