package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainWindow extends JFrame{
    private DataManager dm;
    private JPanel mainPanel;
    private JCanvasPanel canvasPanel;
    private JPanel buttonPanel;
    private JLabel airHumidityLabel;
    private JSpinner airHumiditySpinner;
    private JLabel windLabel;
    private JSpinner windSpinner;
    private JLabel tresholdLabel;
    private JSpinner tresholdSpinner;
    private JLabel gap;
    private JButton generateForestButton;
    private JButton setFireButton;
    private JButton stopButton;
    private JLabel otherOptionsLabel;
    private JButton exportButton;
    private JButton reverseChangesButton;

    public MainWindow (String title) {
        super(title);
        dm = new DataManager();
        Utility util = new Utility(dm);

        try {
            BufferedImage bg = ImageIO.read(new File("image.bmp"));
            dm.img = bg;
        } catch (IOException e) {
            e.printStackTrace();
        }

        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        airHumidityLabel= new JLabel("Air Humidity [0 - 100%]:");
        SpinnerNumberModel humidityModel = new SpinnerNumberModel(60, 0, 100, 10);
        airHumiditySpinner = new JSpinner(humidityModel);

        windLabel= new JLabel("Beaufort wind force scale [0 - 12]:");
        SpinnerNumberModel windModel = new SpinnerNumberModel(3, 0, 12, 1);
        windSpinner = new JSpinner(windModel);

        tresholdLabel = new JLabel("Type binarization level [0-255]: ");
        SpinnerNumberModel binarizationModel = new SpinnerNumberModel(100, 0, 255, 1);
        tresholdSpinner = new JSpinner(binarizationModel);

        gap=new JLabel("");
        generateForestButton = new JButton("Generate forest");
        setFireButton = new JButton("Set fire");
        stopButton = new JButton("Stop");
        stopButton.setVisible(false);

        otherOptionsLabel = new JLabel("Other options: ");
        exportButton = new JButton("Export image");
        reverseChangesButton = new JButton("Reverse changes");

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(13, 1));
        buttonPanel.setBorder(new EmptyBorder(0,10, 0, 0));

        buttonPanel.add(airHumidityLabel);
        buttonPanel.add(airHumiditySpinner);

        buttonPanel.add(windLabel);
        buttonPanel.add(windSpinner);

        buttonPanel.add(tresholdLabel);
        buttonPanel.add(tresholdSpinner);

        buttonPanel.add(gap);

        buttonPanel.add(generateForestButton);
        buttonPanel.add(setFireButton);
        buttonPanel.add(stopButton);

        buttonPanel.add(otherOptionsLabel);
        buttonPanel.add(exportButton);
        buttonPanel.add(reverseChangesButton);


        canvasPanel = new JCanvasPanel(dm);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10,10,10));
        mainPanel.add(BorderLayout.CENTER, canvasPanel);
        mainPanel.add(BorderLayout.EAST, buttonPanel);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);

        this.setSize(new Dimension(750, 384));
        this.setLocationRelativeTo(null);


        generateForestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                util.setForest((Integer) tresholdSpinner.getValue(), (Integer) airHumiditySpinner.getValue(), (Integer) windSpinner.getValue());
                canvasPanel.repaint();}
        });

        setFireButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setFireButton.setVisible(false);
                stopButton.setVisible(true);
                int humidityVal=(Integer) airHumiditySpinner.getValue();
                int windVal=(Integer) windSpinner.getValue();

                final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                executorService.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        util.setFire();
                    }
                }, 0, 1, TimeUnit.MILLISECONDS);

                final ScheduledExecutorService executorService1 = Executors.newSingleThreadScheduledExecutor();
                executorService1.scheduleAtFixedRate(new Runnable() {
                    @Override
                    public void run() {
                        canvasPanel.repaint();
                        if(stopButton.getModel().isPressed()){
                            executorService.shutdown();
                            executorService1.shutdown();

                            setFireButton.setVisible(true);
                            stopButton.setVisible(false);

                            canvasPanel.repaint();
                        }
                        else if((Integer) airHumiditySpinner.getValue()!=humidityVal || (Integer) windSpinner.getValue()!=windVal)  {

                            executorService.shutdown();
                            executorService1.shutdown();
                        }
                    }
                }, 1000, 1, TimeUnit.MILLISECONDS);

            }
        });

        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dm.choice = 2;
                canvasPanel.repaint();
            }
        });

        reverseChangesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    BufferedImage bg = ImageIO.read(new File("image.bmp"));
                    dm.img = bg;
                } catch (IOException e2) {
                    e2.printStackTrace();
                }
                canvasPanel.repaint();
            }
        });

    }

    public static void main(String[] args){
        MainWindow mw = new MainWindow("App");
        mw.setVisible(true);
        mw.canvasPanel.repaint();
        Utility util = new Utility(mw.dm);
        util.constructQT();
    }
}
