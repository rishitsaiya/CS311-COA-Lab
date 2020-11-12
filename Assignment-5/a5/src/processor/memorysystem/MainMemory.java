package processor.memorysystem;

import generic.*;
import processor.*;

public class MainMemory implements Element{
	int[] memory;
	
	public MainMemory() {
		memory = new int[65536];
	}
	
	public int getWord(int address) {
		return memory[address];
	}
	
	public void setWord(int address, int value) {
		memory[address] = value;
	}

	public String toString() {
		return "MainMemory: ";
	}

	public int getFirst() {
		return memory[0];
	}

	public int getSize() {
		return 65536;
	}
	
	public String getContentsAsString(int startingAddress, int endingAddress) {
		if(startingAddress == endingAddress)
			return "";
		
		StringBuilder sb = new StringBuilder();
		sb.append("\nMain Memory Contents:\n\n");
		for(int i = startingAddress; i <= endingAddress; i += 1) {
			sb.append(i + "\t\t: " + memory[i] + "\n");
		}
		sb.append("\n");
		return sb.toString();
	}

	@Override
	public void handleEvent(Event e) {
		if (e.getEventType() == Event.EventType.MemoryRead) {
			System.out.println("Memory Read Event Happening");
			MemoryReadEvent event = (MemoryReadEvent) e ; 

			System.out.println(getWord(event.getAddressToReadFrom()));
			Simulator.getEventQueue().addEvent( 
				new MemoryResponseEvent( 
					Clock.getCurrentTime(), 
					this, 
					event.getRequestingElement(), 
					getWord(event.getAddressToReadFrom())
				)
			); 
		}
		else if(e.getEventType() == Event.EventType.MemoryWrite) {
			System.out.println("Memory Write Event Happening");
			MemoryWriteEvent event = (MemoryWriteEvent) e ; 
			System.out.println(getWord(event.getAddressToWriteTo()));

			this.setWord(event.getAddressToWriteTo(), event.getValue());
			Simulator.getEventQueue().addEvent(
				new ExecutionCompleteEvent(
					Clock.getCurrentTime(), 
					this, 
					event.getRequestingElement())
			);
		}
	}

}
