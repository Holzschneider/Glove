package de.dualuse.glove;

import org.junit.Test;

public class BoundingRectangleTest {

	@Test
	public void setTest() {
		BoundingRectangle br = new BoundingRectangle();
		
		br.reset().union(100, 300, 200, 400);
		
	}

	
	@Test
	public void unionTest() {
		
		
	}
	

	public static void main(String[] args) {
		BoundingRectangle br = new BoundingRectangle();
		br.union(100, 300, 200, 400);
		System.out.println(br);
		
//		br.subtract(100, 300, 150, 450);
		
//		br.chop(100*100);
		System.out.println(br);
	}
	
	
}
