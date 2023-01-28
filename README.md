# System-Programming2
* 2022 2학기 시스템 프로그래밍2
* Operating System Context Switching Code
* UI + Loader, Scheduler, File System, Timer 총 4개의 Therad의 Multi-Tasking을 구현

## System Design
![image](https://user-images.githubusercontent.com/90203250/215274732-a5032630-0c20-4285-bc9b-877f4cb22710.png)


<br>

## project functions
* exe 
  * aseembly language로 instruction 생성
* UI [Swing]
  * Open/Click File + IO Exception 처리
  * Create Process 개수 화면 표시
  * Create Process List [ScrollPane]
* Memory
  * Loader
    * Process 생성
  * Sheduler
    * instruction code를 data segmeent에 저장
  * Loader & Scheduler
    * Semaphore & Critical Section (Blocking)
  * Ready Queue
  * Wait Queue
  * Process Controll Block
* File System
* Interrupt
  * IO Interrupt
  * Process Interrupt
  * Timeout Interrupt 
* Thread 모두 종료 후, main 프로젝트 종료 [ Thread join() ]
