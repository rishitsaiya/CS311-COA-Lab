package processor.pipeline;

public class EX_IF_LatchType {

	boolean IsEnabled;
	int PC;
	
	public EX_IF_LatchType(){
		IsEnabled = false;
	}

	public EX_IF_LatchType(boolean isEnabled, int pcValue) {
		IsEnabled = isEnabled;
		PC = pcValue;
	}

	public boolean getIS_enable() {
		return IsEnabled;
	}

	public void setIS_enable(boolean isEnabled, int newPC) {
		IsEnabled = isEnabled;
		PC = newPC;
	}

	public void setIS_enable(boolean isEnabled) {
		IsEnabled = isEnabled;
	}

	public int getPC() {
		return PC;
	}

	public int setPC(int newPC) {
		PC = newPC;
	}

}