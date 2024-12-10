package main.java;

import java.util.Random;
import java.util.concurrent.Semaphore;


public class GameBoard {

    Random random = new Random();
    public static final int EMPTY = 0;
    public static final int PLAYER1 = 1;
    public static final int CAR = 2;
    public static final int PLAYER2 = 3;

    private int[][] matrix;
    private Semaphore semaphore = new Semaphore(1);

    private int player1Row, player1Col;
    private int player2Row, player2Col;
    private int rows, cols;

    public enum Direction { NONE, UP, DOWN }

    private Direction player1Direction = Direction.NONE;
    private Direction player2Direction = Direction.NONE;

    private Car[] cars;

    private int scorePlayer1 = 0;
    private int scorePlayer2 = 0;

    public GameBoard(int rows, int cols, java.awt.Image[] carImages) {
        this.rows = rows;
        this.cols = cols;
        matrix = new int[rows][cols];

        // Inicializa a matriz como EMPTY
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = EMPTY;
            }
        }

        
        cars = new Car[10];
       
        for (int i = 0; i < 5; i++) {
            
            int row = i + 1;
            double startCol = 0;
           
            double baseSpeed = 1.2 + i * 1.3; 
            double randomOffset = random.nextDouble() * 0.5; 
            double speed = baseSpeed + randomOffset; 
            int direction = +1;
            double angle = Math.PI; 
            cars[i] = new Car(row, startCol, speed, direction, carImages[i], angle);
            matrix[row][(int)startCol] = CAR;
        }

    
        for (int i = 5; i < 10; i++) {
            int row = i + 1;
            double startCol = cols - 1;
            double baseSpeed = 6.0 - (i - 5) * 1.0; 
            double randomOffset = random.nextDouble() * 0.5; 
            double speed = baseSpeed - randomOffset;
            int direction = -1;
            double angle = 0.0; 
            cars[i] = new Car(row, startCol, speed, direction, carImages[i], angle);
            matrix[row][(int)startCol] = CAR;
        }
    }

    public void setPlayer1Position(int r, int c) {
        try {
            semaphore.acquire();
            if (isInside(player1Row, player1Col))
                matrix[player1Row][player1Col] = EMPTY;
            player1Row = r;
            player1Col = c;
            matrix[player1Row][player1Col] = PLAYER1;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    public void setPlayer2Position(int r, int c) {
        try {
            semaphore.acquire();
            if (isInside(player2Row, player2Col))
                matrix[player2Row][player2Col] = EMPTY;
            player2Row = r;
            player2Col = c;
            matrix[player2Row][player2Col] = PLAYER2;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    public int[][] getSnapshot() {
        int[][] copy;
        try {
            semaphore.acquire();
            copy = new int[rows][cols];
            for (int i = 0; i < rows; i++) {
                System.arraycopy(matrix[i], 0, copy[i], 0, cols);
            }
        } catch (InterruptedException e) {
            copy = new int[0][0];
        } finally {
            semaphore.release();
        }
        return copy;
    }

    public void setPlayer1Direction(Direction d) {
        try {
            semaphore.acquire();
            player1Direction = d;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    public void setPlayer2Direction(Direction d) {
        try {
            semaphore.acquire();
            player2Direction = d;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    public Direction getPlayer1Direction() {
        try {
            semaphore.acquire();
            return player1Direction;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return Direction.NONE;
        } finally {
            semaphore.release();
        }
    }

    public Direction getPlayer2Direction() {
        try {
            semaphore.acquire();
            return player2Direction;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return Direction.NONE;
        } finally {
            semaphore.release();
        }
    }

    public void clearPlayer1Direction() {
        try {
            semaphore.acquire();
            player1Direction = Direction.NONE;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    public void clearPlayer2Direction() {
        try {
            semaphore.acquire();
            player2Direction = Direction.NONE;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    public void movePlayer1(Direction dir) {
        try {
            semaphore.acquire();
            int newRow = player1Row;
            switch (dir) {
                case UP:
                    newRow = player1Row - 1;
                    break;
                case DOWN:
                    newRow = player1Row + 1;
                    break;
                case NONE:
                    break;
            }

            if (isInside(newRow, player1Col)) {
                if (matrix[newRow][player1Col] == EMPTY) {
                    matrix[player1Row][player1Col] = EMPTY;
                    player1Row = newRow;
                    matrix[player1Row][player1Col] = PLAYER1;
                } else if (matrix[newRow][player1Col] == CAR) {
                    player1Row = player1Row +1;
                }
            }

            if (player1Row == 0) {
                scorePlayer1++;
                resetPlayer1();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    public void movePlayer2(Direction dir) {
        try {
            semaphore.acquire();
            int newRow = player2Row;
            switch (dir) {
                case UP:
                    newRow = player2Row - 1;
                    break;
                case DOWN:
                    newRow = player2Row + 1;
                    break;
                case NONE:
                    break;
            }

            if (isInside(newRow, player2Col)) {
                if (matrix[newRow][player2Col] == EMPTY) {
                    matrix[player2Row][player2Col] = EMPTY;
                    player2Row = newRow;
                    matrix[player2Row][player2Col] = PLAYER2;
                } else if (matrix[newRow][player2Col] == CAR) {
                    player2Row = player2Row -1;
                }
            }

            if (player2Row == 0) {
                scorePlayer2++;
                resetPlayer2();
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    public void updateCarPosition(Car c, double deltaTime) {
        try {
            semaphore.acquire();

            int oldCol = (int) Math.floor(c.getCol());
            if (isInside(c.getRow(), oldCol) && matrix[c.getRow()][oldCol] == CAR) {
                matrix[c.getRow()][oldCol] = EMPTY;
            }

            c.move(deltaTime, cols);

            // Colisão com GAlinha1
            if (isCollision(c.getRow(), c.getCol(), player1Row, player1Col)) {
                
                player1Row = player1Row + 1; //Isso faz voltar apenas 1 linha, modo fácil
                if (isInside(player1Row, player1Col)) {
                    matrix[player1Row][player1Col] = PLAYER1;
                }
            }

            // Colisão com G2
            if (isCollision(c.getRow(), c.getCol(), player2Row, player2Col)) {
            
                player2Row = player2Row + 1;
                if (isInside(player2Row, player2Col)) {
                    matrix[player2Row][player2Col] = PLAYER2;
                }
            }

            int newCol = (int) Math.floor(c.getCol());
            if (isInside(c.getRow(), newCol)) {
                matrix[c.getRow()][newCol] = CAR;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
        }
    }

    private boolean isCollision(int carRow, double carCol, int playerRow, int playerCol) {
        return (carRow == playerRow) && ((int)Math.floor(carCol) == playerCol);
    }

    private void resetPlayer1() {
        if (isInside(player1Row, player1Col))
            matrix[player1Row][player1Col] = EMPTY;
        player1Row = rows - 1;
        player1Col = cols / 3;
        matrix[player1Row][player1Col] = PLAYER1;
    }

    private void resetPlayer2() {
        if (isInside(player2Row, player2Col))
            matrix[player2Row][player2Col] = EMPTY;
        player2Row = rows - 1;
        player2Col = 2 * (cols / 3);
        matrix[player2Row][player2Col] = PLAYER2;
    }

    public boolean isInside(int r, int c) {
        return (r >= 0 && r < rows && c >= 0 && c < cols);
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public int getScorePlayer1() {
        return scorePlayer1;
    }

    public int getScorePlayer2() {
        return scorePlayer2;
    }

    public Car[] getCars() {
        return cars;
    }

    public int getPlayer1Row() {
        try {
            semaphore.acquire();
            return player1Row;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return -1;
        } finally {
            semaphore.release();
        }
    }

    public int getPlayer1Col() {
        try {
            semaphore.acquire();
            return player1Col;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return -1;
        } finally {
            semaphore.release();
        }
    }

    public int getPlayer2Row() {
        try {
            semaphore.acquire();
            return player2Row;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return -1;
        } finally {
            semaphore.release();
        }
    }

    public int getPlayer2Col() {
        try {
            semaphore.acquire();
            return player2Col;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return -1;
        } finally {
            semaphore.release();
        }
    }
}
