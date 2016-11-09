package de.dualuse.glove;

import de.dualuse.glove.GLTexture.GLBoundTexture;

public interface Texture {
	
	public static interface UpdateTracker {
		public boolean update(GLBoundTexture t, int[] imagePlaneTargets, int level, FlowControl c, StreamProgress p);
		public void dispose();
	}
	
	UpdateTracker trackUpdates();
	
//	public void init(int[] imagePlaneTargets, int level);
//	public void update(int[] imagePlaneTargets, int level);
	
}
