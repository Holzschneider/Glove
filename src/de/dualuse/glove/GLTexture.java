package de.dualuse.glove;


//import static org.lwjgl.opengl.GL12.*;
//import static org.lwjgl.opengl.GL13.*;
//import static org.lwjgl.opengl.GL14.*;
//import static org.lwjgl.opengl.GL15.*;
//import static org.lwjgl.opengl.GL20.*;
//import static org.lwjgl.opengl.GL21.*;
import static android.opengl.GLES20.*;
import static android.opengl.GLES30.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Arrays;
import java.util.Queue;


public abstract class GLTexture {
	public static enum TextureClamp {
		REPEAT(GL_REPEAT),
		MIRROR(GL_MIRRORED_REPEAT),
		CLAMP_TO_EDGE(GL_CLAMP_TO_EDGE);
		
		final int param;
		private TextureClamp(int param) {
			this.param = param;
		}
	}
	
	public static enum TextureFilter {
		NEAREST(GL_NEAREST),
		LINEAR(GL_LINEAR),
		LINEAR_MIPMAP_NEAREST(GL_LINEAR_MIPMAP_NEAREST),
		LINEAR_MIPMAP_LINEAR(GL_LINEAR_MIPMAP_LINEAR);
		
		final int param;
		private TextureFilter(int param) {
			this.param = param;
		}
	}
	
	
	protected static class TextureConfiguration {
		private Queue<Runnable> updates = null;
		private int[] textureName = null;
		private Texture[] sources = null;
		private int initialized = 0;
		private TextureTarget target = null;
		
		public TextureConfiguration(TextureTarget target, Texture[] sources) {
			this.target = target;
			this.sources = sources;
		}
	}
	
	
	protected final TextureConfiguration state;
	
	protected GLTexture(GLTexture copy) {
		this.state = copy.state;
	}
	
	public GLTexture(TextureTarget target, Texture... sources) {
		state = new TextureConfiguration(target, sources.clone());
	}
	
	public GLTexture reset() {
		state.initialized = 0;
		state.sources = new Texture[0];
		return this;
	}
	
	public GLTexture attach(Texture a) {
		final int I = state.sources.length;
		Texture[] attached = state.sources;
//		for (int i=0;i<I;i++)
//			if (attached[i]==a) {
//				attached[i] = sources[I-1];
//				attached[I-1] = a;
//				return this;
//			}
		
		attached = Arrays.copyOf(state.sources, I+1);
		attached[I] = a;
		state.sources = attached;
				
		return this;
	}
	
	
	public GLBoundTexture bindTexture() {
		if (state.textureName == null)
			glGenTextures(1, state.textureName = new int[1], 0);

		glBindTexture(state.target.binding, state.textureName[0]);
		
		Texture[] attached = state.sources;
		
		for (int i=0;i<state.initialized;i++)
			attached[i].update(state.target.planes, i);
		
		for (int i=state.initialized,I=attached.length;i<I;i++)
			attached[i].init(state.target.planes, i);
		
		state.initialized = attached.length;
		
		return new GLBoundTexture(this);
	}
	
	public void dispose() {
		glDeleteTextures(1, state.textureName, 0);
	}
	

	public GLTexture texParameter(final int pname, final float param) {
		state.updates.add(new Runnable() {
			public void run() {
				glTexParameterf(state.target.binding, pname, param);
			};
		});
		return this;
	}

	public GLTexture texParameter(final int pname, final int param) {
		state.updates.add(new Runnable() {
			public void run() {
				glTexParameteri(state.target.binding, pname, param);
			};
		});
		return this;
	}

	public GLTexture minFilter(TextureFilter f) { return texParameter(GL_TEXTURE_MIN_FILTER, f.param); }
	public GLTexture magFilter(TextureFilter f) { return texParameter(GL_TEXTURE_MAG_FILTER, f.param); }
	public GLTexture maxLevel(int i) { return texParameter(GL_TEXTURE_MAX_LEVEL, i); }
	
	
	public static class GLBoundTexture extends GLTexture {
		protected GLBoundTexture(GLTexture copy) {
			super(copy);
		}

		public GLBoundTexture texImage2D(int level, int internalformat, int width, int height, int border, int format, int type, ByteBuffer pixels) {
			for (int target: state.target.planes)
				glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels); 
			return this; 
		}

		public GLBoundTexture texImage2D(int level, int internalformat, int width, int height, int border, int format, int type, IntBuffer pixels) {
			for (int target: state.target.planes)
				glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels); 
			return this; 
		}
			
		public GLBoundTexture texImage2D(int level, int internalformat, int width, int height, int border, int format, int type, FloatBuffer pixels) { 
			for (int target: state.target.planes)
				glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels); 
			return this; 
		}
		
		public GLBoundTexture texImage2D(int level, Texture t) {
			t.init(state.target.planes, level);
			return this; 
		}
		
		
		// ------
		
		public GLBoundTexture texSubImage2D(int level, int xoffset, int yoffset, int width, int height, Texture t) {
			t.init(state.target.planes, level);
			return this; 
		}
		
		
		public GLBoundTexture texSubImage2D(int level, int xoffset, int yoffset, int width, int height, int format, int type, ByteBuffer pixels) {
			for (int target: state.target.planes)
				glTexSubImage2D(target, level,xoffset,yoffset,width,height,format, type, pixels);
			return this; 
		}

		public GLBoundTexture texSubImage2D(int level, int xoffset, int yoffset, int width, int height, int format, int type, IntBuffer pixels) {
			for (int target: state.target.planes)
				glTexSubImage2D(target, level,xoffset,yoffset,width,height,format, type, pixels);
			return this; 
		}
			
		public GLBoundTexture texSubImage2D(int level, int xoffset, int yoffset, int width, int height, int format, int type, FloatBuffer pixels) {
			for (int target: state.target.planes)
				glTexSubImage2D(target, level,xoffset,yoffset,width,height,format, type, pixels);
			return this; 
		}


		public GLBoundTexture texParameter(int pname, float param) { glTexParameterf(state.target.binding, pname, param); return this; }
		public GLBoundTexture texParameter(int pname, int param) { glTexParameteri(state.target.binding, pname, param); return this; }
		public GLBoundTexture minFilter(TextureFilter f) { return texParameter(GL_TEXTURE_MIN_FILTER, f.param); }
		public GLBoundTexture magFilter(TextureFilter f) { return texParameter(GL_TEXTURE_MAG_FILTER, f.param); }
		public GLBoundTexture maxLevel(int i) { return texParameter(GL_TEXTURE_MAX_LEVEL, i); }
		
		public GLBoundTexture generateMipmap() {
			glGenerateMipmap(state.target.binding);
			return this;
		}
	}



}
