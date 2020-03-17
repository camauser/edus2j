package edus2.adapter.scenecontents;

import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import javafx.stage.Window;
import org.testfx.framework.junit.ApplicationTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public abstract class SceneContentsTest extends ApplicationTest {
    // courtesy of https://stackoverflow.com/questions/48565782/testfx-how-to-test-validation-dialogs-with-no-ids
    protected void assertPopupContents(String expectedMessage) {
        DialogPane popupStage = getPopupDialogPane();

        assertEquals(expectedMessage, popupStage.getContentText());
    }

    protected DialogPane getPopupDialogPane() {
        return (DialogPane)getTopModalStage().getScene().getRoot();
    }

    private javafx.stage.Stage getTopModalStage() {
        // Get a list of windows but ordered from top[0] to bottom[n] ones.
        // It is needed to get the first found modal window.
        final List<Window> allWindows = new ArrayList<>(robotContext().getWindowFinder().listWindows());
        Collections.reverse(allWindows);

        return (javafx.stage.Stage) allWindows
                .stream()
                .filter(window -> window instanceof javafx.stage.Stage)
                .filter(window -> ((javafx.stage.Stage) window).getModality() == Modality.APPLICATION_MODAL)
                .findFirst()
                .orElse(null);
    }
}
