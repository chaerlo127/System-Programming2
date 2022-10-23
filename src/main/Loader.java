package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JOptionPane;

//file : instruction, size, register size
public class Loader {
	// 객체 지향적으로 만들기
	// 프로세스 보고 너가 만들어라
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