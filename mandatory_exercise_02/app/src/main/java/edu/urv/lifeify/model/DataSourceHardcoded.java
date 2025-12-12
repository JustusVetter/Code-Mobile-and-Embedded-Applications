package edu.urv.lifeify.model;

import java.util.ArrayList;
import java.util.List;

public class DataSourceHardcoded extends DataSource{

    // Private constructor to prevent external instantiation
    private DataSourceHardcoded() {}

    // Public method to return the singleton instance
    public static synchronized DataSource getInstance() {
        if (instance == null) {
            instance = new DataSourceHardcoded();
        }
        return instance;
    }

    @Override
    public  void loadSection(ModSection sec){
        if(sec.id.equals("1")){
            sec.objectives = new ArrayList<ModObjective>();
            ModObjective o = new ModObjective("101","Eat Vegetables", ModObjective.ObjType.YES_NO,0.0,0,0,false);
            sec.objectives.add(o);
            o = new ModObjective("102","Walk 60 minutes", ModObjective.ObjType.NUMBER,0.0,0,60,false);
            sec.objectives.add(o);
            o = new ModObjective("103","Drink water", ModObjective.ObjType.YES_NO,0.0,0,0,false);
            sec.objectives.add(o);
        }
    }

    @Override
    public void loadModel()
    {
        SECTIONS = new ArrayList<ModSection>();

        ModSection s = new ModSection("1","Health",0);
        SECTIONS.add(s);
        loadSection(s);

        s = new ModSection("2","Work",0);
        SECTIONS.add(s);
        loadSection(s);

        s = new ModSection("3","Social Life",0);
        SECTIONS.add(s);
        loadSection(s);

    }

    @Override
    public  List<ModSection> getSections()
    {
        return SECTIONS;
    }

    @Override
    public  ModSection getSection(String section_id)
    {

        for (ModSection section : SECTIONS) {
            // Check if the section's id matches the given section_id
            if (section.id.equals(section_id)) {
                // Return the objectives for the matching section
                return section;
            }
        }

        return null;
    }

    @Override
    public  List<ModObjective> getObjectives(String section_id){

        // Loop through each ModSection in the SECTIONS list
        for (ModSection section : SECTIONS) {
            // Check if the section's id matches the given section_id
            if (section.id.equals(section_id)) {
                // Return the objectives for the matching section
                return section.objectives;
            }
        }
        // If no section is found with the given id, return an empty list
        return new ArrayList<ModObjective>();
    }


}
