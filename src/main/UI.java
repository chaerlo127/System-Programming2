package main;

import java.io.File;
import java.util.Vector;

public class UI extends Thread{
	private Scheduler scheduler;
	private Vector<File> files;
	private Vector<String> fileName;
	private boolean exit;
	private int processNum;
	
	public UI(Scheduler scheduler){
		this.scheduler = scheduler; 
		this.files = new Vector<>();
		this.fileName = new Vector<>();
		exit = true;
		processNum = 0;
	}
	
	public void setFile(File file) {
		if(file != null) {
			this.files.add(file);
			this.fileName.add(file.getName());
			this.processNum++;
		}
	}
	public int getCount() {
		return this.processNum;
	}
	public void exitMtd(boolean exit) {
		this.exit = exit;
	}
	public String getFileList() {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		int i = 0;
		
		sb.append("[Process Select List]").append("<br/>");
		
		for(String name:this.fileName) {
			sb.append("[" + ++i + "]").append(name).append("<br/>");
		}
		sb.append("</html>");
		return sb.toString();
	}
	
	// run override 를 하면 자동으로 run을 해줌.
	public void run() {
		Loader loader = new Loader();
		while(exit) {
			if(!files.isEmpty()) {
				Process process =loader.load(files.remove(0)); // 파일들을 저장하고 있는 벡터에서 가장 첫 번째 것을 꺼내서 Scheduler에게 보낸다.
				if(process != null) {
					process.setProNum(processNum);
					scheduler.enReadyQueue(process);
				}  
			} 
		}
	}
}
