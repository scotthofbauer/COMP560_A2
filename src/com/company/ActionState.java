package com.company;

public class ActionState {

    private State state;
    private double probability;
    private double count;

    public ActionState(State state, double probability, double count) {
        this.state = state;
        this.probability = probability;
        this.count = count;
    }

    public State getState() {
        return this.state;
    }

    public double getProbability() {
        return this.probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public double getCount() {
        return this.count;
    }

    public void addCount(){
        this.count++;
    }

    public void updateProbability(double total) {
        if(total == 0){
            this.setProbability(0.0);
        }else {
            System.out.println("Count: " + this.count + " , Total: " + total );
            this.setProbability(this.count / total);
        }
    }
}
