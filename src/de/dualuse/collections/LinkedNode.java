package de.dualuse.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class LinkedNode<T extends LinkedNode<T>> implements Collection<T> {
	
	protected T next = self(), prev = self();
	
	@SuppressWarnings("unchecked")
	protected T self() { return (T)this; }
	
	public T remove() {
		
		this.prev.next = this.next;
		this.next.prev = this.prev;

		this.prev = this.next = self();
		
		return self();
	}
	
	public T append(T node) {

		T thisnext = this.next;
		
		this.next.prev = node;
		node.next = thisnext;
		
		this.next = node;
		node.prev = self();
		
		return node;
	}
	
	
	public T next() {
		return this.next;
	}

	
	//////////
	
	public int size() {
		int count = 1;
		for (LinkedNode<T> start = this, cursor = start.next; cursor!=this;cursor=cursor.next )
			count++;
		
		return count;
	}

	public boolean isEmpty() {
		return false;
	}

	public boolean contains(Object o) {
		for (LinkedNode<T> start = this, cursor = start; ;cursor=cursor.next )
			if (o.equals(cursor))
				return true;
			else if (cursor.next==start)
				return false;
				
	}

	public Iterator<T> iterator() {
		return new Iterator<T>() {
			T start = self();
			T next = null, item = start;
			
			
			public boolean hasNext() {
				return start!=next;
			}

			public T next() {
				T current = item;
				item = next = item.next;
				
				return current;
			}
			
			@Override
			public void remove() {
				item.remove();
			}
		};
	}

	public Object[] toArray() {
		return toArray(new Object[0]);
	}

	@SuppressWarnings("unchecked")
	public <Q> Q[] toArray(Q[] a) {
		Q[] items = Arrays.copyOf(a, size());

		int i=0;
		for (LinkedNode<T> start = this, cursor = start, current = null;current!=start; current = cursor=cursor.next )
			items[i++] = (Q)cursor.self();
			
		return items;
			
	}

	public boolean add(T e) {
		append(e);
		return true;
	}

	public boolean remove(Object o) {
		for (LinkedNode<T> start = this, cursor = start; ;cursor=cursor.next )
			if (o.equals(cursor)) 
				return cursor.remove()==o;
			else if (cursor.next==start)
				return false;

	}

	public boolean containsAll(Collection<?> c) {
		for (Object o: c)
			if (!contains(o))
				return false;
		
		return true;
	}

	public boolean addAll(Collection<? extends T> c) {
		boolean changed = false;
		for (T t: c)
			changed |= add(t);
			
		return changed;
	}

	public boolean removeAll(Collection<?> c) {
		boolean changed = false;
		for (Object t: c)
			changed |= remove(t);
			
		return changed;
	}

	public boolean retainAll(Collection<?> c) {
		boolean changed = false;

		for (LinkedNode<T> start = this, cursor = start, current = null;current!=start; current = cursor=cursor.next )
			if (!c.contains(cursor)) 
				changed |= cursor.remove()==cursor;
		
		return changed;
		
	}

	public void clear() {
		throw new UnsupportedOperationException();
	}
	
	
	///////////////////
	
	
	public T forEach(Visitor<T> v) {
		for (LinkedNode<T> start = this, cursor = start, current = null;current!=start; current = cursor=cursor.next )
			v.visit(cursor.self());
		
		return self();
	}
	
	public static interface Visitor<T> {
		public void visit(T t);
	}
}


