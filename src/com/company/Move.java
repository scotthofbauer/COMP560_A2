package com.company;

public class Move {

    private State firstState;
    private Action action;
    private State resultState;

    public Move (State state1, Action action, State state2){
        this.firstState = state1;
        this.action = action;
        this.resultState = state2;
    }

    public State getFirstState() {
        return firstState;
    }

    public void setFirstState(State firstState) {
        this.firstState = firstState;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public State getResultState() {
        return resultState;
    }

    public void setResultState(State resultState) {
        this.resultState = resultState;
    }

    @Override
    public String toString(){
        return this.getFirstState().getName() + "/" + this.getAction().getName() + "/" + this.getResultState().getName();
    }

}
