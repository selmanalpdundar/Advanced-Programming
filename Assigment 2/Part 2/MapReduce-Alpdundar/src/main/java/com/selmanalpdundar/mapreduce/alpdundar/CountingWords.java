/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.selmanalpdundar.mapreduce.alpdundar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *  #####------- Description of Code  -------#######
 *  Assumptions:
 *      1. I assumed a list of word is a list that is generated from a line by using  String.split("")
 *      there were no explanation about it. before For example "before" is not equal "before.
 *      I did not delete any punctuation.
 *
 *   read -> map -> reduce -> write
 * @author selmanalpdundar
 *
 */
public class CountingWords extends MapReduce<String>{

    public CountingWords(Path pathName) {
        super(pathName);
    }


    /**
     *  It takes a steam of pair which contains file name and lines of file
     *  and return a list of pair that is generated from lines with their occurrences number
     * @param input  file name and their context line by line
     * @return Stream of pair word and occurrences.
     */
    @Override
    public Stream< Pair<String, Integer>>  map(Stream<Pair<String,List<String>>> input) {

        List<Pair<String,Integer>> outputOfMap = new ArrayList< Pair<String,Integer> >();

        /**
         * takes all line of files and merge all of them in a list
         *  map -> call each pair getValue function
         * flatMap List<List<String>> -> List<String> convert stream of list  to list
         *  Pair<FileName, List<Line>> ->  List<Lines>
         * collect collects all of them in a list
         */
        List<String> lines = input
                        .map(Pair::getValue )
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());

        /**
         * Algorithm
         * 1. Take a line
         * 2. Split line to string lis
         * 3. Check word length if it is bigger than 3
         * 4. Collectors.groupingBye take care of getting same word in line and put the counting in long.
         * 5. Create a loop for creating <word,occurrence>  after each line processes to create a pair and add to list.
         *
         *  The static factory methods Collectors.groupingBy() and Collectors.groupingByConcurrent()
         *  provide us with functionality similar to the ‘GROUP BY' clause in the SQL language.
         *  They are used for grouping objects by some property and storing results in a Map instance.
         */

        List<Pair<String,Integer>> pairs = new ArrayList<Pair<String,Integer>>();

        for(String line: lines){
            Map<String, Long> words = Arrays.stream(line.split(" ")) // Creating a List<String> from a line  that is words
                                        .filter(word -> word.length() > 3)  // length control of word for condition on assigment
                                        .collect(Collectors.groupingBy(e -> e.toString().toLowerCase(),Collectors.counting())); //

            words.forEach((k,v) -> {
                    Pair<String,Integer> pair = new Pair<String,Integer>(k,v.intValue());
                    pairs.add(pair);
             });
        }

        return pairs.stream();
    }

    @Override
    public Stream<Pair<String, Integer> > reduce( Stream< Pair<String, List<Integer> > > input) {

        /**
         * Creating a map from stream
         *
         */
        Map<String, Integer> map =input
                .collect(Collectors.toMap(
                        p -> (String) p.getKey(),
                        p -> ((List<Integer>) p.getValue()).stream().mapToInt(Integer::intValue).sum()));

        List<Pair<String,Integer>> pairs = new ArrayList<Pair<String,Integer>>();

        /**
         * Converting map to List for returning stream of list
         */
        map.forEach((k,v) -> {
            Pair<String,Integer> pair = new Pair<String,Integer>(k,v);
            pairs.add(pair);
        });

        return pairs.stream();
    }

    @Override
    public void write(Stream<Pair<String, Integer>> input) {

        /**
         * It just use provided writer class
         */
        File file = new File("countingwords.csv");
        
        try {
            Writer.write(file, input);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CountingWords.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Stream read(Path pathname) {
        /**
         * It just use provided reader class
         */
       Reader reader = new Reader(pathname);
        try {
            return reader.read();
        } catch (IOException ex) {
            Logger.getLogger(CountingWords.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

 

    @Override
    public int compare(String s1, String s2) {

        return s1.compareTo(s2);

    }
 


}
