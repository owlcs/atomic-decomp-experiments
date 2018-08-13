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
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 09/05/2014
 */
public class BinaryOWL2OWLXML {

    public static void main(String[] args) throws Exception {
        File ontologyDocument = new File(args[0]);
        OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
        OWLParserFactoryRegistry.getInstance().registerParserFactory(new BinaryOWLOntologyDocumentParserFactory());
        OWLOntology ont = manager.loadOntologyFromOntologyDocument(ontologyDocument);
        manager.saveOntology(ont, new OWLXMLOntologyFormat(), new FileDocumentTarget(new File(ontologyDocument.getParentFile(), ontologyDocument.getName() + ".owl.xml")));
    }
}
