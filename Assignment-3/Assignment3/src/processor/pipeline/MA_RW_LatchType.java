package processor.pipeline;

import generic.Instruction;

public class MA_RW_LatchType {
	
	boolean RW_enable;
	Instruction instruction;
	int load_result;
	int alu_result;
	
	public MA_RW_LatchType() {
	
		RW_enable = false;
	}

	public MA_RW_LatchType(boolean rW_enable) {

		RW_enable = rW_enable;
	}

	public MA_RW_LatchType(boolean rW_enable, Instruction instruction) {

		RW_enable = rW_enable;
		this.instruction = instruction;
	}

	public MA_RW_LatchType(boolean rW_enable, Instruction instruction, int load_result) {

		RW_enable = rW_enable;
		this.instruction = instruction;
		this.load_result = load_result;
	}

	public MA_RW_LatchType(boolean rW_enable, Instruction instruction, int load_result, int alu_result) {

		RW_enable = rW_enable;
		this.instruction = instruction;
		this.load_result = load_result;
		this.alu_result = alu_result;
	}

	public boolean isRW_enable() {
	
		return RW_enable;
	}

	public void setRW_enable(boolean rW_enable) {
	
		RW_enable = rW_enable;
	}

	public Instruction getInstruction() {
	
		return instruction;
	}

	public void setInstruction(Instruction inst) {
	
		instruction = inst;
	}

	public void setLoad_result(int result) {
	
		load_result = result;
	}

	public int getLoad_result() {
	
		return load_result;
	}

	public int getALU_result() {
	
		return alu_result;
	}

	public void setALU_result(int result) {
	
		alu_result = result;
	}

}
