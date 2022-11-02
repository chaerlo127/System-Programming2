package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class Loader {
	
	public Process load(File file) {
		// file => process
		Process process = null;
		try {
			if(file != null) {
				Scanner scanner = new Scanner(file);
				process = new Process(file.getName());
				process.parse(scanner);
				scanner.close();
			}
			return process;
		} catch (FileNotFoundException e) {
			System.out.println("파일을 찾을 수 없습니다. 다시 입력해주세요");
			JOptionPane.showMessageDialog(null, "파일을 찾을 수 없습니다. 다시 입력해주세요", "Warning", JOptionPane.WARNING_MESSAGE);
		}
		return null;
	}
}