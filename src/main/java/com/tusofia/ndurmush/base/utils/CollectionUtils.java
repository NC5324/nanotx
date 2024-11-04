package com.tusofia.ndurmush.base.utils;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CollectionUtils extends org.apache.commons.collections.CollectionUtils {

    public static <V> Set<V> combineValues(final Map<?, Set<V>> map) {
        return combineValues(map.values());
    }

    public static <V> Set<V> combineValues(final Collection<Set<V>> collection) {
        final Set<V> allValues = new TreeSet<V>();
        for (final Set<V> values : collection) {
            allValues.addAll(values);
        }
        return allValues;
    }

    @SafeVarargs
    public static <V> List<V> combineCollections(final Collection<V>... collections) {
        return combineCollectionsToList(new ArrayList<V>(), collections);
    }

    @SafeVarargs
    public static <V, C extends Collection<V>> C combineCollectionsToCollection(
            final Supplier<C> destinationCollectionSupplier, final Collection<V>... collections) {
        C destinationCollection = destinationCollectionSupplier.get();
        for (final Collection<? extends V> c : collections) {
            if (c != null) {
                destinationCollection.addAll(c);
            }
        }
        return destinationCollection;
    }

    @SafeVarargs
    public static <V> List<V> combineCollectionsToList(final List<V> destinationList, final Collection<? extends V>... collections) {
        for (final Collection<? extends V> c : collections) {
            if (c != null) {
                destinationList.addAll(c);
            }
        }
        return destinationList;
    }

    public static <V> List<V> combineCollectionsCollection(final List<V> destinationList, final Collection<? extends Collection<V>> collections) {
        for (final Collection<V> c : collections) {
            if (c != null) {
                destinationList.addAll(c);
            }
        }
        return destinationList;
    }

    public static <V> List<V> combineCollections(final Collection<? extends Collection<V>> collections) {
        return combineCollectionsCollection(new ArrayList<V>(), collections);
    }

    public static <V> List<V> filterNonNulls(final List<V> list) {
        final List<V> filtered = new ArrayList<V>();
        if (CollectionUtils.isNotEmpty(list)) {
            for (final V v : list) {
                if (v != null) {
                    filtered.add(v);
                }
            }
        }
        return filtered;
    }

    public static <K, V> Map<K, V> filterNonNullValues(final Map<K, V> map) {
        final Map<K, V> filteredMap = new HashMap<K, V>();
        for (final Map.Entry<K, V> entry : map.entrySet()) {
            final K key = entry.getKey();
            final V value = entry.getValue();
            if (value != null) {
                filteredMap.put(key, value);
            }
        }
        return filteredMap;
    }

    public static <K, V, C extends Collection<V>> Map<K, C> filterNonEmptyCollectionValues(final Map<K, C> map) {
        final Map<K, C> filteredMap = new HashMap<>();
        for (final Map.Entry<K, C> entry : map.entrySet()) {
            final K key = entry.getKey();
            final C value = entry.getValue();
            if (isNotEmpty(value)) {
                filteredMap.put(key, value);
            }
        }
        return filteredMap;
    }

    public static <K, V> Map<K, V> filterNonNullKeys(final Map<K, V> map) {
        final Map<K, V> filteredMap = new HashMap<K, V>();
        for (final Map.Entry<K, V> e : map.entrySet()) {
            if (e.getKey() != null) {
                filteredMap.put(e.getKey(), e.getValue());
            }
        }
        return filteredMap;
    }

    public static <K, V> Map<K, V> replaceNullValuesWith(final Map<K, V> map, final V newNullValue) {
        final Map<K, V> filteredMap = new HashMap<K, V>();
        for (final Map.Entry<K, V> entry : map.entrySet()) {
            K key = entry.getKey();
            V value = entry.getValue();
            if (value == null) {
                value = newNullValue;
            }
            filteredMap.put(key, value);
        }
        return filteredMap;
    }

    /**
     * Gets the first element. Returns null if the list is empty or null
     *
     * @param list the list
     * @return Gets the first element. Returns null if the list is empty or null
     */
    public static <V> V get0(final List<V> list) {
        if (isNotEmpty(list)) {
            return list.get(0);
        }
        return null;
    }

    /**
     * Gets the first element. Returns null if the set is empty or null
     *
     * @param set the set
     * @return Gets the first element. Returns null if the set is empty or null
     */
    public static <V> V get0(final Set<V> set) {
        if (isNotEmpty(set)) {
            Optional<V> optionalSet = set.stream().findFirst();
            return optionalSet.orElse(null);
        }
        return null;
    }

    /**
     * Gets the last element. Returns null if the list is empty or null
     *
     * @param list the list
     * @return Gets the last element. Returns null if the list is empty or null
     */
    public static <V> V getLast(final List<V> list) {
        if (isNotEmpty(list)) {
            return list.get(list.size() - 1);
        }
        return null;
    }

    /**
     * Gets the Nth element. Returns the <tt>defaultValue</tt> if the list is
     * empty OR the element doesn't exists or is null
     *
     * @param list         the list
     * @param index        the elementh index
     * @param defaultValue the default value if the elmenth is null or the index doesn't
     *                     exists
     * @return Gets the Nth element. Returns the <tt>defaultValue</tt> if the
     * list is empty OR the element doesn't exists or is null
     */
    public static <V> V getN(final List<V> list, final int index, final V defaultValue) {
        if (list != null && size(list) > index) {
            final V v = list.get(index);
            if (v != null) {
                return v;
            }
        }
        return defaultValue;
    }

    public static <K, V> V getFromMap(final Map<K, V> map, final K key, final V defaultValue) {
        if (map.containsKey(key)) {
            return map.get(key);
        }
        return defaultValue;
    }

    /**
     * Sets the Nth element. If the index is greater than the list size,
     * elements with <tt>defaultValue</tt> are added
     *
     * @param list         the list
     * @param index        the elementh index
     * @param value        the value of the elementh
     * @param defaultValue the default value for the elements added between list
     *                     <tt>size</tt> and <tt>index</tt>
     */
    public static <V> void setN(final List<V> list, final int index, final V value, final V defaultValue) {
        if (list != null) {
            while (list.size() <= index) {
                list.add(defaultValue);
            }
            list.set(index, value);
        }
    }

    /**
     * Sets the list to contain only 1 element. The list is cleared and if the
     * element is not null it is added.
     *
     * @param list    the list
     * @param element element to be added
     */
    public static <V> void set0(final List<V> list, final V element) {
        if (list != null) {
            list.clear();
            if (element != null) {
                list.add(element);
            }
        }
    }

    public static <V> Set<V> collectionToSet(final Collection<V> collection) {
        final Set<V> set = new HashSet<>();
        if (isNotEmpty(collection)) {
            for (final V e : collection) {
                set.add(e);
            }
        }
        return set;
    }

    public static <V> List<V> collectionToList(final Collection<V> collection) {
        final List<V> list = new ArrayList<>();
        if (isNotEmpty(collection)) {
            for (final V e : collection) {
                list.add(e);
            }
        }
        return list;
    }

    public static <V> List<V> arrayToList(@SuppressWarnings("unchecked") final V... collection) {
        final List<V> list = new ArrayList<>();
        if (collection != null) {
            for (final V e : collection) {
                list.add(e);
            }
        }
        return list;
    }

    public static <V> Set<V> intersect(final Collection<V> l1, final Collection<V> l2) {
        final Set<V> intersection = new HashSet<V>();
        if (isNotEmpty(l1) && isNotEmpty(l2)) {
            for (final V e : l1) {
                if (l2.contains(e)) {
                    intersection.add(e);
                }
            }
        }
        return intersection;
    }

    public static <V> List<V> intersectList(final Collection<V> l1, final Collection<V> l2) {
        return new ArrayList<V>(intersect(l1, l2));
    }

    public static <V> Set<V> intersectAOnly(final Collection<V> a, final Collection<V> b) {
        final Set<V> intersection = new HashSet<V>();
        if (isEmpty(b)) {
            intersection.addAll(a);
        } else {
            intersection.addAll(intersect(a, b));
        }
        return intersection;
    }

    public static <V> List<V> intersectAOnlyList(final Collection<V> a, final Collection<V> b) {
        return new ArrayList<V>(intersectAOnly(a, b));
    }

    public static <V> List<List<V>> split(final Collection<V> collection, final int maxSize) {
        if (collection instanceof List) {
            return split((List<V>) collection, maxSize);
        } else {
            return split(new ArrayList<V>(collection), maxSize);
        }
    }

    public static <V> List<List<V>> split(final List<V> list, final int maxSize) {
        final List<List<V>> results = new ArrayList<>();
        if (list != null) {
            final int len = list.size();
            if (len > maxSize) {
                for (int i = 0; i < len / maxSize; i++) {
                    results.add(list.subList(i * maxSize, (i + 1) * maxSize));
                }
                if (len % maxSize != 0) {
                    final int i = len / maxSize;
                    results.add(list.subList(i * maxSize, len));
                }
            } else {
                results.add(list);
            }
        }
        return results;
    }

    public static <K, V> List<V> getMapValues(final Map<K, V> map, final Collection<K> keys) {
        final List<V> values = new ArrayList<>();
        if (isNotEmpty(map) && isNotEmpty(keys)) {
            for (final K key : keys) {
                if (map.containsKey(key)) { // NOSONAR
                    // false positive NPE, since isNotEmpty check if the map is
                    // empty
                    values.add(map.get(key));
                }
            }
        }
        return values;
    }

    @SafeVarargs
    public static <K, V> List<V> getMapValues(final Map<K, V> map, final K... keys) {
        return getMapValues(map, Arrays.asList(keys));
    }

    public static boolean isNotEmpty(final Map<?, ?> map) {
        return !isEmpty(map);
    }

    public static boolean isEmpty(final Map<?, ?> map) {
        return map == null || map.size() == 0;
    }

    public static <T> boolean isNotEmpty(final T[] array) {
        return !isEmpty(array);
    }

    public static <T> boolean isEmpty(final T[] array) {
        return null == array || array.length == 0;
    }

    public static <K, V> void changeAllValues(final Map<K, V> map, final V oldValue, final V newValue) {
        if (isNotEmpty(map)) {
            for (final Map.Entry<K, V> e : map.entrySet()) { // NOSONAR
                // false positive NPE, since isNotEmpty checks if the map is
                // empty
                if (Objects.equals(e.getValue(), oldValue)) {
                    e.setValue(newValue);
                }
            }
        }
    }

    public static <K, V> void removeAllValues(final Map<K, V> map, final V value) {
        if (isNotEmpty(map)) {
            for (final Iterator<Map.Entry<K, V>> it = map.entrySet().iterator(); it.hasNext(); ) { // NOSONAR
                // false positive NPE, since isNotEmpty checks if the map is
                // empty
                final Map.Entry<K, V> e = it.next();
                if (Objects.equals(e.getValue(), value)) {
                    it.remove();
                }
            }
        }
    }

    public static <V> void addAll(final Collection<V> dest, final Collection<V> src) {
        if (isNotEmpty(src) && dest != null) {
            dest.addAll(src);
        }
    }

    public static <V, C extends Collection<V>> C addAll(C dest, final C src, final Supplier<C> collectionCreator) {
        if (isNotEmpty(src)) {
            if (dest == null) {
                if (collectionCreator == null) {
                    throw new IllegalArgumentException("Cannot instantiate collection class of null");
                }
                dest = collectionCreator.get();
            }
            dest.addAll(src);
        }
        return dest;
    }

    public static <V, C extends Collection<V>> C removeAll(C dest, final C elementsToRemove, final java.util.function.Supplier<C> collectionCreator) {
        if (isNotEmpty(elementsToRemove)) {
            if (dest != null) {
                dest.removeAll(elementsToRemove);
            } else if (collectionCreator != null) {
                return collectionCreator.get();
            }
        }
        return dest;
    }

    public static <V> boolean containsAll(final Collection<V> coll1, final Collection<V> coll2) {
        if (coll1.size() == coll2.size()) {
            for (final Iterator<V> it = coll1.iterator(); it.hasNext(); ) {
                if (!coll2.contains(it.next())) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @SafeVarargs
    public static <V> V[] add(final V[] array, final V... newValues) {
        final V[] newArray;
        if (array != null && newValues != null && newValues.length > 0) {
            final List<V> l = new ArrayList<>();
            l.addAll(Arrays.asList(array));
            l.addAll(Arrays.asList(newValues));
            newArray = l.toArray(array);
        } else {
            newArray = array;
        }
        return newArray;
    }

    @SafeVarargs
    public static <V> Set<V> asSet(final V... items) {
        return new HashSet<V>(Arrays.asList(items));
    }

    @SafeVarargs
    public static <V> List<V> asList(final V... items) {
        return new ArrayList<V>(Arrays.asList(items));
    }

    @SafeVarargs
    public static <V> Set<V> toSet(final V... items) {
        return toSet(Arrays.asList(items));
    }

    public static <V> Set<V> toSet(final Collection<V> items) {
        return toSet(items, null);
    }

    public static <V> Set<V> toSet(final Collection<V> items, final Set<V> emptySet) {
        if (items != null) {
            return new HashSet<V>(items);
        } else {
            return emptySet;
        }
    }

    public static <V> List<V> toList(final Collection<V> list) {
        return toList(list, null);
    }

    public static <V> List<V> toList(final Collection<V> list, final List<V> emptyList) {
        final List<V> cloned;
        if (list != null) {
            cloned = new ArrayList<>(list);
        } else {
            cloned = emptyList;
        }
        return cloned;
    }

    /**
     * Returns a new {@link Collection} containing <tt><i>a</i> - <i>b</i></tt>.
     * The cardinality of each element <i>e</i> in the returned
     * {@link Collection} will be the cardinality of <i>e</i> in <i>a</i> minus
     * the cardinality of <i>e</i> in <i>b</i>, or zero, whichever is greater.
     *
     * @param a the collection to subtract from (if null empty list is
     *          returned)
     * @param b the collection to subtract (may be null, then a new list
     *          equals to <tt><i>a</i></tt> is returned)
     * @return a new collection with the results
     * @see Collection#removeAll
     */
    public static <ST> List<ST> subtractSafely(final Collection<ST> a, final Collection<ST> b) {
        final List<ST> list = new ArrayList<>();
        if (a != null) {
            list.addAll(a);
        }
        if (isNotEmpty(b)) {
            list.removeAll(b);
        }
        return list;
    }

    /**
     * Returns a new {@link Collection} containing <tt><i>a</i> - <i>b</i></tt> by given <tt><i>comparator</i></tt>
     *
     * @param <T>               a type that defines the elements of the collections<br>
     * @param <C>               a type that extends Collection<T>
     * @param a                 the collection to subtract from (if null the list from collectionCreator is returned)<br>
     * @param b                 the collection to subtract (may be null, then a new list equals to <tt><i>a</i></tt> is returned)<br>
     * @param comparator        the comparator to subtract by<br>
     * @param collectionCreator a supplier method for creating a collection (e.g. ArrayList::new, HashSet::new)
     * @return a new collection with the results
     * @see Collection#removeIf
     */
    public static <T, C extends Collection<T>> C subtract(final C a, final C b, final Comparator<T> comparator, final Supplier<C> collectionCreator) {
        final C difference = collectionCreator.get();
        addAll(difference, a);

        if (b.isEmpty()) {
            return difference;
        }

        difference.removeIf((e) -> b.stream().anyMatch((e2) -> comparator.compare(e, e2) == 0));
        return difference;
    }

    public static <T, C extends Collection<T>> C intersect(final C a, final C b, final Comparator<T> comparator, final Supplier<C> collectionCreator) {
        final C intersection = collectionCreator.get();

        if (isEmpty(a) || isEmpty(b)) {
            return intersection;
        }

        intersection.addAll(a);
        intersection.removeIf((e) -> b.stream().noneMatch((e2) -> comparator.compare(e, e2) != 0));
        return intersection;
    }

    public static <D> List<D> defaultList(final List<D> list) {
        if (list == null) {
            return new ArrayList<D>();
        } else {
            return list;
        }
    }

    public static <D> List<D> defaultList(final D... list) {
        if (list == null) {
            return new ArrayList<D>();
        } else {
            return asList(list);
        }
    }

    public static <D> Set<D> defaultSet(final Set<D> set) {
        if (set == null) {
            return new HashSet<>();
        } else {
            return set;
        }
    }

    public static <K, V> Map<K, V> defaultMap(final Map<K, V> map) {
        if (map == null) {
            return new HashMap<K, V>();
        } else {
            return map;
        }
    }

    public static <D> List<D> limit(final List<D> list, final int limit) {
        final List<D> limited = new ArrayList<D>();
        if (list != null && list.size() > limit) {
            limited.addAll(list.subList(0, limit));
        } else if (list != null) {
            limited.addAll(list);
        }
        return limited;
    }

    public static <D> D lastNonNull(final List<D> list) {
        D value = null;
        if (CollectionUtils.isNotEmpty(list)) {
            value = previousNonNull(list, list.size() - 1);
        }
        return value;
    }

    public static <D> D firstNonNull(final List<D> list) {
        return nextNonNull(list, 0);
    }

    public static <D> D previousNonNull(final List<D> list, final int startIndex) {
        D value = null;
        if (CollectionUtils.isNotEmpty(list)) {
            int index = Math.min(startIndex, list.size() - 1);
            value = list.get(index);
            while (value == null && index > 0) {
                index--;
                value = list.get(index);
            }
        }
        return value;
    }

    public static <D> D nextNonNull(final List<D> list, final int startIndex) {
        D value = null;
        if (CollectionUtils.isNotEmpty(list) && startIndex < list.size()) {
            value = list.get(startIndex);
            int index = startIndex;
            while (value == null && index < list.size() - 1) {
                index++;
                value = list.get(index);
            }
        }
        return value;
    }

    public static <V, C extends Collection<V>> C clearAndAdd(final C dest, final C src) {
        if (dest != null) {
            dest.clear();
            if (src != null) {
                dest.addAll(src);
            }
        }
        return dest;
    }

    public static boolean isAllNotNull(final Object... coll) {
        if (coll != null) {
            return isAllNotNull(Arrays.asList(coll));
        } else {
            return false;
        }
    }

    public static boolean isAllNotNull(final Collection<?> coll) {
        if (isEmpty(coll)) {
            return false;
        }
        for (final Object o : coll) {
            if (o == null) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAnyNotNull(final Object... coll) {
        if (coll != null) {
            return isAnyNotNull(Arrays.asList(coll));
        } else {
            return false;
        }
    }

    public static boolean isAnyNotNull(final Collection<?> coll) {
        if (isNotEmpty(coll)) {
            for (final Object o : coll) {
                if (o != null) {
                    return true;
                }
            }
        }
        return false;
    }

    public static <T> List<T> copy(final List<T> list) {
        final List<T> result;
        if (list != null) {
            result = new ArrayList<T>(list);
        } else {
            result = null;
        }
        return result;
    }

    public static <T> Set<T> copy(final Set<T> list) {
        final Set<T> result;
        if (list != null) {
            result = new HashSet<T>(list);
        } else {
            result = null;
        }
        return result;
    }

    public static <K, V> Map<K, V> copy(final Map<K, V> map) {
        final Map<K, V> result;
        if (null != map) {
            result = new HashMap<>(map);
        } else {
            result = null;
        }
        return result;
    }

    public static <T> T[] copy(final T[] array) {
        T[] result;
        if (null == array) {
            result = null;
        } else {
            result = array.clone();
        }
        return result;
    }

    public static byte[] copy(final byte[] array) {
        byte[] result;
        if (null == array) {
            result = null;
        } else {
            result = array.clone();
        }
        return result;
    }


    public static <T> List<T> unmodifiableList(final List<T> list) {
        if (list != null) {
            return Collections.unmodifiableList(list);
        } else {
            return null;
        }
    }

    public static <T> Set<T> unmodifiableSet(final Set<T> list) {
        if (list != null) {
            return Collections.unmodifiableSet(list);
        } else {
            return null;
        }
    }

    public static <K, V, M extends Map<K, V>> M put(M map, K key, V value) {
        if (map != null) {
            map.put(key, value);
        }
        return map;
    }

    @SafeVarargs
    public static <K, V> Map<K, V> map(Pair<K, V>... pairs) {
        final Map<K, V> map = new HashMap<>();
        for (final Pair<K, V> p : pairs) {
            map.put(p.getKey(), p.getValue());
        }
        return map;
    }

    public static <K, V> V getMapValue(final Map<K, V> map, K key, V defaultValue) {
        if (map != null && map.get(key) != null) {
            return map.get(key);
        } else {
            return defaultValue;
        }
    }

    public static <T> List<List<T>> getBatches(List<T> collection, int batchSize) {
        int i = 0;
        List<List<T>> batches = new ArrayList<List<T>>();
        while (i < collection.size()) {
            int nextInc = Math.min(collection.size() - i, batchSize);
            List<T> batch = collection.subList(i, i + nextInc);
            batches.add(batch);
            i = i + nextInc;
        }

        return batches;
    }

    public static <T> List<Set<T>> getBatches(Set<T> collection, int batchSize) {
        int i = 0;
        List<Set<T>> batches = new ArrayList<Set<T>>();
        if (CollectionUtils.isNotEmpty(collection)) {
            List<T> list = toList(collection);
            while (i < collection.size()) {
                int nextInc = Math.min(list.size() - i, batchSize);
                List<T> batch = list.subList(i, i + nextInc);
                batches.add(toSet(batch));
                i = i + nextInc;
            }
        }
        return batches;
    }

    public static <T> List<T> getListOfNulls(Class<T> c, int amount) {
        List<T> result = new ArrayList<>();
        IntStream.range(0, amount).forEach(i -> result.add((T) null));
        return result;
    }

    public static <K, V, M extends Map<K, V>> M putIfNotNull(M map, K key, V value) {
        if (map != null && value != null) {
            map.put(key, value);
        }
        return map;
    }

    public static <K, M extends Map<K, Object>> M putIfNotBlank(M map, K key, String value) {
        if (map != null && StringUtils.isNotBlank(value)) {
            map.put(key, value);
        }
        return map;
    }

    public static <T, L extends List<T>> L addIfNotNull(L list, T element) {
        if (null != list && null != element) {
            list.add(element);
        }
        return list;
    }

    public static <T, S extends Set<T>> S addIfNotNull(S set, T element) {
        if (null != set && null != element) {
            set.add(element);
        }
        return set;
    }

    public static <V> V getFirst(final Collection<V> col) {
        if (isNotEmpty(col)) {
            Iterator<V> iterator = col.iterator();
            if (null != iterator) {
                return iterator.next();
            }
        }
        return null;
    }

    public static <T> T getAny(final Set<T> set) {
        if (isNotEmpty(set)) {
            return set.iterator().next();
        }
        return null;
    }

    public static <T> T getAny(final List<T> list) {
        if (isNotEmpty(list)) {
            return list.iterator().next();
        }
        return null;
    }

    public static <T> T getAnyValue(final Map<?, T> map) {
        if (map != null && isNotEmpty(map)) {
            return map.entrySet().iterator().next().getValue();
        }
        return null;
    }

    public static <T, E> List<T> castList(List<E> list) {
        @SuppressWarnings("unchecked")
        List<T> result = (List<T>) list;
        return result;
    }

    public static <K, V> V getFirstNonNull(final Map<K, V> map, final Collection<K> keys) {
        if (isNotEmpty(map) && isNotEmpty(keys)) {
            for (K key : keys) {
                V value = map.get(key);
                if (null != value) {
                    return value;
                }
            }
        }
        return null;
    }

    public static <T> T[] asArray(T... items) {
        return items;
    }

    public static <T> Stream<T> asStream(Iterator<T> sourceIterator) {
        return asStream(sourceIterator, false);
    }

    public static <T> Stream<T> asStream(Iterator<T> sourceIterator, boolean parallel) {
        Iterable<T> iterable = () -> sourceIterator;
        return StreamSupport.stream(iterable.spliterator(), parallel);
    }

    /**
     * Returns the index of the first element tested positively by the provided
     * predicate
     *
     * @param list      the list to search in (if null, return -1)
     * @param predicate predicate to test with
     * @return the index of the first element or -1 if not found
     */
    public static <T> int findFirstIndex(List<T> list, Predicate<T> predicate) {
        if (list == null) {
            return -1;
        }
        return IntStream.range(0, list.size()).filter((a) -> predicate.test(list.get(a))).findFirst().orElse(-1);
    }

    public static Map<String, Set<String>> reverseValuesToKeys(Map<String, String> map) {
        Map<String, Set<String>> inverseMap = new HashMap<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (inverseMap.containsKey(entry.getValue())) {
                inverseMap.get(entry.getValue()).add(entry.getKey());
            } else {
                inverseMap.put(entry.getValue(), new HashSet<>(Arrays.asList(entry.getKey())));
            }
        }
        return inverseMap;
    }

}

