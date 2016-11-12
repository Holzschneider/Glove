package de.dualuse.glove;

import java.nio.*;
import java.util.Arrays;

public class ArrayBufferStack extends BufferStack.AbstractBufferStack {
	static public final ThreadLocal<ArrayBufferStack> stacks = new ThreadLocal<ArrayBufferStack>() {
		protected ArrayBufferStack initialValue() {
			return new ArrayBufferStack();
		};
	};
	
	
	
	public static final int DEFAULT_CAPACITY = 1024;
	public static final int DEFAULT_DEPTH = 16;
	
	short[] shorts = new short[DEFAULT_CAPACITY];
	int[] ints = new int[DEFAULT_CAPACITY];
	byte[] bytes= new byte[DEFAULT_CAPACITY];
	float[] floats = new float[DEFAULT_CAPACITY];
	double[] doubles = new double[DEFAULT_CAPACITY];
	
	
	int frameIndex = 0, length = DEFAULT_DEPTH;
	int shortFrames[] = new int[length], shortPointer = 0;
	int intFrames[] = new int[length], intPointer  = 0;
	int byteFrames[] = new int[length], bytePointer  = 0;
	int floatFrames[] = new int[length], floatPointer  = 0;
	int doubleFrames[] = new int[length], doublePointer  = 0;
	
	
	@Override
	public ArrayBufferStack push() {
		if (frameIndex+1==length) {
			length = length*3/2;
			shortFrames = Arrays.copyOf(shortFrames, length);
			intFrames = Arrays.copyOf(intFrames, length);
			byteFrames = Arrays.copyOf(byteFrames, length);
			floatFrames = Arrays.copyOf(floatFrames, length);
			doubleFrames = Arrays.copyOf(doubleFrames, length);
		}
		
		shortFrames[frameIndex] = shortPointer;
		intFrames[frameIndex] = intPointer;
		byteFrames[frameIndex] = bytePointer;
		floatFrames[frameIndex] = floatPointer;
		doubleFrames[frameIndex] = doublePointer;
		
		frameIndex++;
		return this;
	}

	@Override
	public ArrayBufferStack pop() {
		frameIndex--;
		shortPointer = shortFrames[frameIndex];
		intPointer = intFrames[frameIndex];
		bytePointer = byteFrames[frameIndex];
		floatPointer = floatFrames[frameIndex];
		doublePointer = doubleFrames[frameIndex];
		
		return this;
	}

	@Override
	public ByteBuffer malloc(int num) {
		if ( (bytePointer+=num) > bytes.length)
			bytes = new byte[bytePointer*3/2];
			
		return ByteBuffer.wrap(bytes, bytePointer-num, num);
	}

	@Override
	public ShortBuffer mallocShort(int num) {
		if ( (shortPointer+=num) > shorts.length)
			shorts = new short[shortPointer*3/2];
			
		return ShortBuffer.wrap(shorts, shortPointer-num, num);
	}

	@Override
	public IntBuffer mallocInt(int num) {
		if ( (intPointer+=num) > ints.length)
			ints = new int[intPointer*3/2];
			
		return IntBuffer.wrap(ints, intPointer-num, num);
	}

	@Override
	public FloatBuffer mallocFloat(int num) {
		if ( (floatPointer+=num) > floats.length)
			floats = new float[floatPointer*3/2];
			
		return FloatBuffer.wrap(floats, floatPointer-num, num);
	}


	@Override
	public DoubleBuffer mallocDouble(int num) {
		if ( (doublePointer+=num) > doubles.length)
			doubles = new double[doublePointer*3/2];
			
		return DoubleBuffer.wrap(doubles, doublePointer-num, num);
	}


	@Override
	public ByteBuffer malloc(int alignment, int num) {
		bytePointer = bytePointer + (alignment-bytePointer%alignment)%alignment;
		
		if ( (bytePointer+=num) > bytes.length)
			bytes = new byte[bytePointer*3/2];
			
		return ByteBuffer.wrap(bytes, bytePointer-num, num);
	}

	
	@Override public void close() { pop(); }
	

	
//	public static void main(String[] args) {
//		try (ArrayBufferStack abs = ArrayBufferStack.stacks.get().push() ) {
//			
//			System.out.println( abs.mallocInt(2) );
//			System.out.println( abs.mallocInt(2) );
//			
//			try (ArrayBufferStack bbs = abs.push() ) {
//				
//				System.out.println( bbs.mallocInt(1) );
//				System.out.println( bbs.mallocInt(1) );
//				
//			}
//
//			try (ArrayBufferStack cbs = abs.push() ) {
//				
//				System.out.println( cbs.mallocInt(1) );
//				System.out.println( cbs.mallocInt(1) );
//				
//			}
//		}
//	}
	
}







