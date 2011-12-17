package efgtoxml;

import java.util.ArrayList;

/**
 *
 * @author Avinash Thummala
 */
public class ChoiceNode extends Node{

    ArrayList<String> actionNames;
    ArrayList<Node> nextNodes;    
    Outcome outcome;

    ArrayList<String> probs;

    public void adjustMoveProb(){

        for(int i=0;i<probs.size();i++)
        {
            nextNodes.get(i).setProb();
            nextNodes.get(i).addProbMove(probs.get(i));

            nextNodes.get(i).setMove();
            nextNodes.get(i).addMove(actionNames.get(i));

            nextNodes.get(i).adjustMoveProb();
        }

    }

    public void newPrintNode(){

        if(hasProb() && hasMove())
            System.out.println("<node iSet=\""+getInfoSetNumber()+"\" move=\""+getMove()+"\" prob=\""+getProbMove()+"\" >");

        else if(hasMove())
            System.out.println("<node iSet=\""+getInfoSetNumber()+"\" move=\""+getMove()+"\" >");

        else
            System.out.println("<node iSet=\""+getInfoSetNumber()+"\" >");

        for(int i=0;i<probs.size();i++)
            nextNodes.get(i).newPrintNode();

        if(getBoolOutcome())
            outcome.newPrintNode();

        System.out.println("</node>");
    }

    public ChoiceNode(String nodeName, int infoSetId, String infoSetName){

        setNodeName(nodeName);
        setInfoSetNumber(infoSetId);
        setInfoSetName(infoSetName);
        
        actionNames=new ArrayList();
        probs=new ArrayList();

        nextNodes=new ArrayList();
    }

    public boolean isFull(){
        
        if(actionNames.size()==nextNodes.size())
            return true;
        else
            return false;
    }


    public void addActionName(String val){
        actionNames.add(val);
    }

    public void addNextNode(Node val){
        nextNodes.add(val);
    }

    public void addProb(String val){
        probs.add(val);
    }

    public void addOutcome(Outcome outcome){

        if(getBoolOutcome())
            this.outcome=outcome;
    }

}
