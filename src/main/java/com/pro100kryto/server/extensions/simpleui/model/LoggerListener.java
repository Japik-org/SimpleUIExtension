package com.pro100kryto.server.extensions.simpleui.model;

import com.pro100kryto.server.extensions.simpleui.controller.MainFrameController;
import com.pro100kryto.server.logger.ILoggerListener;
import com.pro100kryto.server.logger.MsgType;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LoggerListener implements ILoggerListener {
    private final DateTimeFormatter dtf;
    private final MainFrameController controller;

    public LoggerListener(MainFrameController controller) {
        this.controller = controller;
        dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss:n");
    }

    @Override
    public void write(String loggerName, LocalDateTime dateTime, MsgType msgType, String msg) {
        controller.appendText(loggerName, ""
                + "[" + dtf.format(dateTime).substring(0, 23) + "] "
                + (msgType!=MsgType.RAW ? "["+msgType.toString()+"] " : "")
                + msg);
    }
}
