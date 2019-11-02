package edus2.adapter.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import edus2.adapter.repository.file.FileScanRepository;
import edus2.application.EDUS2View;
import edus2.application.ScanFacade;
import edus2.domain.ScanRepository;

public class EDUS2JModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(FileScanRepository.class).toInstance(new FileScanRepository(EDUS2View.EDUS2_SAVE_FILE_NAME));

        bind(ScanRepository.class).to(FileScanRepository.class).in(Singleton.class);

        bind(ScanFacade.class).in(Singleton.class);
    }
}
