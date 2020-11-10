package processor.pipeline;

public class EX_IF_LatchType {
	boolean IF_enable,isBranchTaken;
	int branchTarget;
	
	public EX_IF_LatchType(){
		IF_enable=false;
		isBranchTaken=false;
		
	}
	public void setisbranchtaken(){
		isBranchTaken=true;
	}
	public void setbranchtarget(int b){
		branchTarget=b;
	}
	public void setIF_enable(boolean val){
		IF_enable=val;
	}

}
