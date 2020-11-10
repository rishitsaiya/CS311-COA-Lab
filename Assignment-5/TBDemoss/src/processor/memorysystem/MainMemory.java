package processor.memorysystem;

import generic.Element;
import generic.Event;
import generic.Event.EventType;
import generic.MemoryReadEvent;
import generic.MemoryResponseEvent;
import generic.MemoryWriteEvent;
import generic.Simulator;
import processor.Clock;
import processor.pipeline.MemoryAccess;

public class MainMemory implements Element{
	int[] memory;
	boolean ismain_busy;
	
	public MainMemory()
	{
		memory = new int[65536];
	}
	
	public int getWord(int address)
	{
		return memory[address];
	}
	
	public void setWord(int address, int value)
	{
		memory[address] = value;
	}
	public void setmainbusy(boolean m) {
		ismain_busy=m;
	}
	public boolean ismainbusy() {
		return ismain_busy;
	}
	public String getContentsAsString(int startingAddress, int endingAddress)
	{
		if(startingAddress == endingAddress)
			return "";
		
		StringBuilder sb = new StringBuilder();
		sb.append("\nMain Memory Contents:\n\n");
		for(int i = startingAddress; i <= endingAddress; i++)
		{
			sb.append(i + "\t\t: " + memory[i] + "\n");
		}
		sb.append("\n");
		return sb.toString();
	}

	@Override
	public void handleEvent ( Event e ) {
		if ( e.getEventType ( ) == EventType.MemoryRead )
		{
			MemoryReadEvent event = ( MemoryReadEvent ) e ;
			Simulator. getEventQueue().addEvent(
			new MemoryResponseEvent (
			Clock.getCurrentTime () ,
			this ,
			event.getRequestingElement ( ) ,
			getWord (event.getAddressToReadFrom()))) ;
		}
		else if(e.getEventType ( ) == EventType.MemoryWrite) {
			//System.out.println("LLLLEVEVVERRE");
			MemoryWriteEvent event = ( MemoryWriteEvent ) e ;
			//event.getRequestingElement().EX_MA_Latch.setMA_busy(false);
			((MemoryAccess)event.getRequestingElement()).EX_MA_Latch.setMA_busy(false);
			((MemoryAccess)event.getRequestingElement()).MA_RW_Latch.setRW_enable(true);

			setWord(event.getAddressToWriteTo(),event.getValue());
			
		}
	}
}
