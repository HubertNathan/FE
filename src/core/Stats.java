package core;

public class Stats {
    private short LVL, HP, Str, Mag, Skl, Spd, Lck, Def, Res, Mov, Con;
    public Stats(short[] stats){
        assert stats.length == 11;
        LVL = stats[0];
        HP = stats[1];
        Str = stats[2];
        Mag = stats[3];
        Skl = stats[4];
        Spd = stats[5];
        Lck = stats[6];
        Def = stats[7];
        Res = stats[8];
        Mov = stats[9];
        Con = stats[10];
    }
    public short getLVL() {
        return LVL;
    }
    public short getHP() {
        return HP;
    }
    public short getStr() {
        return Str;
    }
    public short getMag() {
        return Mag;
    }
    public short getSkl() {
        return Skl;
    }
    public short getSpd() {
        return Spd;
    }
    public short getLck() {
        return Lck;
    }
    public short getDef() {
        return Def;
    }
    public short getRes() {
        return Res;
    }
    public short getMov() {
        return Mov;
    }
    public short getCon() {
        return Con;
    }
    public void setLVL(short LVL) {
        this.LVL = LVL;
    }
    public void setHP(short HP) {
        this.HP = HP;
    }
    public void setStr(short str) {
        Str = str;
    }
    public void setMag(short mag) {
        Mag = mag;
    }
    public void setSkl(short skl) {
        Skl = skl;
    }
    public void setSpd(short spd) {
        Spd = spd;
    }
    public void setLck(short lck) {
        Lck = lck;
    }
    public void setDef(short def) {
        Def = def;
    }
    public void setRes(short res) {
        Res = res;
    }
    public void setMov(short mov) {
        Mov = mov;
    }
    public void setCon(short con) {
        Con = con;
    }
}
