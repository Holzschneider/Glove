package de.dualuse.glove;

import java.nio.*;

public interface BufferStack extends AutoCloseable {
	
	public BufferStack push();
	
	public BufferStack pop();
	
	public ByteBuffer malloc(int size);
	public ByteBuffer bytes(byte a);
	public ByteBuffer bytes(byte a,byte b);
	public ByteBuffer bytes(byte a,byte b,byte c);
	public ByteBuffer bytes(byte a,byte b,byte c,byte d);
	public ByteBuffer bytes(byte... values);
	
	public ShortBuffer mallocShort(int num);
	public ShortBuffer shorts(short a);
	public ShortBuffer shorts(short a,short b);
	public ShortBuffer shorts(short a,short b,short c);
	public ShortBuffer shorts(short a,short b,short c,short d);
	public ShortBuffer shorts(short... values);
	
	public IntBuffer mallocInt(int num);
	public IntBuffer ints(int a);
	public IntBuffer ints(int a,int b);
	public IntBuffer ints(int a,int b,int c);
	public IntBuffer ints(int a,int b,int c,int d);
	public IntBuffer ints(int... values);
	
	public FloatBuffer mallocFloat(int num);
	public FloatBuffer floats(float a);
	public FloatBuffer floats(float a,float b);
	public FloatBuffer floats(float a,float b,float c);
	public FloatBuffer floats(float a,float b,float c,float d);
	public FloatBuffer floats(float... values);
	
	public DoubleBuffer mallocDouble(int num);
	public DoubleBuffer doubles(double a);
	public DoubleBuffer doubles(double a,double b);
	public DoubleBuffer doubles(double a,double b,double c);
	public DoubleBuffer doubles(double a,double b,double c,double d);
	public DoubleBuffer doubles(double... values);
	
	public ByteBuffer malloc(int alignment, int size);	
	public void close();
	
	
	abstract class AbstractBufferStack implements BufferStack {
		@Override public ByteBuffer bytes(byte a) { return malloc(1).put(0,a); }
		@Override public ByteBuffer bytes(byte a,byte b) { return malloc(2).put(0,a).put(0,b); }
		@Override public ByteBuffer bytes(byte a,byte b,byte c) { return malloc(3).put(0,a).put(0,b).put(0,c); }
		@Override public ByteBuffer bytes(byte a,byte b,byte c,byte d) { return malloc(4).put(0,a).put(0,b).put(0,d); }
		@Override public ByteBuffer bytes(byte... values) { return (ByteBuffer) malloc(values.length).put(values).flip(); }
			
		@Override public ShortBuffer shorts(short a) { return mallocShort(1).put(0,a); }
		@Override public ShortBuffer shorts(short a,short b) { return mallocShort(2).put(0,a).put(0,b); }
		@Override public ShortBuffer shorts(short a,short b,short c) { return mallocShort(3).put(0,a).put(0,b).put(0,c); }
		@Override public ShortBuffer shorts(short a,short b,short c,short d) { return mallocShort(4).put(0,a).put(0,b).put(0,d); }
		@Override public ShortBuffer shorts(short... values) { return (ShortBuffer) mallocShort(values.length).put(values).flip(); }
			
		@Override public IntBuffer ints(int a){ return mallocInt(1).put(0,a); }
		@Override public IntBuffer ints(int a,int b) { return mallocInt(2).put(0,a).put(0,b); }
		@Override public IntBuffer ints(int a,int b,int c) { return mallocInt(3).put(0,a).put(0,b).put(0,c); }
		@Override public IntBuffer ints(int a,int b,int c,int d) { return mallocInt(4).put(0,a).put(0,b).put(0,d); }
		@Override public IntBuffer ints(int... values) { return (IntBuffer) mallocInt(values.length).put(values).flip(); }
		
		@Override public FloatBuffer floats(float a){ return mallocFloat(1).put(0,a); }
		@Override public FloatBuffer floats(float a,float b) { return mallocFloat(2).put(0,a).put(0,b); }
		@Override public FloatBuffer floats(float a,float b,float c) { return mallocFloat(3).put(0,a).put(0,b).put(0,c); }
		@Override public FloatBuffer floats(float a,float b,float c,float d) { return mallocFloat(4).put(0,a).put(0,b).put(0,d); }
		@Override public FloatBuffer floats(float... values) { return (FloatBuffer) mallocFloat(values.length).put(values).flip(); }
		
		@Override public DoubleBuffer doubles(double a){ return mallocDouble(1).put(0,a); }
		@Override public DoubleBuffer doubles(double a,double b) { return mallocDouble(2).put(0,a).put(0,b); }
		@Override public DoubleBuffer doubles(double a,double b,double c) { return mallocDouble(3).put(0,a).put(0,b).put(0,c); }
		@Override public DoubleBuffer doubles(double a,double b,double c,double d) { return mallocDouble(4).put(0,a).put(0,b).put(0,d); }
		@Override public DoubleBuffer doubles(double... values) { return (DoubleBuffer) mallocDouble(values.length).put(values).flip(); }
	}
}
