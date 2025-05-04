package ae.skydoppler.structs;

import ae.skydoppler.skyblock_locations.SkyblockIslandType;
import ae.skydoppler.skyblock_locations.SkyblockLocation;

public class SkyblockPlayerDataStruct {

    // MONEY
    private long purse;
    private long coopBank;
    private long privateBank;

    // BITS
    private long bits;

    // LOCATION
    private SkyblockIslandType islandType;
    private SkyblockLocation skyblockLocation;

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

    public SkyblockIslandType getIslandType() {
        return islandType;
    }

    public void setIslandType(SkyblockIslandType islandType) {
        this.islandType = islandType;
    }

    public SkyblockLocation getSkyblockLocation() {
        return skyblockLocation;
    }

    public void setSkyblockLocation(SkyblockLocation skyblockLocation) {
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

    public SkyblockPlayerDataStruct() {
        this.purse = 0;
        this.coopBank = 0;
        this.privateBank = 0;
        this.bits = 0;
        this.islandType = SkyblockIslandType.NONE;
        this.skyblockLocation = SkyblockLocation.NONE;
        this.maxHealth = 0;
        this.currentHealth = 0;
        this.maxMana = 0;
        this.currentMana = 0;
        this.overflowMana = 0;
        this.defense = 0;
        this.ehp = 0;
        this.trueDefense = 0;
        this.skillXpStruct = new SkyblockSkillXpStruct();
    }

}
