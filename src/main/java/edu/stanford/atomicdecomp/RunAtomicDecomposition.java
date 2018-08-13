package edu.stanford.atomicdecomp;


import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 06/05/2014
 */
public class RunAtomicDecomposition {


    private static ExecutorService executorService = Executors.newFixedThreadPool(3);

    public static void main(String[] args) throws IOException {
        if(args.length != 1) {
            System.out.println("Expected 1 argument specifying the base directory");
            System.exit(0);
        }


        File baseDirectory = new File(args[0]);
        File[] files = baseDirectory.listFiles();
        int count = 0;
        final int total = files.length;
        for(final File ontDocDir : files) {
            count++;
            final int theCount = count;
            final File ontDoc = new File(ontDocDir, ontDocDir.getName().replace(",", "-") + ".ontdoc");
            if(ontDoc.exists()) {
                executorService.submit(new Runnable() {
                    @Override
                    public void run() {
                        System.out.printf("Processing %d of %d    %s\n", theCount, total, ontDocDir.getName());
                        try {
                            RunAtomicDecompositionSingleOnt.main(new String[] {ontDocDir.getPath(), "/tmp/ont-exp-out"});
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        }
    }
}
