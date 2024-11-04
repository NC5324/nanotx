package com.tusofia.ndurmush.base.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringUtils extends org.apache.commons.lang3.StringUtils {


    private static final Logger LOGGER = Logger.getLogger(StringUtils.class.getName());

    private static final Pattern AT_LEAST_TWO_SPACES_PATTERN = Pattern.compile("( ){2,}");

    public static final String DASH = "-";

    public static final String PERCENT = "%";

    public static final String COMA = ",";

    public static final String DOT = ".";

    public static final String YES = "Yes";

    public static final String NO = "No";

    /**
     * Trim the string to desired lenght.
     *
     * @param s
     * @param len
     * @return
     */
    public static String limit(String s, int len) {
        if (!isEmpty(s) && s.length() > len) {
            return s.substring(0, len);
        }
        return s;
    }

    /**
     * Trim the string to desired byte size. Not precise!
     *
     * @param str
     * @param len
     * @return
     */
    public static String limitByteSize(String str, int len) {
        int byteSize = 0;
        String result = str;
        do {
            try {
                byteSize = str.getBytes("UTF-8").length;
            } catch (UnsupportedEncodingException e) {
                LOGGER.log(Level.WARNING, str + " has unsupported encoding", e);
            }
            if (byteSize > len) {
                result = str.substring(0, str.length() - 1);
            }
        } while (byteSize > len);
        return result;
    }

    /**
     * Returns right presentation of a string
     *
     * @param s
     * @param len
     * @return
     */

    /**
     * <p>
     * Gets the rightmost {@code len} characters of a String.
     * </p>
     *
     * <p>
     * If {@code len} characters are not available, or the String is
     * {@code null}, the String will be returned without an exception.
     * </p>
     *
     * <pre>
     * StringUtils.right(null, *)    = null
     * StringUtils.right("abcde", -2)     = "cde"
     * StringUtils.right("abc", 0)   = ""
     * StringUtils.right("abc", 2)   = "bc"
     * </pre>
     *
     * @param s
     *            the String to get the leftmost characters from, may be null
     * @param len
     *            the length of the required String
     * @return the leftmost characters, {@code null} if null String input
     */
    public static String right(String s, int len) {
        String result;
        if (len >= 0) {
            if (isEmpty(s) || s.length() < len) {
                result = s;
            } else {
                result = s.substring(s.length() - len);
            }
        } else {
            if (isEmpty(s) || s.length() < -len) {
                result = s;
            } else {
                result = s.substring(-len);
            }
        }
        return result;
    }

    /**
     * Checks if all strings are empty
     * @param strings strings to be checked
     * @return true if at least one string is not empty
     */
    public static boolean isAllEmpty(String...strings) {
        if (strings != null) {
            for(String s : strings) {
                if (StringUtils.isNotEmpty(s)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if all strings are empty
     * @param strings strings to be checked
     * @return true if at least one string is not empty
     */
    public static boolean isAnyNotEmpty(String...strings) {
        return !isAllEmpty(strings);
    }


    /**
     * Checks if all strings are empty
     * @param strings strings to be checked
     * @return true if at least one string is not empty
     */
    public static boolean isAllBlank(String...strings) {
        if (strings != null) {
            for(String s : strings) {
                if (StringUtils.isNotBlank(s)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if all strings are empty
     * @param strings strings to be checked
     * @return true if at least one string is not empty
     */
    public static boolean isAnyNotBlank(String...strings) {
        return !isAllBlank(strings);
    }

    /**
     * Checks if all strings are not blank
     * @param strings strings to be checked
     * @return false if at least one string is blank
     */
    public static boolean isAllNotBlank(String...strings) {
        return !isAnyBlank(strings);
    }

    /**
     * Checks if all strings are not empty
     * @param strings strings to be checked
     * @return false if at least one string is empty
     */
    public static boolean isAllNotEmpty(String...strings) {
        return !isAnyEmpty(strings);
    }

    /**
     * Compare two strings
     *
     * @param a
     * @param b
     * @return
     */
    public static int compareTo(String a, String b) {
        String inputA = a;
        String inputB = b;
        if (a == null) {
            inputA = "";
        }
        if (b == null) {
            inputB = "";
        }
        return inputA.compareTo(inputB);
    }

    /**
     * Compares ignore case two strings
     *
     * @param a
     * @param b
     * @return
     */
    public static int compareToIgnoreCase(String a, String b) {
        String inputA = a;
        String inputB = b;
        if (a == null) {
            inputA = "";
        }
        if (b == null) {
            inputB = "";
        }
        return inputA.compareToIgnoreCase(inputB);
    }

    /**
     * Returns true if two strings are equals, as trims them before comparing
     *
     * @param s1
     * @param s2
     * @return
     */
    public static boolean equalsTrim(final String s1, final String s2) {
        return equals(trim(s1), trim(s2));
    }

    /**
     * Returns true, if the two strings are not equals, as trims them before
     * comparing
     *
     * @param s1
     * @param s2
     * @return
     */
    public static boolean notEqualsTrim(final String s1, final String s2) {
        return !equalsTrim(s1, s2);
    }

    /**
     * Returns true if the two strings are equals ignore case, as trims them
     * before comparing
     *
     * @param s1
     * @param s2
     * @return
     */
    public static boolean equalsIgnoreCaseTrim(final String s1, final String s2) {
        return equalsIgnoreCase(trim(s1), trim(s2));
    }

    /**
     * Returns true, if the two strings are not equals ignore case, as trims
     * them before comparing
     *
     * @param s1
     * @param s2
     * @return
     */
    public static boolean notEqualsIgnoreCaseTrim(final String s1, final String s2) {
        return !equalsIgnoreCaseTrim(s1, s2);
    }

    /**
     * Returns true, if the two {@code CharSequence} are not equals
     *
     * @param cs1
     * @param cs2
     * @return
     */
    public static boolean notEquals(final CharSequence cs1, final CharSequence cs2) {
        return !equals(cs1, cs2);
    }

    /**
     * Returns true if s1 contains s2, as trims them before evaluation
     *
     * @param s1
     * @param s2
     * @return
     */
    public static boolean containsTrim(final String s1, final String s2) {
        return contains(trim(s1), trim(s2));
    }

    /**
     * Return true if s1 does not contain s2, as trims them before evaluation
     *
     * @param s1
     * @param s2
     * @return
     */
    public static boolean notContainsTrim(final String s1, final String s2) {
        return !contains(trim(s1), trim(s2));
    }

    /**
     * Returns substring of input string, limited to endIndex.
     *
     * @param input
     * @param endIndex
     * @return
     */
    public static String limitTo(String input, int endIndex) {
        if (isNotEmpty(input) && input.length() - 1 > endIndex) {
            String result = input.substring(0, endIndex);
            return result;
        }
        return input;
    }


    /**
     * Returns hashCode of input string s
     *
     * @param s
     * @return
     */
    public static int hashCode(String s) {
        final int h;
        if (s != null) {
            h = s.hashCode();
        } else {
            h = 0;
        }
        return h;
    }

    /**
     * Returns lower case Set of input
     *
     * @param c
     * @return
     */
    public static Set<String> lowerCase(Set<String> c) {
        if (c == null) {
            return new TreeSet<String>();
        }
        final Set<String> lc = new TreeSet<String>();
        for(final String s : c) {
            lc.add(lowerCase(s));
        }
        return lc;
    }

    /**
     * Returns String of non blank items, separated by input separator
     *
     * @param separator
     * @param items
     * @return
     */
    public static String joinNonBlanks(String separator, Collection<String> items) {
        final List<String> itemsList = new ArrayList<>();
        for(final String item : items) {
            if (isNotBlank(item)) {
                itemsList.add(item);
            }
        }
        return join(itemsList, separator);
    }

    /**
     * Returns String of non blank items, separated by input separator. If input
     * items are empty, returns emptyListMessage
     *
     * @param separator
     * @param items
     * @param emptyListMessage
     * @return
     */
    public static String joinNonBlanks(String separator, Collection<String> items, String emptyListMessage) {
        if(items.isEmpty()){
            items.add(emptyListMessage);
        }
        return joinNonBlanks(separator, items);
    }

    /**
     * Returns String of non blank items, separated by input separator
     *
     * @param separator
     * @param items
     * @return
     */
    public static String joinNonBlanks(String separator, String...items) {
        return joinNonBlanks(separator, Arrays.asList(items));
    }

    //-----------------------------------------------------------------------
    /**
     * <p>Gets the leftmost {@code len} characters of a String.</p>
     *
     * <p>If {@code len} characters are not available, or the
     * String is {@code null}, the String will be returned without
     * an exception. An empty String is returned if len is negative.</p>
     *
     * <pre>
     * StringUtils.left(null, *)    = null
     * StringUtils.left("abcde", -2)     = "abc"
     * StringUtils.left("", *)      = ""
     * StringUtils.left("abc", 0)   = ""
     * StringUtils.left("abc", 2)   = "ab"
     * StringUtils.left("abc", 4)   = "abc"
     * </pre>
     *
     * @param str  the String to get the leftmost characters from, may be null
     * @param len  the length of the required String
     * @return the leftmost characters, {@code null} if null String input
     */
    public static String left(final String str, int len) {
        if (str == null) {
            return null;
        }
        int currentLen = len;
        if (len < 0) {
            currentLen = str.length() + len;
            if (currentLen < 0) {
                return "";
            }
        }
        if (str.length() <= currentLen) {
            return str;
        }
        return str.substring(0, currentLen);
    }

    /**
     * Converts a set of strings to a upper cases
     * @param set the set to be converted to upper case
     * @param locale the locale to be used for upper case
     * @return a new HashSet<String> with strings converted to upper case
     */
    public static Set<String> upperCaseSet(Set<String> set, Locale locale) {
        final Set<String> uppercaseSet = new HashSet<String>();
        if (set != null) {
            for(final String s : set) {
                uppercaseSet.add(s.toUpperCase(locale));
            }
        }
        return uppercaseSet;
    }

    /**
     * Replaces 2 or more spaces "  " with one space " "
     * @param s string to be replaced
     * @return string with single spaces
     */
    public static String replaceWithSingleSpace(final String s) {
        return (isNotEmpty(s) ? AT_LEAST_TWO_SPACES_PATTERN.matcher(s).replaceAll(" ") : s);
    }

    /**
     * Returns array of trimmed input {@code array} elements
     *
     * @param array
     * @return
     */
    public static String[] trim(String[] array) {
        if (array != null && array.length > 0) {
            for(int i = 0; i < array.length; i++) {
                array[i] = trim(array[i]);
            }
        }
        return array;
    }

    /**
     * Splits distinct, separated by {@code separator} elements from input
     * {@code str}, into list
     *
     * @param str
     * @param separator
     * @return
     */
    public static List<String> splitDistinctByWholeSeparatorIntoList(final String str, final String separator) {
        if (isBlank(str)) {
            return Collections.emptyList();
        }
        final String[] array = trim(splitByWholeSeparator(str, separator));
        return Arrays.stream(array).filter(StringUtils::isNotBlank).distinct().collect(Collectors.toList());
    }

    /**
     * Splits separated by {@code separator} elements from input {@code str},
     * into list
     *
     * @param str
     * @param separator
     * @return
     */
    public static List<String> splitByWholeSeparatorIntoList(final String str, final String separator) {
        if (isBlank(str)) {
            return Collections.emptyList();
        }
        final String[] array = trim(splitByWholeSeparator(str, separator));
        return Arrays.stream(array).filter(StringUtils::isNotBlank).collect(Collectors.toList());
    }

    /**
     * Splits separated by {@code separator} elements from input {@code str},
     * into set
     *
     * @param str
     * @param separator
     * @return
     */
    public static Set<String> splitByWholeSeparatorIntoSet(final String str, final String separator) {
        if (isBlank(str)) {
            return Collections.emptySet();
        }
        final String[] array = trim(splitByWholeSeparator(str, separator));
        return Arrays.stream(array).filter(StringUtils::isNotBlank).collect(Collectors.toSet());
    }

    public static String addLeadingSymbolsToMatchLength(final String input, final char symbol, final int neededLength) {
        StringBuilder buf = new StringBuilder(input);

        while (buf.length() < neededLength) {
            buf.insert(0, symbol);
        }

        return buf.toString();
    }

    /**
     * Returns input {@code str}, if it is not blank. Otherwise returns
     * {@code defaultValue}
     *
     * @param str
     * @param defaultValue
     * @return
     */
    public static String defaultIfBlank(final String str, final String defaultValue) {
        if (isBlank(str)) {
            return defaultValue;
        }
        return str;
    }

    /**
     * Returns string representation of input collection {@code stringList}, as
     * elements are concatenated and separated by {@code delimiter}
     *
     * @param stringList
     * @param delimiter
     * @return
     */
    public static String splitListToDelimitedString(List<String> stringList, String delimiter) {
        StringBuilder result = new StringBuilder("");
        if (!CollectionUtils.isEmpty(stringList)) {
            stringList.stream().forEach(s -> result.append(s).append(delimiter));
        }
        return result.toString();
    }

    /**
     * Returns string representation of input array {@code values}, concatenated
     * not null elements
     *
     * @param values
     * @return
     */
    public static String concatenateNonNulls(final String... values) {
        List<String> nonNulls = new ArrayList<>(values.length);
        for (String value : values) {
            if (null != value) {
                nonNulls.add(value);
            }
        }
        if (nonNulls.isEmpty()) {
            return null;
        }
        StringBuilder result = new StringBuilder("");
        nonNulls.forEach(s -> result.append(s));
        return result.toString();
    }

    /**
     * Returns string with trimmed first {@code chars} of {@code input}
     *
     * @param input
     * @param chars
     * @return
     */
    public static String trimLeadingChars(String input, final String chars) {
        if (isNotEmpty(input) && isNotEmpty(chars)) {
            int count = chars.length();
            String str = input;
            while (str.startsWith(chars)) {
                str = str.substring(count);
            }
            return str;
        }
        return input;
    }

    /**
     * Returns a string representation of {@code obj}
     *
     * @param obj
     * @return
     */
    public static String toString(final Object obj) {
        if (null != obj) {
            return obj.toString();
        }
        return null;
    }

    /**
     * Returns true, if all elements of {@code strings} are blank
     *
     * @param strings
     * @return
     */
    public static boolean isAllBlank(final Collection<String> strings) {
        if (CollectionUtils.isNotEmpty(strings)) {
            for (String s : strings) {
                if (StringUtils.isNotBlank(s)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns true if least one element of {@code strings}, is not blank
     *
     * @param strings
     * @return
     */
    public static boolean isAnyNotBlank(final Collection<String> strings) {
        return !isAllBlank(strings);
    }

    public static String firstNonBlank(final String... strings) {
        for (String str : strings) {
            if (isNotBlank(str)) {
                return str;
            }
        }
        return null;
    }

    public static String addString(final String str, final String add, final int position) {
        if (null == str) {
            return add;
        }
        if (null == add) {
            return str;
        }
        if (position > str.length()) {
            return str + add;
        }
        return str.substring(0, position) + add + str.substring(position);
    }
}

