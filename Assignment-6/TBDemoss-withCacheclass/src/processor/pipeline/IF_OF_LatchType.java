package processor.pipeline;

public class IF_OF_LatchType {
	
	boolean OF_enable;
	int instruction;
	int insPC;
	boolean isBusy;
	
	public IF_OF_LatchType() {
		OF_enable = false;
		insPC = -1;
		instruction = -1999;
		isBusy = false;
	}
	
	public IF_OF_LatchType(boolean oF_enable, boolean isBusy) {
		OF_enable = oF_enable;
		insPC = -1;
		instruction = -1999;
		this.isBusy = isBusy;
	}

	public boolean isOF_enable() {
		return OF_enable;
	}

	public void setOF_enable(boolean oF_enable) {
		OF_enable = oF_enable;
	}

	public boolean checkInstruction(int instruction) {
		return this.instruction == instruction;
	}

	public boolean checkPC(int pc) {
		return insPC == pc;
	}

	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}

}
