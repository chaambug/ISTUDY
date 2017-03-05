/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.istudy.db.services.intf;

import com.mycompany.istudy.db.entities.Student;
import com.mycompany.istudy.db.entities.Userloginentries;
import java.util.List;

/**
 *
 * @author Varuni
 */
public interface UserLoginEntriesManagerIntf {

    public void insertLoginEntry(Userloginentries obj);

    public List<Userloginentries> getLoginEntriesForUser(Student student);
}
