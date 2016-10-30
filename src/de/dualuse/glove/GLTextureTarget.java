package de.dualuse.glove;

import static android.opengl.GLES20.*;

public enum GLTextureTarget {
		
	TEXTURE_2D(GL_TEXTURE_2D),
	TEXTURE_CUBE_MAP(GL_TEXTURE_CUBE_MAP);
	
	final int target;
	private GLTextureTarget(int target) {
		this.target = target;
	}
	
	
}
