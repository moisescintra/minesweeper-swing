package com.mcc.minefield.model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Board {

	private final int lines;
	private final int columns;
	private final int mines;

	private final List<Field> fields = new ArrayList<>();
	private final List<Consumer<EventResult>> observers = new ArrayList<>();

	public Board(int lines, int columns, int mines) {
		this.lines = lines;
		this.columns = columns;
		this.mines = mines;
		
		createField();
		connectNeighbors();
		randomMines();
	}

	private void createField() {
		for (int l = 0; l < getLines(); l++) {
			for (int c = 0; c < getColumns(); c++) {
				Field field = new Field( l, c);
				field.observerRegister(new classObserver());
				fields.add(field);
			}
		}
	}

	private void connectNeighbors() {
		for(Field f1: fields) {
			for(Field f2: fields) {
				f1.addNeighbor(f2);
			}
		}
	}

	private void randomMines() {
		long setMines = 0;
		Predicate<Field> filterMine = m -> m.isMine();
		int random;
		do {
			random = (int) (Math.random() * fields.size());
			fields.get(random).putMine();
			setMines = fields.stream().filter(filterMine).count();
		}while(setMines < getMines());
	}

//	public void open(int line, int column) {
//		fields.parallelStream()
//			.filter(f -> f.getLine() == line
//				&& f.getColumn() == column)
//			.findFirst()
//			.ifPresent(f -> f.open());
//	}
//
//	public void mark(int line, int column) {
//		fields.parallelStream()
//		.filter(f -> f.getLine() == line
//			&& f.getColumn() == column)
//		.findFirst()
//		.ifPresent(f -> f.changeMark());;
//	}

	public boolean achievedGoal() {
		return fields.stream().allMatch(f -> f.achievedGoal());
	}

	public void reset() {
		fields.stream().forEach(f -> f.reset());
		randomMines();
	}

	public void forEach(Consumer<Field> function) {
		fields.forEach(function);
	}

	public Field getField(int index) {
		return fields.get(index);
	}

	private void showMines() {
		fields.stream()
			.filter(f -> f.isMine())
			.filter(f -> !f.isMarked())
			.forEach(f -> f.setOpened(true));
	}

	public void observerRegister(Consumer<EventResult> observer) {
		observers.add(observer);
	}

	private void observersNotify( boolean event) {
		observers.stream()
			.forEach(o -> o.accept(new EventResult(event)));
	}

	final private class classObserver implements ObserverField{
		@Override
		public void eventOccurred(Field field, EventField event) {
			if(event == EventField.BOOM) {
				showMines();
				observersNotify(false);
			} else if(achievedGoal()) {
				observersNotify(true);
			}
		}
	}

	final public class EventResult{
		private final boolean result;

		public EventResult(boolean result) {
			this.result = result;
		}

		public boolean isResult() {
			return result;
		}
	}

	public int getLines() {
		return lines;
	}

	public int getColumns() {
		return columns;
	}

	public int getMines() {
		return mines;
	}
	
	public String toString(int index) {
		fields.get(index).setOpened(true);
		return fields.get(index).toString();
	}
}


