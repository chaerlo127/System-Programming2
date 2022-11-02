package main;

public class Main {
// 중간고사 점수 : 100점
	public static void main(String[] args) {
		Scheduler scheduler = new Scheduler();
		UI ui = new UI(scheduler);
		
		MainFrame mf = new MainFrame(ui);
		mf.setVisible(true); //main frame 그림.
		
		ui.start();
		scheduler.start();
	}
	
}
