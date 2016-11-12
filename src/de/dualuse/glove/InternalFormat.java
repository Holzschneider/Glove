package de.dualuse.glove;

//import static android.opengl.GLES10.*;
import static android.opengl.GLES11Ext.*;
import static android.opengl.GLES20.*;
import static android.opengl.GLES30.*;

public interface InternalFormat {
	///https://www.opengl.org/sdk/docs/man/html/glTexImage2D.xhtml
	int internalFormat();
	
	public static enum Base implements InternalFormat {
		RED(GL_RED),
		RG(GL_RG),
		RGB(GL_RGB),
		RGBA(GL_RGBA),
		ALPHA(GL_ALPHA),
		LUMINANCE(GL_LUMINANCE),
		LUMINANCE_ALPHA(GL_LUMINANCE_ALPHA),
		DEPTH_STENCIL(GL_DEPTH_STENCIL),
		DEPTH_COMPONENT(GL_DEPTH_COMPONENT);

		private int format;
		
		private Base(int format) {
			this.format = format;
		}
		
		@Override
		public int internalFormat() {
			return format;
		}
	}
	
	public static enum Sized implements InternalFormat {
		R8(GL_R8),R16I(GL_R16I),R16UI(GL_R16UI),R16F(GL_R16F),R32F(GL_R32F),
		RG8(GL_RG8),RG16I(GL_RG16I),RG16UI(GL_RG16UI),RG16F(GL_R16F),RG32F(GL_R32F),
		RGB565(GL_RGB565),RGB8(GL_RGB8),RGB16I(GL_RGB16I),RGB16UI(GL_RGB16UI),RGB16F(GL_RGB16F),RGB32F(GL_RGB32F),
		RGBA4(GL_RGBA4),RGB4_A1(GL_RGB5_A1),RGBA8(GL_RGBA8),RGB10_A2(GL_RGB10_A2),RGBA16F(GL_RGBA16F),RGBA32F(GL_RGBA32F),
		DEPTH24_STENCIL8(GL_DEPTH24_STENCIL8),
		DEPTH_COMPONENT16(GL_DEPTH_COMPONENT16),DEPTH_COMPONENT24(GL_DEPTH_COMPONENT24),DEPTH_COMPONENT32F(GL_DEPTH_COMPONENT32F);

		private int format;
		
		private Sized(int format) {
			this.format = format;
		}
		
		@Override
		public int internalFormat() {
			return format;
		}
	}

//	public static enum Compressed implements TextureFormat {
//		;
//
//		private int format;
//		
//		private Compressed(int format) {
//			this.format = format;
//		}
//		
//		@Override
//		public int internalFormat() {
//			return format;
//		}
//	}

	
}