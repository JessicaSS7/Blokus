import javax.print.DocPrintJob;
import javax.swing.*;
import javafx.util.Pair;
import java.awt.Color;
import java.awt.event.*;
import java.util.*;

public class Game {
    private JFrame frame;
    private JPanel centerpanel;
    private List<ArrayList<JButton>> buttons;
    private List<ArrayList<JButton>> ShapeBoard;
    private List<ArrayList<JButton>> PendingBoard;
    private JButton flip, rotate, put;
    public static int marginx = 500;
    public static int marginy = 150;
    public static int block_size = 20;
    public static int dim = 20;

    Game() {
        buttons = new ArrayList<>();
        ShapeBoard = new ArrayList<>();
        PendingBoard = new ArrayList<>();
        frame = new JFrame("Blokus");
        centerpanel = new JPanel();
        centerpanel.setLayout(null);
        InitialBoard();
        InitialShapeBoard();
        InitialPendingBoard();
        InitialOperationButtons();
        PaintShapeBoard();
        PaintNeighbor();
        frame.getContentPane().add(centerpanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(2000, 2000);
        frame.setVisible(true);
    }

    private void CycleButtons(ArrayList<JButton> buttons) {
        if (buttons.isEmpty()) {
            System.err.println("Empty buttons for cycling");
            return;
        }
        Color tmp = buttons.get(buttons.size() - 1).getBackground();
        for (int i = buttons.size() - 1; i > 0; --i) {
            buttons.get(i).setBackground(buttons.get(i - 1).getBackground());
        }
        buttons.get(0).setBackground(tmp);
    }

    private void InitialOperationButtons() {
        flip = new JButton("Flip");
        flip.setBounds(10, 450, 50, 50);
        flip.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 5; ++i) {
                    for (int j = 0; j < 4 - j; ++j) {
                        JButton button1 = PendingBoard.get(i).get(j);
                        JButton button2 = PendingBoard.get(i).get(4 - j);
                        Color tmp_background = button1.getBackground();
                        button1.setBackground(button2.getBackground());
                        button2.setBackground(tmp_background);
                    }
                }
            }
        });
        rotate = new JButton("Rotate Clockwisely");
        rotate.setBounds(70, 450, 50, 50);
        rotate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < 3; ++i) {
                    for (int j = 0; j < 2; ++j) {
                        ArrayList<JButton> button_chain = new ArrayList<JButton>();
                        button_chain.add(PendingBoard.get(i).get(j));
                        button_chain.add(PendingBoard.get(j).get(4 - i));
                        button_chain.add(PendingBoard.get(4 - i).get(4 - j));
                        button_chain.add(PendingBoard.get(4 - j).get(i));
                        CycleButtons(button_chain);
                    }
                }
            }
        });
        put = new JButton("Put");
        put.setBounds(130, 450, 50, 50);
        centerpanel.add(flip);
        centerpanel.add(rotate);
        centerpanel.add(put);
    }

    private void InitialBoard() {
        for (int i = 0; i < dim; ++i) {
            ArrayList<JButton> row = new ArrayList<JButton>();
            for (int j = 0; j < dim; ++j) {
                JButton button = new JButton();
                button.setBounds(marginx + j * block_size, marginy + i * block_size, block_size, block_size);
                button.setOpaque(true);
                button.setBorderPainted(true);
                row.add(button);
                centerpanel.add(button);
            }
            buttons.add(row);
        }
    }

    private void InitialShapeBoard(){
        for (int i = 0; i < 17; ++i) {
            ArrayList<JButton> row = new ArrayList<JButton>();
            for (int j = 0; j < 17; ++j) {
                JButton button = new JButton();
                button.setBounds(100 + j * block_size, 100 + i * block_size, block_size, block_size);
                button.setOpaque(true);
                button.setBorderPainted(true);
                row.add(button);
                centerpanel.add(button);
            }
            ShapeBoard.add(row);
        } 
    }

    private void InitialPendingBoard(){
        for (int i = 0; i < 5; ++i) {
            ArrayList<JButton> row = new ArrayList<JButton>();
            for (int j = 0; j < 5; ++j) {
                JButton button = new JButton();
                button.setBounds(100 + j * block_size, 500 + i * block_size, block_size, block_size);
                button.setOpaque(true);
                button.setBorderPainted(false);
                row.add(button);
                centerpanel.add(button);
            }
            PendingBoard.add(row);
        } 
    }

    private void PaintNeighbor() {
        for (int i = 0; i < dim; ++i) {
            for (int j = 0; j < dim; ++j) {
                final Integer ii = new Integer(i);
                final Integer jj = new Integer(j);
                List<Pair<Integer, Integer>> neighbor = new ArrayList<>();
                neighbor.add(new Pair<Integer, Integer>(0, 0));
                neighbor.add(new Pair<Integer, Integer>(-1, 0));
                neighbor.add(new Pair<Integer, Integer>(0, 1));
                buttons.get(i).get(j).addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        for (Pair<Integer, Integer> n : neighbor) {
                            int newi = ii + n.getKey();
                            int newj = jj + n.getValue();
                            if (0 <= newi && newi < dim && 0 <= newj && newj < dim) {
                                JButton b = buttons.get(newi).get(newj);
                                b.setBackground(Color.blue);
                                b.setBorderPainted(false);
                            }
                        }
                    }
                });
            }
        }
    }

    private void PaintShapeBoard() {
        Shape shape1 = new Shape(2,0,new ArrayList<Pair<Integer,Integer>>() {
            {
                add(new Pair<Integer, Integer>(-2, 0));
                add(new Pair<Integer, Integer>(-1, 0));
                add(new Pair<Integer, Integer>(0, 0));
                add(new Pair<Integer, Integer>(1, 0));
                add(new Pair<Integer, Integer>(2, 0));
            }
        });
        Shape shape2 = new Shape(8,0,new ArrayList<Pair<Integer,Integer>>() {
            {
                add(new Pair<Integer, Integer>(-2, 0));
                add(new Pair<Integer, Integer>(-1, 0));
                add(new Pair<Integer, Integer>(0, 0));
                add(new Pair<Integer, Integer>(1, 0));
                add(new Pair<Integer, Integer>(1, 1));
            }
        });
        ArrayList<Shape> shapes = new ArrayList<Shape>();
        shapes.add(shape1);
        shapes.add(shape2);
        for (Shape s : shapes){
            int cur_i = s.i;
            int cur_j = s.j;
            for (Pair<Integer,Integer> p : s.neighbor){
                int x = cur_i + p.getKey();
                int y = cur_j + p.getValue();
                JButton b = ShapeBoard.get(y).get(x);
                b.setBackground(Color.blue);
                b.setBorderPainted(false);
                b.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        for (int i = 0; i < 5; ++i) {
                            for (int j = 0; j < 5; ++j) {
                                PendingBoard.get(i).get(j).setBackground(Color.white);
                            }
                        }
                        for (Pair<Integer,Integer> p : s.neighbor){
                            int x = 2 + p.getKey();
                            int y = 2 + p.getValue();
                            JButton b = PendingBoard.get(y).get(x);
                            b.setBackground(Color.blue);
                            b.setBorderPainted(false);
                        }
                    }
                });
            }
        }
    }
}
