package com.tinkerpop.gremlin;

import com.tinkerpop.gremlin.compiler.Atom;
import com.tinkerpop.gremlin.compiler.operations.Operation;
import com.tinkerpop.gremlin.compiler.operations.UnaryOperation;
import junit.framework.TestCase;
import org.antlr.runtime.RecognitionException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public class BaseTest extends TestCase {

    double timer = -1.0d;

    public void testTrue() {
        assertTrue(true);
    }

    public List<String> generateUUIDs(int count) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            list.add(UUID.randomUUID().toString());
        }
        return list;
    }

    public static void printIterator(final Iterator itty) {
        while (itty.hasNext()) {
            System.out.println(itty.next());
        }
    }

    public static int count(final Iterator iterator) {
        int counter = 0;
        while (iterator.hasNext()) {
            iterator.next();
            counter++;
        }
        return counter;
    }

    public static int count(final Iterable iterable) {
        return count(iterable.iterator());
    }

    public static List asList(final Iterable iterable) {
        return asList(iterable.iterator());
    }

    public static List asList(final Iterator iterator) {
        List list = new ArrayList();
        while (iterator.hasNext()) {

            list.add(iterator.next());
        }
        return list;
    }

    public List evaluateGremlinScriptIterable(String script, boolean printStatistics) throws RecognitionException {
        this.stopWatch();
        Iterable itty = (Iterable) Gremlin.evaluate(script).iterator().next();
        if (printStatistics)
            printPerformance(script, 1, "pipe constructed", this.stopWatch());
        this.stopWatch();
        // todo: make a "illegal pipe constructed error"
        if (null == itty)
            return null;
        List results = asList(itty);
        if (printStatistics)
            printPerformance(script, 1, "pipe listed", this.stopWatch());
        return results;
    }

    public Object evaluateGremlinScriptPrimitive(String script, boolean printStatistics) throws RecognitionException {
        this.stopWatch();
        Object object = Gremlin.evaluate(script).iterator().next();
        if (printStatistics)
            printPerformance(script, 1, "pipe evaluated", this.stopWatch());
        return object;
    }


    public double stopWatch() {
        if (this.timer == -1.0d) {
            this.timer = System.nanoTime() / 1000000.0d;
            return -1.0d;
        } else {
            double temp = (System.nanoTime() / 1000000.0d) - this.timer;
            this.timer = -1.0d;
            return temp;
        }
    }

    public static void printPerformance(String name, Integer events, String eventName, double timeInMilliseconds) {
        if (null != events)
            System.out.println("\t" + name + ": " + events + " " + eventName + " in " + timeInMilliseconds + "ms");
        else
            System.out.println("\t" + name + ": " + eventName + " in " + timeInMilliseconds + "ms");
    }

    public static UnaryOperation createUnary(Object object) {
        return new UnaryOperation(new Atom(object));
    }

    public static List<Operation> createUnaryArgs(Object... objects) {
        List<Operation> args = new ArrayList<Operation>();
        if (null != objects) {
            for (Object object : objects) {
                args.add(createUnary(object));
            }
        }
        return args;
    }
}