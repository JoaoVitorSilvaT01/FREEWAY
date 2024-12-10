package main.java;

public class CarThread extends Thread {
    private GameBoard board;
    private Car car;
    private long lastMoveTime;

    public CarThread(GameBoard board, Car car) {
        this.board = board;
        this.car = car;
        this.lastMoveTime = System.currentTimeMillis();
    }

    @Override
    public void run() {
        while (true) {
            long currentTime = System.currentTimeMillis();
            double deltaTime = (currentTime - lastMoveTime) / 1000.0;

            if (deltaTime >= 0.03) { 
                board.updateCarPosition(car, deltaTime);
                lastMoveTime = currentTime;
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
