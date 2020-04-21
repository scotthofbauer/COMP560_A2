package com.company;

import java.util.*;

public class Action {
    private static final double DISCOUNT = 0.9;
    private static final double ALPHA = 0.2;
    private static final double REWARD = 1.0;
    public static final double EXPLORATION_VALUE = 0.2;
    private String name;
    private double resultProbability;
    private LinkedList<ActionState> actionStates;
    private LinkedList<Integer> scores = new LinkedList<>();
    private double count;
    private ActionState resultActionState;
    private double functionValue = 0.0;

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

    //Okay so this is a kinda screwy way of picking based on each resulting state probability
    //So we have a map of (State, Probability) pairs and we have to shuffle them so that they don't get iterrated the same way everytime
    //Then we randomly pick a probability from 0-1 (p) and set the cumulative total to 0.
    //Then we iterate over each State and add each states probability to the totalprobabilty, once the total probability is greater than the random probability
    //We return the most recent State that was added to totalProbability
    //This was the best way I could think of, sorry for what is the overly complicated way of doing it but I'm no genius :)
    public State chooseResultState(){
        Random rand = new Random();
        double p = Math.random();
        Collections.shuffle(this.actionStates);
        if(p<EXPLORATION_VALUE){
            int x = rand.nextInt(this.actionStates.size());
            State returnState = actionStates.get(x).getState();
            this.resultProbability = actionStates.get(x).getProbability();
            this.setResultActionState(this.actionStates.get(x));
            return returnState;
        }else{
            double totalProbability = 0.0;

            for (ActionState actionState : this.actionStates) {
                totalProbability += actionState.getProbability();
                if (p <= totalProbability) {
                    State returnState = actionState.getState();
                    this.resultProbability = actionState.getProbability();
                    this.setResultActionState(actionState);
                    return returnState;
                }
            }
        }
        return null;
    }

    public State chooseRandomResultState(){
        Random rand = new Random();
        int x = rand.nextInt(this.getActionStates().size());
        return this.getActionStates().get(x).getState();
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


    public double getFunctionValue() {
        return this.functionValue;
    }

    public void setFunctionValue(double functionValue) {
        this.functionValue = functionValue;
    }

    public void calculateFunctionValue(Action newAction){
        functionValue = newAction.getFunctionValue() + ALPHA*(REWARD + DISCOUNT*this.functionValue - newAction.getFunctionValue());
        this.setFunctionValue(functionValue);
    }


}
