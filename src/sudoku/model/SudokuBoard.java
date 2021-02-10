package sudoku.model;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public final class SudokuBoard extends JPanel {

	private final JTextField[][] board;

	private final int size = 9;
	private static final Font font = new Font("Comic Sans MS", Font.BOLD, 20);
	private final JPanel boardPanel = new JPanel();
	private final JPanel buttonPanel = new JPanel();
	private final JButton loadBoardButton;
	private final JButton demoButton;
	private final JButton playCzajkowskiButton;
	private final JPanel[][] fields;

	public SudokuBoard() {

		// sudoku board
		this.board = new JTextField[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				JTextField field = new JTextField();

				// center input numbers
				field.setHorizontalAlignment(JTextField.CENTER);

				// only allow numbers
				field.addKeyListener(new KeyAdapter() {

					public void keyTyped(KeyEvent e) {

						char ch = e.getKeyChar();
						if (!(ch >= '0' && ch <= '9')) {
							e.consume();
						}
					}

					// check if its done
					public void keyReleased(KeyEvent e) {
						boolean isEmptyField = false;

						for (JTextField[] row : board) {
							for (JTextField field : row) {
								if (field.getText().equals("")) {
									isEmptyField = true;
									break;
								}

							}
						}

						if (!isEmptyField) {
							String dialogMessage = isValid(board) ? "You won!" : "You lost!";
							JOptionPane.showMessageDialog(null, dialogMessage);
						}
					}

					private boolean isValid(JTextField[][] board) {
						// checking rows
						for (int i = 0; i < 9; i++) {
							int[] row = new int[9];
							for (int j = 0; j < 9; j++) {
								row[j] = (Integer.parseInt(board[i][j].getText().trim()));
							}
							if (containsDiplicates(row)) {
								return false;
							}
						}

						// checking columns
						for (int i = 0; i < 9; i++) {
							int[] column = new int[9];
							for (int j = 0; j < 9; j++) {
								column[j] = (Integer.parseInt(board[j][i].getText().trim()));
							}
							if (containsDiplicates(column)) {
								return false;
							}
						}

						// checking miniboards
						for (int row = 0; row < 9; row = row + 3) {
							for (int col = 0; col < 9; col = col + 3) {
								Set<Integer> set = new HashSet<Integer>();
								for (int r = row; r < row + 3; r++) {
									for (int c = col; c < col + 3; c++) {
										if ((Integer.parseInt(board[r][c].getText().trim())) < 0
												|| (Integer.parseInt(board[r][c].getText().trim())) > 9) {
											return false;
										} else if ((Integer.parseInt(board[r][c].getText().trim())) != 0) {
											if (set.add((Integer.parseInt(board[r][c].getText().trim()))) == false) {
												return false;
											}
										}
									}
								}
							}
						}

						return true;
					}

					private boolean containsDiplicates(int[] row) {
						boolean duplicates = false;
						for (int j = 0; j < row.length; j++)
							for (int k = j + 1; k < row.length; k++)
								if (k != j && row[k] == row[j])
									duplicates = true;
						return duplicates;
					}

				});

				board[i][j] = field;
			}
		}

		Border border = BorderFactory.createLineBorder(Color.PINK, 1);
		Dimension fieldDimension = new Dimension(40, 40);

		for (int j = 0; j < size; j++) {
			for (int k = 0; k < size; k++) {
				JTextField field = board[j][k];
				field.setBorder(border);
				field.setPreferredSize(fieldDimension);
				field.setFont(font);

			}
		}

		int fieldsDimension = (int) Math.sqrt(size);

		this.boardPanel.setLayout(new GridLayout(fieldsDimension, fieldsDimension));

		this.fields = new JPanel[fieldsDimension][fieldsDimension];

		Border fieldsBorder = BorderFactory.createLineBorder(Color.PINK, 1);

		for (int i = 0; i < fieldsDimension; i++) {
			for (int j = 0; j < fieldsDimension; j++) {
				JPanel panel = new JPanel();
				panel.setLayout(new GridLayout(fieldsDimension, fieldsDimension));
				panel.setBorder(fieldsBorder);
				fields[i][j] = panel;
				boardPanel.add(panel);
			}
		}

		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				int fieldX = i / fieldsDimension;
				int fieldY = j / fieldsDimension;

				fields[fieldX][fieldY].add(board[i][j]);
			}
		}
		this.boardPanel.setBorder(BorderFactory.createLineBorder(Color.PINK, 2));

		this.loadBoardButton = new JButton("LoadBoard");
		this.demoButton = new JButton("Demo");
		this.playCzajkowskiButton = new JButton("Play Czajkowski");

		this.buttonPanel.setLayout(new BorderLayout());
		this.buttonPanel.add(loadBoardButton, BorderLayout.WEST);
		this.buttonPanel.add(demoButton, BorderLayout.CENTER);
		this.buttonPanel.add(playCzajkowskiButton, BorderLayout.EAST);

		this.setLayout(new BorderLayout());
		this.add(boardPanel, BorderLayout.NORTH);
		this.add(buttonPanel, BorderLayout.SOUTH);

		loadBoardButton.addActionListener((ActionEvent e) -> {
			loadBoard();
		});

		demoButton.addActionListener((ActionEvent e) -> {
			loadDemoBoard();
		});

		playCzajkowskiButton.addActionListener((ActionEvent e) -> {
			playCzajkowski("./Czaj.wav");
		});
	}

	public void openFile(File file) {
		try {
			Scanner scanner = new Scanner(file);

			int fields = size * size;
			int y = 0;
			int x = 0;

			while (fields > 0) {
				if (scanner.hasNext()) {
					String text = scanner.next();

					try {
						int number = Integer.parseInt(text);

						if (number > 0 && number <= size) {
							board[y][x].setText(" " + number);
						}
					} catch (NumberFormatException e) {
						System.out.println(e.getMessage());
					}

					x++;

					if (x == size) {
						x = 0;
						y++;
					}
				} else {
					break;
				}
				fields--;
			}
			scanner.close();
		} catch (FileNotFoundException ex) {
			System.out.println("File not found");
		}
	}

	void loadBoard() {
		for (JTextField[] row : board) {
			for (JTextField field : row) {
				field.setText("");
			}
		}
		int randomNumber = (int) ((Math.random() * (3 - 1)) + 1);
		File myObj = new File("resources/SampleBoard" + randomNumber + ".txt");
		openFile(myObj);
	}

	void loadDemoBoard() {
		for (JTextField[] row : board) {
			for (JTextField field : row) {
				field.setText("");
			}
		}

		File demo = new File("resources/SampleBoardDemo.txt");
		openFile(demo);
	}

	private void playCzajkowski(String string) {
		new Thread(new Runnable() {

			public void run() {
				try {
					Clip clip = AudioSystem.getClip();
					AudioInputStream inputStream = AudioSystem
							.getAudioInputStream(SudokuBoard.class.getResourceAsStream(string));
					clip.open(inputStream);
					clip.start();
				} catch (Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}).start();

	}

}
