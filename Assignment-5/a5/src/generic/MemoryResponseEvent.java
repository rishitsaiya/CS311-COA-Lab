package generic;

public class MemoryResponseEvent extends Event {

	int value;
	
	public MemoryResponseEvent(long eventTime, Element requestingElement, Element processingElement, int value) {
		super(eventTime, EventType.MemoryResponse, requestingElement, processingElement);
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public boolean checkEquality(int value) {
		return this.value == value;
	} 

	public String toString() {
		return "Memory response event, Set and get value";
	}
}
