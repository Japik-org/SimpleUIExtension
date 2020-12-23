package com.pro100kryto.server.extensions.simpleui.model;

import com.pro100kryto.server.extensions.simpleui.controller.MainFrameController;
import com.pro100kryto.server.logger.ILoggerChangesListener;

public class LoggerChangesListener implements ILoggerChangesListener {
    private final MainFrameController controller;

    public LoggerChangesListener(MainFrameController controller) {
        this.controller = controller;
    }

    @Override
    public void onLoggerRegistered(String loggerName) {
        if (!controller.tabExists(loggerName))
            controller.createTab(loggerName);
        controller.appendText(loggerName, "--- logger registered ---");
    }

    @Override
    public void onLoggerUnregistered(String loggerName) {
        controller.appendText(loggerName, "--- logger unregistered ---");
    }
}
