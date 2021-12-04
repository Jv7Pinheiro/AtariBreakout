import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;

public class GamePlay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;

    private int totalBricks = 35;

    private Timer timer;
    private int delay = 8;

    private int playerX = 310;

    private int ballPosX = 350;
    private int ballPosY = 350;
    private int ballXDir = -2;
    private int ballYDir = -4;
    private int paddleMultiplier = 0;

    private MapGenerator map;

    public GamePlay() {
        map = new MapGenerator(5, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g){
        //background
        g.setColor(Color.BLACK);
        g.fillRect(1, 1, 692, 592);

        // drawing map
        map.draw((Graphics2D)g);

        //borders
        g.setColor(Color.ORANGE);
        g.fillRect(0, 0, 3, 590);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(684, 0, 3, 592);

        //scores
        g.setColor(Color.WHITE);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("" + score, 590, 30);

        //the paddle
        g.setColor(Color.GREEN);
        g.fillRect(playerX, 550, 100, 8);

        //the ball
        g.setColor(Color.MAGENTA);
        g.fillOval(ballPosX, ballPosY, 20, 20);

        if(totalBricks <= 0){
            play = false;
//            ballXDir = 0;
//            ballYDir = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 25));
            g.drawString("You Won", 250, 300);
            g.setFont(new Font("serif", Font.BOLD, 15));
            g.drawString("Press Enter to Restart", 250, 350);
        }

        //ball goes off-screen
        if(ballPosY > 570){
            play = false;
//            ballXDir = 0;
//            ballYDir = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 25));
            g.drawString("Game Over", 250, 300);
            g.setFont(new Font("serif", Font.BOLD, 15));
            g.drawString("Press Enter to Restart", 250, 350);
        }

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if(play){
            if(new Rectangle(ballPosX, ballPosY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))){
                ballYDir = -ballYDir;
            }

            A: for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if(map.map[i][j] > 0){
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
                        Rectangle ballRect = new Rectangle(ballPosX, ballPosY, 20, 20);
                        Rectangle brickRect = rect;

                        if (ballRect.intersects(brickRect)){
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if(ballPosX + 19 <= brickRect.x || ballPosX + 1 >= brickRect.x + brickRect.width){
                                ballXDir = -ballXDir;
                            }
                            else{
                                ballYDir = -ballYDir;
                            }

                            break A;
                        }
                    }
                }
            }

            ballPosX += ballXDir;
            ballPosY += ballYDir;
            if(ballPosX < 0){
                ballXDir = -ballXDir;
            }
            if(ballPosY < 0){
                ballYDir = -ballYDir;
            }
            if(ballPosX > 670){
                ballXDir = -ballXDir;
            }
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT){
            if(playerX >= 600){
                playerX = 600;
            }
            else{
                moveRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT){
            if(playerX < 10){
                playerX = 10;
            }
            else {
                moveLeft();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER){
            if(!play) {
                play = true;
                ballPosX = 350;
                ballPosY = 350;
                if (totalBricks <= 0){
                    ballXDir = ballXDir - 1;
                    ballYDir = ballYDir - 1;
                    paddleMultiplier += 10;
                }
                else{
                    ballXDir = ballXDir;
                    ballYDir = ballYDir;
                }
                playerX = 310;
                score = 0;
                totalBricks = 35;
                map = new MapGenerator(5, 7);

                repaint();
            }
        }
    }

    public void moveRight(){
        play = true;
        playerX += 50 + paddleMultiplier;

    }

    public void moveLeft(){
        play = true;
        playerX -= 50 - paddleMultiplier;
    }
}