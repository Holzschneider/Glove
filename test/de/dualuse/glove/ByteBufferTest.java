package de.dualuse.glove;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteBufferTest {

	public static void main(String[] args) {
		
		ByteBuffer buffer = ByteBuffer.allocateDirect(100).order(ByteOrder.nativeOrder());

		System.out.println(buffer.order());

		System.out.println(buffer.slice().order());
		
		
	}
}
