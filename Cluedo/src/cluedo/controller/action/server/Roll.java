package cluedo.controller.action.server;

import java.util.Random;

import cluedo.controller.action.ActionHelper;
import cluedo.controller.connection.MasterConnection;
import cluedo.exception.IllegalRequestException;
import cluedo.model.Board;
import cluedo.model.Player;

/**
 * Server side action
 * 
 * will return a random number between 1 to 6 to server for dice
 * 
 * @author C
 *
 */

public class Roll implements MasterAction{

	MasterConnection connection;
	
	public Roll(MasterConnection con){
		connection = con;
	}
	
	@Override
	public void execute(MasterConnection[] connections,Board game) {
		try {
			System.out.println("Server response to roll");
			Player player = game.getPlayer(connection.uid());
			Random r = new Random();
			player.setDice(r.nextInt(6)+1);
			
			player.setStatus(Player.STATUS.MOVING);
			
		} catch (IllegalRequestException e) {
			e.printStackTrace();
		}
		
		ActionHelper.broadcast(connections, game);
	}


}
