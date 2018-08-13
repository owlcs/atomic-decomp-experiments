package edu.stanford.atomicdecomp;

import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentFormat;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentStorer;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.FileDocumentTarget;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import java.io.File;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 08/05/2014
 */
public class PrepareBioPortalCorpus {

    public static void main(String[] args) {
        File rootDirectory = new File(args[0]);
        File output = new File("/tmp/out");
        output.mkdirs();
        int counter = 0;
        for(File f : rootDirectory.listFiles()) {
            counter++;
            try {
                OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
                OWLOntology ont = manager.loadOntologyFromOntologyDocument(f);
                manager.setSilentMissingImportsHandling(true);
                OWLOntologyManager outMan = OWLManager.createOWLOntologyManager();
                OWLOntology outOnt = outMan.createOntology(ont.getOntologyID());
                for(OWLOntology o : ont.getImportsClosure()) {
                    outMan.addAxioms(outOnt, o.getLogicalAxioms());
                }
                outMan.addOntologyStorer(new BinaryOWLOntologyDocumentStorer());
                String fName = f.getName();
                String name = fName.substring(0, fName.length() - ".owl.xml".length());
                File outDir = new File(output, name);
                outDir.mkdirs();
                File outDoc = new File(outDir, name + ".ontdoc");
                outMan.saveOntology(outOnt, new BinaryOWLOntologyDocumentFormat(), new FileDocumentTarget(outDoc));
            } catch (Throwable e) {
                e.printStackTrace();
            }

        }

    }
}
