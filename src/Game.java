import javax.swing.*;
import java.awt.Color;
import java.awt.event.*;
import java.util.*;

public class Game implements ActionListener {
    private JFrame frame;
    private JPanel centerpanel;
    private List<List<JButton>> buttons;
    public static int margin = 50;
    public static int block_size = 20;
    public static int dim = 10;

    Game() {
        frame = new JFrame("Blokus");
        centerpanel = new JPanel();
        centerpanel.setLayout(null);
        for (int i = 0; i < dim; ++i) {
            List<JButton> row = new ArrayList<JButton>();
            for (int j = 0; j < dim; ++j) {
                JButton button = new JButton();
                button.setBounds(margin + j * block_size, margin + i * block_size, block_size, block_size);
                button.setOpaque(true);
                button.setBorderPainted(true);
                button.addActionListener(this);
                row.add(button);
                centerpanel.add(button);
            }
        }
        frame.getContentPane().add(centerpanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        frame.setVisible(true);
    }
    public void actionPerformed(ActionEvent event) {
    }
}