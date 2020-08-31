package assignment0;

public class Infiltrator {
    private int currentX;
    private int currentY;
    private boolean reachedDefendingCountry;
    private Border border;

    Infiltrator(int borderLength, int borderBreadth, double probabilityOfHeads) {
        // assuming the Inflator starts from coordinate (0, 0)
        this.currentX = 0;
        this.currentY = 0;
        this.reachedDefendingCountry = false;
        this.border = new Border(borderLength, borderBreadth, probabilityOfHeads);
    }

    // Checks if Infiltrator has reached the defending country
    public boolean hasReachedDefendingCountry() {
        return this.reachedDefendingCountry;
    }

    /**
     * For the Infiltrator to make next move, considering the active state
     * of its all eight neighbours
     */
    public void moveNext() {
        // if Infiltrator has already reached the defending country, no need to move
        if (this.hasReachedDefendingCountry())
            return;

        // get details about all 8 neighbouring cells
        bool neighbours[][] = new bool[3][3];
        for (int i = -1; i < 2; i += 1) {
            for (int j = -1; j < 2; j += 1) {
                neighbours[i+1][j+1] = this.border.isActive(currentX + i, currentY + j);
            }
        }

        // make a move and update currentX, currentY
        // to be updated, till then throws error
        // implementing a simple foolish move
        for (int i = 1; i >= -1; i -= 1) {
            for (int j = 1; j >= -1; j -= 1) {
                if (i == j)
                    continue;
                if (neighbours[i][j] == false) {
                    this.currentX += i;
                    this.currentY += j;
                    i = -2; j = -1;
                }
            }
        }

        // check if it has reached the Defending Country
        this.reachedDefendingCountry = this.border.belongsToDefendingCountry(currentX, currentY);
    }

    public String getCoordinates() {
        return String.valueOf(this.currentX) + "," + String.valueOf(this.currentY);
    }
}