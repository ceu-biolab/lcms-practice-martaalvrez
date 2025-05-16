/*package lipid;

import adduct.Adduct;
import adduct.MassTransformation;

import java.util.Set;

public class DebugAnnotationMain {

    public static void main(String[] args) {
       /* System.out.println("=== TEST: Detectar [M+K]+ con dos picos ===");

        // 1. Establecemos la monoisotopic mass simulada
        double monoMass = 699.4927;

        // 2. Calculamos los m/z teóricos de dos aductos
        double mzK = MassTransformation.getMZFromMonoisotopicMass(monoMass, "[M+K]+"); // esperado ~738.4564
        double mzH = MassTransformation.getMZFromMonoisotopicMass(monoMass, "[M+H]+"); // esperado ~700.5000

        // 3. Creamos los picos (el de K+ es más intenso)
        Peak mK = new Peak(mzK, 100000.0);     // [M+K]+ → principal
        Peak mH = new Peak(mzH, 60000.0);      // [M+H]+ → secundario

        // 4. Creamos el lípido
        Lipid lipid = new Lipid(1, "PC 34:1", "C42H82NO8P", LipidType.PC, 34, 1);

        // 5. Creamos la anotación con ambos picos agrupados
        Annotation annotation = new Annotation(
                lipid,
                mzK, // este tiene que ser igual al mz teórico de [M+K]+
                100000.0,
                6.5,
                IoniationMode.POSITIVE,
                Set.of(mK, mH)
        );

        // 6. Mostramos el resultado
        System.out.println("\n--- Resultado ---");
        System.out.printf("mz observado: %.6f\n", annotation.getMz());
        System.out.println("Aducto detectado: " + annotation.getAdduct());

        Peak singlyCharged = new Peak(700.500, 100000.0);  // [M+H]+
        Peak doublyCharged = new Peak(350.754, 85000.0);   // [M+2H]2+

        Lipid lipid1 = new Lipid(3, "TG 54:3", "C57H104O6", LipidType.TG, 54, 3);
        Annotation annotation1 = new Annotation(lipid1, doublyCharged.getMz(), doublyCharged.getIntensity(), 10d, IoniationMode.POSITIVE, Set.of(singlyCharged, doublyCharged));
        System.out.println(annotation1);

    }

}*/