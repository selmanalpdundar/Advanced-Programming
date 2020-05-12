/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.selmanalpdundar.drones.alpdundar;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JLabel;
import javax.swing.JPanel;
/**
 *
 * @author selmanalpdundar
 */
public class CustomLabel extends JLabel implements PropertyChangeListener{
     
     JPanel panel;
     int xLimit;
     int yLimit ;
     
     public CustomLabel(JPanel pnlDrones){
         this.panel = pnlDrones;
         xLimit = panel.getWidth();
         yLimit = panel.getHeight();
            
     }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
         // Update label even it is out of border
         
         
         /**
          *  Check  if fired property is loc
          */
        if("loc".equals(evt.getPropertyName())){
            
            // Getting currenct drone location
            Location currentLocation = (Location) evt.getNewValue();
          
            
             // Get location from location provider
            int x = (int) (currentLocation.getX());
            int y = (int) (currentLocation.getY());
            
            // System.out.println("X : "+x+ "  Y : " +y); // For debug
            
            if(
                    (x <= (xLimit - this.getX()) && x > 0 ) && 
                    (y <= (yLimit - this.getY()) && y > 0)  ){
                
                 this.setText(String.format(">%d,%d<",x,y));
                 this.setBounds(x,y,this.getWidth(),this.getHeight());
                 this.panel.repaint();
                

            } else {
                  
                 this.setText(String.format("<%d,%d>",x,y));
            }
           
            
        
            

        }
                
    }
    
    
    
}
