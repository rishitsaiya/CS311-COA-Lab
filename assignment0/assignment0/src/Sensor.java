// package assignment0;

public class Sensor {
    private boolean isTurnedOn;
    private Coin coin;

    Sensor(final double probabilityOfHeads) {
        this.coin = new Coin(probabilityOfHeads);
        this.isTurnedOn = false;
    }

    /**
     * restarts the cell, to be switched on/ off depending
     * on if the result of flipping of biased coin is Heads/ Tails
     */
    public void restart() {
        this.isTurnedOn = this.coin.toss();
    }

    /**
     * @return: boolean representing if the Sensor is
     * active at the given time
     */
    public boolean isActive() {
        return this.isTurnedOn;
    }

    /**
     * Method that represents string representation of the object
     */
    public String toString() {
        return String.valueOf(this.isTurnedOn);
    }
}