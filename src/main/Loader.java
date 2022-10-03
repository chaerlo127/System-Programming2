package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//file : instruction, size, register size
public class Loader {
	
	//  code, data, stack, heap
	private void parseData(Scanner scanner) {
		String command = scanner.next();
		while(command.compareTo(".code") != 0) {
			parseData(scanner);
		}
	}
	private void parseCode(Scanner scanner) {
		String command = scanner.next();
		while(command.compareTo(".end") != 0) {
			parseData(scanner);
		}
	}
	// 문장을 해석하는 것
	private void parse(Scanner scanner) {
		String command = scanner.next();
		parseData(scanner);
		parseCode(scanner);

	}
	public void load(String exeName) {
		// file => process
		try {
			File file = new File("data" + "/" + exeName);
			Scanner scanner = new Scanner(file);
			this.parse(scanner);
			scanner.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
