package seedu.addressbook.data.person;

import seedu.addressbook.data.exception.IllegalValueException;


/**
 * Represents a Person's fees due in the address book.
 * Guarantees: immutable;
 */
public class Fees implements Printable {
    public static final String EXAMPLE = "$3560.98";
    public static final String MESSAGE_FEES_CONSTRAINTS = "Person's fees should be positive and have 2 decimal places.";
    public static final String MESSAGE_DATE_CONSTRAINTS = "Due date for fees should be in DD-MM-YYYY.";
    public static final String FEES_VALIDATION_REGEX = "[0-9]+([,.][0-9]{1,2})?";
    public static final String DATE_VALIDATION_REGEX = "[0-9]{1,2}-[0-9]{1,2}-[0-9]{4}";

    public final String value;
    public final String duedate;
    public boolean isEdited = false;

    //TODO hide the fees when it's first initialised as 00-00-0000
    // new command for when they paid fees, fully, thus reinitiliasing as 00-00-0000
    // so that it doesnt fail valid date check in edit fees
    // therefore fees will always either be 00-00-0000 or a valid date,
    // and will always be hidden when its 00-00-0000
    // this way is easier as dont need to change all the adapted person and stuff

    /**
     * Validates given AdaptedFees.
     *
     * @throws IllegalValueException if given fees string is invalid.
     */
    public Fees(String fees, String date) throws IllegalValueException {
        if (!isValidFees(fees)) {
            throw new IllegalValueException(MESSAGE_FEES_CONSTRAINTS);
        }
        if (!isValidDate(date)) {
            throw new IllegalValueException(MESSAGE_DATE_CONSTRAINTS);
        }
        this.value = fees;
        this.duedate = date;
    }

    /**
     * Initialises all fees of everyone to 0.
     *
     *
     */
    public Fees() {
        this.value = "0.00";
        this.duedate = "00-00-0000";
    }

    /**
     * Checks if a given string is a valid person fee.
     *
     */

    public static boolean isValidFees(String test) {
        return test.matches(FEES_VALIDATION_REGEX);
    }

    /**
     * Checks if a given string is a valid date.
     *
     */
    public static boolean isValidDate(String test) {
        return test.matches(DATE_VALIDATION_REGEX);
    }

    public boolean isPrivate() {
        return true;
    }

    @Override
    public String getPrintableString(boolean showPrivate) {
        if (isPrivate()) {
            if (showPrivate) {
                return "{private Fees: " + value + " / " + duedate + "} ";
            } else {
                return "";
            }
        }
        return "Fees: " + value + " " + duedate;
    }
}
