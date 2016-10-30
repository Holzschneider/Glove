package de.dualuse.glove;

public class FlowControl {
	public static final FlowControl UNLIMITED = new FlowControl(Long.MAX_VALUE, 1);
	public static final FlowControl DEFAULT = new FlowControl(1000000000, (long)(1e9));
	
	
	public FlowControl(long bandwidth, long timeframe) {
		this.bandwidth = bandwidth;
		this.timeframe = timeframe;
	}
	
	LongQueue portions = new LongQueue();
	LongQueue timestamps = new LongQueue();
	
	long timeframe = 0;
	long bandwidth = 0;
	long pending = 0;
	long load = 0;
	

	public void enqueue(long size) {
		pending += size;
	}


	public void dequeue(long size) {
		pending -= size;
	}
	


	public long allocate(long when, long portion) {
		
		for (long timestamp = timestamps.peek();timestamps.size()>0 && timestamp<=when-timeframe;timestamp = timestamps.poll())
			load-=portions.poll();
		
		long loadstamp = when;
		if (timestamps.size()>0)
			loadstamp = timestamps.peek();
		
		long timepassed = when-loadstamp;
		long timeleft = ((timeleft=(timeframe-timepassed))>timeframe?timeframe:timeleft)<0?0:timeleft;
		
		long spaceleft = (long)(((spaceleft=(bandwidth-load))<0?0:spaceleft));
		
		double spaceleftPerTimeleftAsTimePassed = spaceleft*1.0/(timeleft/(timepassed+1.0));
		
		
		long allowed = (long)(portion*spaceleftPerTimeleftAsTimePassed/pending);
		
		
		long allocated = allowed<portion?allowed:portion;
		
		pending -= allocated;
		
		load += allocated*0.5;
		portions.push(allocated);
		timestamps.push(when);
		
		return allocated;
	}
	
	
}
