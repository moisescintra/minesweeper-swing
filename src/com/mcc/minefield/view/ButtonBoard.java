package com.mcc.minefield.view;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingUtilities;

import com.mcc.minefield.model.EventField;
import com.mcc.minefield.model.Field;
import com.mcc.minefield.model.ObserverField;

public class ButtonBoard extends JButton implements ObserverField, MouseListener{
	private static final long serialVersionUID = 1L;

	private final Color BG_DEFAULT = new Color( 184, 184, 184);
	private final Color BG_MARKED = new Color( 8, 179, 247);
	private final Color BG_BOOM = new Color( 189, 66, 68);
	private final Color TEXT_GREEN = new Color( 0, 100, 0);

	private Field field;

	public ButtonBoard(String string) {
		super(string);
	}

	public ButtonBoard(Field field) {
		this.field = field;

		setDefaultStyle();

		addMouseListener(this);

		field.observerRegister(this);
	}

	@Override
	public void eventOccurred(Field field, EventField event) {
		switch(event) {
		case OPEN:
			setOpenStyle();
			break;
		case BOOM:
			setBommStyle();
			break;
		case MARKED:
			setMarkedStyle();
			break;
		case UNMARKED:
		case RESET:
		default:
			setDefaultStyle();
			break;
		}

		//Para forçar o componente ser atualizado, nao é necessário
		SwingUtilities.invokeLater(() -> {
			repaint();
			validate();
		});
	}

	private void setDefaultStyle() {
		setOpaque(false);
		setBackground(BG_DEFAULT);
		setBorder(BorderFactory.createBevelBorder(0));
		setText("");
	}

	private void setBommStyle() {
		setOpaque(true);
		setBackground(BG_BOOM);
		setForeground(Color.BLACK);
		setText("X");
	}

	private void setMarkedStyle() {
		setOpaque(true);
		setBackground(BG_MARKED);
		setText("!");
	}

	private void setOpenStyle() {
		setOpaque(true);
		setBorder(BorderFactory.createLineBorder(Color.GRAY));

		if(field.isMine()) {
			setBackground(BG_BOOM);
			return;
		}

		setBackground(BG_DEFAULT);

		switch (field.mineInNeighborhood()) {
		case 1:
			setForeground(TEXT_GREEN);
			break;
		case 2:
			setForeground(Color.BLUE);
			break;
		case 3:
			setForeground(Color.YELLOW);
			break;
		case 4:
		case 5:
		case 6:
			setForeground(Color.RED);
			break;
		default:
			setForeground(Color.PINK);
		}

		String text = !field.safeNeighborhood() ? field.mineInNeighborhood() + "" : "";
		setText(text);
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == 1) //System.out.println("Esquerdo");
			field.open();
		else if(e.getButton() == 3) //System.out.println("direito");
			field.changeMark();
		else
			System.out.println("outro");
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e){
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
