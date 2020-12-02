package processor.pipeline;

import generic.Simulator;
import processor.Processor;
import processor.Clock;
import generic.Statistics;
import generic.Element;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.Event;
import generic.Event.EventType;
import configuration.Configuration;
import generic.Instruction.OperationType;

public class InstructionFetch implements Element {
	
	Processor containingProcessor;
	IF_EnableLatchType IF_EnableLatch;
	IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	
	public InstructionFetch(Processor containingProcessor,
							IF_EnableLatchType iF_EnableLatch,
							IF_OF_LatchType iF_OF_Latch,
							EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}

	public void handleEvent(Event e)
	{
		if (IF_OF_Latch.OF_busy)
		{
			e.setEventTime(Clock.getCurrentTime() + 1);
			Simulator.getEventQueue().addEvent(e);
		}
		else if (e.getEventType() == EventType.MemoryResponse)
		{
			MemoryResponseEvent event = (MemoryResponseEvent) e;
			int newInstruction = event.getValue();
			IF_OF_Latch.setInstruction(newInstruction);
			IF_EnableLatch.setIF_busy(false);
			IF_OF_Latch.setOF_enable(true);

			System.out.println("****IF Event Handled****");

			String instruction = Integer.toBinaryString(newInstruction);
			while (instruction.length() != 32)
			{
				instruction = "0" + instruction;
			}
			String opcode = instruction.substring(0, 5);
			int type_operation = Integer.parseInt(opcode, 2);
			OperationType[] operationType = OperationType.values();
			OperationType operation = operationType[type_operation];
			System.out.println("****IF is enabled****: " + operation);
		}
	}
	
	public void performIF()
	{
		if(IF_EnableLatch.isIF_busy())
		{
			return;
		}

		if(IF_EnableLatch.isIF_enable())
		{

			if(EX_IF_Latch.getIS_enable())
			{
				int newPC = EX_IF_Latch.getPC();
				containingProcessor.getRegisterFile().setProgramCounter(newPC);
				EX_IF_Latch.setIS_enable(false);
			}

			int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
			System.out.println("****currentPC**** " + Integer.toString(currentPC));

			Simulator.getEventQueue().addEvent(
				new MemoryReadEvent(
					Clock.getCurrentTime() + Configuration.mainMemoryLatency,
					this,
					containingProcessor.getMainMemory(),
					currentPC));
			System.out.println("****IF Event Added****");

			IF_EnableLatch.setIF_busy(true);
			containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);
			Statistics.setNumberOfInstructions(Statistics.getNumberOfInstructions() + 1);
		}
	}

}
