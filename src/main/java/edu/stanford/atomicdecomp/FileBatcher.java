package edu.stanford.atomicdecomp;

import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 07/05/2014
 */
public class FileBatcher {

    final static int BATCH_SIZE = 400;

    public static void main(String[] args) throws IOException {
        File directory = new File(args[0]);
        File parentDirectory = directory.getParentFile();

        int counter = 0;
        int currentBatchNumber = 0;
        File batchDirectory = null;
        for(File file : directory.listFiles()) {
            if(!file.isHidden() && file.isDirectory()) {
                if(currentBatchNumber != getBatchNumber(counter) || batchDirectory == null) {
                    currentBatchNumber = getBatchNumber(counter);
                    batchDirectory = new File(parentDirectory, directory.getName() + "-Batch-" + currentBatchNumber);
                    if(!batchDirectory.exists()) {
                        batchDirectory.mkdirs();
                    }
                }
                File from = new File(file, file.getName() + ".ontdoc");
                File toParent = new File(batchDirectory, from.getParentFile().getName());
                toParent.mkdirs();
                File to = new File(toParent, from.getName());
                if (!to.exists()) {
                    Files.copy(from, to);
                }
                counter++;
            }
        }
    }


    private static int getBatchNumber(int count) {
        return count / BATCH_SIZE;
    }
}
