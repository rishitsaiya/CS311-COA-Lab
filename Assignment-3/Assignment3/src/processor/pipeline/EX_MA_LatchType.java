package processor.pipeline;

import generic.Instruction;

public class EX_MA_LatchType {
	
	boolean MA_enable;
	int alu_result;
	Instruction instruction;

	public EX_MA_LatchType() {
		MA_enable = false;
	}

	public EX_MA_LatchType(boolean mA_enable) {
		MA_enable = mA_enable;
	}

	public EX_MA_LatchType(boolean mA_enable, int alu_result) {
		MA_enable = mA_enable;
		this.alu_result = alu_result;
	}

	public EX_MA_LatchType(boolean mA_enable, int alu_result, Instruction instruction) {
		MA_enable = mA_enable;
		this.alu_result = alu_result;
		this.instruction = instruction;
	}

	public boolean isMA_enable() {
		return MA_enable;
	}

	public void setMA_enable(boolean mA_enable) {
		MA_enable = mA_enable;
	}

	public Instruction getInstruction() {
		return instruction;
	}

	public void setInstruction(Instruction inst) {
		instruction = inst;
	}

	public int getALU_result() {
		return alu_result;
	}

	public void setALU_result(int result) {
		alu_result = result;
	}

}
