package lipid;

import adduct.Adduct;
import adduct.AdductList;
import adduct.MassTransformation;

import java.util.*;

/**
 * Class to represent the annotation over a lipid
 */
public class Annotation {

    private final Lipid lipid;
    private final double mz;
    private final double intensity; // intensity of the most abundant peak in the groupedPeaks
    private final double rtMin;
    private final IoniationMode ionizationMode;
    private String adduct; // !!TODO The adduct will be detected based on the groupedSignals
    private final Set<Peak> groupedSignals;
    private int score;
    private int totalScoresApplied;


    /**
     * @param lipid
     * @param mz
     * @param intensity
     * @param retentionTime
     * @param ionizationMode
     */
    public Annotation(Lipid lipid, double mz, double intensity, double retentionTime, IoniationMode ionizationMode) {
        this(lipid, mz, intensity, retentionTime, ionizationMode, Collections.emptySet());
    }

    /**
     * @param lipid
     * @param mz
     * @param intensity
     * @param retentionTime
     * @param ionizationMode
     * @param groupedSignals
     */
    public Annotation(Lipid lipid, double mz, double intensity, double retentionTime, IoniationMode ionizationMode, Set<Peak> groupedSignals) {
        this.lipid = lipid;
        this.mz = mz;
        this.rtMin = retentionTime;
        this.intensity = intensity;
        this.ionizationMode = ionizationMode;
        // !!TODO This set should be sorted according to help the program to deisotope the signals plus detect the adduct
        this.groupedSignals = new TreeSet<>(Comparator.comparingDouble(Peak::getMz));
        this.groupedSignals.addAll(groupedSignals); //nos aseguramos de añadir todas las señales al conjunto
        this.score = 0;
        this.totalScoresApplied = 0;
        this.adduct = detectAdductFromGroupedSignals();
    }

    public Lipid getLipid() {
        return lipid;
    }

    public double getMz() {
        return mz;
    }

    public double getRtMin() {
        return rtMin;
    }

    public String getAdduct() {
        return adduct;
    }

    public void setAdduct(String adduct) {
        this.adduct = adduct;
    }

    public double getIntensity() {
        return intensity;
    }

    public IoniationMode getIonizationMode() {
        return ionizationMode;
    }

    public Set<Peak> getGroupedSignals() {
        return Collections.unmodifiableSet(groupedSignals);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    // !CHECK Take into account that the score should be normalized between -1 and 1
    public void addScore(int delta) {
        this.score += delta;
        this.totalScoresApplied++;
    }

    /**
     * @return The normalized score between 0 and 1 that consists on the final number divided into the times that the rule
     * has been applied.
     */
    public double getNormalizedScore() {
        return (double) this.score / this.totalScoresApplied;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Annotation)) return false;
        Annotation that = (Annotation) o;
        return Double.compare(that.mz, mz) == 0 &&
                Double.compare(that.rtMin, rtMin) == 0 &&
                Objects.equals(lipid, that.lipid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lipid, mz, rtMin);
    }

    @Override
    public String toString() {
        return String.format("Annotation(%s, mz=%.4f, RT=%.2f, adduct=%s, intensity=%.1f, score=%d)",
                lipid.getName(), mz, rtMin, adduct, intensity, score);
    }

     public String detectAdductFromGroupedSignals() {
        double mzTolerance = 0.1;
        if (groupedSignals == null || groupedSignals.size() < 2) { //si no hay por lo menos dos peaks no se puede comparar con nada
            System.out.println("detectAdduct: Not enough signals (" +
                    (groupedSignals == null ? 0 : groupedSignals.size()) + ")");
            return null;
        }

        Map<String, Double> adductMap = (getIonizationMode() == IoniationMode.POSITIVE)
                ? AdductList.MAPMZPOSITIVEADDUCTS //busca en la lista de positivos si el aducto es positivo
                : AdductList.MAPMZNEGATIVEADDUCTS; //busca en la lista de negativos si el aducto es negativo

        double observedMz = this.getMz();
        System.out.println("detectAdduct: observedMz = " + observedMz + ", mode = " + getIonizationMode());

        // itera cada nombre de aducto de la lista de aductos positiva/negativa
        for (String candidateAdduct : adductMap.keySet()) {
            System.out.println("  Probando candidateAdduct: " + candidateAdduct); //imprime el aducto candidato
            try {
                //calculamos la masa monoisotópica del aducto candidato para compararla luego con la del segundo candidato
                //si las masas monoisotópicas son iguales, sabremos que son la misma molécula
                double monoisotopicMass = MassTransformation.getMonoisotopicMassFromMZ(observedMz, candidateAdduct);
                System.out.println("monoisotopicMass = " + monoisotopicMass);

                // Buscamos otro peak cuya monoisotopic mass se corresponda con esa misma masa
                for (Peak otherPeak : groupedSignals) {
                    System.out.println("    Comparando con otherPeak: " + otherPeak);
                    if (Math.abs(otherPeak.getMz() - observedMz) <= mzTolerance) {
                        // Es el mismo pico objetivo, lo saltamos porque no aporta información nueva sobre la molécula
                        System.out.println("      Skip: same as observedMz");
                        continue;
                    }
                    // Probamos todos los aductos posibles para ese otherPeak
                    for (String secondAdduct : adductMap.keySet()) {
                        double expectedMz = MassTransformation
                                .getMZFromMonoisotopicMass(monoisotopicMass, secondAdduct);
                        double diff = Math.abs(expectedMz - otherPeak.getMz());
                        System.out.println("      secondAdduct=" + secondAdduct + ", expectedMz=" + expectedMz + ", observed=" + otherPeak.getMz() + ", diff=" + diff);
                        if (diff <= mzTolerance) {
                            System.out.println("    DETECTED adduct: " + candidateAdduct + " (via " + secondAdduct + ")");
                            return candidateAdduct;
                        }
                    }
                }

            } catch (IllegalArgumentException e) {
                // Si el aducto no se puede parsear, simplemente lo ignoramos
                System.out.println("    Ignorado candidateAdduct (parse error): " + candidateAdduct);
            }
        }

        System.out.println("detectAdduct: Ningún aducto reconocido");
        return null;
    }

    /*public String detectAdductFromGroupedSignals() {
        double mzTolerance=0.1;
        int intensityTolerance=1000;
        if (groupedSignals == null || groupedSignals.size() < 2) {//entonces no se puede encontrar ningún aducto
            System.out.println("No grouped signals.");
            return "Adduct not found";
        }

        Map<String, Double> adductMap = ionizationMode == IoniationMode.POSITIVE
                ? AdductList.MAPMZPOSITIVEADDUCTS //si es positivo el aducto se usa la lista de positivos
                : AdductList.MAPMZNEGATIVEADDUCTS; //en caso contrario, la de negativos

        List<Peak> peaks = new ArrayList<>(groupedSignals); //creamos la lista de picos

        // intensidad máxima
        double maxInten=0.0;
        for(Peak peak1 : peaks){
            if(peak1.getIntensity()>maxInten){
                maxInten=peak1.getIntensity();
                System.out.println(maxInten);
            }
        }
        for (Peak peak1 : peaks) {
            System.out.println("Probando con peak1: " + peak1.getMz() + " (intensity: " + peak1.getIntensity() + ")");
            for (String adduct1 : adductMap.keySet()) { //buscamos en los aductos de la lista, en todos
                try {
                    double monoisotopicMass = MassTransformation.getMonoisotopicMassFromMZ(peak1.getMz(), adduct1);

                    for (String adduct2 : adductMap.keySet()) {
                        double theoreticalMz2 = MassTransformation.getMZFromMonoisotopicMass(monoisotopicMass, adduct2);
                        double observedMz2 = this.getMz();

                        double absDiff = Math.abs(theoreticalMz2 - observedMz2);
                        double intenDiff=Math.abs(peak1.getIntensity() - maxInten);

                        System.out.printf("  → Testing %s -> theoMz: %.6f vs obsMz: %.6f | Δmz: %.6f | Δint: %.2f\n",
                                adduct1, theoreticalMz2, observedMz2, absDiff, intenDiff);

                        if (absDiff <= mzTolerance && intenDiff<=intensityTolerance) {
                            System.out.println("match: " +adduct2);
                            return adduct2;
                        }
                    }
                } catch (Exception e) {

                }
            }
        }

        return null;
    }*/


    // !!TODO Detect the adduct with an algorithm or with drools, up to the user.
}
