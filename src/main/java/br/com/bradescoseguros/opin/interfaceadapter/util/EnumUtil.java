package br.com.bradescoseguros.opin.interfaceadapter.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EnumUtil {

	private static final String FROM_VALUE = "fromValue";
	private static final String LOG_MESSAGE = "%s, for %s";

	public static Object getEnumValue(@NonNull final Class<?> clazz, final String value) {
		try {
			Method method = ReflectionUtils.findMethod(clazz, FROM_VALUE, String.class);
			return method != null ? method.invoke(null, value) : null;
		} catch (Exception e) {
			log.error(String.format(LOG_MESSAGE, e.getCause().getMessage(), clazz.getSimpleName()));
			return null;
		}
	}
}