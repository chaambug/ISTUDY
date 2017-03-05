/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.istudy.controller;

import java.util.Observable;

/**
 *
 * @author Varuni
 */
public class NotificationColllector extends Observable{
    
    public NotificationColllector(){
    }
    
    public void notificationChnaged(){
        setChanged();
        notifyObservers();
    }
    
    
}
