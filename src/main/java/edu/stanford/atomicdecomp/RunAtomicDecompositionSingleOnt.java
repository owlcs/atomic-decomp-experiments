package edu.stanford.atomicdecomp;

import com.google.common.io.Files;
import org.coode.owlapi.owlxmlparser.OWLXMLParserFactory;
import org.coode.owlapi.rdfxml.parser.RDFXMLParserFactory;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentParserFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.OWLParserFactoryRegistry;
import org.semanticweb.owlapi.model.*;
import org.semanticweb.owlapi.util.DLExpressivityChecker;
import uk.ac.manchester.cs.atomicdecomposition.Atom;
import uk.ac.manchester.cs.atomicdecomposition.AtomicDecomposerOWLAPITOOLS;
import uk.ac.manchester.cs.atomicdecomposition.AtomicDecomposition;
import uk.ac.manchester.cs.owl.experimentbench.Stats;
import uk.ac.manchester.cs.owlapi.modularity.ModuleType;

import java.io.*;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 06/05/2014
 */
public class RunAtomicDecompositionSingleOnt {

    public static final int MODULE_TYPE_ARGUMENT_INDEX = 0;
    public static final int ABOX_TREATMENT_ARGUMENT_INDEX = 1;
    public static final int ONTOLOGY_DOCUMENT_ARGUMENT_INDEX = 2;
    public static final int OUTPUT_DOCUMENT_ARGUMENT_INDEX = 3;
    public static final String DISCARD_ABOX = "DISCARD_ABOX";

    public static void main(String[] args) throws IOException {
        if(args.length != 4) {
            System.err.println("Expected four arguments: [BOT|TOP|STAR] [KEEP_ABOX|DISCARD_ABOX] <root directory> <output directory>");
            dumpArgs(args, System.err);
            return;
        }
        String moduleTypeNameArgument = args[MODULE_TYPE_ARGUMENT_INDEX];
        if(!moduleTypeNameArgument.equals("BOT") && !moduleTypeNameArgument.equals("TOP") && !moduleTypeNameArgument.equals("STAR")) {
            System.err.printf("Invalid module type: %s.  Expected one of TOP, BOT or STAR.\n", moduleTypeNameArgument);
            return;
        }
        ModuleType moduleType = ModuleType.valueOf(moduleTypeNameArgument);
        String aboxTreatmentArgument = args[ABOX_TREATMENT_ARGUMENT_INDEX];
        if(!aboxTreatmentArgument.equals("KEEP_ABOX") && !aboxTreatmentArgument.equals(DISCARD_ABOX)) {
            System.err.printf("Invalid ABox treatment: %s.  Expected one of KEEP_ABOX or DISCARD_ABOX.\n", aboxTreatmentArgument);
            return;
        }
        File ontDoc = new File(args[ONTOLOGY_DOCUMENT_ARGUMENT_INDEX]);
        File outFile = new File(args[OUTPUT_DOCUMENT_ARGUMENT_INDEX]);
        File outDir = outFile.getParentFile();
        if(outDir == null) {
            outDir = new File(".");
        }
        else if(!outDir.exists()) {
            outDir.mkdirs();
        }
//        String propertiesFileName = ontDocDir.getName() + "." + moduleType.name() + ".properties";
//        String propertiesFileName = String.format("%s.%s.%s.properties", ontDocDir.getName(), moduleType.name(), aboxTreatmentArgument);
//        File outFile = new File(outDir, propertiesFileName);
        if(outFile.exists()) {
            System.out.println("Output file already exists.  Not re-running.");
            return;
        }
        File outFileRunning = new File(outDir, outFile.getName() + ".running");
        if(outFileRunning.exists()) {
            System.out.println("Output running file already exists.  Not re-running.");
            return;
        }
        try {
            OWLOntologyManager manager = getManager();
            OWLOntology ont = manager.loadOntologyFromOntologyDocument(ontDoc);
            if(aboxTreatmentArgument.equals(DISCARD_ABOX)) {
                discardABox(manager, ont);
            }
            PrintWriter runningWriter = new PrintWriter(new FileWriter(outFileRunning));
            writeProperty("document", ontDoc.getName(), runningWriter);
            writeProperty("Ontology.Axioms.Count", ont.getAxiomCount(), runningWriter);
            writeProperty("Ontology.Axioms.Count.Logical", ont.getLogicalAxiomCount(), runningWriter);
            writeProperty("Ontology.Expressivity", new DLExpressivityChecker(ont.getImportsClosure()).getDescriptionLogicName(), runningWriter);
            writeProperty("Ontology.Length", ont.accept(new LengthComputer()), runningWriter);
            writeProperty("Ontology.Signature.Size.Entities", ont.getSignature().size(), runningWriter);
            writeProperty("Ontology.Signature.Size.Classes", ont.getClassesInSignature().size(), runningWriter);
            writeProperty("Ontology.Signature.Size.ObjectProperties", ont.getObjectPropertiesInSignature().size(), runningWriter);
            writeProperty("Ontology.Signature.Size.DataProperties", ont.getDataPropertiesInSignature().size(), runningWriter);
            writeProperty("Ontology.Signature.Size.AnnotationProperties", ont.getAnnotationPropertiesInSignature().size(), runningWriter);
            writeProperty("Ontology.Signature.Size.Individuals", ont.getIndividualsInSignature().size(), runningWriter);
            writeProperty("Ontology.ABox.Discard", aboxTreatmentArgument.equals(DISCARD_ABOX), runningWriter);
            manager.removeAxioms(ont, ont.getAxioms(AxiomType.ANNOTATION_ASSERTION));
            manager.removeAxioms(ont, ont.getAxioms(AxiomType.DECLARATION));
            computeModule(runningWriter, ont, moduleType);
            runningWriter.close();
            Files.move(outFileRunning, outFile);
        } catch (Throwable e) {
            System.err.printf("ERROR: %s\n", e.getClass().getSimpleName());
            e.printStackTrace();
        }
    }

    private static void discardABox(OWLOntologyManager manager, OWLOntology ont) {
        manager.removeAxioms(ont, ont.getAxioms(AxiomType.CLASS_ASSERTION));
        manager.removeAxioms(ont, ont.getAxioms(AxiomType.NEGATIVE_OBJECT_PROPERTY_ASSERTION));
        manager.removeAxioms(ont, ont.getAxioms(AxiomType.OBJECT_PROPERTY_ASSERTION));
        manager.removeAxioms(ont, ont.getAxioms(AxiomType.DATA_PROPERTY_ASSERTION));
        manager.removeAxioms(ont, ont.getAxioms(AxiomType.NEGATIVE_DATA_PROPERTY_ASSERTION));
    }

    private static void dumpArgs(String[] args, PrintStream out) {
        for(int i = MODULE_TYPE_ARGUMENT_INDEX; i < args.length; i++) {
            out.printf("\t Found Argument %d: %s", i, args[i]);
        }
    }

    private static void writeProperty(String propertyName, Object value, PrintWriter printWriter) {
        printWriter.printf("%s=%s\n", propertyName, value);
        printWriter.flush();
    }


    private static OWLOntologyManager getManager() {
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLParserFactoryRegistry.getInstance().clearParserFactories();
        OWLParserFactoryRegistry.getInstance().registerParserFactory(new BinaryOWLOntologyDocumentParserFactory());
        OWLParserFactoryRegistry.getInstance().registerParserFactory(new OWLXMLParserFactory());
        OWLParserFactoryRegistry.getInstance().registerParserFactory(new RDFXMLParserFactory());
        return manager;
    }

    private static void computeModule(PrintWriter pw, OWLOntology ont, ModuleType type) throws IOException {
        writeProperty("Module.Type", type.name(), pw);
        ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
        long currentCPUTime = threadMXBean.getCurrentThreadCpuTime();
        AtomicDecomposition atomicDecomposition = new AtomicDecomposerOWLAPITOOLS(ont, type);
        Set<Atom> atomSet = atomicDecomposition.getAtoms();
        long currentCPUTime2 = threadMXBean.getCurrentThreadCpuTime();
        long timeNS = (currentCPUTime2 - currentCPUTime) / (1000);
        writeProperty(type.name() + ".Time.ns", timeNS, pw);
        Stats stats = new Stats();
        for(Atom atom : atomSet) {
            stats.add(atom.getAxioms().size());
        }
        writeProperty(type.name() + ".Atom.Count", atomSet.size(), pw);
        writeProperty(type.name() + ".Atom.Size.Min", stats.getMin(), pw);
        writeProperty(type.name() + ".Atom.Size.Max", stats.getMax(), pw);
        writeProperty(type.name() + ".Atom.Size.Mean", stats.getMean(), pw);
        writeProperty(type.name() + ".Atom.Size.StdDev", stats.getStdDev(), pw);
        writeProperty(type.name() + ".Atom.Size.P25", stats.getNthPercentile(25), pw);
        writeProperty(type.name() + ".Atom.Size.P50", stats.getNthPercentile(50), pw);
        writeProperty(type.name() + ".Atom.Size.P75", stats.getNthPercentile(75), pw);
        writeProperty(type.name() + ".Atom.Size.P90", stats.getNthPercentile(90), pw);
        writeProperty(type.name() + ".Atom.Size.P99", stats.getNthPercentile(99), pw);
    }
}
