package cluedo.controller.connection;

import java.io.IOException;
import java.net.Socket;

import cluedo.controller.ActionHandler;
import cluedo.controller.action.ActionHelper;
import cluedo.controller.action.Action;
import cluedo.controller.action.ActionHelper.ActionType;
import cluedo.controller.action.client.Notify;

public class SlaveConnection extends AbstractConnection{

	public SlaveConnection(Socket socket,int clock){
		super(socket,clock);
	}
	
	@Override
	public void run(){
		while(!socket.isClosed()){
			try {
				if(input.available() != 0){
					int index = input.readInt();
					ActionType actionType = ActionType.values()[index];
					
					assert(actionType.equals(ActionType.NOTIFY));
					
					System.out.println("Notify recieved");
					
					handler.offerAction(new Notify(input));
					
					//all the action recieved will be a notify action
					//depends on the type of the notify, update the view
					
				}
				
				Thread.sleep(boardcastClock);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
