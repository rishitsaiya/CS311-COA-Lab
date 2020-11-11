package processor.pipeline;

public class control_unit {
	String opcode;
	
	String Imm;

	public void setopcode(String op){
		opcode=op;
	}
	public String getopcode(){
		return opcode;
	}
	public boolean isSt(){
		if(opcode.equals("10111")){
				return true;
		}
		else return false ;
	}
	
	public boolean isimm1(){
		//System.out.println(opcode.equals("10110"));

		if ((!opcode.equals("11101") && opcode.charAt(4)=='1') || opcode.equals("10110") || opcode.equals("11010") ){

			return true;
		}
		else return false ;
	}
	public boolean isimm(){
		//System.out.println(opcode.equals("10110"));

		if (( opcode.charAt(4)=='1'|| opcode.equals("10110")) && !( opcode.equals("11101") || opcode.equals("11001") || opcode.equals("11011"))){

			return true;
		}
		else return false ;
	}
	public boolean isLd(){
		if (opcode.equals("10110")){
				return true;
		}
		return false ;
	}
	
	public boolean isWb(){
		if ((opcode.charAt(0) == '1' && opcode.charAt(1) =='1') || (opcode.equals("10111"))){
			return false;
		}
			return true;
	}
	
	public boolean isend(){
		if(opcode.equals("11101")){
			return true;
		}
		return false;
	}
	
}
