package br.com.bradescoseguros.opin.interfaceadapter.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NumberUtil {

	public static BigDecimal formatBigDecimalNumber(final BigDecimal value) {
		if (value == null) {
			return null;
		}
		return value.setScale(2, RoundingMode.UNNECESSARY);
	}
}