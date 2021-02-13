package brickBracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePlay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;
    private int totalBricks = 21;
    private Timer timer;
    private int delay = 8;
    private int playerX = 310;
    private int ballposX = 120;
    private int ballposY = 350;
    private int ballXdir = -1;
    private int ballYdir = -2;

    private MapGenerator map;

    GamePlay() {
        map = new MapGenerator(3, 7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics graphics) {
        // background
        graphics.setColor(Color.black);
        graphics.fillRect(1, 1, 692, 592);

        // draw bricks
        map.draw((Graphics2D) graphics);

        // borders
        graphics.setColor(Color.yellow);
        graphics.fillRect(0, 0, 3, 592);
        graphics.fillRect(0, 0, 692, 3);
        graphics.fillRect(691, 0, 3, 592);

        // scores
        graphics.setColor(Color.white);
        graphics.setFont(new Font("serif", Font.BOLD, 25));
        graphics.drawString("" + score, 590, 30);

        // the paddle
        graphics.setColor(Color.green);
        graphics.fillRect(playerX, 550, 100, 8);

        // the ball
        graphics.setColor(Color.yellow);
        graphics.fillOval(ballposX, ballposY, 20, 20);

        if (totalBricks <= 0) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            graphics.setColor(Color.red);
            graphics.setFont(new Font("serif", Font.BOLD, 30));
            graphics.drawString("YOU WON, BRO! Scores: " + score, 150, 300);

            graphics.setFont(new Font("serif", Font.BOLD, 20));
            graphics.drawString("Press enter to restart", 230, 350);
        }

        if (ballposY > 570) {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            graphics.setColor(Color.red);
            graphics.setFont(new Font("serif", Font.BOLD, 30));
            graphics.drawString("GAME OVER, BRO. Scores: " + score, 130, 300);

            graphics.setFont(new Font("serif", Font.BOLD, 20));
            graphics.drawString("Press enter to restart", 230, 350);
        }

        graphics.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        timer.start();
        if (play) {
            if(new Rectangle(ballposX, ballposY, 20, 20)
                .intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYdir = -ballYdir;
            }

            A: for (int i=0; i<map.map.length; i++) {
                for (int j=0; j<map.map[0].length; j++) {
                    if(map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int brickWidth = map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(
                                brickX,
                                brickY,
                                brickWidth,
                                brickHeight
                        );

                        Rectangle ballRect = new Rectangle(
                                ballposX,
                                ballposY,
                                20,
                                20
                        );
                        Rectangle brickRect = rect;

                        if (ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 5;

                            if (ballposX + 19 <= brickRect.x
                                    || ballposX +1 >= brickRect.x + brickRect.width) {
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }
                            break A;
                        }
                    }
                }
            }

            ballposX += ballXdir;
            ballposY += ballYdir;
            if (ballposX < 0) {
                ballXdir = -ballXdir;
            }
            if (ballposY < 0) {
                ballYdir = -ballYdir;
            }
            if (ballposX > 670) {
                ballXdir = -ballXdir;
            }
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX < 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }
        if (keyEvent.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                play = true;
                ballposX = 120;
                ballposY = 320;
                ballXdir = -1;
                ballYdir = -2;
                playerX = 310;
                totalBricks = 21;
                score = 0;
                map = new MapGenerator(3, 7);
                repaint();
            }
        }
    }

    private void moveLeft() {
        play = true;
        playerX -= 20;
    }

    private void moveRight() {
        play = true;
        playerX += 20;
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {}

    @Override
    public void keyReleased(KeyEvent keyEvent) {}
}
