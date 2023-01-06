package br.com.bradescoseguros.opin.businessrule.usecase.rangecep;

import br.com.bradescoseguros.opin.domain.rangecep.ResidentialRangeCep;

import java.util.List;

public interface ResidentialRangeCepUseCase {

    List<ResidentialRangeCep> findListElegibleProductsByCep(String zipcode);
}
