package com.mcc.minefield.model;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Field {
	
	private final int line;
	private final int column;
	
	private boolean opened = false;
	private boolean mine = false;
	private boolean marked = false;
	
	private List<Field> neighbors = new ArrayList<>();

	private Set<ObserverField> observers = new LinkedHashSet<>();

	//package
	Field(int line, int column) {
		this.line = line;
		this.column = column;
	}

	public void observerRegister( ObserverField observer) {
		observers.add(observer);
	}

	private void observersNotify( EventField event) {
		observers.stream()
			.forEach(o -> o.eventOccurred( this, event));
	}

	//package
	boolean addNeighbor (Field neighbor) {
		boolean differentLine = this.line != neighbor.line;
		boolean differentColumn = this.column != neighbor.column;
		boolean diagonal = differentLine && differentColumn;
		
		int deltaLine = Math.abs(this.line - neighbor.line);
		int deltaColumn = Math.abs(this.column - neighbor.column);
		int deltaTotal = deltaLine + deltaColumn;
		
		if(deltaTotal == 1 && !diagonal) {
			neighbors.add(neighbor);
			return true;
		} else if(deltaTotal == 2 && diagonal) {
			neighbors.add(neighbor);
			return true;
		} else
			return false;
	}

	public void changeMark() {
		if(!isOpened()) {
			setMarked(!isMarked());
			
			if(isMarked())
				observersNotify(EventField.MARKED);
			else
				observersNotify(EventField.UNMARKED);
		}
	}

	public boolean open() {
		if(!isOpened() && !isMarked()) {
			if(isMine()) {
				observersNotify(EventField.BOOM);
				return true;
			}

			setOpened(true);

			if(safeNeighborhood())
				neighbors.forEach(n -> n.open());

			return true;
		}
		return false;
	}

	//package
	void putMine() {
		if(!isMine())
			setMine(true);
	}

	public boolean safeNeighborhood() {
		return neighbors.stream().noneMatch(n -> n.isMine());
	}

	public int mineInNeighborhood() {
		return (int) neighbors.stream().filter(n -> n.isMine()).count();
	}

	//package
	boolean achievedGoal() {
		boolean unraveled = !isMine() && isOpened();
		boolean secured = isMine() && isMarked();
		return unraveled || secured;
	}

	//package
	void reset() {
		setMarked(false);
		setMine(false);
		setOpened(false);

		observersNotify(EventField.RESET);
	}

	@Override
	public String toString() {
		if(isMarked())
			return "x";
		else if(isOpened() && isMine())
			return "*";
		else if(isOpened() && mineInNeighborhood() > 0)
			return Long.toString(mineInNeighborhood());
		else if(isOpened())
			return " ";
		else
			return "?";
	}

	//package
	int getLine() {
		return this.line;
	}

	//package
	int getColumn() {
		return this.column;
	}

	//package
	boolean isOpened() {
		return this.opened;
	}

	//package
	void setOpened(boolean opened) {
		this.opened = opened;

		if(opened)
			observersNotify(EventField.OPEN);
	}

	public boolean isMine() {
		return mine;
	}

	private void setMine(boolean mine) {
		this.mine = mine;
	}

	//package
	boolean isMarked() {
		return marked;
	}

	private void setMarked(boolean marked) {
		this.marked = marked;
	}
}
