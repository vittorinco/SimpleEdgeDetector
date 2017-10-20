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
        display(image2, "Gradient Map");

        BufferedImage image3 = addSaltPepperNoise(image1, 0.2);
        display(image2, "Salt & Pepper Noise added");

        System.out.println("End of Edge Detector program");
        return;
    }

    private BufferedImage getGradientMap(BufferedImage image) {
        // Sobel kernels
        int[][] hx = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
        int[][] hy = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};

        BufferedImage gradientMap = new BufferedImage(image.getWidth()-2, image.getHeight()-2, BufferedImage.TYPE_BYTE_GRAY);

        for (int i = 1; i < image.getWidth()-2; i++) {
            for (int j = 1; j < image.getHeight()-2; j++) {
                // Single pixel operation
                int sum = 0;
                for (int k = 0; k < 2; k++) {
                    for (int l = 0; k < 2; l++) {
                        System.out.println("OK1: " + ", k: " + k + " l: " + l + " hx[k][l]: " + hx[k][l]);
                        sum = sum + hx[k][l];
                        //* image.getData().getSample(i-k+1, j-l+2, 0);
                    }
                }
                if (sum > 150)
                    gradientMap.setRGB(i, j, Color.WHITE.getRGB());
                else
                    gradientMap.setRGB(i, j, Color.BLACK.getRGB());
            }
        }
        return gradientMap;
    }

    private BufferedImage addSaltPepperNoise(BufferedImage image, double percentage) {
        int resolution = image.getWidth()*image.getHeight();
        int noisyPixels = (int)Math.floor(resolution*percentage);
        System.out.println("Resolution: " + resolution + ", noisy pixels: " + noisyPixels);
        System.out.println("SAMPLE : " + image.getData().getSample(0,0,0));

        Random rand = new Random();
        System.out.println("COLOR RED: "+ Color.RED.getRGB());
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

    public static void main(String[] argv) {
        try {
            new EdgeDetector().startEdgeDetector();
        } catch (Exception e) {
            System.out.println(e);
            System.exit(1);
        }
    }
}
