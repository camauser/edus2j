package logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.util.BitSet;
import java.util.Optional;

public class LoggerSingleton {
    private static final String DEFAULT_LOG_FILE_NAME = "edus2j.log";
    private static final String INFO_LEVEL_TEXT = "INFO";
    private static final String WARNING_LEVEL_TEXT = "WARNING";
    private static final String ERROR_LEVEL_TEXT = "ERROR";
    private static Optional<BufferedWriter> logFileHandle = Optional.empty();
    private static final BitSet logLevel = new BitSet(3);

    public static void setLogFileName(String newLogName)
    {
        try
        {
            if (logFileHandle.isPresent())
            {
                logFileHandle.get().close();
            }

            logFileHandle = Optional.of(new BufferedWriter(new FileWriter(new File(newLogName), true)));
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void enableInfoLogging()
    {
        logLevel.set(2, true);
    }

    public static void enableWarningLogging()
    {
        logLevel.set(1, true);
    }

    public static void enableErrorLogging()
    {
        logLevel.set(0, true);
    }

    public static void disableInfoLogging()
    {
        logLevel.set(2, false);
    }

    public static void disableWarningLogging()
    {
        logLevel.set(1, false);
    }

    public static void disableErrorLogging()
    {
        logLevel.set(0, false);
    }

    private static void initializeLogLevelMonitor()
    {
        enableErrorLogging();
    }

    public static void initializeLogger()
    {
        setLogFileName(DEFAULT_LOG_FILE_NAME);
    }
    public static void initializeLogger(String logFileName)
    {
        setLogFileName(logFileName);
        initializeLogLevelMonitor();
    }

    private static boolean infoLoggingEnabled()
    {
        return logLevel.get(2);
    }

    private static boolean warningLoggingEnabled()
    {
        return logLevel.get(1);
    }

    private static boolean errorLoggingEnabled()
    {
        return logLevel.get(0);
    }

    public static void logInfoIfEnabled(String message)
    {
        if (infoLoggingEnabled())
        {
            log(INFO_LEVEL_TEXT, message, LocalDateTime.now().toString());
        }
    }

    public static void logWarningIfEnabled(String message)
    {
        if (warningLoggingEnabled())
        {
            log(WARNING_LEVEL_TEXT, message, LocalDateTime.now().toString());
        }
    }

    public static void logErrorIfEnabled(String message)
    {
        if (errorLoggingEnabled())
        {
            log(ERROR_LEVEL_TEXT, message, LocalDateTime.now().toString());
        }
    }

    private static void log(String level, String message, String timestamp)
    {
        try
        {
            if (logFileHandle.isPresent())
            {
                logFileHandle.get().append("[" + level + "] " + timestamp + ": " + message);
                logFileHandle.get().newLine();
                logFileHandle.get().flush();
            }
            else
            {
                System.err.println("Logger has not been initialized.");
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
