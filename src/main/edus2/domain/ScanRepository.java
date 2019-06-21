package edus2.domain;

import java.util.List;

public interface ScanRepository {
    List<Scan> retrieveAll();

    void save(Scan scan);

    void remove(Scan scan);

    void removeAll();
}
