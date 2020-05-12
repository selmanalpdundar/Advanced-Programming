package com.selmanalpdundar.drones.alpdundar;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * This is used for following locations of drones
 * @author selmanalpdundar
 */
 public class Location{


    /**
     * @return the x
     */
    public double getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public double getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(double y) {
        this.y = y;
    }
    private double x;
    private double y;
      
    Location(){
        
    }
    Location(double x, double y){
        this.setX(x);
        this.setY(y);
    }
 }
