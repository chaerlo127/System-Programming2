package main;


public class Main {

	public static void main(String[] args) {
		Scheduler scheduler = new Scheduler();
		UI ui = new UI(scheduler);
		
		MainFrame mf = new MainFrame(ui);
		mf.setVisible(true); //main frame 그림.
		
		ui.start(); // button 클릭하면 생기도록
		scheduler.start();
	}
}
