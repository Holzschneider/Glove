package de.dualuse.glove;

import static android.opengl.GLES20.*;

public class GLTextureCubeMap extends GLTexture {

	public GLTextureCubeMap(Texture[] sources) {
		super(GL_TEXTURE_CUBE_MAP,sources);
	}
	

	
}
