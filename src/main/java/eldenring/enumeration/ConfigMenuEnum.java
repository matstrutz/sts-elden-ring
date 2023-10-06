package eldenring.enumeration;

public enum ConfigMenuEnum {
    NORMAL("normal", "DisableNormalMonsters", 650F),
    ELITE("elite", "DisableEliteMonsters", 600F),
    BOSS("boss", "DisableBossMonsters", 550F),
    RELIC("relic", "DisableRelics", 500F),
    POTION("potion", "DisablePotions", 450F),
    EVENT("event", "DisableEvents", 400F),;

    ConfigMenuEnum(String fileId, String uiId, Float yPos) {
        this.fileId = fileId;
        this.uiId = uiId;
        this.yPos = yPos;
    }

    private String fileId;
    private String uiId;
    private Float yPos;
    private Boolean button;

    public String getFileId() {
        return fileId;
    }

    public String getUiId() {
        return uiId;
    }

    public Float getyPos() {
        return yPos;
    }

    public Boolean getButton() {
        return button;
    }

    public void setButton(Boolean button) {
        this.button = button;
    }
}
