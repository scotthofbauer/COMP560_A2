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
//                if(tempState == null) {
//
//                    while (tempState == null) {
//                        action = state.chooseAction();
//                        tempState = action.chooseResultState();
//
//                    }
//                }
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
        while(count <5000) {
            System.out.println("ITERATION: " + count);
            State state = this.getState("Fairway");
            Action action = null;
            int score = 1;
//            updateAllUtilities();
//            printUtilities();

            LinkedList<Move> episode = new LinkedList<>();
            while (!state.getName().equals("In")) {
                String firstStateName = state.getName();
                action = state.chooseAction();
                action.updateCount();
                state = action.chooseResultState();
                episode.add(new Move(getState(firstStateName), action, state));
                score++;
            }
            printEpisode(episode);
            addScoresToStates(episode, score);

            System.out.println("Hopefully we make it in the hole! with score: " + (score-1));
            System.out.println();
            count++;
        }
        printPolicies();
    }

    private void addScoresToStates(LinkedList<Move> episode, int score){
        for(int i=  0; i<episode.size(); i++){
            episode.get(i).getAction().addScore(score);
        }
    }

    private void printEpisode(LinkedList<Move> episode){
        for(int i = 0; i<episode.size(); i++){
            System.out.println(episode.get(i).toString());
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


    private void printPolicies(){
        System.out.println("//////////////");
        for(int i = 0; i<states.size(); i++){
            states.get(i).setPolicyByScore();
            System.out.println("State: " + states.get(i).getName() + ", Utility: " + ", Policy: " + states.get(i).getPolicy());
        }
    }



}
