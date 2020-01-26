package edus2.domain;

public enum ManikinScanEnum {
    RIGHT_LUNG("Right Lung"),
    LEFT_LUNG("Left Lung"),
    CARDIAC_PSL_PSS("Cardiac PSL/PSS"),
    CARDIAC_A4C("Cardiac A4C"),
    CARDIAC_SC("Cardiac SC"),
    IVC("IVC"),
    RUQ("Right Upper Quadrant"),
    LUQ("Left Upper Quadrant"),
    ABDOMINAL_AORTA("Abdominal Aorta"),
    PELVIS("Pelvis");

    private String name;

    ManikinScanEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static ManikinScanEnum findByName(String name) {
        for (ManikinScanEnum scanEnum : ManikinScanEnum.values()) {
            if (scanEnum.getName().equals(name)) {
                return scanEnum;
            }
        }

        throw new InvalidManikinScanEnumException(String.format("No scan enum found for '%s'", name));
    }
}
