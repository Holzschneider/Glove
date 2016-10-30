package de.dualuse.glove;

public class IntQueue {
	private int head = 0, tail = 0;
	private int items[] = null;
	
	public IntQueue() { this(16); }
	public IntQueue(int initialCapacity) { items = new int[initialCapacity]; }
	
	
	public void push(int item) {
		if (head-tail==items.length) {
			int more[] = new int[items.length*3/2];
			
			for (int i=tail,l=items.length,L=more.length;i<head;i++)
				more[i%L] = items[i%l];
			
			items = more;
		}
		
		items[head++%items.length] = item;
	}
	
	public int size() {
		return head-tail;
	}
	
	public int poll() {
		return items[tail++%items.length];
	}
	

	public String toString() {
		StringBuilder sb = new StringBuilder(14+size()*4);
		
		sb.append("IntQueue(");
		
		for (int i=tail;i<head;i++)
			if (i==tail) sb.append(items[i%items.length]);
			else sb.append(',').append(items[i%items.length]);
		
		sb.append(")");
		
		return sb.toString();
	}
}
