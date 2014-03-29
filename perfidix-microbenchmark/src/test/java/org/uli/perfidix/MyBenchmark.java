/**
 * 
 */
package org.uli.perfidix;

import java.util.HashSet;
import java.util.Set;

import org.perfidix.AbstractConfig;
import org.perfidix.Benchmark;
import org.perfidix.annotation.BenchmarkConfig;
import org.perfidix.element.KindOfArrangement;
import org.perfidix.meter.AbstractMeter;
import org.perfidix.meter.Time;
import org.perfidix.meter.TimeMeter;
import org.perfidix.ouput.AbstractOutput;
import org.perfidix.ouput.TabularSummaryOutput;
import org.perfidix.result.BenchmarkResult;

/**
 * @author uli
 *
 */
public class MyBenchmark {
    public static void main(String[] args) {
        for (int i : new int[] {10,100,1000}) {
            final Benchmark b = new Benchmark(new Config(i));
            b.add(BeanPropertySetterBenchmark.class);
            final BenchmarkResult res = b.run();
            new TabularSummaryOutput().visitBenchmark(res);
        }
    }
    
}

@BenchmarkConfig
class Config extends AbstractConfig {

    //private final static int RUNS = 1000;
    private final static Set<AbstractMeter> METERS = new HashSet<AbstractMeter>();
    private final static Set<AbstractOutput> OUTPUT = new HashSet<AbstractOutput>();

    private final static KindOfArrangement ARRAN = KindOfArrangement.SequentialMethodArrangement;
    private final static double GCPROB = 1.0d;

    static {
        METERS.add(new TimeMeter(Time.MilliSeconds));
        //METERS.add(new MemMeter(Memory.Byte));

        // OUTPUT.add(new TabularSummaryOutput());
    }

    /**
     * Public constructor.
     */
    public Config (final int runs) {
        super(runs, METERS, OUTPUT, ARRAN, GCPROB);
    }

}
