package processor.pipeline;

public class OF_EX_LatchType {
	
	boolean EX_enable,EX_busy;
	int immx;
	int branchtarget;
	int operand1;
	int operand2;
	int rd;
	int instruction;
	public void setEX_busy(boolean eX_busy){
		 EX_busy=eX_busy;
	}
	public boolean isEX_busy() {
		return EX_busy;
	}

	
	public OF_EX_LatchType()
	{
		EX_enable = false;
	}

	public boolean isEX_enable() {
		return EX_enable;
	}

	public void setEX_enable(boolean eX_enable) {
		EX_enable = eX_enable;
	}

	public void setimmx(int imm) {
		this.immx = imm;
	}
	
	public int getimmx() {
		return immx;
	}
	
	public void setbranchtarget(int bt) {
		this.branchtarget = bt;
	}
	
	public int getbranchtarget() {
		return branchtarget;
	}
	
	public void setoperand1(int op1) {
		this.operand1 = op1;
	}
	public void setrd(int r) {
		this.rd = r;
	}
	public int getrd() {
		return this.rd;
	}
	
	public int getoperand1() {
		return operand1;
	}
	
	public void setoperand2(int op2) {
		this.operand2 = op2;
	}
	
	public int getoperand2() {
		return operand2;
	}	
	
	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}
}
