package br.com.bradescoseguros.opin.businessrule.usecase.homeinsurance;

import br.com.bradescoseguros.opin.domain.residential.ResidentialDomain;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface HomeInsuranceUseCase {

    Page<ResidentialDomain> getResidentialInsuranceProducts(final Pageable pageable, final String zipCode);

}
