package edu.stanford.atomicdecomp;

import com.google.common.base.Stopwatch;
import com.google.common.io.Files;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentFormat;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentParserFactory;
import org.semanticweb.binaryowl.owlapi.BinaryOWLOntologyDocumentStorer;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.FileDocumentTarget;
import org.semanticweb.owlapi.io.OWLParserFactoryRegistry;
import org.semanticweb.owlapi.model.*;
import uk.ac.manchester.cs.atomicdecomposition.Atom;
import uk.ac.manchester.cs.atomicdecomposition.AtomicDecomposerOWLAPITOOLS;
import uk.ac.manchester.cs.atomicdecomposition.AtomicDecomposition;
import uk.ac.manchester.cs.owlapi.modularity.ModuleType;

import java.io.File;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 01/05/2014
 */
public class PrepareCorpus {

    public static void main(String[] args) throws Exception {
        File out = new File("/tmp/onts");
        for(File folder : new File(args[0]).listFiles()) {
            if(folder.isDirectory()) {
                File[] files = folder.listFiles();
                System.out.printf("Processing %d files.\n", files.length);
                for(File file : files) {
                    if(!file.isHidden() && file.isFile() && file.getName().endsWith(".binaryowl")) {
                        int extPos = file.getName().indexOf(".");
                        String basicFilName = file.getName().substring(0, extPos);
                        File cpDir = new File(out, basicFilName);
                        cpDir.mkdirs();
                        File cpDoc = new File(cpDir, basicFilName + ".ontdoc");
                        Files.copy(file, cpDoc);
                    }
                }
            }
        }
    }
}
