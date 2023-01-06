package br.com.bradescoseguros.opin.domain.residential;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoverageAttributesDetails implements Serializable {

	private BigDecimal amount;
	private CoverageAttributesDetailsUnit unit;
}