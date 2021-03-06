package cluedo.controller.action.server;

import java.io.DataInputStream;
import java.io.IOException;

import cluedo.controller.connection.MasterConnection;
import cluedo.exception.IllegalRequestException;
import cluedo.model.Board;
import cluedo.model.Card;
import cluedo.model.Card.TYPE;
import cluedo.model.Player;
import cluedo.model.Player.STATUS;
import cluedo.model.Room;

/**
 * Server side action
 * 
 * will read the suggestion from connection
 * When executing
 * 
 * @author C
 *
 */
public class Suggestion implements MasterAction{

	private MasterConnection connection;
	
	private Card.CHARACTER character;
	private Card.WEAPON weapon;
	private Card.ROOM room;
	
	public Suggestion(MasterConnection master){
		connection = master;
		
		try {
			System.out.println("Server Suggestion recieved");
			DataInputStream input = connection.getInput();
			
			character = Card.CHARACTER.values()[input.readInt()];
			weapon = Card.WEAPON.values()[input.readInt()];
			room = Card.ROOM.values()[input.readInt()];
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	@Override
	public void execute(MasterConnection[] connections,Board game) {

		assert(connections != null);
		assert(game != null);
		//set the suggestion to board
		//update player's status
		try {
			Player player = game.getPlayer(connection.uid());
			
			boolean inRoom = false;
			Room[] rooms = game.getRooms();
			for(Room r:rooms){
				if(r.getName().equals(room)){
					if(r.getCharactersInside().contains(player.getCharacter())){
						// if in room, make suggestion
						
						System.out.println("Suggestion is made");
						System.out.println("Suggestion, Character:"+character+" Weapon:"+weapon+" Room:"+room);
						
						Card[] suggestion = {
								new Card(TYPE.CHARCTER, character.toString()),
								new Card(TYPE.WEAPON,weapon.toString()),
								new Card(TYPE.ROOM,room.toString()),
						};
						
						game.setSuggestion(suggestion);
						
						player.setStatus(STATUS.WAITING);
					}
				}
			}
			
			
			
			
		} catch (IllegalRequestException e) {
			e.printStackTrace();
		}
		

		
	}


}
