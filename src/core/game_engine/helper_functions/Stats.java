package core.game_engine.helper_functions;

public class Stats {
    private byte LVL, HP, Str, Mag, Skl, Spd, Lck, Def, Res, Mov, Con;
    public Stats(byte[] stats){
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
    public byte getLVL() {
        return LVL;
    }
    public byte getHP() {
        return HP;
    }
    public byte getStr() {
        return Str;
    }
    public byte getMag() {
        return Mag;
    }
    public byte getSkl() {
        return Skl;
    }
    public byte getSpd() {
        return Spd;
    }
    public byte getLck() {
        return Lck;
    }
    public byte getDef() {
        return Def;
    }
    public byte getRes() {
        return Res;
    }
    public byte getMov() {
        return Mov;
    }
    public byte getCon() {
        return Con;
    }
    public void setLVL(byte LVL) {
        this.LVL = LVL;
    }
    public void setHP(byte HP) {
        this.HP = HP;
    }
    public void setStr(byte str) {
        Str = str;
    }
    public void setMag(byte mag) {
        Mag = mag;
    }
    public void setSkl(byte skl) {
        Skl = skl;
    }
    public void setSpd(byte spd) {
        Spd = spd;
    }
    public void setLck(byte lck) {
        Lck = lck;
    }
    public void setDef(byte def) {
        Def = def;
    }
    public void setRes(byte res) {
        Res = res;
    }
    public void setMov(byte mov) {
        Mov = mov;
    }
    public void setCon(byte con) {
        Con = con;
    }
}
