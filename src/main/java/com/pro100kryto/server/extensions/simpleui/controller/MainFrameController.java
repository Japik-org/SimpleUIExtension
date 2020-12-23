package com.pro100kryto.server.extensions.simpleui.controller;


import com.pro100kryto.server.IServer;
import com.pro100kryto.server.extension.IExtension;
import com.pro100kryto.server.extensions.simpleui.model.LoggerChangesListener;
import com.pro100kryto.server.extensions.simpleui.model.LoggerListener;
import com.pro100kryto.server.extensions.simpleui.view.MainFrame;
import com.pro100kryto.server.logger.ILogger;
import com.pro100kryto.server.logger.LoggerManager;

import javax.swing.*;
import java.awt.event.*;

public class MainFrameController{
    private final IServer server;
    private final LoggerManager loggerManager;
    private final ILogger logger;
    private IExtension cmd;
    private MainFrame mainFrame;
    private final LoggerListener loggerListener;
    private final LoggerChangesListener loggerChangesListener;

    public MainFrameController(IServer server) {
        initComponents();
        // load
        this.server = server;
        loggerManager = server.getLoggerManager();
        loggerListener = new LoggerListener(this);
        loggerManager.setMainLoggerListener(loggerListener);
        loggerChangesListener = new LoggerChangesListener(this);
        loggerManager.addLoggerChangesListener(loggerChangesListener);
        //createTab("ext:SimpleUI");
        logger = loggerManager.createLogger("ext:SimpleUI");
    }

    public void showMainFrameWindow(){
        mainFrame.setVisible(true);
        mainFrame.getCommandsField().requestFocus();
    }

    private void initComponents() {
        mainFrame = new MainFrame();

        mainFrame.getBtnSendCommand().addActionListener(new SendCommandListener());
        mainFrame.getCommandsField().addKeyListener(new CommandsFieldKeyAdapter());
        mainFrame.getBtnClearLog().addActionListener(new ClearLogListener());
        mainFrame.getBtnStop().addActionListener(new StopServerListener());
    }

    public synchronized boolean tabExists(String name){
        return mainFrame.getTabbedPane().indexOfTab(name)!=-1;
    }

    public synchronized void createTab(String name){
        JTextArea textArea = new JTextArea();
        textArea.setAutoscrolls(true);
        textArea.setSize(mainFrame.getTextAreaMain().getSize());
        textArea.setForeground(mainFrame.getTextAreaMain().getForeground());

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setAutoscrolls(true);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                if (!mainFrame.getCbLockScroll().isSelected()) return;
                e.getAdjustable().setValue(e.getAdjustable().getMaximum());
            }
        });

        mainFrame.getTabbedPane().addTab(name, scrollPane);
    }

    public synchronized void appendText(String tabName, String text) {
        try {
            int index = mainFrame.getTabbedPane().indexOfTab(tabName);
            JScrollPane scrollPane = (JScrollPane) mainFrame.getTabbedPane().getComponentAt(index);
            JTextArea textArea = (JTextArea) scrollPane.getViewport().getComponent(0);
            textArea.append(text+System.lineSeparator());
        } catch (Throwable throwable){
            appendText("main", text);
        }
    }


    public void close(boolean force) {
        if (force) {
            loggerManager.resetMainLoggerListener();
            loggerManager.removeLoggerChangesListener(loggerChangesListener);
            mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
        }
    }

    private IExtension getCmdSafe(){
        if (cmd==null){
            //logger.writeWarn("command line is not available. Fixing...");
            cmd = server.getExtension("CmdInterpreter");
            if (cmd==null){
                logger.writeWarn("CmdInterpreter not found!");
            }
        }
        return cmd;
    }

    private class SendCommandListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                getCmdSafe().sendCommand(mainFrame.getCommandsField().getText());
            } catch (Throwable throwable) {
                logger.writeException(throwable);
            }
            mainFrame.getCommandsField().setText("");
        }
    }

    private class CommandsFieldKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode()==KeyEvent.VK_ENTER){
                mainFrame.getBtnSendCommand().getActionListeners()[0].actionPerformed(null);
            }
        }
    }

    private class ClearLogListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            logger.writeInfo("Clear logs");
            for(int i=0; i<mainFrame.getTabbedPane().getTabCount(); i++){
                try {
                    String tabName = mainFrame.getTabbedPane().getTitleAt(i);
                    try {
                        JTextArea textArea = (JTextArea) ((JScrollPane) mainFrame.getTabbedPane().getTabComponentAt(
                                mainFrame.getTabbedPane().indexOfTab(tabName)
                        )).getComponent(0);
                        textArea.setText("");
                    } catch (Throwable throwable) {
                        mainFrame.getTextAreaMain().append("Failed clear logs for '" + tabName + "'. ["
                                + throwable.getClass().getCanonicalName() + "]" + throwable.getMessage());
                    }
                } catch (IndexOutOfBoundsException indexOutOfBoundsException){
                    mainFrame.getTextAreaMain().append("Failed clear logs for tab #"+i+". "
                            +indexOutOfBoundsException.getClass().getCanonicalName()+". "
                            +indexOutOfBoundsException.getMessage());
                }
            }
        }
    }

    private class StopServerListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                getCmdSafe().sendCommand("server stop");
            } catch (Throwable throwable) {
                logger.writeException(throwable);
            }
        }
    }
}
