package processor.pipeline;

import generic.Statistics;
import processor.Processor;

public class OperandFetch {
	Processor containingProcessor;
	IF_OF_LatchType IF_OF_Latch;
	OF_EX_LatchType OF_EX_Latch;
	co_unit controlunit = new co_unit();
	boolean is_end = false;
	
	public boolean r3(String op) {
		if(op.equals("")) return false;
		if(op.charAt(4)=='0' && !(op.charAt(0)=='1' && op.charAt(1)=='1') && !op.equals("10110"))
			return true;
		else return false;
		
	}
	public boolean r2i(String op) {
		if(op.equals("")) return false;
		if( ((op.charAt(4)=='1') || op.equals("10110") || op.equals("11010") ||op.equals("11100") ) && !op.equals("11101"))
			return true;
		else return false;
		
	}
	
	public boolean r2i1(String op) {
		if(op.equals("")) return false;
		if(op.charAt(4)=='1'  && !(op.charAt(0)=='1' && op.charAt(1)=='1') && !op.equals("10111"))
			return true;
		else return false;
		
	}
	
	public boolean r2i2(String op) {//load or store
		if(op.equals("")) return false;
		if((op.equals("10110") || op.equals("10111")))
			return true;
		else return false;
		
	}
	
	public boolean r2i3(String op) { // branch
		if(op.equals("")) return false;
		if((op.charAt(0)=='1' && op.charAt(1)=='1')&&!op.equals("11000")&&!op.equals("11101")  )
			return true;
		else return false;
		
	}
	
	public boolean ri(String op) {
		if(op.equals("")) return false;
		if(op.equals("11101") || op.equals("11000"))
			return true;
		else return false;
		
	}
	public OperandFetch(Processor containingProcessor, IF_OF_LatchType iF_OF_Latch, OF_EX_LatchType oF_EX_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_OF_Latch = iF_OF_Latch;
		this.OF_EX_Latch = oF_EX_Latch;
	}
	
	public int convertbin( String s) {
		int n = s.length();
		int i = Integer.parseInt(s,2);
		if (s.charAt(0) == '1') {
			i = i - (int)Math.pow(2, n);

			
			return i;
		}
		else return i;
	}
	
	public void performOF()
	{
		if(IF_OF_Latch.isOF_enable() && !is_end)
		{
			//TODO
			if(OF_EX_Latch.isEX_busy()) {
				IF_OF_Latch.OF_busy=true;
				return;
			}
			else {
				IF_OF_Latch.OF_busy=false;
			}

			int instruction = IF_OF_Latch.getInstruction();
			controlunit.setInstruction(instruction);
			String instructionString = Integer.toBinaryString(instruction);
			
			int n = instructionString.length();
			String todo="";
			for (int i=0;i<32-n;i++){
				todo = todo + "0" ;
			}
			instructionString = todo + instructionString;

			String opcode="",rs1="",rs2="",rd="",immx = "";
			rs1 = instructionString.substring(5,10);
			rs2 = instructionString.substring(10,15);
			opcode=instructionString.substring(0,5);
			
			if (instructionString.substring(0,5).equals("11101")) {
				//System.out.println("pppppppppppppppppppppppppppp123");
				is_end = true;
				containingProcessor.getIFUnit().is_end=true;
				
			}
			
			switch(String.valueOf(r3(opcode)))
			{
				case "true":
					rd=instructionString.substring(15,20);
					immx=instructionString.substring(20,32);
					break;
			}
			switch(String.valueOf(r2i(opcode)))
			{
				case "true":
					rd=instructionString.substring(10,15);
					immx=instructionString.substring(15,32);
					break;
			}
			switch(String.valueOf(ri(opcode)))
			{
				case "true":
					rd=instructionString.substring(5,10);
					immx=instructionString.substring(10,32);
					break;
			}
		
			String rp1 ;
			String rp2 ;
			if(!(opcode.equals("10111"))){ // if store
				rp1 = rs1;
				rp2 = rs2;
			}
			else{
				rp1 = rd;
				rp2 = rs1;
			}
			int operand1 = containingProcessor.getRegisterFile().getValue( Integer.parseInt(rp1,2) );
			int operand2 = containingProcessor.getRegisterFile().getValue( Integer.parseInt(rp2,2) );
			
			
			boolean conflict1 = false;
			boolean conflict2=false;
			boolean conflict3=false;
			switch(String.valueOf(r3(opcode)))
			{
				case "true":
					if(containingProcessor.getEXUnit().controlunit.opcode.equals("00111") || containingProcessor.getEXUnit().controlunit.opcode.equals("00110")) {
						if(rs1.equals("11111")){
							conflict1 = true;
						}
						if(rs2.equals("11111")){
							conflict1 = true;
						}
					}
					if(r3(containingProcessor.getEXUnit().controlunit.opcode) || r2i1(containingProcessor.getEXUnit().controlunit.opcode)) {
						if(rs1.equals(containingProcessor.getEXUnit().controlunit.rd)){
							conflict1 = true;
						}
						if(rs2.equals(containingProcessor.getEXUnit().controlunit.rd)){
								conflict1 = true;
						}//System.out.println("*********EX1***********");
					}
					if(r3(containingProcessor.getMAUnit().controlunit.opcode) || r2i1(containingProcessor.getMAUnit().controlunit.opcode)) {
						if(rs1.equals(containingProcessor.getMAUnit().controlunit.rd)){
							conflict2 = true;
						}
						if(rs2.equals(containingProcessor.getMAUnit().controlunit.rd)){
							conflict2 = true;
						}//System.out.println("*********MA1***********");
					}
					if(r3(containingProcessor.getRWUnit().controlunit.opcode) || r2i1(containingProcessor.getRWUnit().controlunit.opcode)) {
						if(rs1.equals(containingProcessor.getRWUnit().controlunit.rd)){
							conflict3 = true;
						}
						if(rs2.equals(containingProcessor.getRWUnit().controlunit.rd)){
							conflict3 = true;
						}//System.out.println("*********RW1***********"+conflict);
					}
					if(containingProcessor.getEXUnit().controlunit.opcode.equals("10110")) {
						if(rs1.equals(containingProcessor.getEXUnit().controlunit.rd)){
							conflict1= true;
						}
						if(rs2.equals(containingProcessor.getEXUnit().controlunit.rd)){
							conflict1 = true;
						}//System.out.println("*********EX2***********");
					}
					if(containingProcessor.getMAUnit().controlunit.opcode.equals("10110")) {
						if(rs1.equals(containingProcessor.getMAUnit().controlunit.rd)){
							conflict2 = true;
						}
						if(rs2.equals(containingProcessor.getMAUnit().controlunit.rd)){
							conflict2 = true;
						}//System.out.println("*********MA2***********");
					}
			}
			
			switch(String.valueOf(r2i1(opcode)))
			{
				case "true":
					if(containingProcessor.getEXUnit().controlunit.opcode.equals("00111") || containingProcessor.getEXUnit().controlunit.opcode.equals("00110")) {
						if(rs1.equals("11111")){
							conflict1 = true;
						}
					}
					//System.out.println("PPPPPPPPP"+containingProcessor.getEXUnit().controlunit.opcode);
					if(r3(containingProcessor.getEXUnit().controlunit.opcode) || r2i1(containingProcessor.getEXUnit().controlunit.opcode)) {
							
						if(rs1.equals(containingProcessor.getEXUnit().controlunit.rd)){
							conflict1 = true;
						}//System.out.println("*********EX3***********");
					}
					if(r3(containingProcessor.getMAUnit().controlunit.opcode) || r2i1(containingProcessor.getMAUnit().controlunit.opcode)) {
						if(rs1.equals(containingProcessor.getMAUnit().controlunit.rd)){
							conflict2 = true;
						}//System.out.println("*********MA3***********");
					}
					if(r3(containingProcessor.getRWUnit().controlunit.opcode) || r2i1(containingProcessor.getRWUnit().controlunit.opcode)) {
						if(rs1.equals(containingProcessor.getRWUnit().controlunit.rd)){
							conflict3 = true;
						}//System.out.println("*********RW2***********");
					}
					if(containingProcessor.getEXUnit().controlunit.opcode.equals("10110")) {
					//	System.out.println("^^^^^^^^^^^^^"+rs1);
						if(rs1.equals(containingProcessor.getEXUnit().controlunit.rd)){
							conflict1 = true;
							//System.out.println("^^^^^^^^^^^^^"+rs1);
							//System.out.println("^^^^^^^^^^^^^^^^^^^^^"+containingProcessor.getEXUnit().controlunit.rd);
						}//System.out.println("*********EX4***********");
					}
					if(containingProcessor.getMAUnit().controlunit.opcode.equals("10110")) {
						if(rs1.equals(containingProcessor.getMAUnit().controlunit.rd)){
							conflict2 = true;
						}//System.out.println("*********MA4***********");
					}
			}
			
			switch(String.valueOf(r2i3(opcode)))
			{
				case "true":
					if(containingProcessor.getEXUnit().controlunit.opcode.equals("00111") || containingProcessor.getEXUnit().controlunit.opcode.equals("00110")) {
						if(rs1.equals("11111")){
							conflict1 = true;
						}
						if(rd.equals("11111")){
							conflict1 = true;
						}
					}
					if(r3(containingProcessor.getEXUnit().controlunit.opcode) || r2i1(containingProcessor.getEXUnit().controlunit.opcode)) {
						if(rs1.equals(containingProcessor.getEXUnit().controlunit.rd)){
							conflict1 = true;
						}
						if(rd.equals(containingProcessor.getEXUnit().controlunit.rd)){
								conflict1 = true;
						}//System.out.println("*********EX5***********");
					}
					if(r3(containingProcessor.getMAUnit().controlunit.opcode) || r2i1(containingProcessor.getMAUnit().controlunit.opcode)) {
						if(rs1.equals(containingProcessor.getMAUnit().controlunit.rd)){
							conflict2 = true;
						}
						if(rd.equals(containingProcessor.getMAUnit().controlunit.rd)){
							conflict2 = true;
						}//System.out.println("*********MA5***********"+conflict);
					}
					if(r3(containingProcessor.getRWUnit().controlunit.opcode) || r2i1(containingProcessor.getRWUnit().controlunit.opcode)) {
						if(rs1.equals(containingProcessor.getRWUnit().controlunit.rd)){
							conflict3 = true;
						}
						 if(rd.equals(containingProcessor.getRWUnit().controlunit.rd)){
							conflict3 = true;
						}//System.out.println("*********RW3***********"+conflict);
					}
					if(containingProcessor.getEXUnit().controlunit.opcode.equals("10110")) {
						if(rs1.equals(containingProcessor.getEXUnit().controlunit.rd)){
							conflict1 = true;
						}
						if(rd.equals(containingProcessor.getEXUnit().controlunit.rd)){
							conflict1 = true;
						}//System.out.println("*********EX6***********");
					}
					if(containingProcessor.getMAUnit().controlunit.opcode.equals("10110")) {
						if(rs1.equals(containingProcessor.getMAUnit().controlunit.rd)){
							conflict2 = true;
						}
						if(rd.equals(containingProcessor.getMAUnit().controlunit.rd)){
							conflict2 = true;
						}//System.out.println("*********MA6***********"+conflict);
					}
			}
			
			switch(String.valueOf(r2i2(opcode)))
			{
				case "true":
					switch(opcode)
					{
						case "10110":
							if(containingProcessor.getEXUnit().controlunit.opcode.equals("00111") || containingProcessor.getEXUnit().controlunit.opcode.equals("00110")) {
								if(rs1.equals("11111")){
									conflict1 = true;
								}
							}
							if(r3(containingProcessor.getEXUnit().controlunit.opcode) || r2i1(containingProcessor.getEXUnit().controlunit.opcode)) {
								if(rs1.equals(containingProcessor.getEXUnit().controlunit.rd)){
									conflict1 = true;
								}//System.out.println("*********EX7***********");
							}
							if(r3(containingProcessor.getMAUnit().controlunit.opcode) || r2i1(containingProcessor.getMAUnit().controlunit.opcode)) {
								if(rs1.equals(containingProcessor.getMAUnit().controlunit.rd)){
									conflict2 = true;
								}//System.out.println("*********MA7***********");
							}
							if(r3(containingProcessor.getRWUnit().controlunit.opcode) || r2i1(containingProcessor.getRWUnit().controlunit.opcode)) {
								if(rs1.equals(containingProcessor.getRWUnit().controlunit.rd)){
									conflict3 = true;
								}//System.out.println("*********RW4***********");
							}
							if(containingProcessor.getEXUnit().controlunit.opcode.equals("10110")) {
								if(rs1.equals(containingProcessor.getEXUnit().controlunit.rd)){
									conflict1 = true;
								}//System.out.println("*********EX8***********");
							}
							if(containingProcessor.getMAUnit().controlunit.opcode.equals("10110")) {
								if(rs1.equals(containingProcessor.getMAUnit().controlunit.rd)){
									conflict2 = true;
								}//System.out.println("*********MA8***********");
							}
							if(containingProcessor.getEXUnit().controlunit.opcode.equals("10111")) {
								if((Integer.parseInt(rs1,2) + convertbin(immx) )== 
												Integer.parseInt((containingProcessor.getEXUnit().controlunit.rd),2) +
												convertbin(containingProcessor.getEXUnit().controlunit.Imm) ) {
									conflict1 = true;
								}//System.out.println("*********EX9***********");
							}
							if(containingProcessor.getMAUnit().controlunit.opcode.equals("10111")) {
								if((Integer.parseInt(rs1,2) + convertbin(immx) )== 
												Integer.parseInt((containingProcessor.getMAUnit().controlunit.rd),2) +
												convertbin(containingProcessor.getMAUnit().controlunit.Imm) ) {
									conflict2 = true;
								}//System.out.println("*********MA9***********");
							}
					}	
			}
			
			switch(String.valueOf(r2i2(opcode)))
			{
				case "true":
					switch(opcode)
					{
						case "10111":
							if(containingProcessor.getEXUnit().controlunit.opcode.equals("00111") || containingProcessor.getEXUnit().controlunit.opcode.equals("00110")) {
								if(rs1.equals("11111")){
									conflict1 = true;
								}
								if(rd.equals("11111")){
									conflict1 = true;
								}
							}
							if(r3(containingProcessor.getEXUnit().controlunit.opcode) || r2i1(containingProcessor.getEXUnit().controlunit.opcode)) {
								if(rs1.equals(containingProcessor.getEXUnit().controlunit.rd)){
									conflict1 = true;
								}
								if(rd.equals(containingProcessor.getEXUnit().controlunit.rd)){
										conflict1 = true;
								}//System.out.println("*********EX10***********");
							}
							if(r3(containingProcessor.getMAUnit().controlunit.opcode) || r2i1(containingProcessor.getMAUnit().controlunit.opcode)) {
								if(rs1.equals(containingProcessor.getMAUnit().controlunit.rd)){
									conflict2 = true;
								}
								if(rd.equals(containingProcessor.getMAUnit().controlunit.rd)){
									conflict2 = true;
								}//System.out.println("*********MA10***********");
							}
							if(r3(containingProcessor.getRWUnit().controlunit.opcode) || r2i1(containingProcessor.getRWUnit().controlunit.opcode)) {
								if(rs1.equals(containingProcessor.getRWUnit().controlunit.rd)){
									conflict3 = true;
								}
								if(rd.equals(containingProcessor.getRWUnit().controlunit.rd)){
									conflict3 = true;
								}//System.out.println("*********RW5***********");
							}
							if(containingProcessor.getEXUnit().controlunit.opcode.equals("10110")) {
								if(rs1.equals(containingProcessor.getEXUnit().controlunit.rd)){
									conflict1 = true;
								}
								if(rd.equals(containingProcessor.getEXUnit().controlunit.rd)){
									conflict1 = true;
								}//System.out.println("*********EX11***********");
							}
							if(containingProcessor.getMAUnit().controlunit.opcode.equals("10110")) {
								if(rs1.equals(containingProcessor.getMAUnit().controlunit.rd)){
									conflict2 = true;
								}
								if(rd.equals(containingProcessor.getMAUnit().controlunit.rd)){
									conflict2 = true;
								}//System.out.println("*********MA11***********");
							}
					}
			}
			if(!conflict1 && !conflict2 && !conflict3) {
				OF_EX_Latch.setInstruction(instruction);
				OF_EX_Latch.setimmx(convertbin(immx));
				OF_EX_Latch.setbranchtarget(convertbin(immx) + containingProcessor.getRegisterFile().getProgramCounter()-1);
				OF_EX_Latch.setoperand1(operand1);
				OF_EX_Latch.setoperand2(operand2);
				OF_EX_Latch.setrd(Integer.parseInt(rd,2));
				IF_OF_Latch.setOF_enable(false);
				OF_EX_Latch.setEX_enable(true);
				containingProcessor.getIFUnit().IF_EnableLatch.setIF_enable(true) ;
			}
			else {
				if(conflict1 && containingProcessor.getEXUnit().OF_EX_Latch.isEX_enable())
					Statistics.datahaz++;
				else if(conflict2 && containingProcessor.getMAUnit().EX_MA_Latch.isMA_enable() )
					Statistics.datahaz++;
				else if(conflict3 && containingProcessor.getRWUnit().MA_RW_Latch.isRW_enable())
					Statistics.datahaz++;
				containingProcessor.getIFUnit().IF_EnableLatch.setIF_enable(false) ;
				
			}
			
		}
		
	}

}
