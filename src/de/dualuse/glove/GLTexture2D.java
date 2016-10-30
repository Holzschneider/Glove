package de.dualuse.glove;

import static android.opengl.GLES20.*;

public class GLTexture2D extends GLTexture {

	public GLTexture2D(Texture[] sources) {
		super(GL_TEXTURE_2D,sources);
	}

}
