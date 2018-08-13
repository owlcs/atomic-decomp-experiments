package edu.stanford.atomicdecomp;

import uk.ac.manchester.cs.owlapi.modularity.ModuleType;

import java.io.*;
import java.util.Properties;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 08/05/2014
 */
public class ExtractData {

    public static void main(String[] args) throws IOException {
        File file = new File(args[0]);
        PrintWriter writer = new PrintWriter(new FileWriter(new File("/tmp/all-out.csv")));
        for(File propertiesFile : file.listFiles()) {
            try {
                Properties properties = new Properties();
                BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(propertiesFile));
                properties.load(inStream);
                inStream.close();
                addValue("document", properties, writer, propertiesFile.getName());
                addValue("Ontology.Axioms.Count.Logical", properties, writer);
                addValue("Ontology.Expressivity", properties, writer);
                addValue("Ontology.Length", properties, writer);
                addValue("Ontology.Signature.Size.Entities", properties, writer);
                addValue("Ontology.Signature.Size.Classes", properties, writer);
                addValue("Ontology.Signature.Size.ObjectProperties", properties, writer);
                addValue("Ontology.Signature.Size.DataProperties", properties, writer);
                addValue("Ontology.Signature.Size.Individuals", properties, writer);

                addModuleProperties(ModuleType.BOT, properties, writer);
                addModuleProperties(ModuleType.TOP, properties, writer);
                addModuleProperties(ModuleType.STAR, properties, writer);

                writer.println();
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writer.close();

    }

    private static void addModuleProperties(ModuleType moduleType, Properties properties, PrintWriter writer) {
        addValue(moduleType.name() + ".Time.ns", properties, writer);
        addValue(moduleType.name() + ".Atom.Count", properties, writer);
        addValue(moduleType.name() + ".Atom.Size.Min", properties, writer);
        addValue(moduleType.name() + ".Atom.Size.Max", properties, writer);
        addValue(moduleType.name() + ".Atom.Size.Mean", properties, writer);
        addValue(moduleType.name() + ".Atom.Size.StdDev", properties, writer);
        addValue(moduleType.name() + ".Atom.Size.P25", properties, writer);
        addValue(moduleType.name() + ".Atom.Size.P50", properties, writer);
        addValue(moduleType.name() + ".Atom.Size.P75", properties, writer);
        addValue(moduleType.name() + ".Atom.Size.P90", properties, writer);
        addValue(moduleType.name() + ".Atom.Size.P99", properties, writer);
    }


    private static void addValue(String propertyName, Properties properties, PrintWriter writer) {
        addValue(propertyName, properties, writer, null);
    }

    private static void addValue(String propertyName, Properties properties, PrintWriter writer,  String defaultValue) {
        String value = properties.getProperty(propertyName);
        writer.write(",");
        if (value == null) {
            value = defaultValue;
        }
        if(value != null) {
            writer.write(value.replace(",", ";"));
        }
    }
}
