package bounceable.util;

import java.util.List;

public class BounceBlock {
	
	List<Integer> metaList = null;
	double limitFactor = 0.0D;
	double gainFactor = 0.0D;
	
	public BounceBlock() {}
	
	public void setMeta(List<Integer> meta) {
		this.metaList = meta;
	}
	
	public void setLimitFactor(double limit) {
		this.limitFactor = limit;
	}
	
	public void setGainFactor(double gain) {
		this.gainFactor = gain;
	}
	
	public List<Integer> getMeta() {
		return this.metaList;
	}
	
	public double getLimitFactor() {
		return this.limitFactor;
	}
	
	public double getGainFactor() {
		return this.gainFactor;
	}
}