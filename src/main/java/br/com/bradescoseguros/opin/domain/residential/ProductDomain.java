package br.com.bradescoseguros.opin.domain.residential;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDomain implements Serializable {

	private String name;
	private String code;
	private List<PropertyCharacteristics> propertyCharacteristics;
	private String propertyZipCode;
	private Boolean protective;
	private List<String> additional;
	private List<PremiumPayment> premiumPayments;
	private String additionalOthers;
	private List<ComplementaryAssistanceService> assistanceServices;
	private List<TermsAndConditions> termsAndConditions;
	private List<Validity> validity;
	private List<String> customerServices;
	private List<String> premiumRates;
	private List<MinimumRequirement> minimumRequirements;
	private List<String> targetAudiences;
	private List<Coverages> coverages;

}