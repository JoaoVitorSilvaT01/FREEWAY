package main.java;

public class Car {
    private int row;
    private double col; 
    private double speed; 
    private int direction; // +1: esquerda->direita, -1: direita->esquerda
    private boolean active;
    private java.awt.Image carImage;
    private double angle; 

    public Car(int row, double startCol, double speed, int direction, java.awt.Image carImage, double angle) {
        this.row = row;
        this.col = startCol;
        this.speed = speed;
        this.direction = direction;
        this.active = true;
        this.carImage = carImage;
        this.angle = angle;
    }

    public int getRow() {
        return row;
    }

    public double getCol() {
        return col;
    }

    public double getSpeed() {
        return speed;
    }

    public int getDirection() {
        return direction;
    }

    public boolean isActive() {
        return active;
    }

    public java.awt.Image getCarImage() {
        return carImage;
    }

    public double getAngle() {
        return angle;
    }

    public void move(double deltaTime, int cols) {
        col += direction * speed * deltaTime;

    
        if (direction > 0 && col >= cols) {
            col = 0;
        } else if (direction < 0 && col < 0) {
            col = cols - 1;
        }
    }
}
