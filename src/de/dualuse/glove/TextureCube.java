package de.dualuse.glove;

import static android.opengl.GLES20.*;

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
	
	int[] targets = {0};
	public void init(int[] target, int level) {
		
		for (int i=0,t = targets[0] = target[i], I=target.length,J=planes.length;i<I; t = target[++i], targets[0] = t)
			planes[i%J].init( targets, level);
			
	}

	public void update(int[] target, int level) {
		
		for (int i=0,t = targets[0] = target[i], I=target.length,J=planes.length;i<I; t = target[++i], targets[0] = t)
			planes[i%J].update( targets, level);
			
		
	}

}
