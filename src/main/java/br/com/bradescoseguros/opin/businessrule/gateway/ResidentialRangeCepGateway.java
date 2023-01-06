package br.com.bradescoseguros.opin.businessrule.gateway;

import br.com.bradescoseguros.opin.domain.rangecep.ResidentialRangeCep;

import java.util.List;

public interface ResidentialRangeCepGateway {

    List<ResidentialRangeCep> findListElegibleProductsByCep(final String zipCode);
}
