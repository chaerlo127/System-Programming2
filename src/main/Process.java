package main;

import java.util.Scanner;
import java.util.Vector;

public class Process {
	
	private int codeSize, dataSize, stackSize, heapSize;
	private Vector<String> codeList;
	
	//getters
	public int getCodeSize() {
		return codeSize;
	}
	public int getDataSize() {
		return dataSize;
	}
	public int getStackSize() {
		return stackSize;
	}
	public int getHeapSize() {
		return heapSize;
	}
	public Vector<String> getCodeList() {
		return codeList;
	}

	public Process() {
		this.codeList = new Vector<String>();
	}
	
	//이해가 안됨...
	private void loadDataSegment(Scanner scanner) {
		String token = scanner.next(); // 독립적으로 떨어진 것
		while (token.compareTo(".code") != 0) {
			token = scanner.next();// codeSize
//			String sizetoken = scanner.next(); // 256
			int size = Integer.parseInt(token);
			if(token.compareTo("codeSize") == 0) {
				this.codeSize = size;
			}else if(token.compareTo("dataSize") == 0) {
				this.codeSize = size;
			}else if(token.compareTo("stackSize") == 0) {
				this.codeSize = size;
			}else if(token.compareTo("heapSize") == 0) {
				this.codeSize = size;
			}
		}
	}
	
	private void loadCodeSegment(Scanner scanner) {
		String line = scanner.nextLine(); // 독립적으로 떨어진 것
		while (line.compareTo(".end") != 0) {
			this.codeList.add(line);
			line = scanner.nextLine();
		}
	}

	public void load(Scanner scanner) {
		String line;
		while (scanner.hasNext()) {
			line = scanner.next();
			while (line.compareTo(".end") != 0) {
				// segment 읽기
				if (line.compareTo(".data") == 0) {
					loadDataSegment(scanner);
				} else if (line.compareTo(".code") == 0) {
					loadCodeSegment(scanner);
				}

			}

		}
	}

}
