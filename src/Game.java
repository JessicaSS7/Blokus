import javax.swing.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Game {
    private JFrame frame;
    private JPanel centerpanel;
    // The buttons in the middle for the shared board.
    private List<ArrayList<JButton>> buttons;
    // The shape board for two players.
    private List<List<ArrayList<JButton>>> ShapeBoard;
    // The shared pending board.
    private List<ArrayList<JButton>> PendingBoard;
    private List<Pair> dirs;
    private List<Pair> diag_dirs;
    private int placed_block_count;
    // The shared functionality buttons.
    private JButton flip, rotate;
    private Shape ActiveShape;
    private int score;
    private List<JLabel> ScoreMessage;
    // Indicator of the active player.
    private int ActivePlayer;
    private Color ActiveColor;
    public static int marginx = 500;
    public static int marginy = 150;
    public static int block_size = 20;
    public static int button_size = 19;
    public static int dim = 15;
    public static int pending_board_size = 5;
    public static int shape_board_size = 17;
    // Colors representing a playable preview, a blank cell, and non-playable preview.
    public static List<Color> playable_colors = new ArrayList<Color>() {{add(new Color(15, 15, 250));add(new Color(30, 250, 30));}};
    public static Color blank_color = Color.white;
    public static Color nonplayable_color = Color.gray;
    public static List<Color> player_colors = new ArrayList<Color>() {{add(new Color(15, 15, 150)); add(new Color(15, 100, 15));}};

    Game() {
        placed_block_count = 0;
        buttons = new ArrayList<>();
        ShapeBoard = new ArrayList<>();
        dirs = new ArrayList<Pair>() {
            {
                add(new Pair(0, 0));
                add(new Pair(1, 0));
                add(new Pair(-1, 0));
                add(new Pair(0, 1));
                add(new Pair(0, -1));
            }
        };
        diag_dirs = new ArrayList<Pair>() {
            {
                add(new Pair(1, 1));
                add(new Pair(-1, -1));
                add(new Pair(-1, 1));
                add(new Pair(1, -1));
            }
        };
        score = 0;
        PendingBoard = new ArrayList<>();
        frame = new JFrame("Blokus");
        centerpanel = new JPanel();
        centerpanel.setLayout(null);
        InitializeScoreMessage();
        InitialBoard();
        InitialShapeBoard();
        InitialPendingBoard();
        InitialOperationButtons();
        PaintShapeBoard();
        PaintNeighbor();
        UpdateScoreBoard();
        frame.add(centerpanel);
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
        flip.setBounds(500, 450, 50, 50);
        flip.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < pending_board_size; ++i) {
                    for (int j = 0; j < pending_board_size - 1 - j; ++j) {
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
        rotate.setBounds(570, 450, 150, 50);
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
        centerpanel.add(flip);
        centerpanel.add(rotate);
    }

    private void InitializeScoreMessage() {
        ScoreMessage = new ArrayList<JLabel>();
        for (int player = 0; player < 2; ++player) {
            JLabel PlayerScoreMessage = new JLabel("Total penalty: 1000");
            PlayerScoreMessage.setFont(new Font("Verdana", Font.PLAIN, 18));
            PlayerScoreMessage.setSize(PlayerScoreMessage.getPreferredSize());
            PlayerScoreMessage.setLocation(100 + player * 820, 70);
            centerpanel.add(PlayerScoreMessage);
            centerpanel.validate();
            ScoreMessage.add(PlayerScoreMessage);
        }
    }

    private void InitialBoard() {
        for (int i = 0; i < dim; ++i) {
            ArrayList<JButton> row = new ArrayList<JButton>();
            for (int j = 0; j < dim; ++j) {
                JButton button = new JButton();
                button.setBounds(marginx + j * block_size, marginy + i * block_size, button_size, button_size);
                button.setBackground(blank_color);
                button.setOpaque(true);
                button.setBorderPainted(true);
                row.add(button);
                centerpanel.add(button);
            }
            buttons.add(row);
        }
    }

    private void InitialShapeBoard() {
        for (int player = 0; player < 2; ++player) {
            List<ArrayList<JButton>> PlayerShapeBoard = new ArrayList<ArrayList<JButton>>();
            for (int i = 0; i < shape_board_size; ++i) {
                ArrayList<JButton> row = new ArrayList<JButton>();
                for (int j = 0; j < shape_board_size; ++j) {
                    JButton button = new JButton();
                    button.setBounds(100 + j * block_size + player * block_size * 41, 100 + i * block_size, button_size,
                            button_size);
                    button.setOpaque(true);
                    button.setBorderPainted(false);
                    row.add(button);
                    centerpanel.add(button);
                }
                PlayerShapeBoard.add(row);
            }
            ShapeBoard.add(PlayerShapeBoard);
        }
    }

    private void ClearPendingBoard() {
        for (int i = 0; i < pending_board_size; ++i) {
            for (int j = 0; j < pending_board_size; ++j) {
                JButton button = PendingBoard.get(i).get(j);
                button.setBackground(blank_color);
                // button.setBorderPainted(true);
                button.setOpaque(true);
            }
        }
    }

    private void UpdateScoreBoard() {
        for (int player = 0; player < 2; ++player) {
            score = 0;
            for (int i = 0; i < shape_board_size; ++i) {
                for (int j = 0; j < shape_board_size; ++j) {
                    if (ShapeBoard.get(player).get(i).get(j)
                            .getBackground() == player_colors.get(player)) {
                        ++score;
                    }
                }
            }
            ScoreMessage.get(player).setText("Total penalty: " + score);
        }
    }

    private void InitialPendingBoard() {
        for (int i = 0; i < pending_board_size; ++i) {
            ArrayList<JButton> row = new ArrayList<JButton>();
            for (int j = 0; j < pending_board_size; ++j) {
                JButton button = new JButton();
                button.setBounds(550 + j * block_size, 500 + i * block_size, button_size, button_size);
                button.setBackground(blank_color);
                button.setOpaque(true);
                button.setBorderPainted(false);
                row.add(button);
                centerpanel.add(button);
            }
            PendingBoard.add(row);
        }
    }

    private boolean IsBlock(Color color) {
        return color == player_colors.get(0) || color == player_colors.get(1);
    }

    private boolean HasViolation(List<Pair> neighbor, final Integer ii, final Integer jj) {
        boolean has_violation = false;
        for (Pair n : neighbor) {
            int newi = ii + n.getKey();
            int newj = jj + n.getValue();
            if (0 <= newi && newi < dim && 0 <= newj && newj < dim) {
                // Checks if any of the four neighbor directions is already taken.
                for (Pair p : dirs) {
                    int neighbori = newi + p.getKey();
                    int neighborj = newj + p.getValue();
                    if (0 <= neighbori && neighbori < dim && 0 <= neighborj && neighborj < dim
                            && (buttons.get(neighbori).get(neighborj).getBackground() == ActiveColor)) {
                        has_violation = true;
                    }
                }
                // Cannot overlaps a block directly.
                if (IsBlock(buttons.get(newi).get(newj).getBackground())) {
                    has_violation = true;
                }
            } else {
                has_violation = true;
                break;
            }
        }
        if (!has_violation) {
            // Checks one corner touches some other corner.
            has_violation = true;
            for (Pair n : neighbor) {
                int newi = ii + n.getKey();
                int newj = jj + n.getValue();
                if (0 <= newi && newi < dim && 0 <= newj && newj < dim) {
                    // Checks if any of the four neighbor directions is already taken.
                    for (Pair p : diag_dirs) {
                        int neighbori = newi + p.getKey();
                        int neighborj = newj + p.getValue();
                        if (placed_block_count == 0) {
                            if (neighbori == -1 && neighborj == -1) {
                                has_violation = false;
                                break;
                            }
                        } else if (placed_block_count == 1) {
                            if (neighbori == dim && neighborj == dim) {
                                has_violation = false;
                                break;
                            }
                        } else if (0 <= neighbori && neighbori < dim && 0 <= neighborj
                                && neighborj < dim && buttons.get(neighbori).get(neighborj)
                                        .getBackground() == ActiveColor) {
                            has_violation = false;
                            break;
                        }
                    }
                }
            }
        }
        return has_violation;
    }

    private void PaintNeighbor() {
        for (int i = 0; i < dim; ++i) {
            for (int j = 0; j < dim; ++j) {
                final Integer ii = Integer.valueOf(i);
                final Integer jj = Integer.valueOf(j);
                // When mouse hovers on a button, show the preview.
                // When mouse exits, clear that preview.
                buttons.get(i).get(j).addMouseListener(new MouseAdapter() {
                    public void mouseExited(MouseEvent e) {
                        // On mouse exit, change all non-playable previews to blank.
                        for (int di = -2; di <= 2; ++di) {
                            for (int dj = -2; dj <= 2; ++dj) {
                                int newi = ii + di;
                                int newj = jj + dj;
                                if (0 <= newi && newi < dim && 0 <= newj && newj < dim) {
                                    JButton b = buttons.get(newi).get(newj);
                                    if (b.getBackground() == nonplayable_color || b.getBackground() == playable_colors.get(ActivePlayer)) {
                                        b.setBackground(blank_color);
                                        b.setOpaque(true);
                                        b.setBorderPainted(true);
                                    }
                                }
                            }
                        }
                    }

                    public void mouseEntered(MouseEvent e) {
                        List<Pair> neighbor = new ArrayList<>();
                        for (int i2 = 0; i2 < pending_board_size; ++i2) {
                            for (int j2 = 0; j2 < pending_board_size; ++j2) {
                                if (PendingBoard.get(i2).get(j2).getBackground() == ActiveColor) {
                                    neighbor.add(new Pair(i2 - 2, j2 - 2));
                                }
                            }
                        }
                        boolean has_violation = HasViolation(neighbor, ii, jj);
                        for (Pair n : neighbor) {
                            int newi = ii + n.getKey();
                            int newj = jj + n.getValue();
                            if (0 <= newi && newi < dim && 0 <= newj && newj < dim) {
                                JButton b = buttons.get(newi).get(newj);
                                if (!IsBlock(b.getBackground())) {
                                    b.setBackground(has_violation ? nonplayable_color : playable_colors.get(ActivePlayer));
                                    b.setBorderPainted(false);
                                }
                            }
                        }
                    }
                });
                // When mouse clicks a button, commit the change unless there is violation.
                buttons.get(i).get(j).addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        List<Pair> neighbor = new ArrayList<>();
                        for (int i2 = 0; i2 < pending_board_size; ++i2) {
                            for (int j2 = 0; j2 < pending_board_size; ++j2) {
                                if (IsBlock(PendingBoard.get(i2).get(j2).getBackground())) {
                                    neighbor.add(new Pair(i2 - 2, j2 - 2));
                                }
                            }
                        }
                        if (HasViolation(neighbor, ii, jj)) {
                            return;
                        }
                        for (Pair n : neighbor) {
                            int newi = ii + n.getKey();
                            int newj = jj + n.getValue();
                            if (0 <= newi && newi < dim && 0 <= newj && newj < dim) {
                                JButton b = buttons.get(newi).get(newj);
                                b.setBackground(ActiveColor);
                                b.setBorderPainted(false);
                            }
                        }
                        // Removes this shape from both the pending board and the shape board.
                        ClearPendingBoard();
                        for (Pair p : ActiveShape.neighbor) {
                            int x = ActiveShape.i + p.getKey();
                            int y = ActiveShape.j + p.getValue();
                            JButton b = ShapeBoard.get(ActivePlayer).get(y).get(x);
                            b.setBackground(blank_color);
                            b.setOpaque(false);
                            b.setBorderPainted(false);
                        }
                        // Updates the score board.
                        UpdateScoreBoard();
                        ++placed_block_count;
                    }
                });
            }
        }
    }

    private void PaintShapeBoard() {
        ArrayList<Shape> shapes = new ArrayList<Shape>();
        try (Scanner s = new Scanner(new File(System.getProperty("user.dir") + "/Blokus/config/shapes.txt"))) {
            int shape_count = s.nextInt();
            for (int i = 0; i < shape_count; ++i) {
                int start_x = s.nextInt();
                int start_y = s.nextInt();
                int neighbor_count = s.nextInt();
                ArrayList<Pair> neighbors = new ArrayList<Pair>();
                for (int j = 0; j < neighbor_count; ++j) {
                    int neighbor_x = s.nextInt();
                    int neighbor_y = s.nextInt();
                    neighbors.add(new Pair(neighbor_x, neighbor_y));
                }
                shapes.add(new Shape(start_x, start_y, neighbors));
            }
        } catch (IOException ex) {
            throw new RuntimeException("Cannot load config properties", ex);
        }
        for (Shape s : shapes) {
            int cur_i = s.i;
            int cur_j = s.j;
            for (Pair p : s.neighbor) {
                int x = cur_i + p.getKey();
                int y = cur_j + p.getValue();
                for (int player = 0; player < 2; ++player) {
                    final Integer pp = Integer.valueOf(player);
                    JButton b = ShapeBoard.get(player).get(y).get(x);
                    b.setBackground(player_colors.get(player));
                    b.setBorderPainted(false);
                    b.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            ActivePlayer = pp;
                            ActiveColor = player_colors.get(pp);
                            ActiveShape = s;
                            ClearPendingBoard();
                            for (Pair p : s.neighbor) {
                                int x = 2 + p.getKey();
                                int y = 2 + p.getValue();
                                JButton b = PendingBoard.get(y).get(x);
                                b.setBackground(ActiveColor);
                                b.setBorderPainted(false);
                            }
                        }
                    });
                }
            }
        }
    }
}
