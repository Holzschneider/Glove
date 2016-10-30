package de.dualuse.glove;


import static android.opengl.GLES20.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayDeque;
import java.util.Arrays;

public class GLProgram {
	final static GLProgram DEFAULT = new GLProgram(0);
	
	final ArrayDeque<GLShader> shaders = new ArrayDeque<GLShader>(3);
	final ArrayDeque<GLUniform> uniforms = new ArrayDeque<GLUniform>();
	final ArrayDeque<GLVertexAttribute> attributes = new ArrayDeque<GLVertexAttribute>();

	final static int UNINITIALIZED = -1;
	int program = UNINITIALIZED ;
	
	private GLProgram(int program) { this.program = program; }
	
	public GLProgram(GLShader... toBeAttached) {
		shaders.addAll(Arrays.asList(toBeAttached.clone()));
	}
	
	public void glUseProgram() {
		if (program==UNINITIALIZED )
			program = android.opengl.GLES20.glCreateProgram();
		
		if (!shaders.isEmpty()) {
			while (!shaders.isEmpty()) 
				android.opengl.GLES20.glAttachShader(program, shaders.pollFirst().glCompileShader());
			
			android.opengl.GLES20.glLinkProgram(program);
			
//			int[] params = new int[1];
			IntBuffer params = ByteBuffer.allocateDirect(4).order(ByteOrder.nativeOrder()).asIntBuffer();
			android.opengl.GLES20.glGetProgramiv(program, GL_COMPILE_STATUS, params);
//			android.opengl.GLES20.glGetProgramiv(program, GL_COMPILE_STATUS, params);
			
			if (params.get(0)!=GL_TRUE) 
				System.out.println( android.opengl.GLES20.glGetProgramInfoLog(program) );
		}
		
		while (!uniforms.isEmpty())
			uniforms.poll().glGetUniformLocation(this);
		
		while (!attributes.isEmpty())
			attributes.poll().glGetAttribLocation(this);
		
		android.opengl.GLES20.glUseProgram(program);
	}
	
	
	public GLUniform getUniform(String name) {
		GLUniform u = new GLUniform(name);
		uniforms.add(u);
		return u;
	}
	
	public GLVertexAttribute getAttribute(String name) {
		GLVertexAttribute v = new GLVertexAttribute(name);
		attributes.add(v);
		return v;
	}
	
	
}
