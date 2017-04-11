package com.mycompany.istudy.db.services.intf;

import com.mycompany.istudy.db.entities.Investedhoursperweekformodule;
import com.mycompany.istudy.db.entities.Modul;
import com.mycompany.istudy.db.entities.Semester;
import java.util.List;

/**
 * Interface for the Manager class of InvestedHoursPerWeekForModule entity.
 * Created by Cham on 17.06.2016.
 */
public interface InvestedHoursPerWeekForModuleManagerIntf {

    public List<Investedhoursperweekformodule> getAllEntriesForModule(Modul module, Semester activeSemester);

    public Investedhoursperweekformodule getByModuleAndWeek(Modul module, int week, Semester activeSemester);

    public void updateEntity(Investedhoursperweekformodule entity);

    public void insertEntity(Investedhoursperweekformodule entity);
}
