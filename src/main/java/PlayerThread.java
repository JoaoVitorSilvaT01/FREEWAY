package main.java;

public class PlayerThread extends Thread {
    private GameBoard board;
    private int playerNumber; // 1 ou 2

    public PlayerThread(GameBoard board, int playerNumber) {
        this.board = board;
        this.playerNumber = playerNumber;
    }

    @Override
    public void run() {
        while (true) {
            GameBoard.Direction dir;
            if (playerNumber == 1) {
                dir = board.getPlayer1Direction();
                if (dir != GameBoard.Direction.NONE) {
                    board.movePlayer1(dir);
                    board.clearPlayer1Direction();
                }
            } else {
                dir = board.getPlayer2Direction();
                if (dir != GameBoard.Direction.NONE) {
                    board.movePlayer2(dir);
                    board.clearPlayer2Direction();
                }
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
