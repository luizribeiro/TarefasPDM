/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package touch;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.game.GameCanvas;

/**
 *
 * @author Caio
 */
public class AngryShooterCanvas extends GameCanvas implements Runnable{
    private boolean playing = false;
    private Thread game;
    private Graphics g;
    private int ballX = -1, ballY = -1;

    public AngryShooterCanvas(){
        super(false);
    }
    public void run() {
        g = getGraphics();
        this.setFullScreenMode(true);

        while(playing){
            drawScreen(g);
            getAction();
            try {
                Thread.sleep(25);
            } catch (InterruptedException ex) {
            }
        }
    }
    public void drawScreen(Graphics g){
        g.setColor(255, 255, 255);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        if(ballX == -1 && ballY == -1){

        } else{
            g.setColor(0, 0, 0);
            g.drawArc(ballX-10, ballY-10, 20, 20, 0, 360);
        }
        // Metodo que evita que a tela fique piscando, executar no final do metodo
        flushGraphics();
    }
    public void getAction(){
        this.getKeyStates();
    }
    public void start(){
        game = new Thread(this);
        game.start();
        playing = true;
    }
    protected void pointerPressed(int x, int y){
        ballX = x;
        ballY = y;
        this.repaint();
    }
    protected void pointerReleased(int x, int y){
        if(x != ballX && y != ballY){
            int j = diffX, k = diffY;
            for(int i = 0; j != 0 || k != 0; i++){
                if(j > 0) j += Math.max(0, j--);
                else ballX -= Math.max(0, Math.abs(j++));
                if(k > 0) k += Math.max(0, k--);
                else ballY -= Math.max(0, Math.abs(k++));
                try {
                    Thread.sleep(25);
                } catch (InterruptedException ex) {
                }
            }
            ballX %= this.getWidth();
            ballY %= this.getHeight();
        }
        this.repaint();
    }
    int diffX, diffY;
    protected void pointerDragged(int x, int y){
        //if(x > ballX-10 && x < ballX + 10){
            //if(y > ballY-10 && y < ballY + 10){
                diffX = ballX - x;
                diffY = ballY - y;
            //}
        //}
        System.out.println("Dx = " + diffX + " Dy = " + diffY);
        this.repaint();
    }
}
