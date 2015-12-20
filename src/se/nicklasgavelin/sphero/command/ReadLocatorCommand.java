package se.nicklasgavelin.sphero.command;

/**
 *
 * Created by cd on 20/12/2015.
 */
public class ReadLocatorCommand extends CommandMessage {
    /**
     * read the locator command.
     */
    public ReadLocatorCommand() {
        super(COMMAND_MESSAGE_TYPE.READ_LOCATOR);
    }
}


