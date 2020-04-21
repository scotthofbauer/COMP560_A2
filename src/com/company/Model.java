package com.company;

import java.util.LinkedList;
import java.util.Random;

public class Model {

    private LinkedList<State> states;

    public Model(LinkedList<State> states){
        this.states = states;
    }

    public void learnModel(){
        int count = 1;
        double totalChosen = 0.0;
        while(count <5000) {
            System.out.println("ITERATION: " + count);
//            printResults();
            State state = this.getState("Fairway");
            Action action = null;


            while (!state.getName().equals("In")) {

                String firstStateName = state.getName();
                action = state.chooseAction();

                State tempState = action.chooseResultState();
                if(tempState == null) {

                    while (tempState == null) {
                        action = state.chooseAction();
                        tempState = action.chooseResultState();

                    }
                }
                action.updateCount();
                action.updateResultActionState();
                state = action.chooseResultState();

                System.out.println(firstStateName+"/"+action.getName()+"/"+state.getName()+"/"+action.getResultProbability());

            }
            System.out.println("Hopefully we make it in the hole!: " + state.getName());
            if(count%1000==0){
                System.out.println("RECALCULATING");
                updateAllProbability();
            }
            updateAllUtilities();
            System.out.println();
            count++;
        }

        System.out.println("////////////////////////////////");
        printAllProbabilies();
        System.out.println();
        System.out.println("FINAL RESULTS: ");
        printResults();
    }


    public void learnModelFree(){
        int count = 1;
        while(count <100) {
            System.out.println("Episode: " + count);
            int score = 1;
            //Initialize S, Q(S,A) is already initialized to zero by creating the actions.
            State state = this.getState("Fairway");
            //choose A(S) using eplison greedy from Q
            Action action = state.chooseActionGreedyQ();
            while (!state.getName().equals("In")) {
                //take action A, get reward, S'
                State newState = action.chooseRandomResultState();
                System.out.println(state.getName() + "/" + action.getName() + "/" + newState.getName() + " " + action.getFunctionValue());
                System.out.println("");
                if(newState.getName().equals("In")){break;}
                //choose A'(S') using epsilon greedy from Q
                Action newAction = newState.chooseActionGreedyQ();
                //Q(s,a) -> Q(s,a) + alpha[Reward + gamma*Q(s',a') - Q(s,a)
                action.calculateFunctionValue(newAction);
                //A -> a'
                action = newAction;
                state = newState;
                score++;
            }
            System.out.println("Hopefully we make it in the hole! with score: " + (score));
            System.out.println();
            count++;
        }
        setModelFreePolicies();
//        printFunctionValues();
    }

    private void setModelFreePolicies(){
        for(int i=  0; i<states.size(); i++){
            states.get(i).setPolicyByFunctionValue();
            System.out.println(states.get(i).getName() + ", Policy: " + states.get(i).getPolicy() + " " + states.get(i).getUtility());
        }
    }

    private void printFunctionValues(){
        for(int i = 0; i<states.size(); i++){
            System.out.println("State: " + states.get(i).getName());
            System.out.println("////////");
            states.get(i).printFunctionValues();
        }

    }



    private void updateAllProbability(){
        for(int i = 0; i<states.size(); i++){
            states.get(i).updateProbability();
        }
    }

    private void updateAllUtilities(){
        for(int i = 0; i<states.size(); i++){
            states.get(i).updateUtility();
        }
    }

    private void printUtilities(){
        for(int i = 0; i<states.size(); i++){
            System.out.println("State: " + states.get(i).getName() + ", Utility Value: " + states.get(i).getUtility() + ", Policy: " + states.get(i).getPolicy());
        }
    }

    private void printAllProbabilies(){
        for(int i = 0; i<states.size(); i++){
            System.out.println(states.get(i).getName());
            System.out.println("///////////");
            for(int j = 0; j<states.get(i).getActions().size(); j++){
                System.out.println(states.get(i).getActions().get(j).getName());
                System.out.println("Action: " + states.get(i).getActions().get(j).getName() + " has these value pairs:");
                states.get(i).getActions().get(j).printPairs();
            }
            System.out.println("///////////");

        }
    }


    private void printResults(){
        System.out.println("//////////////");
        for(int i = 0; i<states.size(); i++){
            states.get(i).updateUtility();
            System.out.println("State: " + states.get(i).getName() + ", Utility: " + states.get(i).getUtility()  + ", Policy: " + states.get(i).getPolicy());
        }
        System.out.println("//////////////");
    }


    //helper function to get a specific state from the array of possible states in the model
    private State getState(String state){
        for(int i = 0; i<this.states.size(); i++){
            if(this.states.get(i).getName().equals(state)){
                return this.states.get(i);
            }
        }
        //no state is contained in this model
        return null;
    }


//    private void printPolicies(){
//        System.out.println("//////////////");
//        for(int i = 0; i<states.size(); i++){
//            states.get(i).setPolicyByScore();
//            System.out.println("State: " + states.get(i).getName() + ", Utility: " + ", Policy: " + states.get(i).getPolicy());
//        }
//    }



}
