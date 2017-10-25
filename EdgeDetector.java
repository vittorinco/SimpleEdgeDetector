/*
    Vittorio Longi
    CS 559 - Computer Vision
    Assignment 3, Problem A
    10/25/2017
 */

import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import java.util.Scanner;
import java.io.*;
import javax.swing.*;
import java.util.Random;

public class EdgeDetector {

    private void startEdgeDetector() {
        System.out.println("Starting Edge Detector program");
        System.out.print("Source image: ");
        BufferedImage image1 = getBWimage(getImage());
        display(image1, "Source image");

        BufferedImage image2 = getGradientMap(image1);
        display(image2, "Gradient Map (w/o noise)");
        export(image2, "GradientMap_NoNoise.png");

        // Repeat with added noise
        BufferedImage image3 = addSaltPepperNoise(image1, 0.2);
        display(image3, "Salt & Pepper Noise added");
        export(image3, "SaltPepperNoiseAdded.png");

        BufferedImage image4 = getGradientMap(image3);
        display(image4, "Gradient Map (w/ noise)");
        export(image4, "GradientMap_WithNoise.png");

        System.out.println("End of Edge Detector program");
        return;
    }

    private BufferedImage getGradientMap(BufferedImage image) {
        // Sobel kernels
        int[][] hx = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
        int[][] hy = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};

        BufferedImage gradientMap = new BufferedImage(image.getWidth()-2, image.getHeight()-2, BufferedImage.TYPE_BYTE_GRAY);

        System.out.println("Starting convolution (this may take several minutes)...");

        // iterate from 0 to 97 (in 100 image)
        for (int i = 1; i < image.getWidth()-2; i++) {
            for (int j = 1; j < image.getHeight()-2; j++) {
                // Single pixel operation
                int sumx = 0; // 255
                int sumy = 0;
                for (int k = 0; k < 2; k++) {
                    for (int l = 0; l < 2; l++) {
                        sumx = sumx + hx[k][l] * image.getData().getSample(i-k, j-l, 0);
                        sumy = sumy + hy[k][l] * image.getData().getSample(i-k, j-l, 0);
                    }
                }
                int magnitude = Math.abs(sumx) + Math.abs(sumy);
                if (magnitude > 750) // or use arbitrary value, like 150
                    gradientMap.setRGB(i, j, Color.WHITE.getRGB());
                else
                    gradientMap.setRGB(i, j, Color.BLACK.getRGB());
            }
        }
        return gradientMap;
    }

    private BufferedImage addSaltPepperNoise(BufferedImage image, double percentage) {
        System.out.println("Adding Salt and Pepper noise");
        int resolution = image.getWidth()*image.getHeight();
        int noisyPixels = (int)Math.floor(resolution*percentage);
        System.out.println("Resolution: " + resolution + ", noisy pixels: " + noisyPixels);

        Random rand = new Random();
        for (int i = 0; i < noisyPixels-1; i++) {
                int blackOrWhite = rand.nextInt(2);
                if (blackOrWhite == 0)
                    image.setRGB(rand.nextInt(image.getWidth()), rand.nextInt(image.getHeight()), Color.BLACK.getRGB());
                else
                    image.setRGB(rand.nextInt(image.getWidth()), rand.nextInt(image.getHeight()), Color.WHITE.getRGB());
        }
        return image;
    }

    private BufferedImage getImage() {
        Scanner scan = new Scanner(System.in);
        String fileName = scan.next();
        File imageFile = new File(fileName);
        BufferedImage image = null;
        try {
            image = ImageIO.read(imageFile);
            System.out.println("Successfully opened image (size: " + image.getWidth() + "x" + image.getHeight() + ")");
        } catch (Exception e){
            System.out.println("Error reading image");
            System.exit(1);
        }
        return image;
    }

    private BufferedImage getBWimage(BufferedImage image) {
        BufferedImage imageBW = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        Graphics g = imageBW.getGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return imageBW;
    }

    private void display(BufferedImage image, String title) {
        JFrame f = new JFrame(title);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new JLabel(new ImageIcon(image)), BorderLayout.WEST);
        f.pack();
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }

    private void export(BufferedImage image, String filename) {
        try {
            File outputfile = new File(filename);
            ImageIO.write(image, "png", outputfile);
        } catch (IOException e) {
            System.out.println("Error exporting " + filename);
        }
    }

    public static void main(String[] argv) {
        try {
            new EdgeDetector().startEdgeDetector();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(1);
        }
    }
}
