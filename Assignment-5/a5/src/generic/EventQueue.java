package generic;

import java.util.Comparator;
import java.util.PriorityQueue;

import processor.Clock;

public class EventQueue {
	
	PriorityQueue<Event> queue;
	
	public EventQueue() {
		queue = new PriorityQueue<Event>(new EventComparator());
	}
	
	public void addEvent(Event event) {
		queue.add(event);
	}

	public void EventQueue(PriorityQueue<Event> queue) {
		this.queue = queue;
	}

	public boolean isEmpty() {
		return queue.size() == 0;
	}

	public int getSize() {
		return queue.size();
	}

	public void processEvents() {
		while(queue.isEmpty() == false && queue.peek().getEventTime() <= Clock.getCurrentTime()) {
			Event event = queue.poll();
			event.getProcessingElement().handleEvent(event);
		}
	}
	
	public String toString() {
		return "Add event and process event";
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
