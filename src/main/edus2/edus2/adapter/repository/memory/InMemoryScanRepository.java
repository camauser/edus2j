package edus2.adapter.repository.memory;

import edus2.domain.Scan;
import edus2.domain.ScanRepository;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class InMemoryScanRepository implements ScanRepository {

    private List<Scan> scans;

    @Inject
    public InMemoryScanRepository() {
        this.scans = new ArrayList<>();
    }

    @Override
    public List<Scan> retrieveAll() {
        return scans;
    }

    @Override
    public void save(Scan scan) {
        scans.add(scan);
    }

    @Override
    public void remove(Scan scan) {
        scans.remove(scan);
    }

    @Override
    public void removeAll() {
        scans.clear();
    }
}
