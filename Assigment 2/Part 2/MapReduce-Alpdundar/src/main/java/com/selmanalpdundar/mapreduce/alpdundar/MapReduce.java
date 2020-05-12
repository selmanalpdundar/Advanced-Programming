/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.selmanalpdundar.mapreduce.alpdundar;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *   read -> map -> myList -> reduce -> write
 * @author selmanalpdundar
 *
 */

/**
 * @param <T>
 */
public abstract class MapReduce<T> {
     
    private Path pathName;
     /**
      * 
      * @param pathName directory pathname
      * @throws IOException 
      */
    public MapReduce(Path pathName){
       this.pathName = pathName;
    }
    
    /**
     * Template method 
     */
    public final void run(){
        // 1. Reading directory to get file name and contents
        Stream<Pair<String, List<String>>> outputOfRead =  this.read(getPathName());
        // 2. Map all the files to with their occurences in to pair
        Stream< Pair<String, Integer>> outputOfMap = this.map(outputOfRead);
        // 3. Create a list with unique word and list of occurence  Pair<Word,{1,2,,3,4,1}>
        Stream< Pair<String, List<Integer>>> outPutOfMyList = this.makeList(outputOfMap);
        // 4. Reduce all the integer list to sum
        Stream<Pair<String, Integer>> outputOfReduce = this.reduce(outPutOfMyList);
        // 5. write to files results
        write(outputOfReduce);
        
    }

    /**
     * it is a bridge function  which help reduce to take streem from map
     * @param input
     * @return
     */
    public Stream< Pair<String, List<Integer>>> makeList( Stream<Pair<String, Integer>> input){

        ArrayList< Pair<String, List<Integer>>> pairs = new  ArrayList< Pair<String,List<Integer>>>();

        Map<String, List<Integer>> outputOfMakeList = input
                .collect(Collectors.groupingBy(p -> (String) p.getKey(),
                        Collectors.mapping(p -> (Integer) p.getValue(), Collectors.toList())));

        outputOfMakeList.forEach((k, v) -> {
            Pair<String,List<Integer>> pair = new Pair<String, List<Integer>>(k,v);
            pairs.add(pair);
        });

        return pairs.stream();
    }
    
    /**
     * 
     * @param input
     * @return 
     */
        public abstract Stream< Pair<String, Integer>> map(Stream<Pair<String, List<String>>> input);
    
    
    /**
     * 
     * @param input
     * @return 
     */
    public abstract Stream<Pair<String, Integer> > reduce( Stream< Pair<String, List<Integer> > > input );
    

    /**
     * 
     * @param stream 
     */
    public abstract void write(Stream<Pair<String, Integer>>  stream);
    
    
    /**
     * 
     * @param pathname directory path name
     * @return return stream of pair that contains filename and text inside list
     */
    public abstract Stream<Pair<String, List<String>>> read(Path pathname);
    
    
    /**
     * 
     * @param s1 First string
     * @param s2 Second string
     * @return  check if s1 is bigger than s2 return true if it is.
     */
    public abstract int compare(T s1, T s2);

    /**
     * @return the pathName
     */
    public Path getPathName() {
        return pathName;
    }

    /**
     * @param pathName the pathName to set
     */
    public void setPathName(Path pathName) {
        this.pathName = pathName;
    }

}
