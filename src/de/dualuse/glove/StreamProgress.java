package de.dualuse.glove;

import de.dualuse.glove.GLTexture.GLBoundTexture;

public interface StreamProgress {
	
	public void update(GLBoundTexture bt, int level, int x, int y, int width, int height, boolean done);
	
}