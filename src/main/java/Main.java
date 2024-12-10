package main.java;

import javax.swing.*;


public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Freeway Java - Jogo I");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        int rows = 12;
        int cols = 20;

        
        java.awt.Image[] carImages = new java.awt.Image[10];
        for (int i = 0; i < 10; i++) {           
            String carImageName = "carro" + i + ".png";
            carImages[i] = new ImageIcon(Main.class.getResource(carImageName)).getImage();
        }

        GameBoard board = new GameBoard(rows, cols, carImages);

       
        int startRow = rows - 1;
        int startCol1 = cols / 3;
        int startCol2 = 2 * (cols / 3);
        board.setPlayer1Position(startRow, startCol1);
        board.setPlayer2Position(startRow, startCol2);

        GamePanel panel = new GamePanel(board);
        frame.add(panel);
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Threads dos jogadores
        PlayerThread playerThread1 = new PlayerThread(board, 1);
        PlayerThread playerThread2 = new PlayerThread(board, 2);

        // Threads dos carros
        Car[] cars = board.getCars();
        CarThread[] carThreads = new CarThread[cars.length];
        for (int i = 0; i < cars.length; i++) {
            carThreads[i] = new CarThread(board, cars[i]);
        }

        // Thread de renderização
        RenderThread renderThread = new RenderThread(panel);

        // Inicia as threads
        playerThread1.start();
        playerThread2.start();
        for (CarThread ct : carThreads) {
            ct.start();
        }
        renderThread.start();
    }
}
