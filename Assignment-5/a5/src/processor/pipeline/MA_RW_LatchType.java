package processor.pipeline;

public class MA_RW_LatchType {
	
	boolean RW_enable;
	int alures;
	int ldres;
	int rd;
	int instruction ;
	
	public MA_RW_LatchType()
	{
		RW_enable = false;
	}

	public boolean isRW_enable() {
		return RW_enable;
	}

	public void setRW_enable(boolean rW_enable) {
		RW_enable = rW_enable;
	}
	
	public void setalures(int al){
		alures = al;
	}

	public void setldres(int ld){
		ldres = ld;
	}
	
	public int getalures(){
		return alures;
	}

	public int getldres(){
		return ldres;
	}
	
	public void setrd(int r){
		this.rd = r;
	}
	
	public int getrd(){
		return rd;
	}
	public int getInstruction() {
		return instruction;
	}

	

	public void setInstruction(int instruction2) {
		this.instruction = instruction2;
		
	}
}
