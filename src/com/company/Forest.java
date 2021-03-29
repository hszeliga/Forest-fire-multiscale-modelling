package com.company;

public class Forest {

    public enum Condition {
        NORMAL, FIRE, BURNT;
    }

    boolean check;
    int airHumidity;
    int wind;
    Condition condition;

    public Forest() {
        this.condition = Condition.BURNT;
        this.check =true;
        this.airHumidity =-1;
        this.wind=-1;
    }

    public Forest(Condition condition) {
        this.condition = condition;
        this.check =true;
        this.airHumidity =-1;
        this.wind=-1;
    }

    public Forest(int airHumidity, int wind, Condition condition, boolean check) {
        this.airHumidity =-1;
        this.wind =-1;
        this.condition = Condition.BURNT;
        this.check = check;
        if (this.check) {
            this.condition = condition;
            this.airHumidity = airHumidity;
            this.wind=wind;
        }
    }
}
