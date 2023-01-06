package br.com.bradescoseguros.opin.dummy;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ObjectRandomUtil {

    protected static final int ONE_ELEMENT = 1;

    protected static final List<Class<?>> IMPLEMENTED_CLASS_LIST = Arrays.asList(
            Boolean.class,
            String.class,
            Long.class,
            Integer.class,
            BigDecimal.class,
            BigInteger.class,
            Double.class,
            UUID.class,
            HashSet.class,
            HashMap.class,
            Object.class
    );

    public static Boolean nextBoolean() {
        return org.apache.commons.lang3.RandomUtils.nextBoolean();
    }

    public static String nextString(int size) {
        return RandomStringUtils.randomAlphanumeric(size);
    }

    public static Long nextLong() {
        return org.apache.commons.lang3.RandomUtils.nextLong();
    }

    public static Long nextLong(long min, long max) {
        return org.apache.commons.lang3.RandomUtils.nextLong(min, max);
    }

    public static Integer nextInt() {
        return org.apache.commons.lang3.RandomUtils.nextInt();
    }

    public static Integer nextInt(int min, int max) {
        return org.apache.commons.lang3.RandomUtils.nextInt(min, max + 1);
    }

    public static BigDecimal nextBigDecimal() {
        return BigDecimal.valueOf(nextLong());
    }

    public static BigDecimal nextBigDecimal(long min, long max, int scale) {
        return BigDecimal.valueOf(nextLong(min, max), scale);
    }

    public static BigInteger nextBigInteger() {
        return BigInteger.valueOf(nextLong());
    }

    public static BigInteger nextBigInteger(long min, long max) {
        return BigInteger.valueOf(nextLong(min, max));
    }

    public static Double nextDouble() {
        return org.apache.commons.lang3.RandomUtils.nextDouble();
    }

    public static Double nextDouble(double min, double max) {
        return org.apache.commons.lang3.RandomUtils.nextDouble(min, max);
    }

    public static UUID nextUUID() {
        return UUID.randomUUID();
    }

    public static <T> T randomItemArray(T[] arr) {
        return arr[ObjectRandomUtil.nextInt(0, arr.length - 1)];
    }

    public static <T> T randomItemArrayList(List<T> arrayList) {
        return arrayList.get(ObjectRandomUtil.nextInt(0, arrayList.size() - 1));
    }

    public static <E extends Enum<E>> E randomItemEnum(Class<E> enumClass) {
        final E[] values = enumClass.getEnumConstants();
        return randomItemArray(values);
    }

    public static Object nextObject() {
        return of(randomItemArrayList(IMPLEMENTED_CLASS_LIST));
    }

    public static <E> List<E> nextList(final Class<E> clazz, final int size) {
        return nextList(clazz, size, new DummyObjectsUtil.SelfReferenceHandler());
    }

    public static <E> List<E> nextList(final Class<E> clazz) {
        return nextList(clazz, ONE_ELEMENT);
    }

    public static <E> List<List<E>> nextListOfList(final Class<E> clazz, final int size) {
        return nextListOfList(clazz, size, new DummyObjectsUtil.SelfReferenceHandler());
    }

    public static <E> List<List<E>> nextListOfList(final Class<E> clazz) {
        return nextListOfList(clazz, ONE_ELEMENT);
    }

    public static <E> List<Set<E>> nextListOfSet(final Class<E> clazz, final int size) {
        return nextListOfSet(clazz, size, new DummyObjectsUtil.SelfReferenceHandler());
    }

    public static <E> List<Set<E>> nextListOfSet(final Class<E> clazz) {
        return nextListOfSet(clazz, ONE_ELEMENT);
    }

    public static <E> Set<E> nextSet(final Class<E> clazz, final int size) {
        return nextSet(clazz, size, new DummyObjectsUtil.SelfReferenceHandler());
    }

    public static <E> Set<E> nextSet(final Class<E> clazz) {
        return nextSet(clazz, ONE_ELEMENT);
    }

    public static <E> Set<List<E>> nextSetOfList(final Class<E> clazz, final int size) {
        return nextSetOfList(clazz, size, new DummyObjectsUtil.SelfReferenceHandler());
    }

    public static <E> Set<List<E>> nextSetOfList(final Class<E> clazz) {
        return nextSetOfList(clazz, ONE_ELEMENT);
    }

    public static <E> Set<Set<E>> nextSetOfSet(final Class<E> clazz, final int size) {
        return nextSetOfSet(clazz, size, new DummyObjectsUtil.SelfReferenceHandler());
    }

    public static <E> Set<Set<E>> nextSetOfSet(final Class<E> clazz) {
        return nextSetOfSet(clazz, ONE_ELEMENT);
    }

    public static <K, V> Map<K, V> nextMap(final Class<K> keyClass, final Class<V> valueClass, final int size) {
        return nextMap(keyClass, valueClass, size, new DummyObjectsUtil.SelfReferenceHandler());
    }

    public static <K, V> Map<K, V> nextMap(final Class<K> keyClass, final Class<V> valueClass) {
        return nextMap(keyClass, valueClass, ONE_ELEMENT);
    }

    public static <K, V> Map<List<K>, V> nextMapOfKeyList(final Class<K> keyClass, final Class<V> valueClass, final int size) {
        return nextMapOfKeyList(keyClass, valueClass, size, new DummyObjectsUtil.SelfReferenceHandler());
    }

    public static <K, V> Map<List<K>, V> nextMapOfKeyList(final Class<K> keyClass, final Class<V> valueClass) {
        return nextMapOfKeyList(keyClass, valueClass, ONE_ELEMENT);
    }

    public static <K, V> Map<K, List<V>> nextMapOfValueList(final Class<K> keyClass, final Class<V> valueClass, final int size) {
        return nextMapOfValueList(keyClass, valueClass, size, new DummyObjectsUtil.SelfReferenceHandler());
    }

    public static <K, V> Map<K, List<V>> nextMapOfValueList(final Class<K> keyClass, final Class<V> valueClass) {
        return nextMapOfValueList(keyClass, valueClass, ONE_ELEMENT);
    }

    public static <K, V> Map<List<K>, List<V>> nextMapOfKeyListValueList(final Class<K> keyClass, final Class<V> valueClass, final int size) {
        return nextMapOfKeyListValueList(keyClass, valueClass, size, new DummyObjectsUtil.SelfReferenceHandler());
    }

    public static <K, V> Map<List<K>, List<V>> nextMapOfKeyListValueList(final Class<K> keyClass, final Class<V> valueClass) {
        return nextMapOfKeyListValueList(keyClass, valueClass, ONE_ELEMENT);
    }

    public static <K, V> Map<Set<K>, V> nextMapOfKeySet(final Class<K> keyClass, final Class<V> valueClass, final int size) {
        return nextMapOfKeySet(keyClass, valueClass, size, new DummyObjectsUtil.SelfReferenceHandler());
    }

    public static <K, V> Map<Set<K>, V> nextMapOfKeySet(final Class<K> keyClass, final Class<V> valueClass) {
        return nextMapOfKeySet(keyClass, valueClass, ONE_ELEMENT);
    }

    public static <K, V> Map<K, Set<V>> nextMapOfValueSet(final Class<K> keyClass, final Class<V> valueClass, final int size) {
        return nextMapOfValueSet(keyClass, valueClass, size, new DummyObjectsUtil.SelfReferenceHandler());
    }

    public static <K, V> Map<K, Set<V>> nextMapOfValueSet(final Class<K> keyClass, final Class<V> valueClass) {
        return nextMapOfValueSet(keyClass, valueClass, ONE_ELEMENT);
    }

    public static <K, V> Map<Set<K>, Set<V>> nextMapOfKeySetValueSet(final Class<K> keyClass, final Class<V> valueClass, final int size) {
        return nextMapOfKeySetValueSet(keyClass, valueClass, size, new DummyObjectsUtil.SelfReferenceHandler());
    }

    public static <K, V> Map<Set<K>, Set<V>> nextMapOfKeySetValueSet(final Class<K> keyClass, final Class<V> valueClass) {
        return nextMapOfKeySetValueSet(keyClass, valueClass, ONE_ELEMENT);
    }

    public static <K, V> Map<List<K>, Set<V>> nextMapOfKeyListValueSet(final Class<K> keyClass, final Class<V> valueClass, final int size) {
        return nextMapOfKeyListValueSet(keyClass, valueClass, size, new DummyObjectsUtil.SelfReferenceHandler());
    }

    public static <K, V> Map<List<K>, Set<V>> nextMapOfKeyListValueSet(final Class<K> keyClass, final Class<V> valueClass) {
        return nextMapOfKeyListValueSet(keyClass, valueClass, ONE_ELEMENT);
    }

    public static <K, V> Map<Set<K>, List<V>> nextMapOfKeySetValueList(final Class<K> keyClass, final Class<V> valueClass, final int size) {
        return nextMapOfKeySetValueList(keyClass, valueClass, size, new DummyObjectsUtil.SelfReferenceHandler());
    }

    public static <K, V> Map<Set<K>, List<V>> nextMapOfKeySetValueList(final Class<K> keyClass, final Class<V> valueClass) {
        return nextMapOfKeySetValueList(keyClass, valueClass, ONE_ELEMENT);
    }

    public static <E> E of(final Class<E> clazz) {
        DummyObjectsUtil.SelfReferenceHandler selfReferenceHandler = new DummyObjectsUtil.SelfReferenceHandler();
        return of(clazz, selfReferenceHandler);
    }

    protected static <E> E of(final Class<E> clazz, final DummyObjectsUtil.SelfReferenceHandler selfReferenceHandler) {
        if (String.class.isAssignableFrom(clazz)) {
            return (E) nextString(10);
        } else if (long.class.isAssignableFrom(clazz) || Long.class.isAssignableFrom(clazz)) {
            return (E) nextLong(1, Short.MAX_VALUE);
        } else if (int.class.isAssignableFrom(clazz) || Integer.class.isAssignableFrom(clazz)) {
            return (E) nextInt(1, 1000);
        } else if (double.class.isAssignableFrom(clazz) || Double.class.isAssignableFrom(clazz)) {
            return (E) nextDouble(1, 1000);
        } else if (boolean.class.isAssignableFrom(clazz) || Boolean.class.isAssignableFrom(clazz)) {
            return (E) nextBoolean();
        } else if (LocalDateTime.class.isAssignableFrom(clazz)) {
            return (E) LocalDateTime.now();
        } else if (LocalDate.class.isAssignableFrom(clazz)) {
            return (E) LocalDate.now();
        } else if (LocalTime.class.isAssignableFrom(clazz)) {
            return (E) LocalTime.now();
        } else if (Date.class.isAssignableFrom(clazz)) {
            return (E) new Date();
        } else if (Time.class.isAssignableFrom(clazz)) {
            return (E) new Date();
        } else if (Enum.class.isAssignableFrom(clazz)) {
            return (E) ObjectRandomUtil.randomItemEnum((Class) clazz);
        } else if (BigDecimal.class.isAssignableFrom(clazz)) {
            return (E) ObjectRandomUtil.nextBigDecimal(1, 10, 0);
        } else if (BigInteger.class.isAssignableFrom(clazz)) {
            return (E) nextBigInteger(1, 1000);
        } else if (UUID.class.isAssignableFrom(clazz)) {
            return (E) ObjectRandomUtil.nextUUID();
        } else if (List.class.isAssignableFrom(clazz)) {
            return (E) nextList(randomItemArrayList(IMPLEMENTED_CLASS_LIST), ONE_ELEMENT, selfReferenceHandler);
        } else {
            return getRandomEntityOrRandomCollection(clazz, selfReferenceHandler);
        }
    }

    private static <E> E getRandomEntityOrRandomCollection(final Class<E> clazz,
                                                           final DummyObjectsUtil.SelfReferenceHandler selfReferenceHandler) {
        if (Set.class.isAssignableFrom(clazz) || HashSet.class.isAssignableFrom(clazz)) {
            return (E) nextSet(randomItemArrayList(IMPLEMENTED_CLASS_LIST), ONE_ELEMENT, selfReferenceHandler);
        } else if (Map.class.isAssignableFrom(clazz) || HashMap.class.isAssignableFrom(clazz)) {
            return (E) nextMap(randomItemArrayList(IMPLEMENTED_CLASS_LIST), randomItemArrayList(IMPLEMENTED_CLASS_LIST), ONE_ELEMENT, selfReferenceHandler);
        } else if (Object.class.isAssignableFrom(clazz) && selfReferenceHandler.isNotSelfReference(clazz)) {
            E newInstance;
            try {
                newInstance = selfReferenceHandler.newInstance(clazz);
            } catch (Exception ignore) {
                log.warn("Error trying to instantiate object of class {}", clazz.getSimpleName());
                newInstance = (E) nextObject();
            }
            return newInstance;
        }
        return null;
    }

    protected static <E> List<E> nextList(final Class<E> clazz,
                                          final int size,
                                          final DummyObjectsUtil.SelfReferenceHandler selfReferenceHandler) {
        final List<E> list = new ArrayList<>();
        for (int index = 0; index < size; index++) {
            Optional.ofNullable(of(clazz, selfReferenceHandler))
                    .map(o -> {
                        list.add(o);
                        return o;
                    });
        }
        return getNotEmptyListOrNull(list);
    }

    protected static <E> List<List<E>> nextListOfList(final Class<E> clazz,
                                                      final int size,
                                                      final DummyObjectsUtil.SelfReferenceHandler selfReferenceHandler) {
        final List<List<E>> list = new ArrayList<>();
        for (int index = 0; index < size; index++) {
            Optional.ofNullable(nextList(clazz, size, selfReferenceHandler))
                    .map(o -> {
                        list.add(o);
                        return o;
                    });
        }
        return getNotEmptyListOrNull(list);
    }

    protected static <E> List<Set<E>> nextListOfSet(final Class<E> clazz,
                                                    final int size,
                                                    final DummyObjectsUtil.SelfReferenceHandler selfReferenceHandler) {
        final List<Set<E>> list = new ArrayList<>();
        for (int index = 0; index < size; index++) {
            Optional.ofNullable(nextSet(clazz, size, selfReferenceHandler))
                    .map(o -> {
                        list.add(o);
                        return o;
                    });
        }
        return getNotEmptyListOrNull(list);
    }

    protected static <E> Set<E> nextSet(final Class<E> clazz,
                                        final int size,
                                        final DummyObjectsUtil.SelfReferenceHandler selfReferenceHandler) {
        final Set<E> set = new HashSet<>();
        for (int index = 0; index < size; index++) {
            Optional.ofNullable(of(clazz, selfReferenceHandler))
                    .map(o -> {
                        set.add(o);
                        return o;
                    });
        }
        return getNotEmptySetOrNull(set);
    }

    protected static <E> Set<List<E>> nextSetOfList(final Class<E> clazz,
                                                    final int size,
                                                    final DummyObjectsUtil.SelfReferenceHandler selfReferenceHandler) {
        final Set<List<E>> set = new HashSet<>();
        for (int index = 0; index < size; index++) {
            Optional.ofNullable(nextList(clazz, size, selfReferenceHandler))
                    .map(o -> {
                        set.add(o);
                        return o;
                    });
        }
        return getNotEmptySetOrNull(set);
    }

    protected static <E> Set<Set<E>> nextSetOfSet(final Class<E> clazz,
                                                  final int size,
                                                  final DummyObjectsUtil.SelfReferenceHandler selfReferenceHandler) {
        final Set<Set<E>> set = new HashSet<>();
        for (int index = 0; index < size; index++) {
            Optional.ofNullable(nextSet(clazz, size, selfReferenceHandler))
                    .map(o -> {
                        set.add(o);
                        return o;
                    });
        }
        return getNotEmptySetOrNull(set);
    }

    protected static <K, V> Map<K, V> nextMap(final Class<K> keyClass,
                                              final Class<V> valueClass,
                                              final int size,
                                              final DummyObjectsUtil.SelfReferenceHandler selfReferenceHandler) {
        final Map<K, V> map = new HashMap<>();
        for (int index = 0; index < size; index++) {
            Optional.ofNullable(of(keyClass, selfReferenceHandler))
                    .map(mapKey -> {
                        map.put(mapKey, of(valueClass, selfReferenceHandler));
                        return mapKey;
                    });
        }
        return getNotEmptyMapOrNull(map);
    }

    protected static <K, V> Map<List<K>, V> nextMapOfKeyList(final Class<K> keyClass,
                                                             final Class<V> valueClass,
                                                             final int size,
                                                             final DummyObjectsUtil.SelfReferenceHandler selfReferenceHandler) {
        final Map<List<K>, V> map = new HashMap<>();
        for (int index = 0; index < size; index++) {
            Optional.ofNullable(nextList(keyClass, size, selfReferenceHandler))
                    .map(mapKey -> {
                        map.put(mapKey, of(valueClass, selfReferenceHandler));
                        return mapKey;
                    });
        }
        return getNotEmptyMapOrNull(map);
    }

    protected static <K, V> Map<K, List<V>> nextMapOfValueList(final Class<K> keyClass,
                                                               final Class<V> valueClass,
                                                               final int size,
                                                               final DummyObjectsUtil.SelfReferenceHandler selfReferenceHandler) {
        final Map<K, List<V>> map = new HashMap<>();
        for (int index = 0; index < size; index++) {
            Optional.ofNullable(of(keyClass, selfReferenceHandler))
                    .map(mapKey -> {
                        map.put(mapKey, nextList(valueClass, size, selfReferenceHandler));
                        return mapKey;
                    });
        }
        return getNotEmptyMapOrNull(map);
    }

    protected static <K, V> Map<List<K>, List<V>> nextMapOfKeyListValueList(final Class<K> keyClass,
                                                                            final Class<V> valueClass,
                                                                            final int size,
                                                                            final DummyObjectsUtil.SelfReferenceHandler selfReferenceHandler) {
        final Map<List<K>, List<V>> map = new HashMap<>();
        for (int index = 0; index < size; index++) {
            Optional.ofNullable(nextList(keyClass, size, selfReferenceHandler))
                    .map(o -> {
                        map.put(o, nextList(valueClass, size, selfReferenceHandler));
                        return o;
                    });
        }
        return getNotEmptyMapOrNull(map);
    }

    protected static <K, V> Map<Set<K>, V> nextMapOfKeySet(final Class<K> keyClass,
                                                           final Class<V> valueClass,
                                                           final int size,
                                                           final DummyObjectsUtil.SelfReferenceHandler selfReferenceHandler) {
        final Map<Set<K>, V> map = new HashMap<>();
        for (int index = 0; index < size; index++) {
            Optional.ofNullable(nextSet(keyClass, size, selfReferenceHandler))
                    .map(mapKey -> {
                        map.put(mapKey, of(valueClass, selfReferenceHandler));
                        return mapKey;
                    });
        }
        return getNotEmptyMapOrNull(map);
    }

    protected static <K, V> Map<K, Set<V>> nextMapOfValueSet(final Class<K> keyClass,
                                                             final Class<V> valueClass,
                                                             final int size,
                                                             final DummyObjectsUtil.SelfReferenceHandler selfReferenceHandler) {
        final Map<K, Set<V>> map = new HashMap<>();
        for (int index = 0; index < size; index++) {
            Optional.ofNullable(of(keyClass, selfReferenceHandler))
                    .map(mapKey -> {
                        map.put(mapKey, nextSet(valueClass, size, selfReferenceHandler));
                        return mapKey;
                    });
        }
        return getNotEmptyMapOrNull(map);
    }

    protected static <K, V> Map<Set<K>, Set<V>> nextMapOfKeySetValueSet(final Class<K> keyClass,
                                                                        final Class<V> valueClass,
                                                                        final int size,
                                                                        final DummyObjectsUtil.SelfReferenceHandler selfReferenceHandler) {
        final Map<Set<K>, Set<V>> map = new HashMap<>();
        for (int index = 0; index < size; index++) {
            Optional.ofNullable(nextSet(keyClass, size, selfReferenceHandler))
                    .map(mapKey -> {
                        map.put(mapKey, nextSet(valueClass, size, selfReferenceHandler));
                        return mapKey;
                    });
        }
        return getNotEmptyMapOrNull(map);
    }

    protected static <K, V> Map<List<K>, Set<V>> nextMapOfKeyListValueSet(final Class<K> keyClass,
                                                                          final Class<V> valueClass,
                                                                          final int size,
                                                                          final DummyObjectsUtil.SelfReferenceHandler selfReferenceHandler) {
        final Map<List<K>, Set<V>> map = new HashMap<>();
        for (int index = 0; index < size; index++) {
            Optional.ofNullable(nextList(keyClass, size, selfReferenceHandler))
                    .map(mapKey -> {
                        map.put(mapKey, nextSet(valueClass, size, selfReferenceHandler));
                        return mapKey;
                    });
        }
        return getNotEmptyMapOrNull(map);
    }

    protected static <K, V> Map<Set<K>, List<V>> nextMapOfKeySetValueList(final Class<K> keyClass,
                                                                          final Class<V> valueClass,
                                                                          final int size,
                                                                          final DummyObjectsUtil.SelfReferenceHandler selfReferenceHandler) {
        final Map<Set<K>, List<V>> map = new HashMap<>();
        for (int index = 0; index < size; index++) {
            Optional.ofNullable(nextSet(keyClass, size, selfReferenceHandler))
                    .map(mapKey -> {
                        map.put(mapKey, nextList(valueClass, size, selfReferenceHandler));
                        return mapKey;
                    });
        }
        return getNotEmptyMapOrNull(map);
    }

    private static <E> List<E> getNotEmptyListOrNull(List<E> list) {
        return list.isEmpty() ? null : list;
    }

    private static <V> Set<V> getNotEmptySetOrNull(Set<V> set) {
        return set.isEmpty() ? null : set;
    }

    private static <K, V> Map<K, V> getNotEmptyMapOrNull(Map<K, V> map) {
        return map.isEmpty() ? null : map;
    }
}
