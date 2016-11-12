package de.dualuse.glove;

import static android.opengl.GLES11Ext.*;
import static android.opengl.GLES20.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import de.dualuse.glove.Texture.AbstractSampleRenderer;

public class GLTexture2D extends GLTexture {

	public GLTexture2D(Texture... sources ) {
		super(TextureTarget.TEXTURE_2D, sources);
	}
	
	@Override public GLBoundTexture2D bindTexture() { super.bindTexture(); return bound; }
	@Override public GLTexture2D texParameter(int pname, float param) { super.texParameter(pname, param); return this; }
	@Override public GLTexture2D texParameter(int pname, int param) { super.texParameter(pname, param); return this; }
	@Override public GLTexture2D minFilter(TextureFilter f) { super.minFilter(f); return this; }
	@Override public GLTexture2D magFilter(TextureFilter f) { super.magFilter(f); return this; }
	@Override public GLTexture2D baseLevel(int i) { super.baseLevel(i); return this; }
	@Override public GLTexture2D maxLevel(int i) { super.maxLevel(i); return this; }
	@Override public GLTexture2D generateMipmap() { super.generateMipmap(); return this; }

	@Override public GLTexture2D enable() { super.enable(); return this; }
	@Override public GLTexture2D disable() { super.disable(); return this; }
	
	
	private GLBoundTexture2D bound = new GLBoundTexture2D(this);
	class GLBoundTexture2D extends GLBoundTexture {

		protected GLBoundTexture2D(GLTexture copy) {
			super(copy);
		}

		public GLBoundTexture2D texImage2D(int level, InternalFormat i, int width, int height, Texture2D.Sampler2D source) {
			try (DirectBufferStack dbs = DirectBufferStack.stacks.get().push()) {

				IntBuffer pixels = dbs.mallocInt(width*height);
				AbstractSampleRenderer stylus = new AbstractSampleRenderer() {
					int offset = 0;
					public void rgb(int argb) {
						pixels.put(offset++, argb);
					}
				};
				
				for (int y=0;y<height;y++)
					for (int x=0;x<width;x++)
						source.sample(x, y, stylus);

				for (int target: state.target.planes)
					glTexImage2D(target, level, i.internalFormat(), width, height, 0, GL_BGRA, GL_UNSIGNED_BYTE, pixels);
			}
			
			return this;
		}
		

		public GLBoundTexture2D texImage2D(int level, InternalFormat i, int width, int height, SourceFormat f, Texture2D.Sampler2D source) {
			try (DirectBufferStack dbs = DirectBufferStack.stacks.get().push()) {
				
//				Format.RED.FLOAT.renderer(dbs.malloc(100));
				
			}
			
			return null;
		}

		public GLBoundTexture2D texImage2D(int level, InternalFormat i, int width, int height, Texture2D.Pixel2D source) {
			return texImage2D(level, i, width,height, (x,y,s) -> s.rgb(source.sample(x, y)) );
		}
		
		public GLBoundTexture2D texImage2D(int level, int internalFormat, int width, int height, Texture2D.Grabber2D source) {
			try (DirectBufferStack hs = DirectBufferStack.stacks.get().push(); ArrayBufferStack abs = ArrayBufferStack.stacks.get().push() ) {
				IntBuffer A = abs.mallocInt(width*height);
				IntBuffer B = hs.mallocInt(width*height);
				
				B.put(source.grab(0, 0, width, height, A.array(), A.arrayOffset(), width), 0, width*height);
				
				for (int target: state.target.planes)
					glTexImage2D(target,level, internalFormat, width, height, 0, GL_BGRA, GL_UNSIGNED_BYTE, B);
			}
			
			return this;
		}
		
		public GLBoundTexture2D texImage2D(int level, int internalformat, int width, int height, int border, int format, int type, ByteBuffer pixels) {
			for (int target: state.target.planes)
				glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels); 
			return this; 
		}

		public GLBoundTexture2D texImage2D(int level, int internalformat, int width, int height, int border, int format, int type, IntBuffer pixels) {
			for (int target: state.target.planes)
				glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels); 
			return this; 
		}
			
		public GLBoundTexture2D texImage2D(int level, int internalformat, int width, int height, int border, int format, int type, FloatBuffer pixels) { 
			for (int target: state.target.planes)
				glTexImage2D(target, level, internalformat, width, height, border, format, type, pixels); 
			return this; 
		}
		
		public GLBoundTexture2D texImage2D(int level, Texture t) {
//			t.init(state.target.planes, level);
			return this; 
		}
		
		// ------
		
		public GLBoundTexture2D texSubImage2D(int level, int xoffset, int yoffset, int width, int height, Texture t) {
//			t.init(state.target.planes, level);
			return this; 
		}
		
		
		public GLBoundTexture2D texSubImage2D(int level, int xoffset, int yoffset, int width, int height, int format, int type, ByteBuffer pixels) {
			for (int target: state.target.planes)
				glTexSubImage2D(target, level,xoffset,yoffset,width,height,format, type, pixels);
			return this; 
		}

		public GLBoundTexture2D texSubImage2D(int level, int xoffset, int yoffset, int width, int height, int format, int type, IntBuffer pixels) {
			for (int target: state.target.planes)
				glTexSubImage2D(target, level,xoffset,yoffset,width,height,format, type, pixels);
			return this; 
		}
			
		public GLBoundTexture2D texSubImage2D(int level, int xoffset, int yoffset, int width, int height, int format, int type, FloatBuffer pixels) {
			for (int target: state.target.planes)
				glTexSubImage2D(target, level,xoffset,yoffset,width,height,format, type, pixels);
			return this; 
		}

	}

}
