package br.com.bradescoseguros.opin.dummy;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ReflectionUtils;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Stream;

import static br.com.bradescoseguros.opin.dummy.ObjectRandomUtil.*;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DummyObjectsUtil {

    @Getter
    @AllArgsConstructor
    protected enum ImplementedCollections {

        LIST(List.class, Arrays.asList(
                List.class,
                ArrayList.class)),
        SET(Set.class, Arrays.asList(
                Set.class,
                HashSet.class)),
        MAP(Map.class, Arrays.asList(
                Map.class,
                HashMap.class));

        private Class<?> primitiveClass;

        private List<Class<?>> compatibleClassList;
    }

    @Data
    @NoArgsConstructor
    protected static class SelfReferenceHandler {

        protected final List<String> classReferenceList = new ArrayList<>();

        protected SelfReferenceHandler(List<String> classReferenceList) {
            this.classReferenceList.addAll(classReferenceList);
        }

        protected <E> void addClass(Class<E> clazz) {
            this.classReferenceList.add(clazz.getSimpleName());
        }

        protected <E> boolean isSelfReference(Class<E> clazz) {
            return this.classReferenceList.contains(clazz.getSimpleName());
        }

        protected <E> E newInstance(final Class<E> clazz) {
            try {
                final E instance = createInstance(clazz);
                final SelfReferenceHandler newSelfReferenceHandler = new SelfReferenceHandler(this.getClassReferenceList());
                newSelfReferenceHandler.addClass(instance.getClass());
                return populateObject(instance, newSelfReferenceHandler);
            } catch (IllegalArgumentException | SecurityException ex) {
                String error = "Error creating new instance without self reference";
                log.error(error, ex);
                throw new DummyObjectCreateInstanceException(error);
            }
        }

        protected boolean isNotSelfReference(Class<?> instanceClass) {
            return !this.isSelfReference(instanceClass);
        }
    }

    public static <E> E newInstance(final Class<E> clazz) {
        final SelfReferenceHandler selfReferenceHandler = new SelfReferenceHandler();
        return selfReferenceHandler.newInstance(clazz);
    }

    public static void populate(final Object instance) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        final SelfReferenceHandler selfReferenceHandler = new SelfReferenceHandler();
        DummyObjectsUtil.populate(instance, selfReferenceHandler);
    }

    private static void populate(final Object instance,
                                 final SelfReferenceHandler selfReferenceHandler) throws IllegalArgumentException, SecurityException, IllegalAccessException, InstantiationException, NoSuchFieldException {
        final List<Field> allDeclaredFields = filterNonFinal(getAllDeclaredFields(instance.getClass(), new ArrayList<>()));
        for (final Field field : allDeclaredFields) {
            ReflectionUtils.makeAccessible(field);
            final Class<?> fieldClass = field.getType();
            if (String.class.isAssignableFrom(fieldClass)) {
                populateString(instance, field);
            } else if (long.class.isAssignableFrom(fieldClass) || Long.class.isAssignableFrom(fieldClass)) {
                populateLong(instance, field);
            } else if (int.class.isAssignableFrom(fieldClass) || Integer.class.isAssignableFrom(fieldClass)) {
                populateInteger(instance, field);
            } else if (double.class.isAssignableFrom(fieldClass) || Double.class.isAssignableFrom(fieldClass)) {
                populateDouble(instance, field);
            } else if (boolean.class.isAssignableFrom(fieldClass) || Boolean.class.isAssignableFrom(fieldClass)) {
                field.set(instance, nextBoolean());
            } else if (LocalDateTime.class.isAssignableFrom(fieldClass)) {
                field.set(instance, LocalDateTime.now());
            } else if (LocalDate.class.isAssignableFrom(fieldClass)) {
                field.set(instance, LocalDate.now());
            } else if (Date.class.isAssignableFrom(fieldClass)) {
                field.set(instance, new Date());
            } else if(OffsetDateTime.class.isAssignableFrom(fieldClass)) {
                field.set(instance, OffsetDateTime.now());
            } else if (Enum.class.isAssignableFrom(fieldClass)) {
                field.set(instance, ObjectRandomUtil.randomItemEnum((Class) field.getType()));
            } else if (BigDecimal.class.isAssignableFrom(fieldClass)) {
                populateBigDecimal(instance, field);
            } else if (BigInteger.class.isAssignableFrom(fieldClass)) {
                populateBigInteger(instance, field);
            } else if (UUID.class.isAssignableFrom(fieldClass)) {
                field.set(instance, ObjectRandomUtil.nextUUID());
            } else {
                handleEntityOrCollection(instance, selfReferenceHandler, field, fieldClass);
            }
        }
    }

    private static void handleEntityOrCollection(final Object instance,
                                                 final SelfReferenceHandler selfReferenceHandler,
                                                 final Field field,
                                                 final Class<?> fieldClass) throws IllegalAccessException, NoSuchFieldException {
        if (List.class.isAssignableFrom(fieldClass)) {
            populateList(instance, selfReferenceHandler, field);
        } else if (Set.class.isAssignableFrom(fieldClass) || HashSet.class.isAssignableFrom(fieldClass)) {
            populateSet(instance, selfReferenceHandler, field);
        } else if (Map.class.isAssignableFrom(fieldClass) || HashMap.class.isAssignableFrom(fieldClass)) {
            populateMap(instance, selfReferenceHandler, field);
        } else if (Object.class.isAssignableFrom(fieldClass) && !fieldClass.toString().contains("[Z")) {
            populateInstance(instance, selfReferenceHandler, field);
        }
    }

    protected static <E> E populateObject(final E instance, final SelfReferenceHandler selfReferenceHandler) {
        try {
            populate(instance, selfReferenceHandler);
            return instance;
        } catch (IllegalArgumentException | SecurityException | IllegalAccessException | InstantiationException | NoSuchFieldException ex) {
            String error = "Error to populate instance";
            log.error(error, ex);
            throw new DummyObjectCreateInstanceException(error);
        }
    }

    protected static <E> E createInstance(final Class<E> clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException ex) {
            String error = "Error creating new instance";
            log.error(error + " " + clazz, ex);
            throw new DummyObjectCreateInstanceException(error);
        }
    }

    private static List<Field> getAllDeclaredFields(final Class<?> type, final List<Field> fields) {
        fields.addAll(Stream.of(type.getDeclaredFields()).collect(toSet()));
        if (type.getSuperclass() != null && Object.class != type.getSuperclass()) {
            getAllDeclaredFields(type.getSuperclass(), fields);
        }
        return fields;
    }

    private static List<Field> filterNonFinal(final List<Field> fields) {
        return fields.stream().filter(f -> !Modifier.isFinal(f.getModifiers())).collect(toList());
    }

    private static void populateInstance(final Object instance, final SelfReferenceHandler selfReferenceHandler, final Field field) throws IllegalAccessException {
        Class<?> instanceClass = field.getType();
        field.set(instance, selfReferenceHandler.newInstance(instanceClass));
    }

    private static void populateList(final Object instance, final SelfReferenceHandler selfReferenceHandler, final Field field) throws IllegalAccessException, NoSuchFieldException {
        ParameterizedType parameterizedType = mapParameterizedTypeByField(field);
        if (isCollectionOf(parameterizedType, 0, ImplementedCollections.SET)) {
            Class<?> contentClass = (Class<?>) ((ParameterizedType) parameterizedType.getActualTypeArguments()[0]).getActualTypeArguments()[0];
            field.set(instance, ObjectRandomUtil.nextListOfSet(contentClass, ONE_ELEMENT, selfReferenceHandler));
        } else if (isCollectionOf(parameterizedType, 0, ImplementedCollections.LIST)) {
            Class<?> contentClass = (Class<?>) ((ParameterizedType) parameterizedType.getActualTypeArguments()[0]).getActualTypeArguments()[0];
            field.set(instance, ObjectRandomUtil.nextListOfList(contentClass, ONE_ELEMENT, selfReferenceHandler));
        } else if (isCollectionOf(parameterizedType, 0, ImplementedCollections.MAP)) {
            Class<?> contentKeyClass = (Class<?>) ((ParameterizedType) parameterizedType.getActualTypeArguments()[0]).getActualTypeArguments()[0];
            Class<?> contentValueClass = (Class<?>) ((ParameterizedType) parameterizedType.getActualTypeArguments()[0]).getActualTypeArguments()[1];
            field.set(instance, Arrays.asList(ObjectRandomUtil.nextMap(contentKeyClass, contentValueClass, ONE_ELEMENT, selfReferenceHandler)));
        } else {
            Class<?> contentClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
            field.set(instance, ObjectRandomUtil.nextList(contentClass, ONE_ELEMENT, selfReferenceHandler));
        }
    }

    private static void populateSet(final Object instance, final SelfReferenceHandler selfReferenceHandler, final Field field) throws NoSuchFieldException, IllegalAccessException {
        ParameterizedType parameterizedType = mapParameterizedTypeByField(field);
        if (isCollectionOf(parameterizedType, 0, ImplementedCollections.SET)) {
            Class<?> contentClass = (Class<?>) ((ParameterizedType) parameterizedType.getActualTypeArguments()[0]).getActualTypeArguments()[0];
            field.set(instance, ObjectRandomUtil.nextSetOfSet(contentClass, ONE_ELEMENT, selfReferenceHandler));
        } else if (isCollectionOf(parameterizedType, 0, ImplementedCollections.LIST)) {
            Class<?> contentClass = (Class<?>) ((ParameterizedType) parameterizedType.getActualTypeArguments()[0]).getActualTypeArguments()[0];
            field.set(instance, ObjectRandomUtil.nextSetOfList(contentClass, ONE_ELEMENT, selfReferenceHandler));
        } else if (isCollectionOf(parameterizedType, 0, ImplementedCollections.MAP)) {
            Class<?> contentKeyClass = (Class<?>) ((ParameterizedType) parameterizedType.getActualTypeArguments()[0]).getActualTypeArguments()[0];
            Class<?> contentValueClass = (Class<?>) ((ParameterizedType) parameterizedType.getActualTypeArguments()[0]).getActualTypeArguments()[1];
            Set<Map> newSet = new HashSet();
            newSet.add(ObjectRandomUtil.nextMap(contentKeyClass, contentValueClass, ONE_ELEMENT, selfReferenceHandler));
            field.set(instance, newSet);
        } else {
            Class<?> contentClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
            field.set(instance, ObjectRandomUtil.nextSet(contentClass, ONE_ELEMENT, selfReferenceHandler));
        }
    }

    private static void populateMap(Object instance, SelfReferenceHandler selfReferenceHandler, Field field) throws IllegalAccessException, NoSuchFieldException {
        ParameterizedType parameterizedType = mapParameterizedTypeByField(field);
        Class<?> keyClass;
        Class<?> valueClass;
        if (isCollectionOf(parameterizedType, 0, ImplementedCollections.SET)) {
            keyClass = (Class<?>) ((ParameterizedType) parameterizedType.getActualTypeArguments()[0]).getActualTypeArguments()[0];
            if (isCollectionOf(parameterizedType, 1, ImplementedCollections.SET)) {
                valueClass = (Class<?>) ((ParameterizedType) parameterizedType.getActualTypeArguments()[1]).getActualTypeArguments()[0];
                field.set(instance, ObjectRandomUtil.nextMapOfKeySetValueSet(keyClass, valueClass, ONE_ELEMENT, selfReferenceHandler));
            } else if (isCollectionOf(parameterizedType, 1, ImplementedCollections.LIST)) {
                valueClass = (Class<?>) ((ParameterizedType) parameterizedType.getActualTypeArguments()[1]).getActualTypeArguments()[0];
                field.set(instance, ObjectRandomUtil.nextMapOfKeySetValueList(keyClass, valueClass, ONE_ELEMENT, selfReferenceHandler));
            } else {
                valueClass = (Class<?>) parameterizedType.getActualTypeArguments()[1];
                field.set(instance, ObjectRandomUtil.nextMapOfKeyList(keyClass, valueClass, ONE_ELEMENT, selfReferenceHandler));
            }
        } else if (isCollectionOf(parameterizedType, 0, ImplementedCollections.LIST)) {
            keyClass = (Class<?>) ((ParameterizedType) parameterizedType.getActualTypeArguments()[0]).getActualTypeArguments()[0];
            if (isCollectionOf(parameterizedType, 1, ImplementedCollections.SET)) {
                valueClass = (Class<?>) ((ParameterizedType) parameterizedType.getActualTypeArguments()[1]).getActualTypeArguments()[0];
                field.set(instance, ObjectRandomUtil.nextMapOfKeyListValueSet(keyClass, valueClass, ONE_ELEMENT, selfReferenceHandler));
            } else if (isCollectionOf(parameterizedType, 1, ImplementedCollections.LIST)) {
                valueClass = (Class<?>) ((ParameterizedType) parameterizedType.getActualTypeArguments()[1]).getActualTypeArguments()[0];
                field.set(instance, ObjectRandomUtil.nextMapOfKeyListValueList(keyClass, valueClass, ONE_ELEMENT, selfReferenceHandler));
            } else {
                valueClass = (Class<?>) parameterizedType.getActualTypeArguments()[1];
                field.set(instance, ObjectRandomUtil.nextMapOfKeyList(keyClass, valueClass, ONE_ELEMENT, selfReferenceHandler));
            }
        } else {
            keyClass = (Class<?>) parameterizedType.getActualTypeArguments()[0];
            if (isCollectionOf(parameterizedType, 1, ImplementedCollections.SET)) {
                valueClass = (Class<?>) ((ParameterizedType) parameterizedType.getActualTypeArguments()[1]).getActualTypeArguments()[0];
                field.set(instance, ObjectRandomUtil.nextMapOfValueSet(keyClass, valueClass, ONE_ELEMENT, selfReferenceHandler));
            } else if (isCollectionOf(parameterizedType, 1, ImplementedCollections.LIST)) {
                valueClass = (Class<?>) ((ParameterizedType) parameterizedType.getActualTypeArguments()[1]).getActualTypeArguments()[0];
                field.set(instance, ObjectRandomUtil.nextMapOfValueList(keyClass, valueClass, ONE_ELEMENT, selfReferenceHandler));
            } else {
                valueClass = (Class<?>) parameterizedType.getActualTypeArguments()[1];
                field.set(instance, ObjectRandomUtil.nextMap(keyClass, valueClass, ONE_ELEMENT, selfReferenceHandler));
            }
        }
    }

    private static ParameterizedType mapParameterizedTypeByField(Field field) {
        return (ParameterizedType) field.getGenericType();
    }

    private static boolean isCollectionOf(final ParameterizedType parameterizedType,
                                          final int i,
                                          final ImplementedCollections collection) {
        Type contentType = parameterizedType.getActualTypeArguments()[i];
        boolean isCollection = false;
        for (Class<?> clazz : collection.getCompatibleClassList()) {
            if (contentType.getTypeName().startsWith(clazz.getName())) {
                isCollection = true;
                break;
            }
        }
        return isCollection;
    }

    private static void populateBigInteger(final Object instance, final Field field) throws IllegalAccessException {
        if (field.isAnnotationPresent(Max.class)) {
            final Long min = field.isAnnotationPresent(Min.class) ? field.getAnnotation(Min.class).value() : 1L;
            final Long max = field.getAnnotation(Max.class).value();
            field.set(instance, nextBigInteger(min.intValue(), max.intValue()));
            return;
        }
        field.set(instance, nextBigInteger(1, 1000));
    }

    private static void populateBigDecimal(final Object instance, final Field field) throws IllegalAccessException {
        if (field.isAnnotationPresent(Max.class)) {
            final Long min = field.isAnnotationPresent(Min.class) ? field.getAnnotation(Min.class).value() : 1L;
            final Long max = field.getAnnotation(Max.class).value();
            field.set(instance, nextBigDecimal(min.longValue(), max.longValue(), 0));
            return;
        } else if (field.isAnnotationPresent(Digits.class)) {
            final Integer maxInteger = field.getAnnotation(Digits.class).integer();
            final Integer fraction = field.getAnnotation(Digits.class).fraction();
            field.set(instance, nextBigDecimal(1, maxInteger, fraction));
            return;
        } else {
            field.set(instance, nextBigDecimal(1, 10, 0));
        }
    }

    private static void populateDouble(final Object instance, final Field field) throws IllegalAccessException {
        if (field.isAnnotationPresent(Max.class)) {
            final Long min = field.isAnnotationPresent(Min.class) ? field.getAnnotation(Min.class).value() : 1L;
            final Long max = field.getAnnotation(Max.class).value();
            field.set(instance, nextDouble(min.doubleValue(), max.doubleValue()));
            return;
        }
        field.set(instance, nextDouble(1, 1000));
    }

    private static void populateInteger(final Object instance, final Field field) throws IllegalAccessException {
        if (field.isAnnotationPresent(Max.class)) {
            final Long min = field.isAnnotationPresent(Min.class) ? field.getAnnotation(Min.class).value() : 1L;
            final Long max = field.getAnnotation(Max.class).value();
            field.set(instance, nextInt(min.intValue(), max.intValue()));
            return;
        }
        field.set(instance, nextInt(1, 1000));
    }

    private static void populateLong(final Object instance, final Field field) throws IllegalAccessException {
        field.set(instance, nextInt(1, Short.MAX_VALUE).longValue());
    }

    private static void populateString(final Object instance, final Field field) throws IllegalAccessException {
        int max = field.isAnnotationPresent(Size.class) ? field.getAnnotation(Size.class).max() : 10;
        field.set(instance, nextString(max > 500 ? 500 : max));
    }
}