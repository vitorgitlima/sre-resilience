package br.com.bradescoseguros.opin.interfaceadapter.util;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class EnumUtilTest {

	public static final String VALUE_ONE = "one";

	@Test
	@Tag("unit")
	void getEnumValue_whenClazzParameterIsNull_shouldThrowNullPointerException() {
		//Act
		assertThrows(NullPointerException.class, () -> EnumUtil.getEnumValue(null, "value"));
	}

	@Test
	@Tag("unit")
	void getEnumValue_whenEnumDoesNotHaveFromValueMethod_shouldReturnNull() {
		//Act
		assertThat(EnumUtil.getEnumValue(BaseEnumTest.class, "value")).isNull();
	}

	@Test
	@Tag("unit")
	void getEnumValue_whenValueParameterIsNull_shouldReturnNull() {
		//Act
		assertThat(EnumUtil.getEnumValue(EnumTest.class, null)).isNull();
	}

	@Test
	@Tag("unit")
	void getEnumValue_whenValidParameters_shouldReturnEnumValue() {

		//Act
		final EnumTest one = (EnumTest) EnumUtil.getEnumValue(EnumTest.class, VALUE_ONE);

		//Assert
		assertThat(one).isNotNull();
		assertThat(EnumTest.fromValue(VALUE_ONE)).isEqualTo(one);
		assertThat(one.getValue()).isEqualTo(VALUE_ONE);
	}

	@Test
	@Tag("unit")
	void getEnumValue_whenInvalidValue_shouldReturnNull() {

		//Act
		final EnumTest one = (EnumTest) EnumUtil.getEnumValue(EnumTest.class, "invalid");

		//Assert
		assertThat(one).isNull();
	}

	public enum EnumTest {

		VALUE_ONE("one"),
		VALUE_TWO("two");

		private final String value;

		EnumTest(final String value) {
	     this.value = value;
		}

		public String getValue() {
			return value;
		}

		public static EnumTest fromValue(final String value) {
			for (EnumTest test : EnumTest.values()) {
				if (test.value.equals(value)) {
					return test;
				}
			}
			throw new IllegalArgumentException("Unexpected value '" + value + "'");
		}
	}

	public enum BaseEnumTest {
	}
}