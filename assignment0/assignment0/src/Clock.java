// package assignment0;

public class Clock {
    private int currentTime;
    private final int offset;

    // initializes the clock
    Clock(final int offset) {
        this.offset = offset;
        this.currentTime = 0;
    }

    /**
     * @return: the next timeStamp depending on 
     * the value of offset to skip between two timeStamps
     */
    public int nextTimeStamp() {
        this.currentTime += this.offset;
        return this.currentTime;
    }
}