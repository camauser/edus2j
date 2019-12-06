package edus2.domain;

public enum MannequinScanEnum {
    RIGHT_LUNG("Right Lung"),
    LEFT_LUNG("Left Lung"),
    CARDIAC_PSL_PSS("Cardiac PSL/PSS"),
    CARDIAC_A4C("Cardiac A4C"),
    CARDIAC_SC("Cardiac SC"),
    IVC("IVC"),
    RUQ("RUQ"),
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
}
