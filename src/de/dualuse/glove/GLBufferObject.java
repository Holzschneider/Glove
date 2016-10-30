package de.dualuse.glove;


import static android.opengl.GLES20.*;
import static android.opengl.GLES20.GL_STATIC_DRAW;

import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class GLBufferObject {
	final static public int ARRAY_BUFFER = GL_ARRAY_BUFFER;
	final static public int ELEMENT_ARRAY_BUFFER = GL_ELEMENT_ARRAY_BUFFER;

	public Exception transferException = null;
	
	public static GLBufferObject DEFAULT = new GLBufferObject(0);
	
	int[] bufferName = null;
	
	public GLBufferObject() { }
	private GLBufferObject( int name ) { this.bufferName = new int[] { name }; }
	
	public GLBufferObject(Buffer data) { dispatchBufferData(data.remaining(), data, GL_STATIC_DRAW); }
	public GLBufferObject(int size, Buffer data) { dispatchBufferData(size, data, GL_STATIC_DRAW); }
	public GLBufferObject(int size, Buffer data, int usage) { dispatchBufferData(size, data, usage); }
		
	public void glBindBuffer(int target) {
		if (bufferName==null)
			android.opengl.GLES20.glGenBuffers(1, bufferName, 0);
		
		android.opengl.GLES20.glBindBuffer(target, bufferName[0]);
		
		if (isBufferDataPending()) {
			if (bufferSize!=dataSize || bufferUsage!=dataUsage) 
				android.opengl.GLES20.glBufferData(target, bufferSize = dataSize, data, bufferUsage = dataUsage);
			else
				if (data!=null)
					android.opengl.GLES20.glBufferSubData(target, 0, dataSize, data);

			data = null;
			
			if (source!=null) try {
				TRANSFER_BUFFER.clear();
				for (	int offset=0,
						bytesRead=source.read(TRANSFER_BUFFER);
						
						TRANSFER_BUFFER.hasRemaining();
						
						TRANSFER_BUFFER.clear(),
						offset+=bytesRead,
						bytesRead=source.read(TRANSFER_BUFFER))
					
					android.opengl.GLES20.glBufferSubData(target, offset, TRANSFER_BUFFER_SIZE, TRANSFER_BUFFER.rewind());
			} catch (Exception ex) {
				System.out.println(transferException = ex);
			}
			
			source = null;
			updateCount = modCount;
		}
	}
	
	int updateCount = 0;
	int bufferSize = 0, bufferUsage = 0;
	
	int modCount = 0;
	
	public final static int TRANSFER_BUFFER_SIZE = 65536; //takes 4 Frames to upload a 256x256x4 texture
	public static ByteBuffer TRANSFER_BUFFER = ByteBuffer.allocateDirect(TRANSFER_BUFFER_SIZE).order(ByteOrder.nativeOrder()); 
	
	int dataSize;
	int dataUsage;
	Buffer data;
	ReadableByteChannel source;
	
	public void dispatchBufferData(int size, InputStream is, int usage)  
	{ this.dispatchBufferData(size, Channels.newChannel(is), usage); }
	
	public void dispatchBufferData(int dataSize, ReadableByteChannel channel, int usage) {
//		ByteBuffer target = ByteBuffer.allocateDirect(chunkSize).order(ByteOrder.nativeOrder());
//		channel.read(target);
//		data = target;
		
		this.source = channel;
		dispatchBufferData(dataSize, data, usage);
	}
	
	public void dispatchBufferData(int size, Buffer data, int usage) {
		this.dataSize = size;
		this.data = data;
		this.dataUsage = usage;
		
		modCount++;
	}
	
	
	abstract public class BufferUpdate {
		public GLBufferObject dispatch() {
//			updates.offer(this);
//			return GLTexture.this;
			return GLBufferObject.this;
		}
		
		abstract void execute();
	}

	
	public class BufferData extends BufferUpdate {
		public BufferData(Buffer src, int usage) { }
		public BufferData(Buffer[] src, int usage) { }

		void execute() {
			
		}
	}
	
	
	
	
	final public boolean isBufferDataPending() {
		return !isValid();
	}
	
	public boolean isValid() {
		return updateCount==modCount;
	}
	
	
	static public GLBufferObject[] glGenBuffers(int n) {
		GLBufferObject[] buffers = new GLBufferObject[n];
		int names[] = new int[n];
		
		android.opengl.GLES20.glGenBuffers(n, names, 0);
		
		for (int i=0;i<n;i++)
			buffers[i] = new GLBufferObject(names[i]);
		
		return buffers;
	}
	
	
}
