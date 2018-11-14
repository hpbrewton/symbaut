package automata.sfta;

import theory.BooleanAlgebra;
import org.sat4j.specs.TimeoutException;

import java.util.List;
import java.util.stream.Stream;

public class SFTAMove<P,S> {

	public P guard;
	private List<Integer> from;
	private Integer to;
	
	public SFTAMove(Integer to, P guard, List<Integer> from) {
		this.from = from;
		this.guard = guard;
		this.to = to;
	}

	public boolean hasModel(S e, BooleanAlgebra<P, S> ba) throws TimeoutException {
		return ba.HasModel(guard, e);
	}

	public Integer getTo() {
		return to;
	}

	@Override
	public String toString() {
		return String.format("(%d,%s,%s)", to, guard.toString(), from.toString());
	}
}
