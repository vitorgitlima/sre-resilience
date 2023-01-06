package br.com.bradescoseguros.opin.businessrule.usecase.homeinsurance;

import br.com.bradescoseguros.opin.businessrule.usecase.rangecep.ResidentialRangeCepUseCase;
import br.com.bradescoseguros.opin.businessrule.usecase.residential.ResidentialUseCase;
import br.com.bradescoseguros.opin.businessrule.validator.HomeInsuranceValidator;
import br.com.bradescoseguros.opin.domain.rangecep.ResidentialRangeCep;
import br.com.bradescoseguros.opin.domain.residential.ResidentialDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HomeInsuranceUseCaseImpl implements HomeInsuranceUseCase {

    private final HomeInsuranceValidator homeInsuranceValidator;
    private final ResidentialUseCase residentialUseCase;
    private final ResidentialRangeCepUseCase residentialRangeCepUseCase;

    @Override
    public Page<ResidentialDomain> getResidentialInsuranceProducts(Pageable pageable, String zipCode) {

        homeInsuranceValidator.execute(zipCode, pageable.getPageNumber(), pageable.getPageSize());
        log.debug("Request parameters validated.");

        final List<ResidentialRangeCep> codes = residentialRangeCepUseCase.findListElegibleProductsByCep(zipCode);

        return residentialUseCase.getResidentialProducts(pageable, codes);
    }
}

