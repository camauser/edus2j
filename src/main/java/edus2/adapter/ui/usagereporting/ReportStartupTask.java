package edus2.adapter.ui.usagereporting;

import edus2.application.usagereporting.UsageReportingService;
import javafx.concurrent.Task;

import java.util.Optional;

public class ReportStartupTask extends Task<Optional<String>> {

    private UsageReportingService usageReportingService;

    public ReportStartupTask(UsageReportingService usageReportingService) {
        this.usageReportingService = usageReportingService;
    }

    @Override
    protected Optional<String> call() {
        return usageReportingService.reportStartup();
    }
}
