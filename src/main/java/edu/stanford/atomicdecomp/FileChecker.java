package edu.stanford.atomicdecomp;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 11/05/2014
 */
public class FileChecker {

    public static void main(String[] args) {
        File file = new File(args[0]);
        Set<String> topFileNames = new HashSet<>();
        Set<String> starFileNames = new HashSet<>();
        Set<String> starRunningFiles = new HashSet<>();
        Set<String> starFileNamesFull = new HashSet<>();
        for(File f : file.listFiles()) {
//            if(f.getName().endsWith(".properties") || f.getName().endsWith(".running")) {
                int sepIndex = f.getName().indexOf('.');
                String prefix = f.getName().substring(0, sepIndex);
                if(f.getName().contains(".TOP.")) {
                    topFileNames.add(prefix);
                }
                else if(f.getName().contains(".STAR.")) {
                    starFileNames.add(prefix);
                    starFileNamesFull.add(f.getName());
                    if(f.getName().endsWith(".running")) {
                        starRunningFiles.add(f.getName());
                    }
                }
//                File botFile = new File(file, prefix + ".BOT.KEEP_ABOX.properties");
//                if(!botFile.exists()) {
//                    System.out.println("MISSING: " + botFile);
//                }
//                File starFile = new File(file, prefix + ".STAR.KEEP_ABOX.properties");
//                if(!starFile.exists()) {
//                    System.out.println("MISSING: " + starFile);
//                }
//            }
        }
        System.out.println("TOP: " + topFileNames.size());
        System.out.println("STAR: " + starFileNames.size());
        topFileNames.removeAll(topFileNames);

        for(String starRunning : starRunningFiles) {
            if(starFileNames.contains(starRunning.substring(0, starRunning.length() - ".running".length()))) {
                System.out.println("BAD: " + starRunning);
            }
        }


//        for(String fn : starFileNames) {
//            System.out.println("REMAINING: " + fn);
//        }
    }
}
