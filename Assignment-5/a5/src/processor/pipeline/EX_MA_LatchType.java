package processor.pipeline;

public class EX_MA_LatchType {
	
	boolean MA_enable;
	int aluResult;
	int rs1,rs2,rd,imm;
	int rs1addr,rs2addr;
	String opcode;
	int insPC;
	boolean isNop;
	boolean isBusy;
	
	public EX_MA_LatchType()
	{
		MA_enable = false;
		opcode = "70000";
		rs1 = 70000;
		rs2 = 70000;
		rd = 70000;
		imm = 70000;
		aluResult = 70000;
		insPC = -1;
		isNop = false;
		rs1addr = 45;
		rs2addr = 45;
		isBusy = false;
	}

	public boolean isMA_enable() {
		return MA_enable;
	}

	public void setMA_enable(boolean mA_enable) {
		MA_enable = mA_enable;
	}

}
