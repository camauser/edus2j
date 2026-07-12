package edus2.adapter.ui;

import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.videosurface.CallbackVideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurface;
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurfaceAdapters;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormat;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallbackAdapter;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.RenderCallback;
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.format.RV32BufferFormat;

import java.nio.ByteBuffer;

/**
 * Builds a VLC callback video surface that paints into a JavaFX {@link ImageView}
 * via {@link PixelWriter}. Works on JavaFX 11; the stock vlcj-javafx
 * {@code ImageViewVideoSurface} requires {@code PixelBuffer} (JavaFX 13+).
 */
final class PixelWriterVideoSurface {

    private PixelWriterVideoSurface() {
    }

    static VideoSurface create(ImageView imageView) {
        FrameTarget target = new FrameTarget(imageView);
        return new CallbackVideoSurface(target, target, true, VideoSurfaceAdapters.getVideoSurfaceAdapter());
    }

    private static final class FrameTarget extends BufferFormatCallbackAdapter implements RenderCallback {
        private final ImageView imageView;
        private volatile PixelWriter pixelWriter;

        private FrameTarget(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
            Platform.runLater(() -> {
                WritableImage image = new WritableImage(sourceWidth, sourceHeight);
                pixelWriter = image.getPixelWriter();
                imageView.setImage(image);
            });
            return new RV32BufferFormat(sourceWidth, sourceHeight);
        }

        @Override
        public void lock(MediaPlayer mediaPlayer) {
        }

        @Override
        public void display(MediaPlayer mediaPlayer, ByteBuffer[] nativeBuffers, BufferFormat bufferFormat,
                            int displayWidth, int displayHeight) {
            int stride = displayWidth * 4;
            byte[] frame = new byte[stride * displayHeight];
            ByteBuffer src = nativeBuffers[0];
            src.position(0);
            src.get(frame);

            Platform.runLater(() -> {
                PixelWriter writer = pixelWriter;
                if (writer != null) {
                    writer.setPixels(0, 0, displayWidth, displayHeight,
                            PixelFormat.getByteBgraInstance(), frame, 0, stride);
                }
            });
        }

        @Override
        public void unlock(MediaPlayer mediaPlayer) {
        }
    }
}
