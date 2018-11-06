package seedu.addressbook.commands.fees;

import seedu.addressbook.commands.commandformat.indexformat.IndexFormatCommand;
import seedu.addressbook.commands.commandresult.CommandResult;
import seedu.addressbook.common.Messages;
import seedu.addressbook.data.person.Fees;
import seedu.addressbook.data.person.Person;
import seedu.addressbook.data.person.UniquePersonList.PersonNotFoundException;

/**
 * Remove due fees from a respective person
 */
public class PaidFeesCommand extends IndexFormatCommand {
    public static final String COMMAND_WORD = "paidfees";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" + "Removes the existing fees of a person "
            + "in the address book.\n\t"
            + "Parameters: INDEX\n\t"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Fees paid: %1$s";

    /**
     * Constructor used for Privileges
     * Command constructed has no functionality
     * */
    public PaidFeesCommand() {
        // Does nothing
    }

    @Override
    public CommandResult execute() {
        try {
            try {
                Person person = getTargetPerson();
                person.setFees(new Fees());
                return new CommandResult(String.format(MESSAGE_SUCCESS, person.getAsTextShowFee()));
            } catch (PersonNotFoundException pnfe) {
                return new CommandResult(Messages.MESSAGE_PERSON_NOT_IN_ADDRESSBOOK);
            }
        } catch (IndexOutOfBoundsException ie) {
            return new CommandResult(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }
    }

    /**
     * Checks if the command can potentially change the data to be stored
     */
    @Override
    public boolean isMutating() {
        return true;
    }

    @Override
    public Category getCategory() {
        return Category.FEES;
    }

    @Override
    public String getCommandUsageMessage() {
        return MESSAGE_USAGE;
    }

}
