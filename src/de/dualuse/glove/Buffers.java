package de.dualuse.glove;

import java.nio.*;

public class Buffers {
	
	final static ByteOrder NATIVE_ORDER = ByteOrder.nativeOrder();
	
	final static ThreadLocal<FloatBuffer> matrix4f = new ThreadLocal<FloatBuffer>() {
		@Override public FloatBuffer get() {
			FloatBuffer buffer = super.get();
			buffer.clear();
			return buffer;
		}
		
		@Override protected FloatBuffer initialValue() {
			return ByteBuffer.allocateDirect(16*4).order(ByteOrder.nativeOrder()).asFloatBuffer();
		};
	};
	
	public static FloatBuffer matrix4f(float[] values) {
		FloatBuffer buffer = matrix4f.get();
		buffer.put(values);
		buffer.flip();
		return buffer;
	}
	
	final static ThreadLocal<IntBuffer> name = new ThreadLocal<IntBuffer>() {
		@Override public IntBuffer get() {
			IntBuffer buffer = super.get();
			buffer.clear();
			return buffer;
		}
		
		@Override protected IntBuffer initialValue() {
			return ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
		};
	};

	public static IntBuffer name(int i) {
		IntBuffer buffer = name.get();
		buffer.put(1);
		buffer.flip();
		return buffer;
	}
	
	public static IntBuffer name() {
		IntBuffer buffer = name.get();
		return buffer;
	}
	
	
	ByteBuffer malloc(int capacity) {
		return ByteBuffer.allocateDirect(capacity).order(NATIVE_ORDER);
	}
	
	public void free(Buffer b) { }
	
}
