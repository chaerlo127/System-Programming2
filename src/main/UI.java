package main;

import java.io.File;
import java.util.Vector;

public class UI extends Thread{
	private Scheduler scheduler;
	private Vector<File> files;
	private boolean exit;
	private int processNum;

	public UI(Scheduler scheduler){
		this.scheduler = scheduler; // ui에게 Loader를 장착시켜둠.
		this.files = new Vector<>();
		exit = true;
		processNum = 1;
	}
	
	public void getFile(File file) {
		this.files.add(file);
	}
	
	public void exitMtd(boolean exit) {
		this.exit = exit;
	}
	
	// run override 를 하면 자동으로 run을 해줌.
	public void run() {
		Loader loader = new Loader();
		while(exit) {
			if(!files.isEmpty()) {
				Process process =loader.load(files.remove(0)); // process 생성
				if(process != null) {
					process.setProNum(processNum++);
					scheduler.enReadyQueue(process); // blocking 되면 문제가 발생될 수 있음.
				}  
			} 
		}
	}
}
