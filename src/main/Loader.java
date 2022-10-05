package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

//file : instruction, size, register size
public class Loader {	
	// ��ü ���������� �����
	// ���μ��� ���� �ʰ� ������
	public void load(String exeName) {
		// file => process
		try {
			File file = new File("data" + "/" + exeName);
			Scanner scanner = new Scanner(file);
			Process process = new Process();
			process.load(scanner);
			scanner.close();
			
			Scheduler scheduler = new Scheduler();
			scheduler.getReadyQueue().enqueue(process);
			scheduler.run();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
