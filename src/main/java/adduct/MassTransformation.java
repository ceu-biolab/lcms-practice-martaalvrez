package adduct;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MassTransformation {

// !! TODO create functions to transform the mass of the mzs to monoisotopic masses and vice versa.

    /**
     * Calculate the mass to search depending on the adduct hypothesis
     *
     * @param mz mz
     * @param adduct adduct name ([M+H]+, [2M+H]+, [M+2H]2+, etc..)
     *
     * @return the monoisotopic mass of the experimental mass mz with the adduct @param adduct
     */
    public static Double getMonoisotopicMassFromMZ(Double mz, String adduct) {
        // !! TODO METHOD HECHOOO
        // !! TODO Create the necessary regex to obtain the multimer (number before the M) and the charge (number before the + or - (if no number, the charge is 1).
        //primero declaramos las variables que vamos a usar
        Double massToSearch; // masa del aducto
        double monoisotopicMass = 0; // donde guardaremos la masa calculada, el resultado
        int charge = 1; //por defecto
        int numMultimer = 1; //por defecto

        //creamos el regex para identificar el número de antes de la M y el de antes del signo
        //regex es para buscar patrones en el texto

        Pattern searchPattern = Pattern.compile("^(\\d*)M.*?(\\d*)[+-]$"); //crea el patrón que buscamos
        Matcher matcher = searchPattern.matcher(adduct); //compara el patrón con los aductos

        if(matcher.find()){
            String numMultimerString = matcher.group(1); //guarda el número que vaya antes de la M
            String numChargeString = matcher.group(2); //guarda el número que vaya antes del signo

            numMultimer = numMultimerString.isEmpty() ? 1 : Integer.parseInt(numMultimerString);
            charge = numChargeString.isEmpty() ? 1 : Integer.parseInt(numChargeString);
            //se asigna el número de multímero y de carga, y si está vacío el string es porque la carga/numMult = 1
        }

        //ahora buscamos el aducto en la lista de aductos para buscar su masa
        //buscamos en la lista de positivos, si no se encuentra en la de negativos y sino está en ninguna, da error

        massToSearch = AdductList.MAPMZPOSITIVEADDUCTS.get(adduct);
        if(massToSearch == null) {
            massToSearch = AdductList.MAPMZNEGATIVEADDUCTS.get(adduct);
        }
        if(massToSearch == null) {
            throw new IllegalArgumentException("Adduct not recognized" +adduct);
        }

        //aplicamos las fórmulas dependiendo de la carga y el número de multímeros

        /*if (charge == 1) {
            monoisotopicMass = mz + massToSearch;
        } else {
            monoisotopicMass = (mz + massToSearch) * charge;
        }

        if (numMultimer > 1) {
            monoisotopicMass = monoisotopicMass / numMultimer;
            //coge la monoisotopic mass calculada antes, y si el multimer es mayor que 1, lo dividie por ese número
        }*/

        monoisotopicMass = ((mz * charge) + massToSearch) / numMultimer;
        /*
        if Adduct is single charge the formula is M = m/z +- adductMass. Charge is 1 so it does not affect

        if Adduct is double or triple charged the formula is M = ( mz +- adductMass ) * charge

        if adduct is a dimer or multimer the formula is M =  (mz +- adductMass) / numberOfMultimer

        return monoisotopicMass;

         */
        return monoisotopicMass; //monoisotopic mass es la masa neutra de la molécula, sin ningún aducto
    }

    /**
     * Calculate the mz of a monoisotopic mass with the corresponding adduct
     *
     * @param monoisotopicMass
     * @param adduct adduct name ([M+H]+, [2M+H]+, [M+2H]2+, etc..)
     *
     * @return
     */
    public static Double getMZFromMonoisotopicMass(Double monoisotopicMass, String adduct) {
        // !! TODO METHOD HECHOOOO
        // !! TODO Create the necessary regex to obtain the multimer (number before the M) and the charge (number before the + or - (if no number, the charge is 1).

        //declaramos variables
        Double massToSearch; // masa del aducto
        double mz = 0;
        int charge = 1; //por defecto
        int numMultimer = 1; //por defecto

        //creamos el patrón regex igual que antes
        Pattern searchPattern = Pattern.compile("^\\D*(\\d*)M.*?(\\d*)[+-]$"); //crea el patrón que buscamos
        Matcher matcher = searchPattern.matcher(adduct); //compara el patrón con los aductos

        if (matcher.find()) {
            String numMultimerString = matcher.group(1); //guarda el número que vaya antes de la M
            String numChargeString = matcher.group(2); //guarda el número que vaya antes del signo

            numMultimer = numMultimerString.isEmpty() ? 1 : Integer.parseInt(numMultimerString);
            charge = numChargeString.isEmpty() ? 1 : Integer.parseInt(numChargeString);
            //se asigna el número de multímero y de carga, y si está vacío el string es porque la carga/numMult = 1
        }

        //buscamos el aducto en la lista de aductos para buscar su masa
        //buscamos en la lista de positivos, si no se encuentra en la de negativos y sino está en ninguna, da error

        massToSearch = AdductList.MAPMZPOSITIVEADDUCTS.get(adduct);
        if (massToSearch == null) {
            massToSearch = AdductList.MAPMZNEGATIVEADDUCTS.get(adduct);
        }
        if (massToSearch == null) {
            throw new IllegalArgumentException("Adduct not recognized" + adduct);
        }

        //calculamos la mz

        //mz = monoisotopicMass; //empezamos igualando porque la monoisotopicMass es la masa pero sin el aducto, es la masa real

        // Si hay multímero, multiplicamos
        /* if (numMultimer > 1) {
            mz = mz * numMultimer;
        }

        // Si la carga es mayor que 1, dividimos
        if (charge > 1) {
            mz = mz / charge;
        } */


        mz = ((monoisotopicMass * numMultimer) - massToSearch)/charge;

        return mz; //masaReal + masaAducto que es lo que mide la mass Spectrometry


        /*
        if Adduct is single charge the formula is m/z = M +- adductMass. Charge is 1 so it does not affect

        if Adduct is double or triple charged the formula is mz = M/charge +- adductMass

        if adduct is a dimer or multimer the formula is mz = M * numberOfMultimer +- adductMass

        return monoisotopicMass;

         */

    }

}
