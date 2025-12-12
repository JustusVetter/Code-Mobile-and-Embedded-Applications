package edu.urv.lifeify.model;

import java.util.ArrayList;
import java.util.List;

public class ModSection {
   public String id="0";
   public String content="";

   public double level = 0.0;



   public List<ModObjective> objectives = new ArrayList<ModObjective>();

   public ModSection(String id, String content, double level)
   {
         this.id = id;
         this.content = content;
         this.level = level;

   }
}
