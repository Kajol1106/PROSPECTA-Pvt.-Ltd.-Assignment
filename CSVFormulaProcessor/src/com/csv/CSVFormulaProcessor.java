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
			//created the instance of CSVFormulaProcessor
			CSVFormulaProcessor evaluator = new CSVFormulaProcessor();

			//to load the CSV file and store the data
			evaluator.loadCSV(
					"C:\\Users\\kajol\\Desktop\\Refresher Course\\CSVFormulaProcessor\\src\\com\\csv\\input.csv");
			
			//to evaluate the formula
			evaluator.resolveCellValues();

			//to write the result in a new CSV file
			evaluator.saveCSV(
					"C:\\Users\\kajol\\Desktop\\Refresher Course\\CSVFormulaProcessor\\src\\com\\csv\\output.csv");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Map<String, Cell> cells = new HashMap<>();

	// parse the csv file and stores the data in a map
	//this will reads the csv file line by line using BufferedReader
	//each line split by "," to separate the values
	//each value is stored in Map called cess with the key is the cell name ex., A1, B1
	// and the value is Cell object that holds the content ex., number or formula
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

	//If cell contains a formula which is starts with =, this method will use
	//It removes the = sign and splits the formula into parts(token)
	//handling basic math operations +, -, *, /
	//If formula refers to another cell eg., A1 it looks up the value of that cell
	//If that cell also contains formula, it will use recursion to evaluates the value
	//and it will calculates the final result for that formula and return it
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
	//this will goes through each cell in the cells Map
	//It will check the cell contains fomula or not using isFormula() which i defined in Cell class
	//it uses the evaluateFormula() method to get final value and replaces the 
	//formula with result
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
	//once all the cell values are resolved this method will writes the final data in new CSV file
	//It will organize the cells back into rows and columns
	//writing each value in correct position and seprating them by commas
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
