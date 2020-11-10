package processor.pipeline;

public class EX_MA_LatchType {
	
	boolean MA_enable,MA_busy;
	int aluResult;
	int op2;
	int rd;
	int instruction;
	public void setMA_busy(boolean mA_busy){
		 MA_busy=mA_busy;
	}
	public boolean isMA_busy() {
		return MA_busy;
	}

	
	public EX_MA_LatchType()
	{
		MA_enable = false;
	}

	public boolean isMA_enable() {
		return MA_enable;
	}
	public void setop2(int op){
		op2=op;
	}

	public void setMA_enable(boolean mA_enable) {
		MA_enable = mA_enable;
	}
	public void setaluRes(int a){
		aluResult=a;
		
	}
	public void setrd(int r){
		this.rd=r;
		
	}
	public int getrd(){
		return this.rd;
		
	}
	public int getaluRes(){
		return aluResult;
	}
	public int getop2(){
		return op2;
	}
	public int getInstruction() {
		return instruction;
	}

	public void setInstruction(int instruction) {
		this.instruction = instruction;
	}
}
