package com.mycompany.istudy.xml.entities;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "modulesType", propOrder = {
    "module"
})
public class ModulesType {

    protected List<ModuleType> module;

    public List<ModuleType> getModule() {
        if (module == null) {
            module = new ArrayList<ModuleType>();
        }
        return this.module;
    }

    public void setModule(List<ModuleType> module) {
        this.module = module;
    }
}
