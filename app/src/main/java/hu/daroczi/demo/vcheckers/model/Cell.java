package hu.daroczi.demo.vcheckers.model;

public class Cell {

	private boolean selected = false;
	private boolean filled = false;
	private int col;
	private int row;

	public Cell(boolean filled, boolean selected, int row, int col) {
		this.filled = filled;
		this.setSelected(selected);
		this.row = row;
		this.col = col;
	}

	public boolean isFilled() {
		return filled;
	}

	public boolean isEmpty() {
		return !isFilled();
	}

	public void setFilled(boolean filled) {
		this.filled = filled;
	}

	public int getCol() {
		return col;
	}

	public int getColIndex() {
		int index = getCol() - 1;
		return index;
	}

	public void setCol(int col) {
		this.col = col;
	}

	public int getRow() {
		return row;
	}

	public int getRowIndex() {
		int index = getRow() - 1;
		return index;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
