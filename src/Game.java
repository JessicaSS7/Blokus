import javax.swing.*;
import java.awt.event.*;

public class Game implements ActionListener {
    JFrame frame;
    JLabel label1;
    JPanel centerpanel;
    JButton button1;

    Game() {
        frame = new JFrame("HelloWorldSwing");
        label1 = new JLabel("Hello Everyone!");
        centerpanel = new JPanel();
        button1 = new JButton("Click Me");
        button1.addActionListener(this);
        centerpanel.add(label1);
        centerpanel.add(button1);
        frame.getContentPane().add(centerpanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    public void actionPerformed(ActionEvent event){
        label1.setText("Bye Everyone!");
        }
}