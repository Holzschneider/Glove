package de.dualuse.glove;

import java.nio.Buffer;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class GLTexture {
	public static double STREAM_CAPACITY_LIMIT = 10;
	
	public Exception transferException = null;
	public static GLTexture DEFAULT = new GLTexture(0);
	
	private int[] textureName = null;
	private Queue<TextureUpdate> updates = new ConcurrentLinkedQueue<TextureUpdate>();
	
	public GLTexture() { }
	private GLTexture( int name ) { this.textureName = new int[] { name }; }
	
	public void glBindTexture(int target) {
		if (textureName==null)
			android.opengl.GLES20.glGenTextures(1, textureName = new int[1], 0);
		
		android.opengl.GLES20.glBindTexture(target, textureName[0]);

		TextureUpdate tu = updates.peek();
		if (tu!=null) { 
			tu.execute();
			updates.poll();
		}
		
	}
	
	static class TextureSetting {
		final public static TextureSetting NOP = new TextureSetting(null);
		TextureSetting next = null;
		public TextureSetting(TextureSetting next) { this.next = next; }
		void set() {};
		void apply() {
			if (next!=null) next.apply();
			this.set();
		}
	}

	abstract public class TextureUpdate {
		public GLTexture dispatch() {
			updates.offer(this);
			return GLTexture.this;
		}
		
		abstract void execute();
	}
	
	public class TexImage2D extends TextureUpdate {
		final int target, level, internalformat, width, height, format, border, type;
		final Buffer pixels;
		TextureSetting settings = TextureSetting.NOP;
		
		public TexImage2D(int target, int level, int internalformat, int width, int height, int border, int format, int type, Buffer pixels) {
			this.target = target;
			this.level = level;
			this.internalformat = internalformat;
			this.width = width;
			this.height = height;
			this.border = border;
			this.format = format;
			this.type = type;
			this.pixels = pixels;
		}
		
		public TexImage2D generateMipmap() {
			settings = new TextureSetting(settings) { void set() { android.opengl.GLES20.glGenerateMipmap(target); } };
			return this;
		}
		
		public TexImage2D texParameter(final int param, final int value) {
			settings = new TextureSetting(settings) { void set() { android.opengl.GLES20.glTexParameteri(target,param,value); } };
			return this; 
		}
		
		public TexImage2D stream(FlowControl f, StreamProgress p) {
			
			return this;
		}
		
		
		
		void execute() {
			android.opengl.GLES20.glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels);
			settings.apply();
		}
	}
	
	
	public boolean isUpToDate() {
		return updates.isEmpty();
	}
	
	static public void glBindDefaultTexture(int target) {
		android.opengl.GLES20.glBindTexture(target, 0);
	}
	
	static public GLTexture[] glGenTextures(int n) {
		GLTexture[] buffers = new GLTexture[n];
		int names[] = new int[n];
		
		android.opengl.GLES20.glGenTextures(n, names, 0);
		
		for (int i=0;i<n;i++)
			buffers[i] = new GLTexture(names[i]);
		
		return buffers;
	}


	public TexImage2D createTexImage2D() {
		
		return null;
	}
	
	
	

	public interface StreamProgress {
	
	}
		
}
