package br.com.bradescoseguros.opin.interfaceadapter.util;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


@ExtendWith(MockitoExtension.class)
class NumberUtilTest {

	@Test
	@Tag("unit")
	void formatBigDecimalNumber_withNullValue_shouldReturnNull() {
		assertThat(NumberUtil.formatBigDecimalNumber(null)).isNull();
	}

	@Test
	@Tag("unit")
	void formatBigDecimalNumber_withInvalidValue_shouldThrowsNumberFormatException() {
		assertThrows(NumberFormatException.class, () -> NumberUtil.formatBigDecimalNumber(new BigDecimal("abc")));
	}

	@Test
	@Tag("unit")
	void formatBigDecimalNumber_withValidValue_shouldReturnFormattedValue() {
		//Arrange
		final BigDecimal value = new BigDecimal("120.00");

		//Act
		final BigDecimal response = NumberUtil.formatBigDecimalNumber(value);

		//Assert
		assertThat(response).isEqualTo(value);
	}

	@Test
	@Tag("unit")
	void formatBigDecimalNumber_withInvalidScale_shouldReturnValueWithCorrectScale() {
		//Arrange
		final BigDecimal value = new BigDecimal("120.0");

		//Act
		final BigDecimal response = NumberUtil.formatBigDecimalNumber(value);

		//Asserts
		assertThat(response).isEqualTo("120.00");
	}
}