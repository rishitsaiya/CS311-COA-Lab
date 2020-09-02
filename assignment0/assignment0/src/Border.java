// package assignment0;

public class Border {
    private int length;
    private int breadth;
    private double probabilityOfHeads;
    private Sensor[][] cells;

    Border(int borderLength, int borderBreadth, double probabilityOfHeads) {
        this.length = borderLength;
        this.breadth = borderBreadth;
        this.probabilityOfHeads = probabilityOfHeads;
        this.cells = new Sensor[this.breadth][this.length];

        // initialize each cell
        for (int i = 0; i < this.breadth; i += 1) {
            for (int j = 0; j < this.length; j += 1)
                this.cells[i][j] = new Sensor(this.probabilityOfHeads);
        }
    }

    /**
     * updates all the Sensort (flips them or keeps them as it is 
     * using the biased coin probability provided)
     */
    public void updateSensorStates() {
        for (int i = 0; i < this.breadth; i += 1) {
            for (int j = 0; j < this.length; j += 1)
                this.cells[i][j].restart();
        }
    }

    /**
     * Returns if cell in border represented by coordinates x, y is active 
     * @param x: the height 
     * @param y: the width
     * @return: Boolean denoting if cell represented by x, y is active
     */

    public boolean isActive(int x, int y) {
        // we will try to stop Infiltrator from going back to attacking country
        // by assuming each area in attacking country is equivalent to a cell
        // turned on
        if (x < 0 || y < 0)
            return true;
        // we will encourate the Infiltrator to enter defending country by
        // assuming each area in defending country is equivalent to a cell
        // turned off
        else if (y >= this.length || x >= this.breadth)
            return false;

        return cells[x][y].isActive();
    }

    /**
     * Returns if the given coordinate belongs to the Defending country
     * @param x: the height 
     * @param y: the width
     * @return: Boolean denoting if the cell representing by x, y belongs 
     * to the defending country
     */
    public boolean belongsToDefendingCountry(int x, int y) {
        return x >= this.breadth;
    }

    /**
     * Method that represents string representation of the object
     */
    public String toString() {
        String res = new String();

        for (int i = 0; i < this.breadth; i += 1) {
            for (int j = 0; j < this.length; j += 1)
                res += this.cells[i][j].toString() + " ";
            res += '\n';
        }

        return res;
    }
}