package edus2.adapter.scenecontents;

import com.google.inject.Inject;
import edus2.adapter.ui.builder.SceneBuilder;
import edus2.application.version.ApplicationInfo;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import static java.lang.String.format;

public class Credits extends SceneContents {

    @Inject
    public Credits(SceneBuilder sceneBuilder) {
        super(sceneBuilder);
    }

    protected Parent buildSceneContents() {
        BorderPane credits = new BorderPane();
        Text header = new Text("EDUS2J Credits");
        header.setFont(new Font(32.0));
        credits.setTop(header);
        header.setFont(Font.font("Calibri", FontWeight.BOLD,
                FontPosture.ITALIC, 36.0));
        BorderPane.setAlignment(header, Pos.TOP_CENTER);
        Text details = new Text(format(
                "Credits for this project go to: \n"
                        + "Java Porting: Cameron Auser\n"
                        + "Original Design: Paul Kulyk, Paul Olsynski\n"
                        + "EDUS2 is an emergency department ultrasound simulator, "
                        + "and EDUS2J is a port of this original software to Java.\nEDUS2J version: %s", ApplicationInfo.getVersion()));
        details.setFont(new Font("Calibri", 18.0));
        credits.setCenter(details);
        BorderPane.setAlignment(credits, Pos.CENTER);
        return credits;
    }

    @Override
    public String getTitle() {
        return "EDUS2J Credits";
    }
}
