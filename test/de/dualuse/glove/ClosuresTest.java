package de.dualuse.glove;

public class ClosuresTest {
	
	static interface Dimension2i {
		void setSize(int width, int height);
	}
	
	public void size( Dimension2i s ) {
		
		System.out.println("integer");
	}
	
	
	static interface Dimension2f {
		void setSize(float width, float height);
	}
	
	public void size( Dimension2f s ) {
		
		System.out.println("float");
		s.setSize(100, 100);;
	}
	
	
	
	public static void main(String[] args) {

		System.out.println( (byte)(1234 & 0xFF ));
		
		System.out.println( (byte)1234 & -1 );
		
//		new ClosuresTest().size( (int x, int y) -> System.out.println(x+y) ); 
		
//		new ClosuresTest().size(new Dimension2f() {
//
//			@Override
//			public void setSize(int width, int height) {
//			}
//			
//		});
		
	}
}
