package automata.sfta;

import java.util.List;

public class Tree<S> {

	protected List<Tree<S>> children;
	protected S value;

	public Tree(S value, List<Tree<S>> children) {
		this.value = value;
		this.children = children;
	}
}