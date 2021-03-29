package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;

public class JCanvasPanel extends JPanel {
    DataManager dm;

    public JCanvasPanel(DataManager dm) {
        this.dm = dm;
    }

    public void export() {
        BufferedImage image = new  BufferedImage(dm.width, dm.height,BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();
        render(graphics, dm.rootNode);
        graphics.dispose();
        try {
            FileOutputStream out = new FileOutputStream("exported.png");
            ImageIO.write(image, "png", out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void render(Graphics2D g2, Node root) {
        g2.drawImage(dm.img, 0, 0, this);
        g2.setColor(Color.white);
        g2.drawRect(root.rect.origin.x, root.rect.origin.y, root.rect.width, root.rect.height);
    }


    @Override
        public void paint(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.drawImage(dm.img, 0, 0, this);
            if (dm.choice == 2)
                export();
        }

    @Override
    public void repaint() {
        super.repaint();
    }
}
