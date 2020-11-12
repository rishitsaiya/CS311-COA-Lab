package processor.pipeline;

public class EX_IF_LatchType {
	
	boolean isBranchTaken;
	int offset;
	boolean IS_enable;
	int PC;
	
	public EX_IF_LatchType() {
		isBranchTaken = false;
		offset = 70000;
	}
	
	public boolean getIS_enable() {
		return IS_enable;
	}

	public void setIS_enable(boolean iS_enable) {
		IS_enable = iS_enable;
	}

	public void setIS_enable(boolean iS_enable, int newPC) {
		IS_enable = iS_enable;
		PC = newPC;
	}

	public int getPC() {
		return PC;
	}

}
