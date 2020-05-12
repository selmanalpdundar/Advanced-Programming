package com.selmanalpdundar.mapreduce.alpdundar;

import java.nio.file.Path;
import java.nio.file.Paths;


public class Main {

    public static void main(String[] args) {

    // Please change this for changeing
	Path path  = Paths.get("/Users/selmanalpdundar/Desktop/University Drive/School/Master/Second Year/Fist semester/Advanced Programming/AssigmentFinished/Assigment 2/Part 2/MapReduce-Alpdundar/src/main/java/com/selmanalpdundar/mapreduce/alpdundar/Books/");

	// Creating an instance of CountingWord
	CountingWords cw = new CountingWords(path);
	// Run algorithm
	cw.run();
    }
}
