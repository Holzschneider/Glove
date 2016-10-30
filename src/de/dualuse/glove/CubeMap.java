package de.dualuse.glove;

import static android.opengl.GLES20.*;

public enum CubeMap {
	
	POSITIVE_X(GL_TEXTURE_CUBE_MAP_POSITIVE_X),
	NEGATIVE_X(GL_TEXTURE_CUBE_MAP_NEGATIVE_X),
	POSITIVE_Y(GL_TEXTURE_CUBE_MAP_POSITIVE_Y),
	NEGATIVE_Y(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z),
	POSITIVE_Z(GL_TEXTURE_CUBE_MAP_POSITIVE_Z),
	NEGATIVE_Z(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z);

	int planeTarget = 0;
	private CubeMap(int planeTarget) {
		this.planeTarget = planeTarget;
	}
}
