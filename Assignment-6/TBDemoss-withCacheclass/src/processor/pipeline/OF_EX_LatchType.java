package processor.pipeline;

public class OF_EX_LatchType {
	
	boolean EX_enable;
	String opcode;
	int rs1,rs2,rd,imm;
	int rs1addr,rs2addr;
	int insPC;
	boolean isNop;
	boolean isBusy;
	boolean NOP;
	
	public OF_EX_LatchType() {
		EX_enable = false;
		opcode = "70000";
		rs1 = 70000;
		rs2 = 70000;
		rd = 70000;
		imm = 70000;
		insPC = -1;
		isNop = false;
		rs1addr = 45;
		rs2addr = 45;
		isBusy = false;
	}

	public String toString() {
		return "OF_EX_LatchType";
	}

	public boolean comparePC (int pc) {
		return insPC == pc;
	}

	public boolean isEX_enable() {
		return EX_enable;
	}

	public void setEX_enable(boolean eX_enable) {
		EX_enable = eX_enable;
	}
	
	public boolean getIsNOP() {
		return NOP;
	}
	
	public void setIsNOP(boolean is_NOP) {
		NOP = is_NOP;
	}
}
