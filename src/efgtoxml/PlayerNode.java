package efgtoxml;

import java.util.ArrayList;

/**
 *
 * @author Avinash Thummala
 */
public class PlayerNode extends Node{

    ArrayList<String> actionNames;
    ArrayList<Node> nextNodes;
    Outcome outcome;

    int playerId;

    public boolean isFull(){

        if(actionNames.size()==nextNodes.size())
            return true;
        else
            return false;

    }

    public PlayerNode(String nodeName, int playerId, int infoSetId, String infoSetName){

        setNodeName(nodeName);
        setInfoSetNumber(infoSetId);
        setInfoSetName(infoSetName);

        actionNames=new ArrayList();
        nextNodes=new ArrayList();
        
        this.playerId=playerId;
    }

    public void adjustMoveProb(){

        //System.out.println("Test if "+actionNames.size()+"="+nextNodes.size());

        for(int i=0;i<actionNames.size();i++)
        {
            nextNodes.get(i).addMove(actionNames.get(i));
            nextNodes.get(i).setMove();

            nextNodes.get(i).adjustMoveProb();
        }

    }

    public void newPrintNode(){

        if(hasProb() && hasMove())
            System.out.println("<node iSet=\""+getInfoSetNumber()+"\" player=\""+playerId+"\" move=\""+getMove()+"\" prob=\""+getProbMove()+"\" >");

        else if (hasMove())
            System.out.println("<node iSet=\""+getInfoSetNumber()+"\" player=\""+playerId+"\" move=\""+getMove()+"\" >");

        else
            System.out.println("<node iSet=\""+getInfoSetNumber()+"\" player=\""+playerId+"\" >");

        for(int i=0;i<actionNames.size();i++)
            nextNodes.get(i).newPrintNode();

        if(getBoolOutcome())
            outcome.newPrintNode();

        System.out.println("</node>");
    }

    public void addActionName(String val){
        actionNames.add(val);
    }

    public void addNextNode(Node val){
        nextNodes.add(val);
    }

    public void addOutcome(Outcome outcome){

        if(getBoolOutcome())
            this.outcome=outcome;
    }
}
