package lipid;

import java.util.Objects;

public class Lipid {
    private final int compoundId;
    private final String name;
    private final String formula;
    private final LipidType lipidType; // !! OPTIONAL TODO -> TRANSFORM INTO AN ENUMERATION
    private final int carbonCount;
    private final int doubleBondsCount;


    /**
     * @param compoundId
     * @param name
     * @param formula
     * @param lipidType
     * @param carbonCount
     * @param doubleBondCount
     */
    public Lipid(int compoundId, String name, String formula, LipidType lipidType, int carbonCount, int doubleBondCount) {
        this.compoundId = compoundId;
        this.name = name;
        this.formula = formula;
        this.lipidType = lipidType;
        this.carbonCount = carbonCount;
        this.doubleBondsCount = doubleBondCount;
    }

    public int getCompoundId() {
        return compoundId;
    }

    public String getName() {
        return name;
    }

    public String getFormula() {
        return formula;
    }

    public LipidType getLipidType() {
        return this.lipidType;
    }

    public int getCarbonCount() {
        return carbonCount;
    }

    public int getDoubleBondsCount() {
        return doubleBondsCount;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Lipid)) return false;
        Lipid lipid = (Lipid) o;
        return compoundId == lipid.compoundId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(compoundId);
    }

    @Override
    public String toString() {
        return "Lipid{" +
                "compoundId=" + compoundId +
                ", name='" + name + '\'' +
                ", formula='" + formula + '\'' +
                ", lipidType='" + lipidType + '\'' +
                ", carbonCount=" + carbonCount +
                ", doubleBondCount=" + doubleBondsCount +
                '}';
    }

    public double getMonoisotopicMass() {
        switch (this.name) {
            case "PC 34:1": return 699.4927;
            case "PE 36:2": return 699.4927;
            case "TG 54:3": return 699.4927;
            case "TG 52:3": return 856.7520;
            case "TG 56:3": return 912.8147;
            case "TG 54:2": return 886.7989;
            case "TG 54:4": return 882.7676;
            case "PG 34:0": return 750.5411;
            case "PI 34:0": return 838.5571;
            case "PC 34:0": return 760.6087;
            case "PE 34:1": return 721.3927;
            case "PE 36:1": return 742.5767;
            case "PC 36:2": return 788.6238;
            case "PC 36:4": return 784.5927;
            default: return 700.0;
        }
    }
}
