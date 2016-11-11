package de.dualuse.glove;

public class Allocator extends HeapStack implements AutoCloseable {

	static final public ThreadLocal<Allocator> allocators = new ThreadLocal<Allocator>() {
		protected Allocator initialValue() {
			return new Allocator(64*1024);
		};
	};
	
	protected Allocator(int capacity) {
		super(capacity);
	}

	
	public static Allocator stack() {
		return allocators.get().push();
	}
	
	@Override
	public Allocator push() {
		super.push();
		return this;
	}
	

	@Override
	public Allocator pop() {
		super.pop();
		return this;
	}
	
	
	public static void main(String[] args) {
		
		try (Allocator a = Allocator.stack()) {
	
			System.out.println( a.malloc(4) );
			System.out.println( a.malloc(4) );
			
			try (Allocator b = a.push()) {
				
				System.out.println( b.malloc(1) );
				System.out.println( b.malloc(4,1) );
				
			}
			
			try (Allocator c = a.push()) {
				
				System.out.println( c.malloc(4) );
				System.out.println( c.malloc(4) );
				
			}
		}
		
	}
	
}
