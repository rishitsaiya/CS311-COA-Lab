// package assignment0;
import java.lang.Math;

// a biased coin whose biasness can be provided from constructor
public class Coin {

	public double probabilityOfHeads;

	Coin(final double probabilityOfHeads) {
		// check for probability values
		assert probabilityOfHeads > 0 && probabilityOfHeads < 1: "Probability Out Of Bounds";
		this.probabilityOfHeads = probabilityOfHeads;
	}

	/*
	* Return a boolean value if the biased coin lands up in heads
	*/
	public boolean toss() {
		return (boolean)(Math.random() < this.probabilityOfHeads);
	}

}