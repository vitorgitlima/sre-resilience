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
public class PropertyCharacteristics implements Serializable {

	private String propertyType;
	private String propertyBuildType;
	private String propertyUsageType;
	private String destinationInsuredImportance;

}