package processor.pipeline;

import generic.Simulator;
import processor.Processor;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	public void performRW() {
		if(MA_RW_Latch.isRW_enable()) {
			// MA_RW_Latch.RW_enable = false;
			if(MA_RW_Latch.isNop == false) {
				int aluResult = MA_RW_Latch.aluResult;
				int rs1 = MA_RW_Latch.rs1;
				int rs2 = MA_RW_Latch.rs2;
				int rd = MA_RW_Latch.rd;
				int imm = MA_RW_Latch.imm;
				String opcode = MA_RW_Latch.opcode;
				System.out.println("RW " + MA_RW_Latch.insPC + "\trs1:" + rs1 + "\trs2:" + rs2 + "\trd:" + rd + "\timm:" + imm + "\talu:" + aluResult);
				if(MA_RW_Latch.isLoad) {
					containingProcessor.getRegisterFile().setValue(rd, aluResult);
					MA_RW_Latch.isLoad = false;
				}
				else {
					if(opcode.equals("11101") == false) {
						if(opcode.equals("11000") == false) {
							if(opcode.equals("11001") == false) {
								if(opcode.equals("11010") == false) {
									if(opcode.equals("11011") == false) {
										if(opcode.equals("11100") == false) {
											containingProcessor.getRegisterFile().setValue(rd, aluResult);
										}
									}
								}
							}
						}
					}
				}
				
				MA_RW_Latch.setRW_enable(false);
				// IF_EnableLatch.setIF_enable(true);
				if(MA_RW_Latch.opcode.equals("11101")) {
					Simulator.setSimulationComplete(true);
					IF_EnableLatch.setIF_enable(false);
				}
			}
			//TODO
			
		}
		else {
			
		}
	}

}
