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
public class Coverages implements Serializable {

	private String coverageType;
	private String coverageDetail;
	private Boolean coveragePermissionSeparateAcquisition;
	private CoverageAttributes coverageAttributes;
}