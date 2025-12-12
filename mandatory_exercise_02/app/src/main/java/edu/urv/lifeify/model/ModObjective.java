package edu.urv.lifeify.model;

public class ModObjective {

    public enum ObjType {
        NONE,YES_NO, NUMBER
    }
    public String id="0";
    public String content="";

    public ObjType type = ObjType.NONE;
    public int max_value = 0;
    public int act_value = 0;
    public boolean b_value = false;

    public double level = 0.0;
    public double old_level = 0.0;



    public ModObjective(String id, String content,ObjType type, double level,int act_value,int max_value,boolean b_value)
    {
        this.id = id;
        this.content = content;
        this.type = type;

        this.level = level;
        this.old_level = level;

        this.act_value = act_value;
        this.max_value = max_value;
        this.b_value = b_value;
    }

}
