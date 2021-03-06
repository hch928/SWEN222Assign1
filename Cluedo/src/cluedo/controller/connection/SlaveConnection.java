package cluedo.controller.connection;

import java.io.IOException;
import java.net.Socket;

import cluedo.controller.ActionHandler;
import cluedo.controller.action.ActionHelper;
import cluedo.controller.action.Action;
import cluedo.controller.action.ActionHelper.ActionType;
import cluedo.controller.action.client.Notify;

/**
 * Connection at client side
 * Will enqueue action to the handler
 * @author C
 *
 */
public class SlaveConnection extends AbstractConnection{

	public SlaveConnection(Socket socket,int clock){
		super(socket,clock);
		try {
			uid = input.readInt();
			System.out.println("Slave UID: "+uid);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run(){
		if(handler == null) throw new RuntimeException("Require to set an action handler");
		
		while(!socket.isClosed()){
			try {
				if(input.available() != 0){
					int index = input.readInt();
					ActionType actionType = ActionType.values()[index];
					
					assert(actionType.equals(ActionType.NOTIFY) || actionType.equals(ActionType.DISCONNECT));
					
					System.out.println("Slave Action offered");
					
					if(actionType.equals(ActionType.DISCONNECT)){
						int uid = input.readInt();
						if(uid == this.uid){
							socket.close();
							break;
						}
					}
					
					handler.offerAction(new Notify(this));
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
