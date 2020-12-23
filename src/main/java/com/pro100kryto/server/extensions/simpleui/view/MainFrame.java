package com.pro100kryto.server.extensions.simpleui.view;


import javax.swing.*;

public class MainFrame extends JFrame{
    public static final int WIDTH = 600;
    public static final int HEIGHT = 400;

    private JPanel mainPanel;
    private JTextField commandsField;
    private JTextArea textAreaMain;
    private JToolBar toolBar;
    private JButton btnSendCommand;
    private JScrollPane textScrollMain;
    private JButton btnClearLog;
    private JCheckBox cbLockScroll;
    private JButton btnStop;
    private JTabbedPane tabbedPane;

    public MainFrame() {
        setSize(WIDTH, HEIGHT);
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public JTextField getCommandsField() {
        return commandsField;
    }

    public JTextArea getTextAreaMain() {
        return textAreaMain;
    }

    public JTabbedPane getTabbedPane(){
        return tabbedPane;
    }

    public JToolBar getToolBar() {
        return toolBar;
    }

    public JButton getBtnSendCommand() {
        return btnSendCommand;
    }

    public JButton getBtnClearLog() {
        return btnClearLog;
    }

    public JCheckBox getCbLockScroll() {
        return cbLockScroll;
    }

    public JButton getBtnStop() {
        return btnStop;
    }

}