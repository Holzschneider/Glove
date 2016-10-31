package de.dualuse.glove;

import java.util.Arrays;
import java.util.Iterator;

import de.dualuse.collections.LinkedNode;

class StringNode extends LinkedNode<StringNode> {
	final String value;
	
	public StringNode(String value) {
		this.value = value;
	}
	

	@Override
	public String toString() {
		return "StringNode("+value+")";
	}

	@Override
	protected StringNode self() {
		return this;
	}


}




public class LinkedNodeTest {

	public static void main(String[] args) {
		
		
		StringNode a = new StringNode("hallo");
		StringNode b = new StringNode("du");
		StringNode c = new StringNode("welt");
		
		a.append(b).append(c);
		
		System.out.println(a.next());
		
		System.out.println(a.size());
		
		System.out.println(a.contains("hallo"));
		
		for (Iterator<StringNode> i= a.iterator(); i.hasNext(); )
			System.out.println(i.next());
		
		
		System.out.println(Arrays.asList(a.toArray(new StringNode[0])));
		
	}
}
