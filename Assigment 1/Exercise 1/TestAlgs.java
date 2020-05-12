package com.selmanalpdundar.assignment1;

import java.io.*;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author selmanalpdundar
 */
public class TestAlgs {

    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException, MalformedURLException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        // Read file path from argument
        String filePath = args[0];

        // Create new instance
        TestAlgs test = new TestAlgs();

        // Run test class send file path as a parameter
        test.runAlgorithm(filePath);
    }

    private void runAlgorithm(String filePath) throws FileNotFoundException, ClassNotFoundException, MalformedURLException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{

       // It is a supporter class to read all keys
       KeyParser parser = new KeyParser(filePath);

       // It is a supporter class to load .class file
       LoadAlgorithm algorithmLoader = new LoadAlgorithm(filePath);

       // It is a supporter class to read all the words
       WordParser wordParser = new WordParser(filePath);

       // I read all the keys as a arraylist so each time
       ArrayList<String> key = null;


       do{
           // Read key from key parser
           key = parser.nextElement();

           // Checking key is null or not
           if(key != null){

               // Get algorithm class with algorithm loader with algorithm name
               Class algorithm = algorithmLoader.getAlgorithm(key.get(0));

               // Print Algorithm name
               System.out.println(algorithm.getName().split("\\.")[2]);

               // Check if enc, dec, and constructor method proper for given constraints
               if(checkConstructor(algorithm.getConstructors()) && checkMethods(algorithm.getMethods())){
                    // Get constructor from algorithm class that has string parameter
                    Constructor constructor = algorithm.getConstructor(String.class);
                    // Create a object from given constructor
                    Object object = constructor.newInstance(key.get(1));

                    // Get encryption method
                    Method encryptionMethod = null;
                    // Get decryption method
                    Method decryptionMethod = null;

                    // Find encryption methods and decryption methods
                    for(Method method:algorithm.getMethods() ){

                        // Finding encryption method
                        if(method.getName().startsWith("enc")){
                            encryptionMethod = method;
                        }

                        // Finding description method
                        if(method.getName().startsWith("dec")){
                            decryptionMethod = method;
                        }
                    }

                    // Start a loop for word list
                    for(String word : wordParser.getWords()){

                        // encrypted word
                        String encrypted = null;
                        // decrypted word
                        String decrypted = null;

                        // Checking if the encryption method is not  null
                        if(encryptionMethod != null){
                            Object value =  encryptionMethod.invoke(object, word);
                            encrypted = (String) value;
                        }

                        // Checking if the dencryption method is not  null
                        if( decryptionMethod != null){
                            Object value =  decryptionMethod.invoke(object, encrypted);
                            decrypted = (String) value;
                        }

                        // Checking word equals decrypted word or not
                        // Checking decrypted word is start with original word and finish one or more #
                        if(word.equals(decrypted) || decrypted.startsWith(word+"#")  ){

                        } else {
                            System.out.println("KO: "+word+" -> "+encrypted+" -> "+decrypted);

                        }
                    }

                        
                 } else { System.out.println("Enc/Dec methods not found"); }
           } //  key != null

       } while(key != null);
        
    }

   private boolean checkConstructor(Constructor constructors[]){
       
        for(Constructor constructor : constructors){
                        
            if(Modifier.isPublic(constructor.getModifiers()) && constructor.getParameterCount() == 1 && constructor.getParameterTypes()[0].getName().contains("String")){
                return true;
            }
        }
        return false;
    }
    
   private boolean checkMethods(Method methods[]){

       // To follow if there is a method that stat enc
       boolean hasEncryption = false;
       // To follow if there is a method that start with dec
       boolean hasDecryption = false;

       // Iterate all the methods
       for(Method method: methods){

           // get the method name
           String methodName = method.getName();

           // Checking method name if its start with enc and has one parameter that has type string
           if(methodName.startsWith("enc") &&  method.getParameterCount() == 1 &&  method.getParameterTypes()[0].getName().contains("String")){
             hasEncryption = true;
           }

           // Checking method name if its start with dec and has one parameter that has type string
           if(methodName.startsWith("dec") && method.getParameterCount() == 1 && method.getParameterTypes()[0].getName().contains("String")){
               hasDecryption = true;
           }
        }
       // if the methods contain enc and dec and satisfy the constraints return true
        if( hasEncryption && hasDecryption ) return true;
            
        return false;
   }

    /***
     * It uses iteration pattern to give algorithm names and read all the
     * keys from file.
     */
   public class KeyParser {


        ArrayList<ArrayList<String>> keys;
        int index = 0;

       /***
        *  It takes a file pate and read all the keys inside the file.
        * @param filePath it is a file path
        * @throws FileNotFoundException
        */
        public KeyParser(String filePath) throws FileNotFoundException{

            // Create a List of string list to keep algoritm name and keys.
            keys = new ArrayList< ArrayList<String> >();

            // Setting up reader for reading rows from keys.list
            BufferedReader bufferedReader;
            bufferedReader = new BufferedReader( new FileReader(filePath+"/crypto/keys.list"));

            String line = null;

            do{

                try {
                    // Read row
                    line = bufferedReader.readLine();

                    // Checking if it is null or not
                    if(line != null){

                        // Creating new list of string to store algorithm name and key
                        ArrayList<String> key =  new ArrayList<String>();

                        // Spliting row to get algorithm name and key
                        for(String k: line.split(" ")){
                            key.add(k);
                        }

                        // Adding algorithm name to keys
                        keys.add(key);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(KeyParser.class.getName()).log(Level.SEVERE, null, ex);
                }



            } while(line != null);

        }

        // Get next element in list
        public ArrayList<String> nextElement(){
            if(hasNext()){

                return keys.get(index++);
            } else
            {
                return null;
            }
        }

        // Check if there is more element
        public boolean hasNext(){
            if(keys.isEmpty()) return false;
            if(keys.size() == index) return false;
            if(keys.size() > index) return true;

            return false;
        }
    }

   public class LoadAlgorithm {

        String filePath = null;


       /**
        *
        * @param pathName it is given p
        */
        public LoadAlgorithm(String pathName){
            this.filePath = pathName;
        }

       /***
        *  It gets name of the class and return class instance
        * @param algorithmName is the name of .class to create a Class object
        * @return
        * @throws MalformedURLException
        * @throws ClassNotFoundException
        */
        public Class getAlgorithm(String algorithmName) throws MalformedURLException, ClassNotFoundException{

            // Create File with given path
            File myFolder = new File(this.filePath);

            // Get URLClassLoader
            URLClassLoader classLoader = new URLClassLoader(new URL[]{myFolder.toURI().toURL()},Thread.currentThread().getContextClassLoader());

            // Get Class with given name
            Class algorithm = Class.forName(algorithmName, true, classLoader);

            return algorithm;

        }

    }

    public class WordParser {
        ArrayList<String> words;
        int index = 0;

        /***
         * @param filePath It is the location of the file
         * @throws FileNotFoundException it is trowing error and handle file not found error.
         */
        public WordParser(String filePath) throws FileNotFoundException {

            words = new ArrayList<String>();

            // Setting up reader for reading rows from keys.list
            BufferedReader bufferedReader;
            bufferedReader = new BufferedReader( new FileReader(filePath+"/crypto/secret.list"));

            String line = null;

            do{

                try {
                    line = bufferedReader.readLine();

                    if(line != null){

                        words.add(line);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(KeyParser.class.getName()).log(Level.SEVERE, null, ex);
                }



            }while(line != null);

        }

        /***
         * It is return the the word list
         * @return  String array list of word
         */
        public ArrayList<String> getWords(){
            return words;
        }

    }


}

