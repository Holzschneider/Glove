package de.dualuse.glove;

import java.util.concurrent.ConcurrentLinkedQueue;

public class IteratorTest {
	public static void main(String[] args) {
		
		ConcurrentLinkedQueue<String> test = new ConcurrentLinkedQueue<String>();
		
		test.add("hallo");
		test.add("heile");
		test.add("welt");
		
		int i=0;
		for (String s: test) {
			if (i++==1)
				test.add("dada");
			System.out.println(s);
		}
		
		
	}
}
