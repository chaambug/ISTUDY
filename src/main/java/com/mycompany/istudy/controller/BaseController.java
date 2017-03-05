/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.istudy.controller;

import com.mycompany.istudy.gui.UserWin;

/**
 * Ploym.
 * @author Varuni
 */
    public abstract class BaseController {
    
    protected UserWin instance;
    
    BaseController(UserWin instance) {
        this.instance = instance;
    }
    
    public abstract void init();
    
    protected enum ButtonConstants {
        ACTIVATE,
        DEACTIVATE;
    }
}
