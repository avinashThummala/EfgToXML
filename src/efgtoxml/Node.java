//The basic Node class
//To be overridden by ChoiceNode, PlayerNode and Outcome

package efgtoxml;
/**
 *
 * @author Avinash Thummala
 */
public abstract class Node {

    String nodeName;
    int infoSetNumber;
    String infoSetName;
    Node upNode;
    boolean hasOutcome;
    boolean hasProbMove=false;
    boolean hasMove=false;
    String probMove="";
    String move="";


    Node(){
    }
    
    public void setUpNode(Node up){
        upNode=up;
    }

    public void setNodeName(String val){
        nodeName=val;
    }

    public void setInfoSetNumber(int val){
        infoSetNumber=val;
    }

    public void setInfoSetName(String val){
        infoSetName=val;
    }

    public Node getUpNode(){
        return upNode;
    }

    public String getNodeName(){
        return nodeName;
    }

    public int getInfoSetNumber(){
        return infoSetNumber;
    }

    public String getInfoSetName(){
        return infoSetName;
    }

    public void setBoolOutcome(boolean val){
        hasOutcome=val;
    }

    public boolean getBoolOutcome(){
        return hasOutcome;
    }

    public void setProb(){
        hasProbMove=true;
    }

    public boolean hasProb(){
        return hasProbMove;
    }

    public void addProbMove(String val){
        probMove=val;
    }

    public String getProbMove(){
        return probMove;
    }

    public void addMove(String val){
        move=val;
    }

    public String getMove(){
        return move;
    }

    public void setMove(){
        hasMove=true;
    }

    public boolean hasMove(){
        return hasMove;
    }

    public abstract void addOutcome(Outcome outcome);

    public abstract void adjustMoveProb();

    public abstract void newPrintNode();

    public abstract boolean isFull();

    public abstract void addNextNode(Node node);

}
