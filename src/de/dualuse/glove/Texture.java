package de.dualuse.glove;

public interface Texture {
	
	public static interface UpdateTracker {
		public boolean update( int[] imagePlaneTargets, int level, FlowControl c );
		public void dispose();
	}
	
	UpdateTracker trackUpdates();
	
	
//	public static interface TextureConsumer {
//	}
//	public void addConsumer(TextureConsumer tc);
//	public void removeConsumer(TextureConsumer tc);
//		
//	public void init(int[] imagePlaneTargets, int level);
//	public void update(int[] imagePlaneTargets, int level);
	
}
