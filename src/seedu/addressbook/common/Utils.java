package seedu.addressbook.common;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import seedu.addressbook.data.person.Person;

/**
 * Utility methods
 */
public class Utils {

    /**
     * Checks whether any of the given items are null.
     */
    public static boolean isAnyNull(Object... items) {
        for (Object item : items) {
            if (item == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if every element in a collection are unique by {@link Object#equals(Object)}.
     */
    public static boolean elementsAreUnique(Collection<?> items) {
        final List<Object> testSet = new ArrayList<>();
        for (Object item: items) {
            if (objectIsInList(testSet, item)) {
                return false;
            }

            testSet.add(item);
        }
        return true;
    }
    /** Checks if an object is exists in the list*/
    private static boolean objectIsInList (List<Object> list, Object object) {
        return (!list.stream()
                .filter(p -> (p == null && object == null)
                        || (p != null && p.equals(object)))
                .collect(Collectors.toList())
                .isEmpty());
    }

    /**
     * Checks if a given string is a valid date.
     * Solution below adapted from https://stackoverflow.com/a/30578421
     */
    public static boolean isValidDate(String value) {
        boolean isValid;
        final String format = "dd-MM-yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        try {
            String parsedDate = LocalDate.parse(value, formatter).format(formatter);
            isValid = value.equals(parsedDate);
        } catch (DateTimeParseException ex) {
            isValid = false;
        }
        return isValid;
    }

    /**
     * Custom comparator for Date string in Fees in the form of DD-MM-YYYY
     * Allows for sorting of Person's list according to YYYYMMDD of Fees section.
     */
    public static class FeesComparator implements Comparator<Person> {
        @Override
        public int compare(Person o1, Person o2) {
            StringBuilder main1 = new StringBuilder();
            StringBuilder main2 = new StringBuilder();
            main1.append(o1.getFees().duedate.substring(6, 10));
            main1.append(o1.getFees().duedate.substring(3, 5));
            main1.append(o1.getFees().duedate.substring(0, 2));
            main2.append(o2.getFees().duedate.substring(6, 10));
            main2.append(o2.getFees().duedate.substring(3, 5));
            main2.append(o2.getFees().duedate.substring(0, 2));
            return main1.toString().compareTo
                    (main2.toString());
        }
    }
}
