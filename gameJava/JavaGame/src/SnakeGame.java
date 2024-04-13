import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    private class Tile {
        int x;
        int y;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    int boardWidth;
    int boardHeight;
    int tileSize = 25;
   //Snake
    Tile SnakeHead;
    ArrayList<Tile> SnakeBody;

    //Food
    Tile Food;
    Random random;

    //game logic
    Timer gameLoop;
    int velocityX ;
    int velocityY ;
    boolean gameOver = false;
    SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;

        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        SnakeHead = new Tile(5,5);
        SnakeBody = new ArrayList<Tile>();

        Food = new Tile(10,10);
        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 0;
         
        gameLoop = new Timer(100, this);
        gameLoop.start();
    }
    public void placeFood() {
        int maxX = boardWidth / tileSize;
        int maxY = boardHeight / tileSize;
        Food.x = random.nextInt(maxX);
        Food.y = random.nextInt(maxY);
     }
      public boolean collision(Tile tile1, Tile tile2){
         return tile1.x == tile2.x && tile1.y == tile2.y;
      } 

    public void paintComponent(Graphics g) {
          super.paintComponent(g);
          draw(g);
    }
    private void draw(Graphics g) {
        //Grid
          for (int i = 0; i < boardWidth/tileSize; i++) {
               g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
               g.drawLine(0, i*tileSize, boardWidth, i*tileSize);
          }
          //Food
          g.setColor(Color.red);
          g.fillRect(Food.x*tileSize, Food.y*tileSize, tileSize, tileSize);

        //Snake Head
          g.setColor(Color.green);
          g.fillRect(SnakeHead.x*tileSize, SnakeHead.y*tileSize, tileSize, tileSize);

          //Snake Body
          for (int i = 0; i < SnakeBody.size(); i++) {
            Tile snakePart = SnakeBody.get(i);
            g.fillRect(snakePart.x*tileSize, snakePart.y*tileSize, tileSize, tileSize);
          }
    }
    public void move(){
        //eat food
        if (collision(SnakeHead, Food)) {
            SnakeBody.add(new Tile(Food.x, Food.y));
            placeFood();
        }
         //Snake body
          for (int i = SnakeBody.size() - 1; i >= 0; i--) {
             Tile snakePart = SnakeBody.get(i);
             if(i == 0) {
                snakePart.x = SnakeHead.x;
                snakePart.y = SnakeHead.y;
          }
          else {
              Tile prevSnakePart = SnakeBody.get(i-1);
              snakePart.x = prevSnakePart.x;
              snakePart.y = prevSnakePart.y;
          }
        }
        //Snake Head
        SnakeHead.x += velocityX;
        SnakeHead.y += velocityY;

        // Game Over Conditions
        for (int i = 0; i < SnakeBody.size(); i++) {
            Tile  snakePart = SnakeBody.get(i);
            //collide with snake head
            if(collision(SnakeHead, snakePart)){
                gameOver = true;
            }
        }

        if(SnakeHead.x*tileSize < 0 || SnakeHead.x*tileSize > boardWidth || 
           SnakeHead.y*tileSize < 0 ||SnakeHead.y*tileSize > boardHeight){
            gameOver = true;
        }
    }
    @Override
    public void actionPerformed(ActionEvent e) {
         move();
        repaint();
        if(gameOver){
            gameLoop.stop();
        }
     }
     @Override
     public void keyPressed(KeyEvent e) {
         if(e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
         }
         else if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
               velocityX = 0;
               velocityY = 1;
         }
         else if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
         }
         else if(e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
         }
     }

     //Do Not need

    @Override
    public void keyTyped(KeyEvent e) {}
    
    @Override
    public void keyReleased(KeyEvent e) {}

}