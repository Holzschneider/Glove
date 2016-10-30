package de.dualuse.glove;

import static android.opengl.GLES20.*;

public class GLTexture2D extends GLTexture {

	public GLTexture2D(Texture... sources ) {
		super(TextureTarget.TEXTURE_2D, sources);
	}

}
