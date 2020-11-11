package generic;

import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;

import processor.Clock;
import processor.pipeline.InstructionFetch;

public class EventQueue {
	
	PriorityQueue<Event> queue;
	
	public EventQueue() {
		queue = new PriorityQueue<Event>(new EventComparator());
	}

	public EventQueue(PriorityQueue<Event> queue) {
		this.queue = queue;
	}
	
	public void addEvent(Event event) {
		queue.add(event);
	}

	public void clearQueue() {
		this.queue = new PriorityQueue<Event> (new EventComparator());
	}	

	public void deleteeventqueue(long Clock) {
		//System.out.println("jwiarhfiohwihiw"+queue.size());
		Iterator<Event> itr=queue.iterator();
		Event e;
		while(itr.hasNext()) {
			e = itr.next();
			if(e.requestingElement instanceof InstructionFetch  ) {
				
				((InstructionFetch)e.requestingElement).IF_EnableLatch.setIF_busy(false);
				itr.remove();
				
			}
		}
	}

	public void processEvents() {
		while(queue.isEmpty() == false && queue.peek().getEventTime() <= Clock.getCurrentTime()) {
			Event event = queue.poll();
			event.getProcessingElement().handleEvent(event);
		}
	}

	public toString() {
		return "Class EventQueue object here.";
	}
}

// to be passed for comparing order of two events
class EventComparator implements Comparator<Event> {
	@Override
    public int compare(Event x, Event y) {
		if(x.getEventTime() < y.getEventTime()) {
			return -1;
		}
		else if(x.getEventTime() > y.getEventTime()) {
			return 1;
		}
		else {
			return 0;
		}
    }
}
