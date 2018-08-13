package edu.stanford.atomicdecomp;

import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentParserFactory;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.FileDocumentTarget;
import org.semanticweb.owlapi.io.OWLParserFactoryRegistry;
import org.semanticweb.owlapi.io.OWLXMLOntologyFormat;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.io.File;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 13/05/2014
 */
public class ExtractCorpus {

    public static void main(String[] args) {
        File baseDirectory = new File(args[0]);
        File outDirectory = new File(args[1]);
        outDirectory.mkdirs();
        int counter = 0;
        File[] files = baseDirectory.listFiles();
        int size = files.length;
        for(File file : files) {
            counter++;
            System.out.printf("Processing %d of %d\n", counter, size);
            if(!file.isHidden() && file.isDirectory()) {
//                System.out.println(file.getName());
                File ontDoc = new File(file, file.getName() + ".ontdoc");
                try {
                    OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
                    OWLParserFactoryRegistry.getInstance().clearParserFactories();
                    OWLParserFactoryRegistry.getInstance().registerParserFactory(new BinaryOWLOntologyDocumentParserFactory());
                    OWLOntology ont = manager.loadOntologyFromOntologyDocument(ontDoc);
                    File outFile = new File(outDirectory, file.getName() + ".owl.xml");
                    manager.saveOntology(ont, new OWLXMLOntologyFormat(), new FileDocumentTarget(outFile));
                } catch (Throwable e) {
                    System.err.println("ERROR PARSING: " + e.getClass().getSimpleName());
                }

            }
        }
    }
}
