package edus2.adapter.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import edus2.adapter.guice.provider.FileEDUS2ConfigurationProvider;
import edus2.adapter.repository.file.FileManikinRepository;
import edus2.adapter.repository.file.FileScanRepository;
import edus2.adapter.ui.ListenableMediaPlayer;
import edus2.adapter.ui.Toast;
import edus2.adapter.ui.builder.SceneBuilder;
import edus2.application.ScanFacade;
import edus2.domain.EDUS2Configuration;
import edus2.domain.ManikinRepository;
import edus2.domain.ScanRepository;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class EDUS2JModule extends AbstractModule {

    private final Stage stage;
    private final BorderPane mainDisplayPane;
    private ListenableMediaPlayer listenableMediaPlayer;

    public EDUS2JModule(Stage stage, BorderPane mainDisplayPane, ListenableMediaPlayer listenableMediaPlayer) {
        this.stage = stage;
        this.mainDisplayPane = mainDisplayPane;
        this.listenableMediaPlayer = listenableMediaPlayer;
    }

    @Override
    protected void configure() {
        bind(Stage.class).toInstance(stage);

        // todo: would be wise to break out a custom BorderPane class to ensure we're actually looking for the main pane instead of any border pane
        bind(BorderPane.class).toInstance(mainDisplayPane);

        bind(ListenableMediaPlayer.class).toInstance(listenableMediaPlayer);

        bind(ScheduledExecutorService.class).toInstance(Executors.newScheduledThreadPool(5));

        bind(ScanRepository.class).to(FileScanRepository.class).in(Singleton.class);

        bind(EDUS2Configuration.class).toProvider(FileEDUS2ConfigurationProvider.class).in(Singleton.class);

        bind(ScanRepository.class).to(FileScanRepository.class).in(Singleton.class);

        bind(ManikinRepository.class).to(FileManikinRepository.class).in(Singleton.class);

        bind(ScanFacade.class).in(Singleton.class);

        bind(SceneBuilder.class).in(Singleton.class);

        bind(Toast.class).in(Singleton.class);
    }
}
