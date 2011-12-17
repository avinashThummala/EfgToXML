package efgtoxml;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


public class Main {


    ArrayList<String> playerNames;
    Node presentNode;

    Main(String[] args){

        if(args.length!=1)
        {
            System.out.println("Bad usage: Have to provide \"efg\" file");
            return;
        }

        playerNames=new ArrayList();
        presentNode=null;
        readEFGFile(args[0]);
    }
    
    public void addPlayerNames(String firstLine)
    {
        //All we need is the name of Two players. They will be inside brackets.

        int firstIndex=firstLine.indexOf('{');
        int secondIndex=firstLine.indexOf('}');

        String playerNames[]=firstLine.substring(firstIndex+1, secondIndex).split("\"*\"");

        for(int i=0;i<playerNames.length;i++)
            if(!(playerNames[i].length()==1 && playerNames[i].charAt(0)==' '))
                this.playerNames.add(playerNames[i]);
    }

    public void readChoiceNode(String line){

        String nodeName;

        Pattern pattern=Pattern.compile("\"[:(,)a-zA-Z 0-9]*\"");
        Matcher matcher=pattern.matcher(line.substring(2));
        matcher.find();

        String temp=matcher.group();
        nodeName=temp.substring(1,temp.length()-1);

        int pos=1+temp.length()+1;
        int newPos=line.indexOf('\"', pos);
        int infoSetNumber=Integer.parseInt(line.substring(pos+1, newPos-1));

        Pattern newPattern=Pattern.compile("\"[:(,)a-zA-Z 0-9]*\"");
        matcher=newPattern.matcher(line.substring(newPos));
        matcher.find();

        temp=matcher.group();
        String infoSetName=temp.substring(1,temp.length()-1);

        ChoiceNode choiceNode=new ChoiceNode(nodeName, infoSetNumber, infoSetName);

        pos=line.indexOf('{');

        //Lets assume that Actions don't have spaces. Even that case can be handled quite easily

        String actionProbs[]=line.substring(pos+2, line.indexOf('}')-1).split(" ");
        int length=actionProbs.length;

        for(int i=0;i<length;i+=2)
            choiceNode.addActionName(actionProbs[i].substring(1, actionProbs[i].length()-1));

        for(int i=1;i<length;i+=2)
           choiceNode.addProb(actionProbs[i]);

        if(presentNode==null)
        {
            choiceNode.setUpNode(null);
            presentNode=choiceNode;
        }
        else
        {
            //Put this node in a free slot
            //How to find a free slot?
            while(presentNode.getUpNode()!=null && presentNode.isFull())
                presentNode=presentNode.getUpNode();

            presentNode.addNextNode(choiceNode);
            choiceNode.setUpNode(presentNode);

            presentNode=choiceNode;

        }

        pos=line.indexOf('}')+2;

        if(line.charAt(pos)=='0')
            presentNode.setBoolOutcome(false);
        else{

            presentNode.setBoolOutcome(true);

            newPos=line.indexOf('\"',pos);
            int outcomeInfoSetNumber=Integer.valueOf(line.substring(pos, newPos-1));

            pattern=Pattern.compile("\"[:(,)a-zA-Z 0-9]*\"");
            matcher=pattern.matcher(line.substring(newPos-1));
            matcher.find();

            temp=matcher.group();
            String outcomeInfoSetName=temp.substring(1,temp.length()-1);

            Outcome outcome=new Outcome("", outcomeInfoSetNumber, outcomeInfoSetName, playerNames);

            pos=line.lastIndexOf('{');

            //Lets assume that Actions don't have spaces. Even that case can be handled quite easily

            String payoffs[]=line.substring(pos+2, line.lastIndexOf('}')-1).split(" ");
            length=payoffs.length;

            int i=0;
            for(i=0;i<length-1;i++)
                outcome.addPayoff(payoffs[i].substring(0, payoffs[i].length()-1));

            outcome.addPayoff(payoffs[i]);

            presentNode.addOutcome(outcome);
        }

    }

    public void readPlayerNode(String line){

        String nodeName;

        Pattern pattern=Pattern.compile("\"[:(,)a-zA-Z 0-9]*\"");
        Matcher matcher=pattern.matcher(line.substring(2));
        matcher.find();

        String temp=matcher.group();
        nodeName=temp.substring(1,temp.length()-1);

        int pos=1+temp.length()+1;
        int newPos=line.indexOf(' ', pos+1);
        int playerNumber=Integer.parseInt(line.substring(pos+1, newPos));
        
        pos=line.indexOf(' ',newPos+1);
        int infoSetNumber=Integer.parseInt(line.substring(newPos+1, pos));

        Pattern newPattern=Pattern.compile("\"[:(,)a-zA-Z 0-9]*\"");
        matcher=newPattern.matcher(line.substring(pos));
        matcher.find();

        temp=matcher.group();
        String infoSetName=temp.substring(1,temp.length()-1);

        PlayerNode playerNode=new PlayerNode(nodeName, playerNumber, infoSetNumber, infoSetName);

        pos=line.indexOf('{');

        //Lets assume that Actions don't have spaces. Even that case can be handled quite easily

        String actionProbs[]=line.substring(pos+2, line.indexOf('}')-1).split(" ");
        int length=actionProbs.length;

        for(int i=0;i<length;i++)
            playerNode.addActionName(actionProbs[i].substring(1, actionProbs[i].length()-1));

        if(presentNode==null)
        {
            playerNode.setUpNode(null);
            presentNode=playerNode;
        }
        else
        {
            //Put this node in a free slot
            //How to find a free slot?
            while(presentNode.getUpNode()!=null && presentNode.isFull())
                presentNode=presentNode.getUpNode();

            presentNode.addNextNode(playerNode);
            playerNode.setUpNode(presentNode);

            presentNode=playerNode;

        }

        pos=line.indexOf('}')+2;
        
        if(line.charAt(pos)=='0')
            presentNode.setBoolOutcome(false);
        else{

            //System.out.println("Has outcome");

            presentNode.setBoolOutcome(true);

            newPos=line.indexOf('\"',pos);
            int outcomeInfoSetNumber=Integer.valueOf(line.substring(pos, newPos-1));

            pattern=Pattern.compile("\"[:(,)a-zA-Z 0-9]*\"");
            matcher=pattern.matcher(line.substring(newPos-1));
            matcher.find();

            temp=matcher.group();
            String outcomeInfoSetName=temp.substring(1,temp.length()-1);

            Outcome outcome=new Outcome("", outcomeInfoSetNumber, outcomeInfoSetName, playerNames);

            //System.out.println(outcomeInfoSetNumber+" "+outcomeInfoSetName);

            pos=line.lastIndexOf('{');

            //Lets assume that Actions don't have spaces. Even that case can be handled quite easily

            String payoffs[]=line.substring(pos+2, line.lastIndexOf('}')-1).split(" ");
            length=payoffs.length;

            int i=0;
            for(i=0;i<length-1;i++)
                outcome.addPayoff(payoffs[i].substring(0, payoffs[i].length()-1));

            outcome.addPayoff(payoffs[i]);

            presentNode.addOutcome(outcome);
        }
    }

    public void readOutcome(String line){

        String nodeName;

        Pattern pattern=Pattern.compile("\"[:(,)a-zA-Z 0-9]*\"");
        Matcher matcher=pattern.matcher(line.substring(2));
        matcher.find();

        String temp=matcher.group();
        nodeName=temp.substring(1,temp.length()-1);

        int pos=1+temp.length()+1;
        int newPos=line.indexOf('\"', pos);
        int infoSetNumber=Integer.parseInt(line.substring(pos+1, newPos-1));

        if(infoSetNumber!=0)
        {
            Pattern newPattern=Pattern.compile("\"[:(,)a-zA-Z 0-9]*\"");
            matcher=newPattern.matcher(line.substring(newPos));
            matcher.find();

            temp=matcher.group();
            String infoSetName=temp.substring(1,temp.length()-1);

            Outcome outcome=new Outcome(nodeName, infoSetNumber, infoSetName, playerNames);

            pos=line.indexOf('{');

            //Lets assume that Actions don't have spaces. Even that case can be handled quite easily

            String payoffs[]=line.substring(pos+2, line.indexOf('}')-1).split(" ");
            int length=payoffs.length;

            int i=0;
            for(i=0;i<length-1;i++)
                outcome.addPayoff(payoffs[i].substring(0, payoffs[i].length()-1));

            outcome.addPayoff(payoffs[i]);

            while(presentNode.getUpNode()!=null && presentNode.isFull())
                presentNode=presentNode.getUpNode();

            presentNode.addNextNode(outcome);
            
        }
        else{

            Outcome outcome=new Outcome(nodeName, infoSetNumber, "", playerNames);
            
            while(presentNode.getUpNode()!=null && presentNode.isFull())
                presentNode=presentNode.getUpNode();

            presentNode.addNextNode(outcome);            

        }

    }

    public void readEFGFile(String fileName){

        try
        {
            File file=new File(fileName);
            FileReader fr=new FileReader(file);
            BufferedReader br=new BufferedReader(fr);

            //FirstLine Handling

            if(br.ready())
            {
                String firstLine=br.readLine();
                addPlayerNames(firstLine);
            }
            else
                System.out.println("Bad format");

            while(br.ready()){

                String newLine=br.readLine();

                //Check if it is either a choicenode, player node or outcome
                if(newLine.length()>=1)
                {
                    char ch=newLine.charAt(0);

                    if(ch=='c')
                        readChoiceNode(newLine);
                    else if(ch=='p')
                        readPlayerNode(newLine);
                    if(ch=='t')
                        readOutcome(newLine);
                }
            }

            br.close();
            fr.close();

            //Get to the top node

            while(presentNode.getUpNode()!=null)
                presentNode=presentNode.getUpNode();


            presentNode.adjustMoveProb();

            System.out.println("<extensiveForm>");
            presentNode.newPrintNode();
            System.out.println("</extensiveForm>");

        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

        new Main(args);

    }

}
