package generic;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

import processor.Clock;
import processor.pipeline.InstructionFetch;

public class EventQueue {
	
	PriorityQueue<Event> queue;
	
	public EventQueue()
	{
		queue = new PriorityQueue<Event>(new EventComparator());
	}
	
	public void addEvent(Event event)
	{
		queue.add(event);
	}
	

	public void deleteeventqueue(long Clock) {
		/*for(int i=0;i<queue.size();i++) {
			if(queue(i).processingElememt=="InstructonFetch" && queue(i).eventTime<Clocl) {
				queue.remove(i);
			}
		}*/
		//System.out.println("jwiarhfiohwihiw"+queue.size());
		Iterator<Event> itr=queue.iterator();
		Event e;
		while(itr.hasNext()) {
			e=itr.next();
			if(e.requestingElement instanceof InstructionFetch  ) {
				
				//System.out.println("kefnhoerfqbkhewfkhio");
			//	((InstructionFetch)e.requestingElement).IF_OF_Latch.setOF_enable(true);
				/*Commenting the above because the new instruction comes with a latency if you open IF_OF_LAtch so you take the previous saved value again-
				 * IOW the value in OF_Latch will be over written after 40 cycles, and if you open IF_OF_Latch now then it will take the same older instruction and pass it to the Execute stage*/
				((InstructionFetch)e.requestingElement).IF_EnableLatch.setIF_busy(false);
				itr.remove();
				
			}
		}
		//System.out.println("jwiarhfiohwihiw"+queue.size());

		
	}

	public void processEvents()
	{
		//System.out.println(queue.isEmpty());
		while(queue.isEmpty() == false && queue.peek().getEventTime() <= Clock.getCurrentTime())
		{
			Event event = queue.poll();
			event.getProcessingElement().handleEvent(event);
		}
	}
}

class EventComparator implements Comparator<Event>
{
	@Override
    public int compare(Event x, Event y)
    {
		if(x.getEventTime() < y.getEventTime())
		{
			return -1;
		}
		else if(x.getEventTime() > y.getEventTime())
		{
			return 1;
		}
		else
		{
			return 0;
		}
    }
}
