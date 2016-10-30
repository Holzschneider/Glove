package de.dualuse.glove;

import static android.opengl.GLES20.*;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class GLTextureCubeMap extends GLTexture {

	public GLTextureCubeMap(TextureCube... sources) {
		super(TextureTarget.TEXTURE_CUBE_MAP, sources);
	}
	
	
	@Override
	public GLBoundTextureCubeMap bindTexture() {
		return new GLBoundTextureCubeMap(super.bindTexture());
	}
	
	static public class GLBoundTextureCubeMap extends GLBoundTexture {

		protected GLBoundTextureCubeMap(GLTexture copy) {
			super(copy);
		}
		
		public GLBoundTexture texImage2D(CubeMap plane, int level, int internalformat, int width, int height, int border, int format, int type, ByteBuffer pixels) {
			glTexImage2D(plane.planeTarget, level, internalformat, width, height, border, format, type, pixels);
			return this;
		}
		
		public GLBoundTexture texImage2D(CubeMap plane, int level, int internalformat, int width, int height, int border, int format, int type, IntBuffer pixels) {
			glTexImage2D(plane.planeTarget, level, internalformat, width, height, border, format, type, pixels);
			return this;
		}

		public GLBoundTexture texImage2D(CubeMap plane, int level, int internalformat, int width, int height, int border, int format, int type, FloatBuffer pixels) {
			glTexImage2D(plane.planeTarget, level, internalformat, width, height, border, format, type, pixels);
			return this;
		}
		
		public GLBoundTexture texSubImage2D(CubeMap plane, int level, int xoffset, int yoffset, int width, int height, int format, int type, ByteBuffer pixels) {
			glTexSubImage2D(plane.planeTarget, level,xoffset,yoffset,width,height,format, type, pixels);
			return this; 
		}

		public GLBoundTexture texSubImage2D(CubeMap plane, int level, int xoffset, int yoffset, int width, int height, int format, int type, IntBuffer pixels) {
			glTexSubImage2D(plane.planeTarget, level,xoffset,yoffset,width,height,format, type, pixels);
			return this; 
		}
			
		public GLBoundTexture texSubImage2D(CubeMap plane, int level, int xoffset, int yoffset, int width, int height, int format, int type, FloatBuffer pixels) {
			glTexSubImage2D(plane.planeTarget, level,xoffset,yoffset,width,height,format, type, pixels);
			return this; 
		}

	}
	
}
