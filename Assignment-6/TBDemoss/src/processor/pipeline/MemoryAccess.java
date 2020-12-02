package processor.pipeline;

import processor.Processor;
import generic.Instruction;
import generic.Instruction.OperationType;
import generic.Simulator;
import processor.Clock;
import generic.Element;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.MemoryWriteEvent;
import generic.Event;
import generic.Event.EventType;
import configuration.Configuration;

public class MemoryAccess implements Element {

	Processor containingProcessor;
	public EX_MA_LatchType EX_MA_Latch;
	public MA_RW_LatchType MA_RW_Latch;
	IF_EnableLatchType IF_EnableLatch;
	public Instruction instruction;

	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch,
						MA_RW_LatchType mA_RW_Latch, IF_EnableLatchType iF_EnableLatch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_EnableLatch = iF_EnableLatch;
	}

	public void handleEvent(Event e)
	{
		if (e.getEventType() == EventType.MemoryResponse)
		{
			MemoryResponseEvent event = (MemoryResponseEvent) e;
			int load_result = event.getValue();

			MA_RW_Latch.setLoad_result(load_result);
			MA_RW_Latch.setInstruction(instruction);

			EX_MA_Latch.setMA_busy(false);
			MA_RW_Latch.setRW_enable(true);
			containingProcessor.getEXUnit().OF_EX_Latch.EX_busy = false;
			System.out.println("****MA Event Handled (Load)****");
			EX_MA_Latch.setMA_enable(false);
		}
	}

	public void performMA()
	{
		if (EX_MA_Latch.isMA_busy())
		{
			containingProcessor.getEXUnit().OF_EX_Latch.EX_busy = true;
			return;
		}
		else
		{
			containingProcessor.getEXUnit().OF_EX_Latch.EX_busy = false;
		}

		if (EX_MA_Latch.getIsNOP())
		{
			//System.out.println("Got NOP in MA");
			MA_RW_Latch.setIsNOP(true);
			MA_RW_Latch.setInstruction(null);
			EX_MA_Latch.setIsNOP(false);
		}
		else if (EX_MA_Latch.isMA_enable())
		{
			instruction = EX_MA_Latch.getInstruction();
			int alu_result = EX_MA_Latch.getALU_result();
			
			MA_RW_Latch.setALU_result(alu_result);
			OperationType op_type = instruction.getOperationType();
			switch (op_type)
			{
				case store:
					int val_store = containingProcessor.getRegisterFile()
							.getValue(instruction.getSourceOperand1().getValue());

					Simulator.getEventQueue().addEvent(
						new MemoryWriteEvent(
							Clock.getCurrentTime() + Configuration.mainMemoryLatency,
							this,
							containingProcessor.getMainMemory(),
							alu_result,
							val_store));
					EX_MA_Latch.setMA_busy(true);
					System.out.println("****MA Event Added (Store)****");

					return;

				case load:
					Simulator.getEventQueue().addEvent(
						new MemoryReadEvent(
							Clock.getCurrentTime() + Configuration.mainMemoryLatency,
							this,
							containingProcessor.getMainMemory(),
							alu_result));
					EX_MA_Latch.setMA_busy(true);
					System.out.println("****MA Event Added (Load)****");

					return;

				default:
					break;
			}

			if (instruction.getOperationType().ordinal() == 29)
			{
				IF_EnableLatch.setIF_enable(false);
			} 
			MA_RW_Latch.setInstruction(instruction);
			MA_RW_Latch.setRW_enable(true);
			System.out.println("****MA is enabled****: " + instruction);
			EX_MA_Latch.setMA_enable(false);
		}
	}

}
