package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.logic.commands.TagAddCommand.TagAddDescriptor;
import seedu.address.logic.commands.TagRemoveCommand.TagRemoveDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.PersonNotFoundException;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_NAME_JAMES = "James Wong";
    public static final String VALID_NAME_LUCY = "Lucy Lee";
    public static final String VALID_PHONE_AMY = "11111111";
    public static final String VALID_PHONE_BOB = "22222222";
    public static final String VALID_PHONE_JAMES = "33333333";
    public static final String VALID_PHONE_LUCY = "44444444";
    public static final String VALID_EMAIL_AMY = "amy@example.com";
    public static final String VALID_EMAIL_BOB = "bob@example.com";
    public static final String VALID_EMAIL_JAMES = "james@example.com";
    public static final String VALID_EMAIL_LUCY = "lucy@example.com";
    public static final String VALID_ADDRESS_AMY = "Block 312, Amy Street 1";
    public static final String VALID_ADDRESS_BOB = "Block 123, Bobby Street 3";
    public static final String VALID_ADDRESS_JAMES = "Block 231, James Street 2";
    public static final String VALID_ADDRESS_LUCY = "Block 132, Lucy Street 4";
    public static final String VALID_TAG_HUSBAND = "husband";
    public static final String VALID_TAG_FRIEND = "friend";
    public static final String VALID_TAG_COLLEGE_FRIEND = "college friend";

    public static final String WHITESPACE = " ";

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PHONE_BOB;
    public static final String EMAIL_DESC_AMY = " " + PREFIX_EMAIL + VALID_EMAIL_AMY;
    public static final String EMAIL_DESC_BOB = " " + PREFIX_EMAIL + VALID_EMAIL_BOB;
    public static final String ADDRESS_DESC_AMY = " " + PREFIX_ADDRESS + VALID_ADDRESS_AMY;
    public static final String ADDRESS_DESC_BOB = " " + PREFIX_ADDRESS + VALID_ADDRESS_BOB;
    public static final String TAG_DESC_FRIEND = " " + PREFIX_TAG + VALID_TAG_FRIEND;
    public static final String TAG_DESC_HUSBAND = " " + PREFIX_TAG + VALID_TAG_HUSBAND;

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_ADDRESS_DESC = " " + PREFIX_ADDRESS; // empty string not allowed for addresses

    public static final EditCommand.EditPersonDescriptor DESC_AMY;
    public static final EditCommand.EditPersonDescriptor DESC_BOB;
    public static final TagAddCommand.TagAddDescriptor DESC_JAMES;
    public static final TagAddCommand.TagAddDescriptor DESC_LUCY;
    public static final TagRemoveCommand.TagRemoveDescriptor DESCR_JAMES;
    public static final TagRemoveCommand.TagRemoveDescriptor DESCR_LUCY;

    static {
        DESC_AMY = new EditPersonDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        DESC_BOB = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
        DESC_JAMES = new TagAddDescriptor(new PersonBuilder().withName(VALID_NAME_JAMES)
                .withPhone(VALID_PHONE_JAMES).withEmail(VALID_EMAIL_JAMES).withAddress(VALID_ADDRESS_JAMES)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build());
        DESC_LUCY = new TagAddDescriptor(new PersonBuilder().withName(VALID_NAME_LUCY)
                .withPhone(VALID_PHONE_LUCY).withEmail(VALID_EMAIL_LUCY).withAddress(VALID_ADDRESS_LUCY)
                .withTags(VALID_TAG_FRIEND).build());
        DESCR_JAMES = new TagRemoveDescriptor(new PersonBuilder().withName(VALID_NAME_JAMES)
                .withPhone(VALID_PHONE_JAMES).withEmail(VALID_EMAIL_JAMES).withAddress(VALID_ADDRESS_JAMES)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build());
        DESCR_LUCY = new TagRemoveDescriptor(new PersonBuilder().withName(VALID_NAME_LUCY)
                .withPhone(VALID_PHONE_LUCY).withEmail(VALID_EMAIL_LUCY).withAddress(VALID_ADDRESS_LUCY)
                .withTags(VALID_TAG_FRIEND).build());
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the result message matches {@code expectedMessage} <br>
     * - the {@code actualModel} matches {@code expectedModel}
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            Model expectedModel) {
        try {
            CommandResult result = command.execute();
            assertEquals(expectedMessage, result.feedbackToUser);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the address book and the filtered person list in the {@code actualModel} remain unchanged
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        AddressBook expectedAddressBook = new AddressBook(actualModel.getAddressBook());
        List<ReadOnlyPerson> expectedFilteredList = new ArrayList<>(actualModel.getFilteredPersonList());

        try {
            command.execute();
            fail("The expected CommandException was not thrown.");
        } catch (CommandException e) {
            assertEquals(expectedMessage, e.getMessage());
            assertEquals(expectedAddressBook, actualModel.getAddressBook());
            assertEquals(expectedFilteredList, actualModel.getFilteredPersonList());
        }
    }

    /**
     * Updates {@code model}'s filtered list to show only the first person in the {@code model}'s address book.
     */
    public static void showFirstPersonOnly(Model model) {
        ReadOnlyPerson person = model.getAddressBook().getPersonList().get(0);
        final String[] splitName = person.getName().fullName.split("\\s+");
        model.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assert model.getFilteredPersonList().size() == 1;
    }

    /**
     *Updates {@code model}'s filtered list to show only the first person in the {@code model}'s recycle bin.
     */
    public static void showFirstBinPersonOnly(Model model) {
        ReadOnlyPerson person = model.getRecycleBin().getPersonList().get(0);
        final String[] splitName = person.getName().fullName.split("\\s+");
        model.updateFilteredBinList(new NameContainsKeywordsPredicate(Arrays.asList(splitName[0])));

        assert model.getRecycleBinPersonList().size() == 1;
    }

    /**
     * Deletes the first person in {@code model}'s filtered list from {@code model}'s address book.
     */
    public static void deleteFirstPerson(Model model) {
        ReadOnlyPerson firstPerson = model.getFilteredPersonList().get(0);
        ArrayList<ReadOnlyPerson> deleteList = new ArrayList<>();
        deleteList.add(firstPerson);
        try {
            model.deletePerson(deleteList);
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("Person in filtered list must exist in model.", pnfe);
        } catch (DuplicatePersonException d) {
            throw new AssertionError("Person in the filtered list should not be duplicate", d);
        }
    }
}
