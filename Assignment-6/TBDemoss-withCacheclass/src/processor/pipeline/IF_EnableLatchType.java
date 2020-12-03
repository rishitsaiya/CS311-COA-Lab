package processor.pipeline;

public class IF_EnableLatchType {
	
	boolean IF_enable;
	boolean isBusy;
	
	public IF_EnableLatchType() {
		IF_enable = true;
		isBusy = false;
	}

	public boolean checkBusy() {
		return isBusy;
	}

	public boolean isIF_enable() {
		return IF_enable;
	}

	public IF_EnableLatchType(boolean iF_enable, boolean isBusy) {
		this.IF_enable = iF_enable;
		this.isBusy = isBusy;
	}

	public void setIF_enable(boolean iF_enable) {
		IF_enable = iF_enable;
	}

}
