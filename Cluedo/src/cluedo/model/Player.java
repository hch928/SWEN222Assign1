package cluedo.model;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Player {

    public enum STATUS {
        WATCHING,
        MOVING,
        MAKINGANNOUNCEMENT,
        ROLLING,
        REFUTING,
        ELIMINATED,
        INITIALIZING,
    }

    private final int uid;
    private STATUS status;
    private String uName;
    private ArrayList<Card> cards;
    private Chara character;
    private int dice;
    private int stepsRemain;

    public Player(int uid) {
        this.uid = uid;
        cards = new ArrayList<Card>();
    }

    public void toOutputStream(DataOutputStream dos) throws IOException{
        dos.writeByte(uid);
        dos.writeByte(status.ordinal());
        dos.writeByte(uName.length());
        byte[] b = uName.getBytes("UTF-8");
        dos.write(b);
        dos.writeByte(character.getName().ordinal());
        dos.writeByte(dice);
        dos.writeByte(stepsRemain);
        //write numbers of cards
        dos.writeByte(cards.size());
        for (Card c: cards) {
            c.toOutputStream(dos);
        }
    }

    public static Player fromInputStream(DataInputStream dis) throws IOException{
        Player temp = new Player(dis.readByte());
        temp.setStatus(Player.STATUS.values()[dis.readByte()]);
        //read name
        int nameLength = dis.readByte();
        byte[] nTemp = new byte[nameLength];
        dis.read(nTemp);
        temp.setUName(new String(nTemp,"UTF-8"));
        temp.setCharacter(new Chara(Card.CHARACTER.values()[dis.readByte()]));
        temp.setDice(dis.readByte());
        temp.setStepsRemain(dis.readByte());

        //read cards
        temp.getCards().clear();
        int size = dis.readByte();
//        System.out.printf("%d\n",size);
        for (int i = 0; i < size; ++i) {
            temp.getCards().add(Card.newCardFromByte(dis));
        }

        return temp;
    }

    /**
     * set the player's status to given status
     * @param status
     */
    public void setStatus(STATUS status) {
        this.status = status;
    }

    /**
     * return current player's status
     * @return
     */
    public STATUS getStatus() {
        return status;
    }

    public void setUName(String name) {
        this.uName = name;
    }

    public void setCharacter(Chara character){
        this.character = character;
    }

    public Chara getCharacter() {
        return character;
    }

    public void setDice(int dice) {
        this.dice = dice;
    }

    public void setStepsRemain(int stepsRemain) {
        this.stepsRemain = stepsRemain;
    }

    public int getUid() {
        return uid;
    }

    public ArrayList<Card> getCards(){
        return cards;
    }

    public boolean equals(Object o) {
        if (o instanceof Player) {
            Player temp =(Player) o;
            return temp.getUid() == this.uid && temp.getUName().equals(this.uName);
        }

        return false;
    }

    public String getUName() {
        return uName;
    }

    public int getStepsRemain() {
        return stepsRemain;
    }

    public int getDice() {
        return dice;
    }
}
