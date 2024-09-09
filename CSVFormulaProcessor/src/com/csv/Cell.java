package com.csv;

public class Cell {

	// Can be a number or a formula
	private String value;

	public Cell(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isFormula() {
		return value.startsWith("=");
	}

}
