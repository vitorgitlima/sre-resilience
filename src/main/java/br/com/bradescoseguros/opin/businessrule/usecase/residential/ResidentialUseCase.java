package br.com.bradescoseguros.opin.businessrule.usecase.residential;

import br.com.bradescoseguros.opin.domain.rangecep.ResidentialRangeCep;
import br.com.bradescoseguros.opin.domain.residential.ResidentialDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ResidentialUseCase {

    Page<ResidentialDomain> getResidentialProducts(final Pageable pageable, final List<ResidentialRangeCep> residentialRange);
}
