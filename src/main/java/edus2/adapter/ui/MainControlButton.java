package edus2.adapter.ui;

import javafx.scene.control.Button;
import javafx.scene.text.Font;

public class MainControlButton extends Button {
    private static final Font BUTTON_FONT = new Font("Calibri", 18);
    private static final String BUTTON_BORDER_COLOR_HEX = "#616361";
    private static final String BUTTON_BACKGROUND_COLOR_HEX = "#7a7d7a";
    private static final String BACKGROUND_COLOR_CSS_STYLE = String.format("-fx-background-color: %s;" +
            " -fx-border-color: %s; -fx-border-radius: 3px", BUTTON_BACKGROUND_COLOR_HEX, BUTTON_BORDER_COLOR_HEX);

    public MainControlButton(String buttonText) {
        super(buttonText);
        this.setFont(BUTTON_FONT);
        this.setStyle(BACKGROUND_COLOR_CSS_STYLE);
    }
}
