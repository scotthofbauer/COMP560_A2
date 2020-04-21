package com.company;

import java.util.*;

public class Action {

    private String name;
    private double resultProbability;
    private LinkedList<ActionState> actionStates;
    private LinkedList<Integer> scores = new LinkedList<>();
    private double count;
    private ActionState resultActionState;

    public Action(String name){
        this.name = name;
        this.actionStates = new LinkedList<>();
    }
    public String getName() {
        return this.name;
    }
    public LinkedList<ActionState> getActionStates() {
        return this.actionStates;
    }
    public void addPair(State state, double prob, double count){
        this.actionStates.add(new ActionState(state, prob, count));
    }
    public void printPairs(){
        for (ActionState actionState : this.actionStates) {
            State state = actionState.getState();
            Double prob = actionState.getProbability();
            Double count =  actionState.getCount();
            System.out.println(state.getName() + " , Prob: " + prob + " , Count: " + count);
        }
    }

    //Okay so this is a kinda fucked way of picking based on each resulting state probability
    //So we have a map of (State, Probability) pairs and we have to shuffle them so that they don't get iterrated the same way everytime
    //Then we randomly pick a probability from 0-1 (p) and set the cumulative total to 0.
    //Then we iterate over each State and add each states probability to the totalprobabilty, once the total probability is greater than the random probability
    //We return the most recent State that was added to totalProbability
    //This was the best way I could think of, sorry for what is the overly complicated way of doing it but I'm no genius :)

    public State chooseResultState(){
        Random rand = new Random();
        double p = Math.random();
        double totalProbability = 0.0;
        Collections.shuffle(this.actionStates);
        for (ActionState actionState : this.actionStates) {
            totalProbability += actionState.getProbability();
            if (p <= totalProbability) {
                State returnState = actionState.getState();
                this.resultProbability = actionState.getProbability();
//                actionState.addCount();
                this.setResultActionState(actionState);
                return returnState;
            }
        }
        //returns null
//        System.out.println("Return null on ChooseResultState");
        return null;
    }

    public void setResultActionState(ActionState actionState){
        this.resultActionState = actionState;
    }

    public void updateResultActionState(){
        this.resultActionState.addCount();
    }


    public void updateProbability(){
        for (ActionState actionState : this.actionStates) {
           actionState.updateProbability(this.count);
        }
    }

    public double getResultProbability() {
        return this.resultProbability;
    }

    public void updateCount(){
        this.count++;
    }

    public void addScore(int score){
        this.scores.add(score);
    }

    public double getAverage(){
        double sum = 0;
        for(int i = 0; i<this.scores.size(); i++){
            sum += this.scores.get(i);
        }
        return sum/this.count;
    }




}
