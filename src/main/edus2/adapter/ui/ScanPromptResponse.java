package edus2.adapter.ui;

import java.util.Optional;

public class ScanPromptResponse {
    private final String response;
    private boolean cancelled;

    private ScanPromptResponse(String response, boolean cancelled) {
        this.response = response;
        this.cancelled = cancelled;
    }

    public static ScanPromptResponse ofResponse(String response) {
        return new ScanPromptResponse(response, false);
    }

    public static ScanPromptResponse ofCancelled() {
        return new ScanPromptResponse(null, true);
    }

    public Optional<String> getResponse() {
        return Optional.ofNullable(response);
    }

    public boolean isCancelled() {
        return cancelled;
    }
}
