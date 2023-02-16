package com.mcc.minefield.view;

import javax.swing.JFrame;

import com.mcc.minefield.model.Board;

public class MainScreen extends JFrame{
	private static final long serialVersionUID = 1L;

	public MainScreen() {
		Board board = new Board(10, 20, 10);

		add(new BoardPanel(board));

		setTitle("Campo Minado");
		setSize( 690, 438);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args) {
		new MainScreen();
	}
}
