package ae.skydoppler.structs;

public class SkyblockPlayerDataStruct {

    // MONEY
    private long purse;
    private long coopBank;
    private long privateBank;

    // BITS
    private long bits;

    // LOCATION
    private String islandName;
    private String skyblockLocation;

    // ACTION BAR STUFF
    private int maxHealth;
    private int currentHealth;

    private int maxMana;
    private int currentMana;
    private int overflowMana;

    private int defense;
    private int ehp;
    private int trueDefense;

    // SKILLS
    private SkyblockSkillXpStruct skillXpStruct;

    public long getPurse() {
        return purse;
    }

    public void setPurse(long purse) {
        this.purse = purse;
    }

    public long getCoopBank() {
        return coopBank;
    }

    public void setCoopBank(long coopBank) {
        this.coopBank = coopBank;
    }

    public long getPrivateBank() {
        return privateBank;
    }

    public void setPrivateBank(long privateBank) {
        this.privateBank = privateBank;
    }

    public long getBits() {
        return bits;
    }

    public void setBits(long bits) {
        this.bits = bits;
    }

    public String getIslandName() {
        return islandName;
    }

    public void setIslandName(String islandName) {
        this.islandName = islandName;
    }

    public String getSkyblockLocation() {
        return skyblockLocation;
    }

    public void setSkyblockLocation(String skyblockLocation) {
        this.skyblockLocation = skyblockLocation;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public int getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(int maxMana) {
        this.maxMana = maxMana;
    }

    public int getCurrentMana() {
        return currentMana;
    }

    public void setCurrentMana(int currentMana) {
        this.currentMana = currentMana;
    }

    public int getOverflowMana() {
        return overflowMana;
    }

    public void setOverflowMana(int overflowMana) {
        this.overflowMana = overflowMana;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getEhp() {
        return ehp;
    }

    public void setEhp(int ehp) {
        this.ehp = ehp;
    }

    public int getTrueDefense() {
        return trueDefense;
    }

    public void setTrueDefense(int trueDefense) {
        this.trueDefense = trueDefense;
    }

    public SkyblockSkillXpStruct getSkillXpStruct() {
        return skillXpStruct;
    }

    public void setSkillXpStruct(SkyblockSkillXpStruct skillXpStruct) {
        this.skillXpStruct = skillXpStruct;
    }
}
