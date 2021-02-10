package sudoku.view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import sudoku.model.*;

public class BoardWrapper {
	
	public final JFrame wrapper = new JFrame("Sudoku");
	private SudokuBoard board;
	
	// create a wrapper for sudoku with menu
	public BoardWrapper() {
		wrapper.getContentPane().add(board = new SudokuBoard());
		wrapper.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		createMenu();
		wrapper.pack();
		wrapper.setResizable(false);
		center();
		wrapper.setVisible(true);
	}
	
	// center on the screen
	private void center() {
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension wrapperSize = wrapper.getSize();
		wrapper.setLocation((screen.width - wrapperSize.width) >> 1, // bit-shift operator
				(screen.height - wrapperSize.height) >> 1);
	}
	
	private void createMenu() {
		JMenuBar bar = new JMenuBar();
		JMenu sudokuMenu = new JMenu("Sudoku");
		
		JMenuItem about = new JMenuItem("About");
		JMenuItem howToPlay = new JMenuItem("How to play");
		
		sudokuMenu.add(howToPlay);
		sudokuMenu.addSeparator();
		sudokuMenu.add(about);
		
		bar.add(sudokuMenu);
		
		about.addActionListener((ActionEvent e ) -> {
			JOptionPane.showMessageDialog(
					null,
					"Author: Małgorzata Rakowska",
					"About",
					JOptionPane.INFORMATION_MESSAGE);
		});
		
		howToPlay.addActionListener((ActionEvent e ) -> {
			JOptionPane.showMessageDialog(
					null,
					"Fill in the board with numbers.",
					"How to play",
					JOptionPane.INFORMATION_MESSAGE);
		});
		
		wrapper.setJMenuBar(bar);
	}

}
