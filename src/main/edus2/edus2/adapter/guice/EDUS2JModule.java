package edus2.adapter.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import edus2.adapter.guice.provider.FileEDUS2ConfigurationProvider;
import edus2.adapter.repository.file.FileMannequinRepository;
import edus2.adapter.repository.file.FileScanRepository;
import edus2.application.ScanFacade;
import edus2.domain.EDUS2Configuration;
import edus2.domain.MannequinRepository;
import edus2.domain.ScanRepository;

public class EDUS2JModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ScanRepository.class).to(FileScanRepository.class).in(Singleton.class);

        bind(EDUS2Configuration.class).toProvider(FileEDUS2ConfigurationProvider.class).in(Singleton.class);

        bind(ScanRepository.class).to(FileScanRepository.class).in(Singleton.class);

        bind(MannequinRepository.class).to(FileMannequinRepository.class).in(Singleton.class);

        bind(ScanFacade.class).in(Singleton.class);
    }
}
