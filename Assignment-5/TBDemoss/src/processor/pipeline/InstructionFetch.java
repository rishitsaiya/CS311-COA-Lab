package processor.pipeline;

import configuration.Configuration;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.Simulator;
import processor.Clock;
import processor.Processor;
import generic.Element;
import generic.Event;


public class InstructionFetch implements Element{
	
	Processor containingProcessor;
	public IF_EnableLatchType IF_EnableLatch;
	public IF_OF_LatchType IF_OF_Latch;
	EX_IF_LatchType EX_IF_Latch;
	//boolean conflict = false;
	boolean is_end = false;
	
	public InstructionFetch(Processor containingProcessor, IF_EnableLatchType iF_EnableLatch, IF_OF_LatchType iF_OF_Latch, EX_IF_LatchType eX_IF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.IF_EnableLatch = iF_EnableLatch;
		this.IF_OF_Latch = iF_OF_Latch;
		this.EX_IF_Latch = eX_IF_Latch;
	}
	
	public void setEnableDisable(Event e)
	{
		MemoryResponseEvent event=(MemoryResponseEvent) e;
		IF_OF_Latch.setInstruction(event.getValue());
		IF_OF_Latch.setOF_enable(true);
		IF_EnableLatch.setIF_busy(false);
	}
	
	public void performIF()
	{
			if(IF_EnableLatch.isIF_enable() && !is_end)
			{
				if(IF_EnableLatch.isIF_busy()) {
					return;
				}
				
				if(EX_IF_Latch.IF_enable){
					

					if(EX_IF_Latch.isBranchTaken){
						containingProcessor.getRegisterFile().setProgramCounter(EX_IF_Latch.branchTarget);
					}
					EX_IF_Latch.IF_enable=false;
				}
				
			
				int currentPC = containingProcessor.getRegisterFile().getProgramCounter();
				
					Simulator.getEventQueue().addEvent(
							new MemoryReadEvent(
									Clock.getCurrentTime()+Configuration.mainMemoryLatency,
									this,containingProcessor.getMainMemory(),
									containingProcessor.getRegisterFile().getProgramCounter()));
					IF_EnableLatch.setIF_busy(true);
					containingProcessor.getRegisterFile().setProgramCounter(currentPC + 1);
				
				
			}
		
		
	}

	@Override
	public void handleEvent(Event e) {
		// TODO Auto-generated method stub
		if(IF_OF_Latch.OF_busy) {
			e.setEventTime(Clock.getCurrentTime()+1);
			Simulator.getEventQueue().addEvent(e);
		}
		else {
			
			setEnableDisable(e);
		}
		
	}
		

}
