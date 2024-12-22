import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList; // store all the pipes in the game
import java.util.Random; // used to place objects in random place
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener{
    int boardWidth = 360; // the width of the frame, unit is px
    int boardHeight = 640; // the height of the frame, unit is px

    // Images 
    Image backgorundImg;
    Image birdImg;
    Image pipeUpImg;
    Image pipeDownImg;

    // Bird

    int birdX = boardWidth/8; // position of bird in the screen in x axis
    int birdY = boardHeight/2; // position of bird in the screen in y axis
    int birdWidth = 32; // width of bird
    int birdHeight = 24; // heigth of bird

    class Bird {
        int width = birdWidth;
        int height = birdHeight;
        int x = birdX;
        int y = birdY;
        Image img;

        Bird(Image img){
            this.img = img;
        }
    }

    // Pipes

    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;

    class Pipe{
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        Pipe(Image img){
            this.img = img;
        }
    }
    

    // game logic

    Bird bird;
    int velocityX = -4; //moves the pipes to the left every 4 seconds by 4px
    int velocityY = -1;
    int gravity = 2;
    Timer gameLoop; // to run the game for the number of required times
    Timer placePipesTimer;

    boolean gameOver = false;
    double score = 0;
    ArrayList<Pipe> pipes; // to store multiple pipes in an array
    Random random = new Random();

    FlappyBird(){
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.blue);
        setFocusable(true); // it makes sure that any key when is entered, the JPanel takes it as an input or fires any event
        addKeyListener(this);
        // load image

        backgorundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        pipeUpImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        pipeDownImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        // bird image
        bird = new Bird(birdImg);


        pipes = new ArrayList<Pipe>();
        // place pipes timer

        placePipesTimer = new Timer(3500, new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            };
        });
        placePipesTimer.start();

        //game timer
        gameLoop = new Timer(1000/20, this); // in one second the paintComponent shall be called 6 times
        gameLoop.start();
    };

    public void placePipes() {
        //  (0-1)*pipeHeight/2 -> (0-256) // it's just the logic where the pipes will be placed
        // pipeHeight will be ranging from 1/4 to 3/4 of pipeHeight
        
        int randomPipe = (int) (pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openingSpace = boardHeight/5;
        Pipe topPipe = new Pipe(pipeUpImg);
        topPipe.y = randomPipe;
        pipes.add(topPipe);

        Pipe bottomPipe = new Pipe(pipeDownImg);
        bottomPipe.y = topPipe.y + pipeHeight + openingSpace ;
        pipes.add(bottomPipe);
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // super refers to the jPanel as we have inheritated it
        draw(g);
    }

    public void draw(Graphics g) {
        // background
        g.drawImage(backgorundImg, 0, 0, boardWidth, boardHeight, null); // 0,0 are the x and y axis coordinations

        // bird
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

        // Pipes
        for(int i=0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if(gameOver){
            g.drawString("Game Over: " + String.valueOf((int) score), 10, 35);
        } else{
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    // update position of objects in the game
    public void move() {
        // bird
        velocityY += gravity;
        bird.y += velocityY;
        bird.y = Math.max(bird.y, 0); // not letting the bird go up more than the top of the screen

        // pipes
        for(int i=0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if(!pipe.passed && bird.x > pipe.x + pipe.width){
                pipe.passed = true;
                score += 0.5; // as there is 2 number of pipes at a single vertical position, we are adding 0.5 twice
            }
            
            if(collision(bird, pipe)){
                gameOver = true;
            }
        }

        if(bird.y > boardHeight){
            gameOver = true;
        }
    }

    public boolean collision(Bird a, Pipe b) {
        return  a.x < b.x + b.width &&   //a's top left corner doesn't reach b's top right corner
                a.x + a.width > b.x &&   //a's top right corner passes b's top left corner
                a.y < b.y + b.height &&  //a's top left corner doesn't reach b's bottom left corner
                a.y + a.height > b.y;    //a's bottom left corner passes b's top left corner
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint(); // as we r using jpanel, this shorthand will call paintComponent()
        if(gameOver){
            placePipesTimer.stop();
            gameLoop.stop();
        }
    }
    
    @Override
    public void keyPressed(KeyEvent e) {
        // event fires when any key is clicked 
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            velocityY = -15;
            if(gameOver){
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                gameOver = false;
                score = 0;
                gameLoop.start();
                placePipesTimer.start();
            }
        }
    }
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}
