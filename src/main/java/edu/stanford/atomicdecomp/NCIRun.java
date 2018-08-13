package edu.stanford.atomicdecomp;

import org.semanticweb.binaryowl.BinaryOWLOntologyDocumentHandlerAdapter;
import org.semanticweb.binaryowl.BinaryOWLOntologyDocumentSerializer;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentFormat;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentParser;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentParserFactory;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentStorer;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.FileDocumentTarget;
import org.semanticweb.owlapi.io.OWLParserFactoryRegistry;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.owl.owlapi.OWLDataFactoryImpl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 08/05/2014
 */
public class NCIRun {

    public static void main(String[] args) throws Exception {
        File doc = new File(args[0]);
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

        ThreadMXBean bean = ManagementFactory.getThreadMXBean();
        long in0 = bean.getCurrentThreadCpuTime();
        OWLOntology ont = manager.loadOntologyFromOntologyDocument(doc);
        long in1 = bean.getCurrentThreadCpuTime();
        System.out.println("Ax count: " + ont.getAxiomCount());
        System.out.println("Log ax count: " + ont.getLogicalAxiomCount());

        System.out.println("Time to load: " + (in1 - in0));
        manager.addOntologyStorer(new BinaryOWLOntologyDocumentStorer());
        long t0 = bean.getCurrentThreadCpuTime();
        File outFile = new File(doc.getParent(), doc.getName() + ".bin");
        for(OWLImportsDeclaration decl : new ArrayList<>(ont.getImportsDeclarations())) {
            manager.applyChange(new RemoveImport(ont, decl));
        }
        manager.saveOntology(ont, new BinaryOWLOntologyDocumentFormat(), new FileDocumentTarget(outFile));
        long t1 = bean.getCurrentThreadCpuTime();
        System.out.println("Time to save: " + (t1 - t0));

        for (int i = 0; i < 5; i++) {

            manager = null;
            System.gc();
            System.gc();
            System.gc();
            System.gc();

            manager = OWLManager.createOWLOntologyManager();
            BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(outFile));


            BinaryOWLOntologyDocumentSerializer serializer = new BinaryOWLOntologyDocumentSerializer();
            try {
                final Set<OWLAxiom> axs = new HashSet<>();
                long t0In = bean.getCurrentThreadCpuTime();
                serializer.read(inputStream, new BinaryOWLOntologyDocumentHandlerAdapter() {
                    @Override
                    public void handleAxioms(Set set) throws Throwable {
                        axs.addAll(set);
                    }
                }, new OWLDataFactoryImpl());
                long t1In = bean.getCurrentThreadCpuTime();
                System.out.println("Time to load: " + (t1In - t0In));
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
            inputStream.close();
//            OWLParserFactoryRegistry.getInstance().registerParserFactory(new BinaryOWLOntologyDocumentParserFactory());

//            manager.loadOntologyFromOntologyDocument(outFile);


        }
    }
}
