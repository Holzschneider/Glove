package de.dualuse.glove;

import java.nio.*;
import java.util.Arrays;

import de.dualuse.glove.BufferStack.AbstractBufferStack;

public class DirectBufferStack extends AbstractBufferStack {
	
	static final public ThreadLocal<DirectBufferStack> stacks = new ThreadLocal<DirectBufferStack>() {
		protected DirectBufferStack initialValue() {
			return new DirectBufferStack();
		};
	};
	
	////////////////////
	
	private ByteBuffer stack = ByteBuffer.allocateDirect(64*1024).order(ByteOrder.nativeOrder());
	
	
	int frames[] = new int[8], index = 0, offset = 0;
	
	public DirectBufferStack push() {
		if (frames.length==index+1)
			frames = Arrays.copyOf(frames, index*3/2);
		
		frames[index++] = offset;
		
		return this;
	}
	
	public DirectBufferStack pop() {
		offset = frames[--index];
		
		return this;
	}
	
	public ByteBuffer malloc(int size) { return this.malloc(1, size); }
	public ShortBuffer mallocShort(int num) { return malloc(2, num<<1).asShortBuffer(); }
	public IntBuffer mallocInt(int num) { return malloc(4, num<<2).asIntBuffer(); }
	public FloatBuffer mallocFloat(int num) { return malloc(4, num<<2).asFloatBuffer(); }
	public DoubleBuffer mallocDouble(int num) { return malloc(8, num<<3).asDoubleBuffer(); }
	
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
