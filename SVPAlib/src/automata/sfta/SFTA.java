package automata.sfta;

import automata.AutomataException;

import com.google.common.collect.Sets;
import org.sat4j.specs.TimeoutException;
import theory.BooleanAlgebra;

import java.util.*;
import java.util.stream.Collectors;


public class SFTA <P, S> {

    private boolean isDeterministic;

	private Set<Integer> states;
	private Set<Integer> roots;
	private Set<Integer> leaves;
	private Map<List<Integer>, Collection<SFTAMove<P, S>>> moves;

	private BooleanAlgebra<P, S> booleanAlgebra;

	public SFTA(BooleanAlgebra<P, S> booleanAlgebra) {
		states = new HashSet<>();
		roots = new HashSet<>();
		leaves = new HashSet<>();

		moves = new HashMap<>();
		this.booleanAlgebra = booleanAlgebra;
	}

	// can reach root state?
	public boolean accepts(Tree<S> tree) throws AutomataException {
		Set<Integer> intersection = new HashSet<Integer>(roots);
		Set<Integer> tracen = traces(tree);
		intersection.retainAll(tracen);
		return intersection.size() > 0;
	}

	// returns the set of end states 
	public Set<Integer> traces(Tree<S> tree) {

	    if (tree.children.size() == 0) {
	        return moves.get(tree.children).stream()
                    .filter(m -> {
                        try {
                            boolean sat = m.hasModel(tree.value, booleanAlgebra);
                            return sat;
                        } catch (TimeoutException e) {
                            return false;
                        }
                    }).map(m -> m.getTo()).collect(Collectors.toSet());
        }

		List<Set<Integer>> children = tree.children.stream()
                .map(this::traces)
                .collect(Collectors.toList());

		// product of two subtrees moved
		return Sets.cartesianProduct(children)
                .stream()
                .filter(e -> moves.containsKey(e))
                .flatMap(e -> moves.get(e).stream())
                .filter(m -> {
                    try {
                        boolean sat = m.hasModel(tree.value, booleanAlgebra);
                        return sat;
                    } catch (TimeoutException e) {
                        return false;
                    }
                }).map(m -> m.getTo()).collect(Collectors.toSet());
	}

    public void addState(Integer i) {
	    states.add(i);
    }

    public void addTransition(List<Integer> from, SFTAMove<P, S> transition) {
	    if (moves.containsKey(from)) {
	        moves.get(from).add(transition);
        } else {
	        HashSet<SFTAMove<P, S>> set = new HashSet<>();
	        set.add(transition);
	        moves.put(from, set);
        }
    }

    public void setRoots(Set<Integer> roots) {
        this.roots = roots;
    }

    public void determinize() {
        ArrayList<P> predicates = new ArrayList<>(moves.values().stream()
                .flatMap(e -> e.stream())
                .map(e -> e.guard)
                .collect(Collectors.toList()));
        Set<P> pPreds = booleanAlgebra.GetMinterms(predicates)
                .stream()
                .map(e -> e.first)
                .collect(Collectors.toSet());


    }
}