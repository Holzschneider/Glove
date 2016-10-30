package de.dualuse.glove;

import static android.opengl.GLES20.*;

public class GLShader {
	final static public int VERTEX_SHADER = GL_VERTEX_SHADER;
	final static public int FRAGMENT_SHADER = GL_FRAGMENT_SHADER;
	
	
	final int type;
	final String code;
	
	int name = 0;
	
	public GLShader(int type, String code) {
		this.type = type;
		this.code = code;
	}
	

	int glCompileShader() {
		if (name!=0)
			return name;
		
		name = android.opengl.GLES20.glCreateShader(type);
		android.opengl.GLES20.glShaderSource(name, code);
		android.opengl.GLES20.glCompileShader(name);

		int[] params = new int[1];
		android.opengl.GLES20.glGetShaderiv(name, GL_COMPILE_STATUS, params, 0);
		if (params[0]!=GL_TRUE)
			System.out.println( android.opengl.GLES20.glGetShaderInfoLog(name) );
		
		return name;
	}
	
}
