package automata.sfta;

import java.util.List;

public class Tree<S> {

	protected List<Tree<S>> children;
	protected S value;

	public Tree(S value, List<Tree<S>> children) {
		this.value = value;
		this.children = children;
	}

	public void debugPrint(int indents) {
		for (int i = 0; i < indents; ++i) {
			System.out.print("\t");
		}
		System.out.println(value);
		for (Tree<S> child : children) {
			child.debugPrint(indents+1);
		}	
	}
}
