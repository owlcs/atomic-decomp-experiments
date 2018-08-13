package edu.stanford.atomicdecomp;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.ListMultimap;
import org.javax.csv.csvio.CsvReader;
import org.javax.csv.csvio.CsvReaderFactory;
import org.javax.csv.csvio.impl.BufferedCsvReader;
import org.javax.csv.csvio.impl.CsvReaderImpl;
import uk.ac.manchester.cs.owl.experimentbench.Stats;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 09/05/2014
 */
public class CheckExpressivity {

    public static void main(String[] args) throws IOException {
        File file = new File(args[0]);
        FileReader fileReader = new FileReader(file);
        CsvReader reader = new BufferedCsvReader(fileReader, ',');
        List <String> row;
        ListMultimap<String, Integer> countMap = LinkedListMultimap.create();
        while((row = reader.read()) != null) {
            Integer numberOfAxioms = Integer.parseInt(row.get(0));
            String expressivity = row.get(1);
            Integer lengthOfOntology = Integer.parseInt(row.get(2));
            countMap.put(expressivity, lengthOfOntology);
        }
        fileReader.close();

        int rowCount = 0;
        for(String expressivity : countMap.keySet()) {
            for (Integer i : countMap.get(expressivity)) {
//                System.out.printf("%d,%s,%d\n", rowCount, expressivity, i);
//                System.out.printf("%d,%s\n", rowCount, expressivity);
//
                System.out.println(i);
            }

            rowCount++;
        }
    }
}
