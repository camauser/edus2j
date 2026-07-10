package edus2.adapter.ui;

import javafx.scene.image.ImageView;

@FunctionalInterface
public interface MediaPlayerEventHandler {
    void handleEvent(ImageView videoView);
}
