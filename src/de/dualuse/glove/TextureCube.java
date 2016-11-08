package de.dualuse.glove;

import de.dualuse.collections.LinkedNode;

public class TextureCube implements Texture {

	Texture2D planes[] = {null,null,null,null,null,null};
	
	public TextureCube(Texture2D right, Texture2D left, Texture2D top, Texture2D bottom, Texture2D back, Texture2D front) {
		planes[0] = right;
		planes[1] = left;
		planes[2] = top;
		planes[3] = bottom;
		planes[4] = back;
		planes[5] = front;
	}
	
	public synchronized UpdateTracker trackUpdates() {
		return dirty.append(new UpdateTracker());
	}

	UpdateTracker dirty = new UpdateTracker();
	
	class UpdateTracker extends LinkedNode<UpdateTracker> implements Texture.UpdateTracker {
		Texture2D.UpdateTracker planeTracker[] = {
			planes[0].trackUpdates(),
			planes[1].trackUpdates(),
			planes[2].trackUpdates(),
			planes[3].trackUpdates(),
			planes[4].trackUpdates(),
			planes[5].trackUpdates()
		};
		
		public void dispose() {
			synchronized(TextureCube.this) {
				remove();
			}
		}
		
		int[] plane = {0};
		public boolean update(int[] planeTargets, int level, FlowControl c) {
			boolean updated = false;
			
			for (int i=0,t=plane[0]=planeTargets[i],I=planeTargets.length;i<I;t=planeTargets[++i],plane[0]=t)
				updated |= planeTracker[i].update(plane, level, c);
			
			return updated;
		}

	}

}
