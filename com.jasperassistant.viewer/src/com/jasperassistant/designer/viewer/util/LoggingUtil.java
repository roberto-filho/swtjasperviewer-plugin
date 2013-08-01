package com.jasperassistant.designer.viewer.util;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;

public final class LoggingUtil {
    
    private static final String BUNDLE_ID = "com.jasperassistant.viewer";
    private static final Bundle BUNDLE = Platform.getBundle(BUNDLE_ID);

    public static void logError(String msg, Throwable exception) {
        Platform.getLog(BUNDLE).log(new Status(Status.ERROR, BUNDLE_ID, Status.OK, msg, exception));
    }
    
}
