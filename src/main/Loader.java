package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//file : instruction, size, register size
public class Loader {
	// 객체 지향적으로 만들기
	// 프로세스 보고 너가 만들어라
	public Process load(String exeName) {
		// file => process
		try {
			File file = new File("data" + "/" + exeName);
			Scanner scanner = new Scanner(file);
			Process process = new Process();
			process.parse(scanner);
			scanner.close();
			return process;
		} catch (FileNotFoundException e) {
			System.out.println("파일을 찾을 수 없습니다. 다시 입력해주세요");
//			e.printStackTrace();
		}
		return null;
	}
}