package main.java;

public class RenderThread extends Thread {
    private GamePanel panel;

    public RenderThread(GamePanel panel) {
        this.panel = panel;
    }

    @Override
    public void run() {
        while (true) {
            panel.repaint();
            try {
                Thread.sleep(30);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
