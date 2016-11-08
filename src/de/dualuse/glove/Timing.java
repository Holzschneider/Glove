package de.dualuse.glove;

import java.util.concurrent.TimeUnit;

public interface Timing {
	
	final public static Timing IMMEDIATE = new Immediate();
	
	public double time();
	
	
	public static class Immediate implements Timing {
		protected final long start;

		public Immediate() { this(System.nanoTime(), TimeUnit.NANOSECONDS); }
		public Immediate(long reference, TimeUnit tu) { 
			start = tu.toNanos(reference);
		}
		
		public double time() {
			return (System.nanoTime()-start)/1e9;
		}
	}
	
	
	public static class Manual implements Timing {
		public long time;
		
		public Manual(long initialTime, TimeUnit tu) {
			time = tu.toNanos(initialTime);
		}

		public Manual time(long time, TimeUnit tu) {
			this.time = tu.toNanos(time);
			return this;
		}
		
		public double time() {
			return time/1e9;
		}
	}
	
	
	public static class FrameTime extends Immediate {
		public FrameTime() { }
		public FrameTime(long reference, TimeUnit tu) {
			super(reference, tu);
		}
		
		long last = start;
		public FrameTime swap(long when, TimeUnit tu) {
			last = tu.toNanos(when);
			return this;
		}
		
		public FrameTime swap() {
			return swap(System.nanoTime(), TimeUnit.NANOSECONDS);
		}
		
		public double time() {
			return (last-start)/1e9;
		}
		
	}
	
	
	
//	static private final ThreadLocal<Timing> timingContexts = new ThreadLocal<Timing> () {
//		protected Timing initialValue() {
//			return new Timing();
//		};
//	};
//	
//	long start = System.nanoTime();
//	long frame = 0;
//	public Timing() { }
//	
//	public Timing swap(long time, TimeUnit tu) {
//		frame = tu.toNanos(time);
//		return this;
//	}
//	
//	public Timing swap() {
//		return swap(System.nanoTime(), TimeUnit.NANOSECONDS);
//	}
//	
//	public double time() {
//		if (frame == 0)
//			return System.nanoTime()/1e9;
//		else
//			return frame;
//	}
//	
//
//	public static Timing current() {
//		return timingContexts.get();
//	}
}
