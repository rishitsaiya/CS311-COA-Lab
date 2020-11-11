package processor.pipeline;

import configuration.Configuration;
import generic.Element;
import generic.Event;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.MemoryWriteEvent;
import generic.Simulator;
import processor.Clock;
import processor.Processor;

public class MemoryAccess implements Element{
	Processor containingProcessor;
	public EX_MA_LatchType EX_MA_Latch;
	public MA_RW_LatchType MA_RW_Latch;
	co_unit controlunit = new co_unit();
	boolean is_end = false;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performOperations()
	{
		controlunit.opcode="";
		controlunit.rs1="";
		controlunit.rs2="";
		controlunit.rd="";
		controlunit.Imm = "";
	}
	
	public void setEnableDisable()
	{
		MA_RW_Latch.setRW_enable(true);
		EX_MA_Latch.setMA_enable(false);
	}
	
	public void performMA()
	{
		if(EX_MA_Latch.isMA_enable() && !is_end) {
			if(EX_MA_Latch.isMA_busy()) {
				return;
			}
			//System.out.println("MA:"+"\n");
			int op2 = EX_MA_Latch.getop2();
			int alures = EX_MA_Latch.getaluRes();
			int ldres=0;
			int instruction = EX_MA_Latch.getInstruction();
			controlunit.setInstruction(instruction);
			/*System.out.print("Instruction code:"+instruction+"\n");
			System.out.print("Opcode:"+controlunit.opcode+"\n");
			System.out.print("rs1:"+Integer.parseInt(controlunit.rs1,2)+"\n");
			System.out.print("rs2:"+Integer.parseInt(controlunit.rs2,2)+"\n");
			System.out.print("rd:"+Integer.parseInt(controlunit.rd,2)+"\n");*/
			MA_RW_Latch.setInstruction(instruction);
			
			if(controlunit.opcode.equals("11101")) {
				is_end = true;
			}
				
			
			if(controlunit.isSt()){
				Simulator.getEventQueue().addEvent(
						new MemoryWriteEvent(
								Clock.getCurrentTime()+Configuration.mainMemoryLatency,
								this,containingProcessor.getMainMemory(),
								alures,op2));
				EX_MA_Latch.setMA_busy(true);
				//containingProcessor.getMainMemory().setWord( alures, op2);
			}
			else if (controlunit.opcode.equals("10110")){
				Simulator.getEventQueue().addEvent(
						new MemoryReadEvent(
								Clock.getCurrentTime()+Configuration.mainMemoryLatency,
								this,containingProcessor.getMainMemory(),
								alures));
				EX_MA_Latch.setMA_busy(true);
	
				//ldres = containingProcessor.getMainMemory().getWord(alures);
				//MA_RW_Latch.setldres(ldres);
			}
			else{
				MA_RW_Latch.setalures(alures);
				setEnableDisable();
			}
			//MA_RW_Latch.setRW_enable(true);
			
			MA_RW_Latch.setrd(EX_MA_Latch.getrd());
		

		}
		else {
			performOperations();
		}
	
	}

	@Override
	public void handleEvent(Event e) {
		// TODO Auto-generated method stub
		MemoryResponseEvent event=(MemoryResponseEvent) e;
		MA_RW_Latch.setldres(event.getValue());
		EX_MA_Latch.setMA_busy(false);
		MA_RW_Latch.setRW_enable(true);
		EX_MA_Latch.setMA_enable(false);
		
		
	}

}
