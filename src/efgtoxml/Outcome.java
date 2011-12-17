package efgtoxml;

import java.util.ArrayList;

/**
 *
 * @author Avinash Thummala
 */
public class Outcome extends Node{

    ArrayList<String> payoffs;
    ArrayList<String> players;

    public void newPrintNode(){

        if(getInfoSetNumber()!=0)
        {
            if(hasMove())
                System.out.println("<outcome move=\""+getMove()+"\" >");
            else
                System.out.println("<outcome>");

            for(int i=0;i<payoffs.size();i++)
                System.out.println("<payoff player=\""+(i+1)+"\" value=\""+payoffs.get(i)+"\" />");

            System.out.println("</outcome>");
        }
    }

    public Outcome(String nodeName, int infoSetNumber, String outcomeName, ArrayList<String> playerNames){

        setNodeName(nodeName);
        setInfoSetNumber(infoSetNumber);
        setInfoSetName(outcomeName);
        players=playerNames;

        payoffs=new ArrayList();
    }

    public void addPayoff(String val){
        payoffs.add(val);
    }

    public boolean isFull(){
        return true;
    }

    public void addNextNode(Node node){
        
    }

    public void addOutcome(Outcome outcome){
        
    }

    public void adjustMoveProb(){
        
    }
}
