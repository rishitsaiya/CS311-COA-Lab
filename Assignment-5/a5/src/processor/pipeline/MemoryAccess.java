package processor.pipeline;

import configuration.Configuration;
import generic.*;
import generic.Event.EventType;
import processor.Clock;
import processor.Processor;

public class MemoryAccess implements Element{
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch) {
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
	}
	
	public void performMA() {
		// if(EX_MA_Latch.MA_enable == false) MA_RW_Latch.RW_enable = false;
		if(EX_MA_Latch.isMA_enable() && EX_MA_Latch.isBusy == false) {
			if(EX_MA_Latch.isNop == true) {
				MA_RW_Latch.isNop = true;
				MA_RW_Latch.rd = 75000;
			}
			else {
				MA_RW_Latch.isNop = false;
				int aluResult = EX_MA_Latch.aluResult;
				int rs1 = EX_MA_Latch.rs1;
				int rs2 = EX_MA_Latch.rs2;
				int rd = EX_MA_Latch.rd;
				int imm = EX_MA_Latch.imm;
				String opcode = EX_MA_Latch.opcode;

				MA_RW_Latch.insPC = EX_MA_Latch.insPC;
				System.out.println("MA\t" + EX_MA_Latch.insPC + "\trs1:" + rs1 + "\trs2:" + rs2 + "\trd:" + rd + "\timm:" + imm + "\talu:" + aluResult);

				MA_RW_Latch.aluResult = aluResult;
				MA_RW_Latch.rs1 = rs1;
				MA_RW_Latch.rs2 = rs2;
				MA_RW_Latch.rd = rd;
				MA_RW_Latch.imm = imm;
				MA_RW_Latch.opcode = opcode;

				// if(Simulator.storeresp + Configuration.mainMemoryLatency >= Clock.getCurrentTime()) EX_MA_Latch.isBusy = false;

				if(opcode.equals("10110")) { //load
					MA_RW_Latch.isLoad = true;
					EX_MA_Latch.isBusy = true;
					Simulator.getEventQueue().addEvent(
						new MemoryReadEvent(
							Clock.getCurrentTime() + Configuration.mainMemoryLatency, 
							this, 
							containingProcessor.getMainMemory(), aluResult)
					);
					EX_MA_Latch.setMA_enable(false);
					return;
					// aluResult = containingProcessor.getMainMemory().getWord(aluResult);
				}
				if(opcode.equals("10111")) {  //store
					EX_MA_Latch.isBusy = true;
					Simulator.storeresp = Clock.getCurrentTime();
					Simulator.getEventQueue().addEvent(
						new MemoryWriteEvent(
							Clock.getCurrentTime() + Configuration.mainMemoryLatency, 
							this, 
							containingProcessor.getMainMemory(), 
							aluResult, 
							rs1)
					);
					EX_MA_Latch.setMA_enable(false);
					return;

				}
			}
			EX_MA_Latch.setMA_enable(false);
			if(EX_MA_Latch.opcode.equals("11101") == true ) {
				EX_MA_Latch.setMA_enable(false);
			}
			MA_RW_Latch.setRW_enable(true);
		}
		//TODO
	}

	@Override
	public void handleEvent(Event e) {
		if(e.getEventType() == EventType.MemoryResponse) {
			MemoryResponseEvent event = (MemoryResponseEvent) e ; 
			MA_RW_Latch.aluResult = event.getValue();
			MA_RW_Latch.insPC = EX_MA_Latch.insPC;
			MA_RW_Latch.setRW_enable(true);
			EX_MA_Latch.isBusy = false;
		}
		else {
			EX_MA_Latch.isBusy = false;
		}

	}

}
