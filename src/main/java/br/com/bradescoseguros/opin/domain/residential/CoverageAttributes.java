package br.com.bradescoseguros.opin.domain.residential;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoverageAttributes implements Serializable {

	private CoverageAttributesDetails minLMI;
	private CoverageAttributesDetails maxLMI;
	private CoverageAttributesDetails minDeductibleAmount;
	private Integer insuredMandatoryParticipationPercentage;
}