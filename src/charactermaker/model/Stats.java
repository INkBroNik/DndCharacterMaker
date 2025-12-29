package charactermaker.model;
 
import charactermaker.enums.Race;
import charactermaker.enums.Stat;

/**
 * Stats.java - description
 *
 * @author YOUR NAME
 * @since 12 Dec 2025, 10:07:17 am
 */
public class Stats 
{
    private int racialSTR, racialDEX, racialCON, racialINT, racialWIS, racialCHA; // racial bonuses
    private int baseSTR, baseDEX, baseCON, baseINT, baseWIS, baseCHA;  
    private final int BASE = 10; // base value
    private int str, dex, con, intel, wis, cha;
    private boolean hasARace;
    private Race previusRace;

    private void addRacialBonuses(Stat stat, int value) {
        switch (stat) {
            case STR -> racialSTR += value;
            case DEX -> racialDEX += value;
            case CON -> racialCON += value;
            case INT -> racialINT += value;
            case WIS -> racialWIS += value;
            case CHA -> racialCHA += value;
        }
    }
    public void removeRacialBonuses(Race race){
        for (var entry : race.getBonuses().entrySet()) { addRacialBonuses(entry.getKey(), -entry.getValue());}
    }
    public void applyRacialBonuses(Race race){
        if (hasARace) { removeRacialBonuses(previusRace); }
        for (var entry : race.getBonuses().entrySet()) { addRacialBonuses(entry.getKey(), entry.getValue()); }
        previusRace = race;
        hasARace = true;
    }
    public void addBaseStats
        (int STR, int DEX, int CON, int INT, int WIS, int CHA) {
        this.baseSTR = STR;
        this.baseDEX = DEX;
        this.baseCON = CON;
        this.baseINT = INT;
        this.baseWIS = WIS;
        this.baseCHA = CHA;
    }
    
    public  int  getSTR()   { return racialSTR + baseSTR; }
    public  int  getDEX()   { return racialDEX + baseDEX; }
    public  int  getCON()   { return racialCON + baseCON; }
    public  int  getINT()   { return racialINT + baseINT; }
    public  int  getWIS()   { return racialWIS + baseWIS; }
    public  int  getCHA()   { return racialCHA + baseCHA; }

    public  int  getStrMod(){ 
        str = getValue(racialSTR, baseSTR);
        return getMod(str);
    }
    public  int  getDexMod(){
        dex = getValue(racialDEX, baseDEX);
        return getMod(dex);
    }
    public  int  getConMod(){
        con = getValue(racialCON, baseCON);
        return getMod(con);
    }
    public  int  getIntMod(){
        intel = getValue(racialINT, baseINT);
        return getMod(intel);}
    public  int  getWisMod()
    {
        wis = getValue(racialWIS, baseWIS);
        return getMod(wis);
    }
    public  int  getChaMod(){
        cha = getValue(racialCHA, baseCHA);
        return getMod(cha);
    }
    
    private int  getMod(int stat){
        return (int) Math.floor((stat - BASE) / 2.0); 
    }
    private int getValue(int racial, int base) {
        return racial + base;
    }
   
    @Override
    public String toString(){
    return "\nStats:"   +
           "\n\tSTR:"   + getSTR() +
           "\n\tDEX:"   + getDEX() +
           "\n\tCON:"   + getCON() +
           "\n\tINT:"   + getINT() +
           "\n\tWIS:"   + getWIS() +
           "\n\tCHA:"   + getCHA();
    }
}
