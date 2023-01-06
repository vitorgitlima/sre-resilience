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
public class TermsAndConditions implements Serializable {

	private String susepProcessNumber;
	private String definition;
}