package br.com.bradescoseguros.opin.businessrule.usecase.residential;

import br.com.bradescoseguros.opin.businessrule.exception.NoContentException;
import br.com.bradescoseguros.opin.businessrule.gateway.ResidentialGateway;
import br.com.bradescoseguros.opin.businessrule.messages.MessageSourceService;
import br.com.bradescoseguros.opin.domain.rangecep.ResidentialRangeCep;
import br.com.bradescoseguros.opin.domain.residential.ResidentialDomain;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResidentialUseCaseImpl implements ResidentialUseCase {

	private final ResidentialGateway residentialGateway;
	private final MessageSourceService messageSourceService;

	private static final String KEY_EMPTY_PRODUCTS_FOR_ZIPCODE = "residential-range-cep.empty-products-for-zipcode";

	@Override
	public Page<ResidentialDomain> getResidentialProducts(final Pageable pageable, final List<ResidentialRangeCep> residentialRange) {

		final List<String> range = extractProductCodeListFromRangeCeps(residentialRange);
		final Page<ResidentialDomain> productPage = residentialGateway.findByProductCodes(pageable, range);

		checkContentListResponse(productPage.getContent());

		return productPage;
	}

	private void checkContentListResponse(final List<ResidentialDomain> productList) {
		if(CollectionUtils.isEmpty(productList)) {
			throw new NoContentException(messageSourceService.getMessage(KEY_EMPTY_PRODUCTS_FOR_ZIPCODE));
		}
	}

	private List<String> extractProductCodeListFromRangeCeps(final List<ResidentialRangeCep> range) {
		return range
						.stream()
						.map(ResidentialRangeCep::getProductCode)
						.distinct()
						.collect(Collectors.toList());
	}
}