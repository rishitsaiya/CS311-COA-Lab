package processor.pipeline;

import generic.Simulator;
import processor.Processor;
import generic.Instruction;
import generic.Instruction.OperationType;

public class RegisterWrite {
	Processor containingProcessor;
	MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	
	public RegisterWrite(Processor containingProcessor, MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch) {
	
		this.containingProcessor = containingProcessor;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}
	
	public void performRW() {
	
		if(MA_RW_Latch.isRW_enable()) {
	
			Instruction instruction = MA_RW_Latch.getInstruction();
			int alu_result = MA_RW_Latch.getALU_result();
			OperationType op_type = instruction.getOperationType();
				
			switch(op_type) {
	
				case store:
				case jmp:
				case beq:
				case bne:
				case blt:
				case bgt:
					break;
				case load:
					int load_result = MA_RW_Latch.getLoad_result();
					int rd = instruction.getDestinationOperand().getValue();
					containingProcessor.getRegisterFile().setValue(rd, load_result);
					break;
				case end:
					Simulator.setSimulationComplete(true);
					break;
				default:
					rd = instruction.getDestinationOperand().getValue();
					containingProcessor.getRegisterFile().setValue(rd, alu_result);
					break;
			}
			
			MA_RW_Latch.setRW_enable(false);
			IF_EnableLatch.setIF_enable(true);
		}
	}

}
