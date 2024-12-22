import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth = 360; // the width of the frame, unit is px
        int boardHeight = 640; // the height of the frame, unit is px

        JFrame frame = new JFrame("Flappy Bird");
        // frame.setVisible(true); // make the screen visible
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null); //place the fame at the center of the screen
        frame.setResizable(false); // restrict the size of frame to be resized
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // when the x mark is clicked by the user, it will the the operation        

        FlappyBird flappyBird = new FlappyBird();
        frame.add(flappyBird); // flappyBird added to the frame
        frame.pack(); // if it's not added, then the top bar with the name of the Jframe will be included in the dimension of boardHeight and boardWidth
        flappyBird.requestFocus();
        frame.setVisible(true);
    }
}
