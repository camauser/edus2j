package edus2.adapter.ui;

import javafx.scene.control.Button;
import javafx.scene.text.Font;

public class MainControlButton extends Button {
    private static final Font BUTTON_FONT = new Font("Calibri", 18);

    public MainControlButton(String buttonText) {
        super(buttonText);
        this.setFont(BUTTON_FONT);
    }
}
