package de.dualuse.glove;

import de.dualuse.collections.IntQueue;

public class IntQueueTest {

	public static void main(String[] args) {
		
		IntQueue iq = new IntQueue(6);
		
		iq.push(1); System.out.println(iq);
		iq.push(2); System.out.println(iq);
		iq.push(3); System.out.println(iq);
		iq.push(4); System.out.println(iq);
		iq.push(5); System.out.println(iq);
		iq.push(6); System.out.println(iq);
		iq.push(7); System.out.println(iq);
		iq.push(8); System.out.println(iq);
		
		for (int i=0;iq.size()>0;i++)
			System.out.println( iq.poll() +": "+iq);
		
	}
}
