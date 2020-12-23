package com.pro100kryto.server.extensions;

import com.pro100kryto.server.IServerControl;
import com.pro100kryto.server.StartStopStatus;
import com.pro100kryto.server.extension.IExtension;
import com.pro100kryto.server.extensions.simpleui.controller.MainFrameController;

public class SimpleUIExtension implements IExtension {
    private final MainFrameController controller;
    private StartStopStatus status = StartStopStatus.STOPPED;

    public SimpleUIExtension(IServerControl serverControl) {
        controller = new MainFrameController(serverControl);
    }

    @Override
    public void start() throws Throwable {
        if (status!=StartStopStatus.STOPPED) throw new IllegalStateException("Is not stopped");
        status = StartStopStatus.STARTING;

        try{
            controller.showMainFrameWindow();
        } catch (Throwable throwable){
            status = StartStopStatus.STOPPED;
            throw throwable;
        }

        status = StartStopStatus.STARTED;
    }

    @Override
    public void stop(boolean force) throws Throwable {
        if (status==StartStopStatus.STOPPED) throw new IllegalStateException("Already stopped");
        status = StartStopStatus.STOPPING;

        controller.close(force);

        status = StartStopStatus.STOPPED;
    }

    @Override
    public StartStopStatus getStatus() {
        return status;
    }

    @Override
    public String getType() {
        return "SimpleUI";
    }

    @Override
    public void sendCommand(String command) throws Throwable {

    }
}
