package com.company;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
public class Main {

    public final static double epsilon = 0.9;

    public static void main(String[] args) throws IOException {

        LinkedList<String> statesStr = new LinkedList<>();
        LinkedList<State> states = new LinkedList<>();

        BufferedReader br = new BufferedReader(new FileReader("input.txt"));
        String line = br.readLine();
        while(line != null) {
            String[] strs = line.trim().split("/");
            String state = strs[0];
            if (!statesStr.contains(state)) {
                statesStr.add(state);
                State newState = new State(state);
                states.add(newState);

            }
            line = br.readLine();
        }


        BufferedReader br1 = new BufferedReader(new FileReader("input.txt"));
        String line1 = br1.readLine();

        while (line1 != null) {
//            System.out.println(line1);
            String[] strs1 = line1.trim().split("/");
            String state = strs1[0];
            String action = strs1[1];
            String resultState = strs1[2];
//            System.out.println(resultState);
            String probabilityStr = removeLastChar(strs1[3]);
            double probability = Double.parseDouble(probabilityStr);
            //this is to add the "IN" state to the list because it doesn't show up in the first strs[0] column on input.txt
            if(!statesStr.contains(resultState)){
                statesStr.add(resultState);
                State newState = new State(resultState);
                states.add(newState);
            }else if (getState(states, state).checkAction(action)) {
                State currentState = (getState(states, state));
                currentState.addAction(action);
            }
            if(!getState(states, state).getAction(action).getActionStates().contains(getState(states, resultState))) {
                Action currentAction = getState(states, state).getAction(action);
                State result = getState(states, resultState);
                currentAction.addPair(result, probability, 0);
            }
            line1 = br1.readLine();
        }


//        for(int i = 0; i<states.size(); i++){
//            System.out.println(states.get(i).getName());
//            System.out.println("///////////");
//            for(int j = 0; j<states.get(i).getActions().size(); j++){
//                System.out.println(states.get(i).getActions().get(j).getName());
//                System.out.println("Action: " + states.get(i).getActions().get(j).getName() + " has these value pairs:");
//                states.get(i).getActions().get(j).printPairs();
//            }
//            System.out.println("///////////");
//
//        }

        Model model = new Model(states);
        model.learnModel();
        System.out.println("////////////////////////");
        model.learnModelFree();

    }

    //helper function to get a state from a linked list based on the name given.
    public static State getState(LinkedList<State> states, String name){
        for(int i = 0; i<states.size(); i++) {
            if (name.equals(states.get(i).getName())) {
                return states.get(i);
            }
        }
        return null;
    }

    //helper function to remove the "\" character at the end of each probability
    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    private void printSetup(){

    }

}
