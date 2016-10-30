package de.dualuse.glove;

import static android.opengl.GLES20.*;

public enum TextureTarget {
		
	TEXTURE_2D(GL_TEXTURE_2D, GL_TEXTURE_2D),
	TEXTURE_CUBE_MAP(GL_TEXTURE_CUBE_MAP, 
			GL_TEXTURE_CUBE_MAP_POSITIVE_X, 
			GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 
			GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 
			GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 
			GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 
			GL_TEXTURE_CUBE_MAP_NEGATIVE_X );
	
	final int binding;
	final int[] planes;
	
	private TextureTarget(int target, int ... imagePlaneTargets) {
		this.binding = target;
		this.planes = imagePlaneTargets;
	}
	
	
}
