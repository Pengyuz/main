package seedu.address.commons.events.ui;

import seedu.address.commons.events.BaseEvent;
import seedu.address.ui.PersonCard;

//@@author dalessr
/**
 * Indicates a request for opening twitter webview
 */
public class OpenTwitterWebViewEvent extends BaseEvent {

    private final PersonCard newSelection;

    public OpenTwitterWebViewEvent(PersonCard newSelection) {
        this.newSelection = newSelection;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    public PersonCard getNewSelection() {
        return newSelection;
    }
}
