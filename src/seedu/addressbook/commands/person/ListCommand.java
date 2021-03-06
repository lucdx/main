package seedu.addressbook.commands.person;

import java.util.List;

import seedu.addressbook.commands.Command;
import seedu.addressbook.commands.commandresult.CommandResult;
import seedu.addressbook.data.person.ReadOnlyPerson;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n"
            + "Displays all persons (only names) in the address book as a list with index numbers.\n\t"
            + "Example: " + COMMAND_WORD;

    @Override
    public CommandResult execute() {
        List<ReadOnlyPerson> allPersons = addressBook.getAllPersons().immutableListView();
        return new CommandResult(getMessageForPersonListShownSummary(allPersons), allPersons);
    }

    @Override
    public Category getCategory() {
        return Category.PERSON;
    }

    @Override
    public String getCommandUsageMessage() {
        return MESSAGE_USAGE;
    }
}
