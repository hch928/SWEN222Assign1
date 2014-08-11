package cluedo.controller;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import cluedo.controller.action.Action;
import cluedo.controller.action.Initialize;
import cluedo.controller.action.Notify;
import cluedo.controller.connection.Master;
import cluedo.model.Board;

public class ActionMaster extends Thread implements ActionHandler{
	
	private Master[] connections;
	
	private Queue<Action> actionQueue = new ConcurrentLinkedQueue<Action>();
	
	private Board game;
	private Round round;
	
	public ActionMaster(Master[] con){
		connections = con;
		
		//initialize the game
		round = new Round();
		
		Action initialize = new Initialize();
		actionQueue.offer(initialize);
	}
	
	@Override
	public void run(){
		System.out.println("MASTER RUNNING");
		while(1 == 1){
			try {
				Thread.sleep(1000);
				actionQueue.add(new Notify(connections));
				
				System.out.println("Queue an action");
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public boolean isEmpty(){
		return actionQueue.isEmpty();
	}
	
	public Action pollAction(){
		return actionQueue.poll();
	}
}