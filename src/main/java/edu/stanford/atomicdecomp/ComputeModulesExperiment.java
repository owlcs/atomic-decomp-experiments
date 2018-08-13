package edu.stanford.atomicdecomp;

import com.google.common.base.Stopwatch;
import org.semanticweb.owl.explanation.telemetry.*;
import org.semanticweb.owlapi.model.OWLOntology;
import uk.ac.manchester.cs.atomicdecomposition.Atom;
import uk.ac.manchester.cs.atomicdecomposition.AtomicDecomposerOWLAPITOOLS;
import uk.ac.manchester.cs.atomicdecomposition.AtomicDecomposition;
import uk.ac.manchester.cs.owl.experimentbench.*;
import uk.ac.manchester.cs.owlapi.modularity.ModuleType;

import java.io.File;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 06/05/2014
 */

public class ComputeModulesExperiment extends AbstractOntologyDocumentExperiment {

    public ComputeModulesExperiment(ExperimentProperties experimentProperties, File ontologyDocument, OWLOntology ontology, File outputDirectory) {
        super(experimentProperties, ontologyDocument, ontology, outputDirectory);
    }

    @Experiment(name = "compute-bottom-atomic-decomposition")
    public void runAtomicDecompositionExperiments() {
        TelemetryTimer timer = new TelemetryTimer();
        TelemetryTransmitter transmitter = TelemetryTransmitter.getTransmitter();
        TelemetryInfo telemetryInfo = new DefaultTelemetryInfo("BottomAD", true, timer);
        transmitter.beginTransmission(telemetryInfo);
        timer.start();
        ModuleType modType = ModuleType.BOT;
        OWLOntology ont = getOntology();
        AtomicDecomposition decomposition = new AtomicDecomposerOWLAPITOOLS(ont, modType);
        Set<Atom> atoms = decomposition.getAtoms();
        timer.stop();
        transmitter.recordTiming(telemetryInfo, "time", timer);
        transmitter.recordMeasurement(telemetryInfo, "number-of-atoms", atoms.size());
        Stats stats = new Stats();
        for(Atom atom : atoms) {
            stats.add(atom.getAxioms().size());
        }
        transmitter.recordMeasurement(telemetryInfo, "min-size", stats.getMin());
        transmitter.recordMeasurement(telemetryInfo, "max-size", stats.getMax());
        transmitter.recordMeasurement(telemetryInfo, "mean-size", stats.getMean());
        transmitter.recordMeasurement(telemetryInfo, "std-dev", stats.getStdDev());
        transmitter.endTransmission(telemetryInfo);
    }
}
