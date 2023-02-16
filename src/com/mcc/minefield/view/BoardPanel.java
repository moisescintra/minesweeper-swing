package com.mcc.minefield.view;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.mcc.minefield.model.Board;

public class BoardPanel extends JPanel{
	private static final long serialVersionUID = 1L;

	public BoardPanel(Board board) {

		createBoardPanel(board, board.getLines(), board.getColumns());
		
		board.observerRegister(e -> {
			SwingUtilities.invokeLater(() -> {
				if(e.isResult()) {
					JOptionPane.showMessageDialog( this, "Ganhou :)  !!");
				} else
					JOptionPane.showMessageDialog( this, "Perdeu :)  !!");
				board.reset();
			});
		});
	}
	
	private void createBoardPanel(Board board, int rows, int cols) {
		setLayout(new GridLayout( (rows + 2), (cols + 2)));

//		board.forEach(f -> add(new ButtonBoard(f)));

		int total = (rows + 1) * (cols + 2);
		for(int i = 0; i < (cols + 2); i++)
			add(new JLabel(""));
		for(int i = (cols + 2), j = 0; i < total; i++) {
			if(i % (cols + 2) == 0)
				add(new JLabel(""));
			else if((i + 1) % (cols + 2) == 0)
				add(new JLabel(""));
			else
				add(new ButtonBoard(board.getField(j++)));
		}
		for(int i = 0; i < (cols + 2); i++)
			add(new JLabel(""));
	}
}
