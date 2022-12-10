import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JOptionPane;

public class UI extends Thread {
	private Queue<Interrupt> interruptQueue;
	private Vector<File> files;
	private Vector<String> fileName;
	private int processNum;
	private boolean exit;

	public UI(Queue<Interrupt> interruptQueue) {
		this.interruptQueue = interruptQueue;
		this.files = new Vector<>();
		this.fileName = new Vector<>();
		exit = true;
		processNum = 0;
	}

	public void initialize() {
	}

	public void finish() {
		this.exit = false;
	}
	
	public void setFile(File file) {
		if (file != null) {
			this.files.add(file);
			this.fileName.add(file.getName());
			this.processNum++;
		}
	}

	public void exitMtd(boolean exit) {
		this.exit = exit;
	}
	public boolean getExit() {
		return this.exit;
	}
	// getters
	public int getCount() {
		return this.processNum;
	}

	public String getFileList() {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		int i = 0;

		sb.append("[Process Select List]").append("<br/>");

		for (String name : this.fileName) {
			sb.append("[" + ++i + "]").append(name).append("<br/>");
		}
		sb.append("</html>");
		return sb.toString();
	}

	public void run() {
		Loader loader = new Loader();

		// console command
		// "r fileName" -> execute fileName
		// "q" -> quit program
		while (exit) {
			if (!files.isEmpty()) {
				Process process = loader.load(files.remove(0));
				if (process != null) {
					process.setProNum(processNum);
					Interrupt interrupt = new Interrupt(Interrupt.EInterrupt.eProcessStart, process);
					this.interruptQueue.enqueue(interrupt);
				}
			}
		}
	}
	private class Loader {
		public Process load(File file) {
			Process process = null;
			try {
				if (file != null) {
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
}


