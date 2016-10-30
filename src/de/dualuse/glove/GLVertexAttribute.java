package de.dualuse.glove;

//import static android.opengl.GLES20.*;

import java.nio.Buffer;
//import java.nio.ByteBuffer;

public class GLVertexAttribute {
	
	public final static int BYTE = android.opengl.GLES20.GL_BYTE;
	public final static int SHORT = android.opengl.GLES20.GL_SHORT;
	public final static int INT = android.opengl.GLES20.GL_INT;
	public final static int FLOAT = android.opengl.GLES20.GL_FLOAT;
	
	public final static int UNSIGNED_BYTE = android.opengl.GLES20.GL_UNSIGNED_SHORT;
	public final static int UNSIGNED_SHORT = android.opengl.GLES20.GL_UNSIGNED_SHORT;
	public final static int UNSIGNED_INT = android.opengl.GLES20.GL_UNSIGNED_INT;
	
	
	public int location = -1;
	private final String name;
	
	public GLVertexAttribute(String name) { this.name = name; }
	
	public int glGetAttribLocation(GLProgram p) { return location = android.opengl.GLES20.glGetAttribLocation(p.program, name); }
	
	public void glEnableVertexAttribArray() { android.opengl.GLES20.glEnableVertexAttribArray(location); };
	public void glDisableVertexAttribArray() { android.opengl.GLES20.glEnableVertexAttribArray(location); };
	
	public void glVertexAttribPointer(int size, int type, boolean normalize, int stride, Buffer src) 
	{ android.opengl.GLES20.glVertexAttribPointer(location, size, type, false, stride, src); }

	public void glVertexAttribPointer(int size, int type, boolean normalize, int stride, int offset) 
	{ android.opengl.GLES20.glVertexAttribPointer(location, size, type, false, stride, offset); }
	
//	public void enable() { enableVertexAttribArray(); };
//	public void disable() { enableVertexAttribArray(); };
//	
//	public void point(int size, int type, boolean normalized, int stride, Buffer src) { glVertexAttribPointer(size, type, normalized, stride, src); }
//	public void point(int size, int type, boolean normalized, int stride, int offset) { glVertexAttribPointer(size, type, normalized, stride, offset); }
	
}
