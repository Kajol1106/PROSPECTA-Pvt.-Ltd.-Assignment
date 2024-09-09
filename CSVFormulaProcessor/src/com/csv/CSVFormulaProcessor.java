package com.csv;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CSVFormulaProcessor {

	public static void main(String[] args) {
		System.out.println("Just for test");

		try {
			CSVFormulaProcessor evaluator = new CSVFormulaProcessor();
			evaluator.loadCSV(
					"C:\\Users\\kajol\\Desktop\\Refresher Course\\CSVFormulaProcessor\\src\\com\\csv\\input.csv");
			evaluator.resolveCellValues();
			evaluator.saveCSV(
					"C:\\Users\\kajol\\Desktop\\Refresher Course\\CSVFormulaProcessor\\src\\com\\csv\\output.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Map<String, Cell> cells = new HashMap<>();

	// parse the csv file and stores the data in a map
	public void loadCSV(String filePath) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		String line;
		int rowNum = 1;

		while ((line = br.readLine()) != null) {
			String[] values = line.split(",");
			char col = 'A';

			for (String value : values) {
				String cellName = "" + col + rowNum;
				cells.put(cellName, new Cell(value.trim()));
				col++;
			}
			rowNum++;
		}
		br.close();
	}

	private int evaluateFormula(String formula) {
		formula = formula.substring(1); // Remove '='

		int result = 0;
		char operation = '+';

		String[] tokens = formula.split("(?<=[-+*/])|(?=[-+*/])");

		for (String token : tokens) {
			token = token.trim();

			if (token.matches("[-+*/]")) {
				operation = token.charAt(0);
			} else {
				int value;

				// If token is a cell reference, evaluate it recursively
				if (Character.isLetter(token.charAt(0))) {
					String cellValue = cells.get(token).getValue();
					if (cellValue.startsWith("=")) {
						value = evaluateFormula(cellValue);
					} else {
						value = Integer.parseInt(cellValue);
					}
				} else {
					value = Integer.parseInt(token);
				}

				switch (operation) {
				case '+':
					result += value;
					break;
				case '-':
					result -= value;
					break;
				case '*':
					result *= value;
					break;
				case '/':
					result /= value;
					break;
				}
			}
		}
		return result;
	}

	// Resolves the value of each cell
	public void resolveCellValues() {
		for (String cellName : cells.keySet()) {
			Cell cell = cells.get(cellName);
			if (cell.isFormula()) {
				int value = evaluateFormula(cell.getValue());
				cell.setValue(String.valueOf(value));
			}
		}
	}

	// save the final CSV output
	public void saveCSV(String outputPath) throws IOException {
		BufferedWriter bw = new BufferedWriter(new FileWriter(outputPath));

		int rowCount = cells.keySet().stream().mapToInt(s -> Integer.parseInt(s.substring(1))).max().orElse(1);
		int colCount = cells.keySet().stream().mapToInt(s -> s.charAt(0) - 'A' + 1).max().orElse(1);

		for (int i = 1; i <= rowCount; i++) {
			for (char c = 'A'; c < 'A' + colCount; c++) {
				String cellName = "" + c + i;
				bw.write(cells.get(cellName).getValue());
				if (c < 'A' + colCount - 1) {
					bw.write(",");
				}
			}
			bw.newLine();
		}
		bw.close();
	}

}
