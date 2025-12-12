package edu.urv.lifeify.model;

import java.util.ArrayList;
import java.util.List;

import edu.urv.lifeify.placeholder.PlaceholderContent;

public abstract class DataSource {

    public enum DataSourceType {
        HARDCODED,JSON,SQL,FIRESTORE
    }


    public  List<ModSection> SECTIONS = new ArrayList<ModSection>();


    // Singleton instance
    protected static DataSource instance;

    // Protected constructor to prevent external instantiation
    protected DataSource() {}

    // Method that should be implemented by subclasses
    public abstract void loadModel();
    public abstract  void loadSection(ModSection sec);
    public abstract List<ModSection> getSections();
    public abstract List<ModObjective> getObjectives(String section_id);
    public abstract ModSection getSection(String section_id);

    public double Recalculate(String section_id)
    {
        ModSection s = getSection(section_id);

        int num_ok = 0;
        for (ModObjective o : s.objectives) {
           if(o.type == ModObjective.ObjType.YES_NO){
               if(o.b_value){
                   num_ok++;
               }
           }
           else if(o.type == ModObjective.ObjType.NUMBER){
                if(o.act_value>=o.max_value){
                    num_ok++;
                }
            }

        }

        if(num_ok==s.objectives.size()) {
            s.level = 100.0;
        }
        else{
            s.level = 0.0;
        }

        return s.level;
    }

    // Factory method to be implemented by subclasses for returning the singleton instance
    public static DataSource getInstance() {
        return instance;
    }

}
