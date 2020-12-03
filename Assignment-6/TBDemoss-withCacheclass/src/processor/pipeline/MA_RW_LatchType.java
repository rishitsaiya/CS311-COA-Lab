package processor.pipeline;

public class MA_RW_LatchType {
	
	boolean RW_enable;
	int aluResult;
	int rs1,rs2,rd,imm;
	int rs1addr,rs2addr;
	String opcode;
	int insPC;
	boolean isLoad;
	boolean isNop;
	
	public MA_RW_LatchType()
	{
		RW_enable = false;
		opcode = "70000";
		rs1 = 70000;
		rs2 = 70000;
		rd = 70000;
		imm = 70000;
		aluResult = 70000;
		insPC = -1;
		isLoad =  false;
		isNop = false;
		rs1addr = 45;
		rs2addr = 45;
	}

	public boolean isRW_enable() {
		return RW_enable;
	}

	public void setRW_enable(boolean rW_enable) {
		RW_enable = rW_enable;
	}

}
