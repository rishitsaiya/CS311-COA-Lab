package processor.pipeline;

import processor.Processor;
import generic.Instruction;
import generic.Instruction.OperationType;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performMA()
	{
		if(EX_MA_Latch.isMA_enable())
		{
			Instruction instruction = EX_MA_Latch.getInstruction();
			int alu_result = EX_MA_Latch.getALU_result();
			MA_RW_Latch.setALU_result(alu_result);
			OperationType op_type = instruction.getOperationType();
			switch(op_type)
			{
				case store:
					int val_store = containingProcessor.getRegisterFile().getValue(
							instruction.getSourceOperand1().getValue());
					containingProcessor.getMainMemory().setWord(alu_result, val_store);
					break;

				case load:
					int load_result = containingProcessor.getMainMemory().getWord(alu_result);
					MA_RW_Latch.setLoad_result(load_result);
					break;
					
				default:
					break;
			}
			MA_RW_Latch.setInstruction(instruction);
			MA_RW_Latch.setRW_enable(true);
			EX_MA_Latch.setMA_enable(false);
		}
	}

}
