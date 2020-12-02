package processor.pipeline;

import generic.Instruction;

public class EX_MA_LatchType {
	
	boolean MA_enable, MA_busy;
	int alu_result;
	boolean NOP;
	Instruction instruction;

	public EX_MA_LatchType()
	{
		MA_enable = false;
		MA_busy = false;
		NOP = false;
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
	
	public boolean getIsNOP() {
		return NOP;
	}
	
	public void setIsNOP(boolean is_NOP) {
		NOP = is_NOP;
	}

	public void setMA_busy(boolean mA_busy) {
		MA_busy = mA_busy;
	}

	public boolean isMA_busy() {
		return MA_busy;
	}

}
