package generic;

public class Event {
	
	public enum EventType {ExecutionComplete, MemoryRead, MemoryResponse, MemoryWrite};
	
	long eventTime;
	Element requestingElement;
	Element processingElement;
	EventType eventType;
	
	public Event(long eventTime, EventType eventType, Element requestingElement, Element processingElement) {
		this.eventTime = eventTime;
		this.eventType = eventType;
		this.requestingElement = requestingElement;
		this.processingElement = processingElement;
	}

	public long getEventTime() {
		return eventTime;
	}

	public void setEventTime(long eventTime) {
		this.eventTime = eventTime;
	}

	public long checkParity() {
		return eventTime % 2;
	}

	public void setEventAndRequest(long eventTime, Element requestingElement) {
		this.eventTime = eventTime;
		this.requestingElement = requestingElement;
	}

	public Element getRequestingElement() {
		return requestingElement;
	}

	public void setRequestingElement(Element requestingElement) {
		this.requestingElement = requestingElement;
	}

	public void setRequestAndProcess(Element requestingElement, Element processingElement) {
		this.requestingElement = requestingElement;
		this.processingElement = processingElement;
	}

	public Element getProcessingElement() {
		return processingElement;
	}

	public void setProcessingElement(Element processingElement) {
		this.processingElement = processingElement;
	}

	public void setProcessAndType(Element processingElement, EventType eventType) {
		this.processingElement = processingElement;
		this.eventType = eventType;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}
	
	public String toString() {
		return "Set Event ";
	}
}
