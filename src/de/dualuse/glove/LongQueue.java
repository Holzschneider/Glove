package de.dualuse.glove;

public class LongQueue {
	private int head = 0, tail = 0;
	private long items[] = null;
	
	public LongQueue() { this(16); }
	public LongQueue(int initialCapacity) { items = new long[initialCapacity]; }
	
	
	public void push(long item) {
		if (head-tail==items.length) {
			long more[] = new long[items.length*3/2];
			
			for (int i=tail,l=items.length,L=more.length;i<head;i++)
				more[i%L] = items[i%l];
			
			items = more;
		}
		
		items[head++%items.length] = item;
	}
	
	public int size() {
		return head-tail;
	}
	
	public long poll() {
		return items[tail++%items.length];
	}
	
	public long peek() {
		return items[tail%items.length];
	}

	public String toString() {
		StringBuilder sb = new StringBuilder(14+size()*4);
		
		sb.append("LongQueue(");
		
		for (int i=tail;i<head;i++)
			if (i==tail) sb.append(items[i%items.length]);
			else sb.append(',').append(items[i%items.length]);
		
		sb.append(")");
		
		return sb.toString();
	}
}
