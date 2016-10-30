package de.dualuse.glove;

import android.opengl.GLES20;

public class GLUniform {
	int location;
	final String name;
	
	public GLUniform(String name) { this.name = name; }
	
	public int glGetUniformLocation(GLProgram p) { return location = android.opengl.GLES20.glGetUniformLocation(p.program, name); }
	
	public void glUniform1f(float x) { GLES20.glUniform1f(location, x); }
	public void glUniform2f(float x, float y) { GLES20.glUniform2f(location, x, y); }
	public void glUniform3f(float x, float y, float z) { GLES20.glUniform3f(location, x, y, z); }
	public void glUniform4f(float x, float y, float z, float w) { GLES20.glUniform4f(location, x, y, z, w); }
//	public void glUniform4fv(float... v) { GLES20.glUniform4fv(location, v.length/4, v, 0); }
	
	public void glUniform1i(int x) { GLES20.glUniform1i(location, x); }
	public void glUniform2i(int x, int y) { GLES20.glUniform2i(location, x, y); }
	public void glUniform3i(int x, int y, int z) { GLES20.glUniform3i(location, x, y, z); }
	public void glUniform4i(int x, int y, int z, int w) { GLES20.glUniform4i(location, x, y, z, w); }
//	public void glUniform4iv(int... v) { GLES20.glUniform4iv(location, v.length/4, v, 0); }
	
	public void glUniformMatrix2fv(float... v) { GLES20.glUniformMatrix2fv(location, v.length/4, false, v, 0); }
	public void glUniformMatrix2fv(boolean transpose, float... v) { GLES20.glUniformMatrix2fv(location, v.length/4, transpose, v, 0); }

	public void glUniformMatrix3fv(float... v) { GLES20.glUniformMatrix3fv(location, v.length/9, false, v, 0); }
	public void glUniformMatrix3fv(boolean transpose, float... v) { GLES20.glUniformMatrix3fv(location, v.length/9, transpose, v, 0); }
	
	public void glUniformMatrix4fv(float... v) { GLES20.glUniformMatrix4fv(location, v.length/16, false, v, 0); }
	public void glUniformMatrix4fv(boolean transpose, float... v) { GLES20.glUniformMatrix4fv(location, v.length/16, transpose, v, 0); }
	
}


