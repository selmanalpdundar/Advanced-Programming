package com.selmanalpdundar.drones.alpdundar;


import java.beans.PropertyChangeEvent;
import java.io.Serializable;
import javafx.util.Pair;
import java.util.Timer;
import java.util.TimerTask;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Random;
/***
 *  
 * @author selmanalpdundar
 */


// implements seriazlizable interface 
public class Drones implements Serializable{
    
   // Creating properties 
    private Location loc; 
    private boolean flying;
    private Timer timer;
    
    // Creating property change support to be able to fire event change
    private PropertyChangeSupport propertyChangeSupport;
    
    // Default contructor to inilizate some properites
    public Drones(){
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.flying = false;
    }
    
    
    /**
     * It is read only property
     * @return the loc 
     */
    public Location getLoc() {
        return loc;
    }

    /**
     * It is a read only property
     * @return the flying
     */
    public boolean isFlying() {
        return flying;
    }
    
    /**
     * It takes location coordinate to take off drones.
     * Also, it creates a timer to move drones -10, 10
     * @param initLoc is a Location class for getting locations
     */
    public void takeOff(Location initLoc){
        
        // fire propery change aeach time property change
     
        boolean oldValue = this.flying;
        this.flying = true;
        propertyChangeSupport.firePropertyChange("flying",oldValue, this.flying);
        
        
        // Initilation of loc propery
        // old and new value is same because it is initilaziton
        this.loc = initLoc;
   
        propertyChangeSupport.firePropertyChange("loc",loc, loc);

        this.timer = new Timer();
        
        // System.out.println("Drones is taking off"); // For debug
       
       // Timer definition for location generator
        TimerTask task;
        task = new TimerTask(){
            @Override
            public void run() {
                
                // Old location of drone
                Location oldValue = new Location(loc.getX(),loc.getY());
                
                // x += <-10, +10 >
                // y += < -10, +10 >
                loc.setX(loc.getX() + randomNumberGenerator());
                loc.setY(loc.getY() + randomNumberGenerator());
                
                propertyChangeSupport.firePropertyChange("loc",oldValue, loc);
                
             
                // System.out.println("Drone location was uptaded x = "+loc.getX()+" y = "+loc.getY());   // For debug

            }
            
        };
        
        timer.schedule(task,0,1000);
       
    }
    
    /**
     * It lands the drons and cancel timer
     */
    public void land(){
       
        // System.out.println("Drones is landing");  // For Debug
        
        boolean oldValue = flying;
        this.flying = false;
        propertyChangeSupport.firePropertyChange("flying",oldValue, this.flying);
        timer.cancel();
        
    }
    
    /**
     *  It creates an integer
     * @return an integer between -10  and +10
     */
    public int randomNumberGenerator(){
        Random random =  new Random();
        int number =  random.nextInt(20) - 10;
        //For debug
       // System.out.println("Number "+ number);
        return number;
    }
    
    //-***** It was not  clear on the assignment text I implement both wat
    // to show I am able to do it in both way.

//    /**
//     * It registers a listener to flying property change event
//     * @param listener 
//     */
//    public void addFlyingListener(PropertyChangeListener listener ){
//        this.propertyChangeSupport.addPropertyChangeListener("flying", listener);
//    }
//    
//    /**
//     * It removes a listener from flying property change vent
//     * @param listener 
//     */
//    public void removeFlyingListener(PropertyChangeListener listener){
//        this.propertyChangeSupport.removePropertyChangeListener("flying", listener);
//    }
//    
//    /**
//     * It register a listener to loc property change event
//     * @param listener 
//     */
//    public void addLocListener(PropertyChangeListener listener){
//        this.propertyChangeSupport.addPropertyChangeListener("loc", listener);
//    }
//
//    /**
//     * It removes a listener from loc property change vent
//     * @param listener 
//     */
//    public void removeLocListener(PropertyChangeListener listener){
//        this.propertyChangeSupport.removePropertyChangeListener("loc", listener);
//    }

    /**
     * It adds a listener to property change support
     * @param listener 
     */
   public void addListener(PropertyChangeListener listener){
       this.propertyChangeSupport.addPropertyChangeListener(listener);
   }
   
   /**
    * It removes a listener from property change support
    * @param listener 
    */
   public void removeListener(PropertyChangeListener listener){
      this.propertyChangeSupport.removePropertyChangeListener(listener);
   }
   
}
