package edu.stanford.atomicdecomp;

import org.javax.csv.csvio.CsvWriter;
import org.javax.csv.csvio.impl.BufferedCsvWriter;

import java.io.*;
import java.util.*;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 11/05/2014
 */
public class Properties2Csv {


    public static void main(String[] args) throws IOException {
        File file = new File(args[0]);
        if(!file.isDirectory()) {
            System.err.println("Supplied file is not a directory.  Expected a directory containing properties files.");
            return;
        }
        String contains = "";
        if(args.length > 1) {
            contains = args[1];
        }
        int counter = 0;
        File outFile = new File(file.getParentFile(), file.getName() + "." + contains + ".csv");
        CsvWriter csvWriter = new BufferedCsvWriter(new BufferedWriter(new FileWriter(outFile)), ',');
        Set<String> done = new HashSet<>();
        for(File propertiesFile : file.listFiles()) {
            if(propertiesFile.isFile() && !propertiesFile.isHidden()
                    && propertiesFile.getName().contains("." + contains + ".")
                    && (propertiesFile.getName().endsWith(".properties") || propertiesFile.getName().endsWith(".running"))) {
                int sepIndex = propertiesFile.getName().indexOf(".");
                String prefix = propertiesFile.getName().substring(0, sepIndex);
                if(!done.add(prefix)) {
                    System.out.println("WARNING: There appears to be a duplicate. " + prefix);
                }
                final List<String> propertyNames = new ArrayList<>();
                Properties properties = new Properties() {

                    @Override
                    public synchronized Object put(Object key, Object value) {
                        propertyNames.add(key.toString());
                        return super.put(key, value);
                    }
                };
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(propertiesFile));
                properties.load(bis);
                bis.close();
                List<String> propertyValues = new ArrayList<>();
                if(counter == 0) {
                    csvWriter.write(propertyNames);
                }
                for(String propertyName : propertyNames) {
                    propertyValues.add(properties.getProperty(propertyName).replace(",","_"));
                }
                csvWriter.write(propertyValues);
                counter++;
            }
        }
        csvWriter.close();

    }
}
