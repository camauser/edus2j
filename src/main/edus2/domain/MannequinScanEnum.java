package edus2.domain;

public enum MannequinScanEnum {
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

    MannequinScanEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static MannequinScanEnum findByName(String name) {
        for (MannequinScanEnum scanEnum : MannequinScanEnum.values()) {
            if (scanEnum.getName().equals(name)) {
                return scanEnum;
            }
        }

        throw new InvalidMannequinScanEnumException(String.format("No scan enum found for '%s'", name));
    }
}
