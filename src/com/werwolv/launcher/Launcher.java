package com.werwolv.launcher;

import com.werwolv.game.main.Main;

import javax.swing.*;
import java.awt.Dimension;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class Launcher extends JFrame {

    private JPanel rootPanel;
    private JTabbedPane tabbedPane1;
    private JButton start;
    private JCheckBox VSyncCheckBox;
    private JCheckBox anisotropicFilterCheckBox;
    private JCheckBox antialiasingCheckBox;
    private JCheckBox shadowsCheckBox;
    private JCheckBox bloomCheckBox;
    private JTextArea console;
    private JCheckBox fullscreenCheckBox;
    private JTextField textField1;
    private JPasswordField passwordField1;
    private JEditorPane editorPane1;

    private TextAreaOutputStream consoleStream = new TextAreaOutputStream(console);

    public Launcher() {
        super("Launcher");

        setContentPane(rootPanel);
        try {
            editorPane1.setPage("http://www.paulsaegesser.ch");

        } catch (IOException e) {
            e.printStackTrace();
        }

        pack();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 600));
        setMinimumSize(new Dimension(1000, 600));
        setMaximumSize(new Dimension(1000, 600));
        setVisible(true);

        System.setOut(new PrintStream(consoleStream));
        System.setErr(new PrintStream(consoleStream));

        start.addActionListener((e -> {
            List<String> args = new ArrayList<>();

            if(VSyncCheckBox.isSelected()) args.add("vsync");
            if(anisotropicFilterCheckBox.isSelected()) args.add("anisotropicfilter");
            if(antialiasingCheckBox.isSelected()) args.add("antialiasing");
            if(shadowsCheckBox.isSelected()) args.add("shadow");
            if(bloomCheckBox.isSelected()) args.add("bloom");
            if(fullscreenCheckBox.isSelected()) args.add("fullscreen");

            new Thread(() -> new Main().startGame(args)).start();
        }));
    }

    public static void main(String[] args) {
        Launcher launcher = new Launcher();
    }


}
