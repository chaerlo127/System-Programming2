package main;


public class Main {

	public static void main(String[] args) {
		Scheduler scheduler = new Scheduler();
		UI ui = new UI(scheduler);
		ui.start(); // ui를 Thread로 만들어서 돌린다.

		// 초기화:  UI에 Scheduler 정보를 넣어줘야 한다. 포인터를 줘야지 queue의 주소를 알 수 있는 것임.
		scheduler.start();
	}
}
