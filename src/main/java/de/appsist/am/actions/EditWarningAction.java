package de.appsist.am.actions;

import de.appsist.am.ActionFailedException;
import de.appsist.am.PersistenceHandler;
import de.appsist.am.model.ContentModel;
import de.appsist.am.model.GuideModel;
import de.appsist.am.model.WarningModel;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author simon.schwantzer(at)im-c.de
 */
public class EditWarningAction extends BaseAction {
    private final static Logger LOGGER = Logger.getLogger(EditWarningAction.class.getName());
    
    private final WarningModel warning;
    private final PersistenceHandler persistenceHandler;
    private final String newText;
    private final String oldText;
    private final GuideModel guide;
    private final ContentModel content;
    
    public EditWarningAction(WarningModel warning, String newText, ContentModel content, GuideModel guideModel, PersistenceHandler persistenceHandler) {
        this.warning = warning;
        this.persistenceHandler = persistenceHandler;
        this.guide = guideModel;
        this.content = content;
        this.newText = newText;
        this.oldText = warning.textProperty().get();
    }

    @Override
    public void perform() {
        warning.textProperty().set(newText);
        try {
            writeChange();
            notifyActionPerformed();
        } catch (ActionFailedException ex) {
            notifyActionFailed(ex);
        }
    }

    @Override
    public void undo() {
        warning.textProperty().set(oldText);
        try {
            writeChange();
            notifyActionUndone();
        } catch (ActionFailedException ex) {
            notifyUndoFailed(ex);
        }
    }
    
    private void writeChange() throws ActionFailedException {
        try {
            persistenceHandler.writeContentDescriptor(guide.getBean(), content.getBean());
        } catch (IOException ex) {
           LOGGER.log(Level.SEVERE, "Failed to update content descriptor.", ex);
           throw new ActionFailedException("Failed to update content descriptor.", ex);
        }
    }

    @Override
    public String getDescription() {
        return ("Warnung bearbeiten");
    }
   
}
