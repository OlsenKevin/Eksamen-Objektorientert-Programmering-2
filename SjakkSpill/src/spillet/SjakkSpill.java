package spillet;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

import stockfish.Stockfish;

public class SjakkSpill extends JFrame {
	private Stockfish stockfish;

	private Container contents;

	private SjakkSpill competitor;

	// Components
	private JButton[][] squares = new JButton[9][9];
	private String[] letter = { "A", "B", "C", "D", "E", "F", "G", "H" };

	// Colors
	private Color colorBlack = Color.BLACK;

	// Start Position
	private int row = 2;
	private int col = 4;

	// Images:
	private ImageIcon king = new ImageIcon(getClass().getResource("whiteKing.png"));

	//The code behind the method "Sjakkspill" is mostly borrowed and based on a video from Youtube.com, some modifications made by us. Source is found in documentation.
	public SjakkSpill(Stockfish stockfish) {
		super("- an attempt to make a chess game");
		this.stockfish = stockfish;

		contents = getContentPane();
		contents.setLayout(new GridLayout(8, 8));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Create event handlers:
		ButtonHandler buttonHandler = new ButtonHandler();

		// Create and add board components:
		for (int i = 8; i >= 1; i--) {
			for (int j = 8; j >= 1; j--) {
				squares[i][j] = new JButton();

				if ((i + j) % 2 != 0) {
					squares[i][j].setBackground(colorBlack);
				}
				contents.add(squares[i][j]);
				squares[i][j].addActionListener(buttonHandler);
			}
		}

		squares[row][col].setIcon(king);

		// Size and display windows:
		setSize(500, 500);
		setResizable(false);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	//This method is created by us.
	public void add_competitor(SjakkSpill brett) {
		this.competitor = brett;
	}
	
	//This method is created by us.
	private void draw_board(char[][] board) {
		for (int r = 0; r < 8; r++) {
			for (int k = 0; k < 8; k++) {
				
				// setting the old position to blank
				this.squares[(9 - (r + 1))][9 - (k + 1)].setIcon(null);
				char piece = board[r][k];

				// moving the piece to new location
				if (piece == 'P') {
					this.squares[(9 - (r + 1))][9 - (k + 1)].setIcon(king);
				}
			}
			System.out.println("");
		}
	}
	
	//This method is created by us.
	public boolean oppdater(String move) {
		
		// Splitting up the new fen code to update move in the other board
		String[] fish_board = move.substring(0, move.indexOf(" ")).split("/");
		char[][] new_board = new char[8][8];
		for (int row = 0; row < fish_board.length; row++) {
			int pos = 0;
			for (int col = 0; col < fish_board[row].length(); col++) {
				int k;
				try {
					k = Integer.parseInt(fish_board[row].substring(col, col + 1));
					for (int y = 0; y < k; y++) {
						new_board[row][pos + y] = 'x';
					}
					pos = pos + k;
				} catch (NumberFormatException cought) {
					char actuall_piece = (char) fish_board[row].charAt(col);
					new_board[row][pos] = actuall_piece;
					pos++;
				}
			}
		}
		// Drawing the new board after move
		draw_board(new_board);

		return true;
	}

	//Some of this code is borrowed and based on a video from Youtube.com, but most is created by us. Source is found in documentation.
	public void processClick(int i, int j) {
		String oldCol = null;
		String nyCol = null;

		String startFEN = "rnbqkbnr/4p3/8/8/8/8/4P3/RNBQKBNR w KQkq - 0 1";

		squares[row][col].setIcon(null);

		if (col == 1) {
			oldCol = "h";
		} else if (col == 2) {
			oldCol = "g";
		} else if (col == 3) {
			oldCol = "f";
		} else if (col == 4) {
			oldCol = "e";
		} else if (col == 5) {
			oldCol = "d";
		} else if (col == 6) {
			oldCol = "c";
		} else if (col == 7) {
			oldCol = "b";
		} else if (col == 8) {
			oldCol = "a";
		}

		if (j == 1) {
			nyCol = "h";
		} else if (j == 2) {
			nyCol = "g";
		} else if (j == 3) {
			nyCol = "f";
		} else if (j == 4) {
			nyCol = "e";
		} else if (j == 5) {
			nyCol = "d";
		} else if (j == 6) {
			nyCol = "c";
		} else if (j == 7) {
			nyCol = "b";
		} else if (j == 8) {
			nyCol = "a";
		}

		String toStockFish = (oldCol + row + nyCol + i);

		squares[i][j].setIcon(king);
		row = i;
		col = j;

		// Sending current fen code with new move
		stockfish.sendCommand("position fen " + startFEN + " moves " + toStockFish);
		stockfish.sendCommand("d");

		// Getting the output from stockfish after committing new move
		String out = stockfish.getOutput(0);

		// Splitting the new fen for further use
		String updatedFEN = out.split("Fen: ")[1].split("Key:")[0];
		
		// Updating the other board after a move
		this.competitor.oppdater(updatedFEN);

		System.out.println(out + "\n" + updatedFEN);

	}

	//Code borrowed and based on a video from Youtube.com Source is found in documentation.
	public class ButtonHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			Object source = e.getSource();

			for (int i = 8; i >= 1; i--) {
				for (int j = 8; j >= 1; j--) {
					if (source == squares[i][j]) {
						processClick(i, j);

						return;
					}

				}
			}

		}
	}
}
