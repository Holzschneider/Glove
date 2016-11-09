package de.dualuse.glove;

import java.util.concurrent.TimeUnit;

import de.dualuse.collections.LongQueue;

public interface FlowControl {
	public static final FlowControl UNLIMITED = new Fixed(Long.MAX_VALUE);
	public static final FlowControl DEFAULT = new Bandwidth(100*1000*1000, 1, TimeUnit.SECONDS);
	
	public void announce(long size);
	public long allocate(long portion);
	
	public static class Fixed implements FlowControl {
		final long fixed; 
		
		public Fixed(long fixedSize) {
			this.fixed = fixedSize;
		}
		
		public long allocate(long portion) {
			return fixed;
		}
		
		public void announce(long size) { }

	}
	
	public static class Chunked implements FlowControl {
		final long max; 
		
		public Chunked(long chunksize) {
			this.max = chunksize;
		}
		
		public long allocate(long portion) {
			return portion<max?portion:max;
		}
		
		public void announce(long size) { }

	}
	
	public static class Bandwidth implements FlowControl {
		public Bandwidth(long bandwidth, long timeframe, TimeUnit u) {
			this(bandwidth,timeframe, u, Timing.IMMEDIATE);
		}
		
		public Bandwidth(long bandwidth, long timeframe, TimeUnit u, Timing t) {
			this.timing = t;
			this.bandwidth = bandwidth;
			this.timeframe = u.toNanos(timeframe);
		}

		final Timing timing;
		
		LongQueue portions = new LongQueue();
		LongQueue timestamps = new LongQueue();
		
		long timeframe = 0;
		long bandwidth = 0;
		long pending = 0;
		long load = 0;
		

		public void announce(long size) {
			pending += size;
		}


		public long allocate(long portion) {
			return allocate(portion, (long)(timing.time()*1e9));
		}
		
		public long allocate(long portion, long when) {
			
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
	
}
