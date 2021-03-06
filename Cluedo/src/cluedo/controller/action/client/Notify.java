package cluedo.controller.action.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cluedo.controller.action.Action;
import cluedo.controller.action.ActionHelper;
import cluedo.controller.action.ActionHelper.ActionType;
import cluedo.controller.action.server.Move.Direction;
import cluedo.controller.connection.SlaveConnection;
import cluedo.exception.IllegalRequestException;
import cluedo.model.Board;
import cluedo.model.Card;
import cluedo.model.Player;
import cluedo.view.BoardFrame;

/**
 * Client side action, will update the board with the given infomation, 
 * and update the UI base on current player's status
 * @author C
 *
 */
public class Notify implements SlaveAction{
	
	SlaveConnection connection;
	private byte[] state;
	
	public Notify(SlaveConnection con){
		try {
			connection = con;
			DataInputStream input = connection.getInput();
			int lenght = input.readInt();
			state = new byte[lenght];
			input.read(state);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void execute(Board game,BoardFrame frame) {
		//update the board
		try {
			game.fromByte(state);
			System.out.println("Slave board updated");
			frame.repaint();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//check player's status then change the ui correspondingly
		try {
			Player player = game.getPlayer(connection.uid());
			
			Player.STATUS status = player.getStatus();
			System.out.println("Player uid: "+connection.uid()+" status: "+status);
			
			System.out.println("Message for this player: "+player.getString());
			
			if(status.equals(Player.STATUS.INITIALIZING)){
				System.out.println("Need initialize the user");
				frame.showMessage("Initialize user");
				String[] playerInfo = frame.initPlayer();
				Card.CHARACTER character = Card.CHARACTER.valueOf(playerInfo[1]);
				ActionHelper.requestInitialize(connection,playerInfo[0],character);
			}else if(status.equals(Player.STATUS.ROLLING)){
				System.out.println("It your turn!");
				System.out.println("Enable roll");
				frame.enableRoll();
			}else if(status.equals(Player.STATUS.MOVING)){
				frame.requestFocus();
				frame.enableAction();
				System.out.println("Recieved move");
				System.out.println("New Pos, x:"+player.getCharacter().getX()+" y:"+player.getCharacter().getY());
				
			}else if(status.equals(Player.STATUS.MAKINGANNOUNCEMENT)){
				frame.enableAction();
			}else if(status.equals(Player.STATUS.WATCHING)){
				//update messages
			}else if(status.equals(Player.STATUS.REFUTING)){
				//enable refuting
				frame.enableRefute();
			}else if(status.equals(Player.STATUS.ELIMINATED)){
				//disable all user actions
				frame.enableRefute();
			}
		} catch (IllegalRequestException e) {
			e.printStackTrace();
		}
	
		
		frame.repaint();
	}

}
