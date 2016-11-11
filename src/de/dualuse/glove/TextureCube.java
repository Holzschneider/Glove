package de.dualuse.glove;

import de.dualuse.collections.LinkedNode;
import de.dualuse.glove.GLTexture.GLBoundTexture;

public class TextureCube implements Texture {

	BufferedTexture2D planes[] = {null,null,null,null,null,null};
	
	public TextureCube(BufferedTexture2D right, BufferedTexture2D left, BufferedTexture2D top, BufferedTexture2D bottom, BufferedTexture2D back, BufferedTexture2D front) {
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
		Texture.UpdateTracker planeTracker[] = {
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
		public boolean update(GLBoundTexture bt, int[] planeTargets, int level, FlowControl c, StreamProgress p) {
			boolean updated = false;
			
			for (int i=0,t=plane[0]=planeTargets[i],I=planeTargets.length;i<I;t=planeTargets[++i],plane[0]=t)
				updated |= planeTracker[i].update(bt, plane, level, c, p);
			
			return updated;
		}

	}


}
