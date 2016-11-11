package de.dualuse.glove;

import java.nio.*;
import java.util.Arrays;

public class HeapStack implements AutoCloseable {
	
	static final public ThreadLocal<HeapStack> allocators = new ThreadLocal<HeapStack>() {
		protected HeapStack initialValue() {
			return new HeapStack(32*1024);
		};
	};
	
	public static HeapStack stackPush() {
		return allocators.get().push();
	}

	////////////////////
	
	private ByteBuffer stack;
	
	protected HeapStack(int capacity) {
		stack = ByteBuffer.allocateDirect(capacity).order(ByteOrder.nativeOrder());
	}
	
	int frames[] = new int[8], index = 0, offset = 0;
	
	public HeapStack push() {
		if (frames.length==index+1)
			frames = Arrays.copyOf(frames, index*3/2);
		
		frames[index++] = offset;
		
		return this;
	}
	
	public HeapStack pop() {
		offset = frames[--index];
		
		return this;
	}
	
	public ByteBuffer malloc(int size) { return this.malloc(1, size); }
	public ByteBuffer bytes(byte a) { return (ByteBuffer) malloc(1).put(a).flip(); }
	public ByteBuffer bytes(byte a,byte b) { return (ByteBuffer) malloc(2).put(a).put(b).flip(); }
	public ByteBuffer bytes(byte a,byte b,byte c) { return (ByteBuffer) malloc(1).put(a).put(b).put(c).flip(); }
	public ByteBuffer bytes(byte a,byte b,byte c,byte d) { return (ByteBuffer) malloc(1).put(a).put(b).put(c).put(d).flip(); }
	public ByteBuffer bytes(byte... values) { return (ByteBuffer) malloc(values.length).put(values).flip(); }
	
//	public ShortBuffer mallocShort(int num) { return malloc(num<<2).asShortBuffer(); }
//	public ShortBuffer shorts(short a) { return (ShortBuffer) mallocShort(1).put(a).flip(); }
//	public ShortBuffer shorts(short a,short b) { return (ShortBuffer) mallocShort(2).put(a).put(b).flip(); }
//	public ShortBuffer shorts(short a,short b,short c) { return (ShortBuffer) mallocShort(1).put(a).put(b).put(c).flip(); }
//	public ShortBuffer shorts(short a,short b,short c,short d) { return (ShortBuffer) mallocShort(1).put(a).put(b).put(c).put(d).flip(); }
//	public ShortBuffer shorts(short... values) { return (ShortBuffer) mallocShort(values.length).put(values).flip(); }
	
	public IntBuffer mallocInt(int num) { return malloc(num<<2).asIntBuffer(); }
	public IntBuffer ints(int a) { return (IntBuffer) mallocInt(1).put(a).flip(); }
	public IntBuffer ints(int a,int b) { return (IntBuffer) mallocInt(2).put(a).put(b).flip(); }
	public IntBuffer ints(int a,int b,int c) { return (IntBuffer) mallocInt(1).put(a).put(b).put(c).flip(); }
	public IntBuffer ints(int a,int b,int c,int d) { return (IntBuffer) mallocInt(1).put(a).put(b).put(c).put(d).flip(); }
	public IntBuffer ints(int... values) { return (IntBuffer) mallocInt(values.length).put(values).flip(); }
	
	public FloatBuffer mallocFloat(int num) { return malloc(num<<2).asFloatBuffer(); }
	public FloatBuffer floats(float a) { return (FloatBuffer) mallocFloat(1).put(a).flip(); }
	public FloatBuffer floats(float a,float b) { return (FloatBuffer) mallocFloat(2).put(a).put(b).flip(); }
	public FloatBuffer floats(float a,float b,float c) { return (FloatBuffer) mallocFloat(1).put(a).put(b).put(c).flip(); }
	public FloatBuffer floats(float a,float b,float c,float d) { return (FloatBuffer) mallocFloat(1).put(a).put(b).put(c).put(d).flip(); }
	public FloatBuffer floats(float... values) { return (FloatBuffer) mallocFloat(values.length).put(values).flip(); }
	
	public DoubleBuffer mallocDouble(int num) { return malloc(num<<3).asDoubleBuffer(); }
	public DoubleBuffer doubles(double a) { return (DoubleBuffer) mallocDouble(1).put(a).flip(); }
	public DoubleBuffer doubles(double a,double b) { return (DoubleBuffer) mallocDouble(2).put(a).put(b).flip(); }
	public DoubleBuffer doubles(double a,double b,double c) { return (DoubleBuffer) mallocDouble(1).put(a).put(b).put(c).flip(); }
	public DoubleBuffer doubles(double a,double b,double c,double d) { return (DoubleBuffer) mallocDouble(1).put(a).put(b).put(c).put(d).flip(); }
	public DoubleBuffer doubles(double... values) { return (DoubleBuffer) mallocDouble(values.length).put(values).flip(); }
	
	
	
	public ByteBuffer malloc(int alignment, int size) {
		
		int from = offset + (alignment-offset%alignment)%alignment;
		int to = from+size;
		
		if (to>stack.capacity())
			stack = ByteBuffer.allocateDirect(to*3/2).order(ByteOrder.nativeOrder());
		
		ByteBuffer pointer = stack.slice().order(ByteOrder.nativeOrder());
		pointer.position(from);
		pointer.limit(to);
		
		offset = to;
		
		return pointer;
	}
	
	@Override
	public void close() {
		pop();
	}
	
}
