package main.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;

public class GamePanel extends JPanel {
    private GameBoard board;

    private Image chickenImage;
    private Image chickenImage2;
    private Image roadImage;

    public GamePanel(GameBoard board) {
        this.board = board;
        setFocusable(true);
        requestFocusInWindow();

        
        chickenImage = new ImageIcon(getClass().getResource("galinha.png")).getImage();
        chickenImage2 = chickenImage;
        roadImage = new ImageIcon(getClass().getResource("road.png")).getImage();

        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // Galinha1: setas
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    board.setPlayer1Direction(GameBoard.Direction.UP);
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    board.setPlayer1Direction(GameBoard.Direction.DOWN);
                }

                // Galinha2: W/S
                if (e.getKeyCode() == KeyEvent.VK_W) {
                    board.setPlayer2Direction(GameBoard.Direction.UP);
                } else if (e.getKeyCode() == KeyEvent.VK_S) {
                    board.setPlayer2Direction(GameBoard.Direction.DOWN);
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Car[] cars = board.getCars();
        int rows = board.getRows();
        int cols = board.getCols();

        int cellWidth = getWidth() / cols;
        int cellHeight = getHeight() / rows;

        
        g.drawImage(roadImage, 0, 0, getWidth(), getHeight(), this);

        Graphics2D g2d = (Graphics2D) g.create();

        
        for (Car c : cars) {
            if (c.isActive()) {
                int carRow = c.getRow();
                double carCol = c.getCol();
                if (board.isInside(carRow, (int)Math.floor(carCol))) {
                    int x = (int)(carCol * cellWidth);
                    int y = carRow * cellHeight;

                    AffineTransform old = g2d.getTransform();                 
                    g2d.translate(x + cellWidth / 2.0, y + cellHeight / 2.0);                  
                    g2d.rotate(c.getAngle());
                    g2d.translate(-cellWidth / 2.0, -cellHeight / 2.0);
                    g2d.drawImage(c.getCarImage(), 0, 0, cellWidth, cellHeight, this);
                    g2d.setTransform(old);
                }
            }
        }

        
        int p1Row = board.getPlayer1Row();
        int p1Col = board.getPlayer1Col();
        if (board.isInside(p1Row, p1Col)) {
            int x = p1Col * cellWidth;
            int y = p1Row * cellHeight;
            g2d.drawImage(chickenImage, x, y, cellWidth, cellHeight, this);
        }

        int p2Row = board.getPlayer2Row();
        int p2Col = board.getPlayer2Col();
        if (board.isInside(p2Row, p2Col)) {
            int x = p2Col * cellWidth;
            int y = p2Row * cellHeight;
            g2d.drawImage(chickenImage2, x, y, cellWidth, cellHeight, this);
        }

        g2d.setColor(Color.RED);
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        g2d.drawString("GALINHA1: " + board.getScorePlayer1(), 200, 18);
        g2d.drawString("GALINHA2: " + board.getScorePlayer2(), 440, 18);

        g2d.dispose();
    }
}
