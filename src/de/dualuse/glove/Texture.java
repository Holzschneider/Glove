package de.dualuse.glove;

import de.dualuse.glove.GLTexture.GLBoundTexture;

public interface Texture {
	
	public void init(GLBoundTexture bt, int level);
	public void update(GLBoundTexture bt, int level);
	
}
