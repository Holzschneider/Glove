package de.dualuse.glove;

import java.util.Arrays;

public class FlowControlTest {
	public static void main(String[] args) {
//		FlowControl s = FlowControl.DEFAULT;//new FlowControl(10000, 50);
		FlowControl s = new FlowControl(1000, (long)1e9);
//		FlowControl s = new FlowControl(1000, 7);

		long packets[] = {300, 500, 1000};
		for (long packet: packets)
			s.enqueue(packet);
			
		int i = 0,k=0;
		for (i=0;;i+=1e9/60,k++) {
			if (k==15)
				System.out.println("??");
			long rest = 0, transferred = 0;
			for (int j=0;j<packets.length;j++) {
				if (packets[j]<=0)
					continue;
					
//				long allowed = s.allocate(i, i==0?0:packets[j]);
				long allowed = s.allocate(i, packets[j]);
				packets[j]-=allowed;
				rest += packets[j];
				transferred +=allowed;
				System.out.println("   "+j+": "+allowed);
			}
			
			System.out.println(k+": "+Arrays.toString(packets)+": "+transferred);
			
			if (rest == 0)
				break;
			
		}
		
//		for (long packet: packets)
//			s.dequeue(packet);

		
		System.out.println("------------------");
		
		
		long packets2[] = {packets[0], packets[1], packets[2], 500};
//		for (long packet: packets2)
//			s.enqueue(packet);

		s.enqueue(packets2[3]);
		
		for (int I=i+8;i<I;i+=1) {
			
			long rest = 0, transferred = 0;
			for (int j=0;j<packets2.length;j++) {
				if (packets2[j]<=0)
					continue;
					
				long allowed = s.allocate(i, packets2[j]);
				packets2[j]-=allowed;
				rest += packets2[j];
				transferred +=allowed;
				System.out.println(j+": "+allowed);
			}
			
			System.out.println(Arrays.toString(packets2)+": "+transferred);
			
			if (rest == 0)
				break;
			
		}
		
	}

}
