package edu.stanford.atomicdecomp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 07/05/2014
 */
public class Cleaner {

    public static void main(String[] args) {
        File outFolder = new File(args[0]);
        int i = 0;
        for (File file : outFolder.listFiles()) {
            if (file.getName().endsWith("properties")) {
                try {
                    Properties properties = new Properties();
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    properties.load(reader);
                    reader.close();

                    if(!containsAll(properties)) {
                        i++;
                        System.out.println(i);
                        System.out.println("Malformed: " + file.getName());
                        System.out.println(properties);
                        file.delete();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    private static boolean containsAll(Properties properties) {

//        if (!properties.contains("document")) {
//            return false;
//        }
        if (!properties.containsKey("Ontology.Axioms.Count")) {
            return false;
        }
        if (!properties.containsKey("Ontology.Axioms.Count.Logical")) {
            return false;
        }
        if (!properties.containsKey("Ontology.Expressivity")) {
            return false;
        }
        if (!properties.containsKey("Ontology.Length")) {
            return false;
        }
        if (!properties.containsKey("Ontology.Signature.Size.Entities")) {
            return false;
        }
        if (!properties.containsKey("Ontology.Signature.Size.Classes")) {
            return false;
        }
        if (!properties.containsKey("Ontology.Signature.Size.ObjectProperties")) {
            return false;
        }
        if (!properties.containsKey("Ontology.Signature.Size.DataProperties")) {
            return false;
        }
        if (!properties.containsKey("Ontology.Signature.Size.AnnotationProperties")) {
            return false;
        }
        if (!properties.containsKey("Ontology.Signature.Size.Individuals")) {
            return false;
        }
        if (!properties.containsKey("BOT.Time.ns")) {
            return false;
        }
        if (!properties.containsKey("BOT.Atom.Count")) {
            return false;
        }
        if (!properties.containsKey("BOT.Atom.Size.Min")) {
            return false;
        }
        if (!properties.containsKey("BOT.Atom.Size.Max")) {
            return false;
        }
        if (!properties.containsKey("BOT.Atom.Size.Mean")) {
            return false;
        }
        if (!properties.containsKey("BOT.Atom.Size.StdDev")) {
            return false;
        }
        if (!properties.containsKey("BOT.Atom.Size.P25")) {
            return false;
        }
        if (!properties.containsKey("BOT.Atom.Size.P50")) {
            return false;
        }
        if (!properties.containsKey("BOT.Atom.Size.P75")) {
            return false;
        }
        if (!properties.containsKey("BOT.Atom.Size.P90")) {
            return false;
        }
        if (!properties.containsKey("BOT.Atom.Size.P99")) {
            return false;
        }
        if (!properties.containsKey("TOP.Time.ns")) {
            return false;
        }
        if (!properties.containsKey("TOP.Atom.Count")) {
            return false;
        }
        if (!properties.containsKey("TOP.Atom.Size.Min")) {
            return false;
        }
        if (!properties.containsKey("TOP.Atom.Size.Max")) {
            return false;
        }
        if (!properties.containsKey("TOP.Atom.Size.Mean")) {
            return false;
        }
        if (!properties.containsKey("TOP.Atom.Size.StdDev")) {
            return false;
        }
        if (!properties.containsKey("TOP.Atom.Size.P25")) {
            return false;
        }
        if (!properties.containsKey("TOP.Atom.Size.P50")) {
            return false;
        }
        if (!properties.containsKey("TOP.Atom.Size.P75")) {
            return false;
        }
        if (!properties.containsKey("TOP.Atom.Size.P90")) {
            return false;
        }
        if (!properties.containsKey("TOP.Atom.Size.P99")) {
            return false;
        }
        if (!properties.containsKey("STAR.Time.ns")) {
            return false;
        }
        if (!properties.containsKey("STAR.Atom.Count")) {
            return false;
        }
        if (!properties.containsKey("STAR.Atom.Size.Min")) {
            return false;
        }
        if (!properties.containsKey("STAR.Atom.Size.Max")) {
            return false;
        }
        if (!properties.containsKey("STAR.Atom.Size.Mean")) {
            return false;
        }
        if (!properties.containsKey("STAR.Atom.Size.StdDev")) {
            return false;
        }
        if (!properties.containsKey("STAR.Atom.Size.P25")) {
            return false;
        }
        if (!properties.containsKey("STAR.Atom.Size.P50")) {
            return false;
        }
        if (!properties.containsKey("STAR.Atom.Size.P75")) {
            return false;
        }
        if (!properties.containsKey("STAR.Atom.Size.P90")) {
            return false;
        }
        if (!properties.containsKey("STAR.Atom.Size.P99")) {
            return false;
        }
        return true;
    }
}
