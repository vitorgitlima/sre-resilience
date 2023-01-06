package br.com.bradescoseguros.opin.businessrule.usecase.rangecep;

import br.com.bradescoseguros.opin.businessrule.exception.NoContentException;
import br.com.bradescoseguros.opin.businessrule.gateway.ResidentialRangeCepGateway;
import br.com.bradescoseguros.opin.businessrule.messages.MessageSourceService;
import br.com.bradescoseguros.opin.domain.rangecep.ResidentialRangeCep;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResidentialRangeCepUseCaseImpl implements ResidentialRangeCepUseCase {

    private final ResidentialRangeCepGateway residentialRangeCepGateway;
    private final MessageSourceService messageSourceService;

    private static final String KEY_EMPTY_PRODUCTS_FOR_ZIPCODE = "residential-range-cep.empty-products-for-zipcode";

    @Override
    public List<ResidentialRangeCep> findListElegibleProductsByCep(@NonNull String zipcode) {

        var listOfElegibleProducts = residentialRangeCepGateway.findListElegibleProductsByCep(zipcode);

        if (CollectionUtils.isEmpty(listOfElegibleProducts)) {
            throw new NoContentException(messageSourceService.getMessage(KEY_EMPTY_PRODUCTS_FOR_ZIPCODE));
        }

        return listOfElegibleProducts;
    }
}
