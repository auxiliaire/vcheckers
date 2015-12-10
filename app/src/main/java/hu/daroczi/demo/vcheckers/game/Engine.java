package hu.daroczi.demo.vcheckers.game;

import hu.daroczi.demo.vcheckers.model.Cell;

import java.util.List;

public class Engine {

	public final int NO_FIELD = -1;
	public final int FILLED_FIELD = 1;
	public final int EMPTY_FIELD = 0;

	private Cell current;
	private Cell[][] cells;
	private int[][] table;
	private int rows;
	private int cols;

	public Engine(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		table = new int[rows][cols];
		cells = new Cell[rows][cols];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				table[i][j] = NO_FIELD;
				cells[i][j] = null;
			}
		}
	}

	public void addCell(Cell cell) {
		cells[cell.getRowIndex()][cell.getColIndex()] = cell;
		table[cell.getRowIndex()][cell.getColIndex()] = cell.isFilled() ? FILLED_FIELD
				: EMPTY_FIELD;
	}

	public void addCells(List<Cell> cells) {
		for (Cell cell : cells) {
			addCell(cell);
		}
	}

	public Cell move(Cell from, Cell to) {
		if (isValidMove(from, to)) {
			table[from.getRowIndex()][from.getColIndex()] = EMPTY_FIELD;
			table[to.getRowIndex()][to.getColIndex()] = FILLED_FIELD;
			cells[from.getRowIndex()][from.getColIndex()].setFilled(false);
			cells[to.getRowIndex()][to.getColIndex()].setFilled(true);
			return getMiddle(from, to);
		}
		return null;
	}

	public Cell getMiddle(Cell first, Cell last) {
		int middleRow = Math
				.round((first.getRowIndex() + last.getRowIndex()) / 2);
		int middleCol = Math
				.round((first.getColIndex() + last.getColIndex()) / 2);
		if (middleRow < table.length && middleCol < table[middleRow].length) {
			return cells[middleRow][middleCol];
		}
		return null;
	}

	public boolean isValidMove(Cell previous, Cell current) {
		if ((previous.getRow() == current.getRow() && Math.abs(current.getCol()
				- previous.getCol()) == 2)
				|| (previous.getCol() == current.getCol() && Math.abs(current
						.getRow() - previous.getRow()) == 2)) {
			if (previous.isFilled() && previous.isSelected()
					&& getMiddle(previous, current).isFilled()) {
				return true;
			}
		}
		return false;
	}
}
