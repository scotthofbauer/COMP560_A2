package com.company;

import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.Collections;


public class State {
//    private String actions[];
    private String name;
    private double prob = 0.0;
    private LinkedList<State> resultStates = new LinkedList<State>();
    private LinkedList<Action> actions = new LinkedList<>();
    private Map<Action, Double> utilityMap = new HashMap<>();
    private double utility = 0;
    private static final double reward = 1.0;
    private static final double discount = 0.9;
    private String policy;
    private double count;

    public State(String name){
        this.name = name;
        this.policy = "";
        this.count = 0;
    }


    public double getProb() {
        return prob;
    }
    public void setProb(double prob) {
        this.prob = prob;
    }
    public LinkedList<State> getResultStates() {
        return resultStates;
    }
    public void setResultStates(LinkedList<State> resultStates) {
        this.resultStates = resultStates;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPolicy() {
        return this.policy;
    }

    public void setPolicy(String policy) {
        this.policy = policy;
    }

    public double getUtility() {
        return this.utility;
    }
    public void setUtility(double utility) {
        this.utility = utility;
    }

    public void setUtility() {


        Action minKey = null;
        double minValue = Double.MAX_VALUE;
        for (Action key : this.utilityMap.keySet()) {
            double value = this.utilityMap.get(key);
            if (value < minValue) {
                minValue = value;
                minKey = key;
            }
        }
//        this.utility = Collections.min(this.utilityMap.values());
        this.utility = minValue;
        if(minKey == null){
            System.out.println("MinKey is nul");
        }
        this.policy = minKey.getName();

    }
    public void addState(State newState){
        this.resultStates.add(newState);
    }
    public LinkedList<Action> getActions() {
        return this.actions;
    }
    public void setActions(LinkedList<Action> actions) {
        this.actions = actions;
    }
    public void addAction(String name){
        this.actions.add(new Action(name));
    }
    public double getCount() {
        return this.count;
    }
    public void setCount(double count) {
        this.count = count;
    }
    public void addCount(){
        this.count++;
        setCount(count);
    }

    public Action getAction(String name){
        for(int i = 0; i<this.actions.size(); i++) {
            if (name.equals(this.actions.get(i).getName())) {
                return this.actions.get(i);
            }
        }
        return null;
    }
    public Boolean checkAction(String name){
        if(this.actions.size() == 0){return true; }
        for(int i = 0; i<this.actions.size(); i++) {
            if (name.equals(this.actions.get(i).getName())) {
                return false;
            }
        }
        return true;
    }

    public Action chooseAction(){
        Random rand = new Random();
        int x = rand.nextInt(this.actions.size());
        return this.actions.get(x);
    }


    public void updateUtility(){
        for(int i = 0; i<this.actions.size(); i++){
            LinkedList<ActionState> actionStates = this.actions.get(i).getActionStates();
            double sum = 0;
            for (ActionState actionState : actionStates) {
                double stateUtility = actionState.getState().getUtility();
                double value = actionState.getProbability();
                sum += (stateUtility * value);
            }
            double temp = reward + discount * sum;
            this.utilityMap.put(this.actions.get(i), temp);
        }
        if(this.utilityMap.size() !=0){
            this.setUtility();
        }
    }

    public void updateProbability(){
        for(Action action: this.actions){
            action.updateProbability();
        }
    }

    public void setPolicyByScore(){
        double min = Double.MAX_VALUE;
        for(int i = 0; i<this.actions.size(); i++){
            if(this.actions.get(i).getAverage() < min){
                min = this.actions.get(i).getAverage();
                this.setPolicy(this.actions.get(i).getName());
            }
        }

    }






}
