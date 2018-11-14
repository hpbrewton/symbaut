package test.SFTA;

import automata.AutomataException;
import automata.sfta.SFTA;
import automata.sfta.SFTAMove;
import automata.sfta.Tree;
import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import theory.characters.CharPred;
import theory.intervals.UnaryCharIntervalSolver;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class SFTAUnitTest {

    private static UnaryCharIntervalSolver ba = new UnaryCharIntervalSolver();

    private static SFTA<CharPred, Character> sfta1;
    private static Tree<Character> tree1;
    private static Tree<Character> tree2;

    @BeforeClass
    public  static void beforeClass() {
        sfta1 = new SFTA<>(ba);
        sfta1.addState(0);
        sfta1.addState(1);
        sfta1.addState(2);
        sfta1.addState(3);
        sfta1.setRoots(new HashSet<>(Collections.singletonList(3)));
        sfta1.addTransition(Collections.emptyList(), new SFTAMove<>(1, new CharPred('A', 'M'), Collections.emptyList()));
        sfta1.addTransition(Collections.emptyList(), new SFTAMove<>(2, new CharPred('N', 'Z'), Collections.emptyList()));
        sfta1.addTransition(Arrays.asList(1, 2), new SFTAMove<>(3, new CharPred('A', 'Z'), Arrays.asList(1, 2)));

        tree1 = new Tree<>('Q', Arrays.asList(
           new Tree<>('B', new ArrayList<>()),
           new Tree<>('Y', new ArrayList<>())
        ));

        tree2 = new Tree<>('Q', Arrays.asList(
           new Tree<>('Y', new ArrayList<>()),
           new Tree<>('B', new ArrayList<>())
        ));
    }

    @Test
    public void sanityCheck() throws AutomataException {
        assertTrue(sfta1.accepts(tree1));
        assertFalse(sfta1.accepts(tree2));
    }
}
