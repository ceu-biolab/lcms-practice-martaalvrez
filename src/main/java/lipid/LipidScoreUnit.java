package lipid;

import org.drools.ruleunits.api.DataSource;
import org.drools.ruleunits.api.DataStore;
import org.drools.ruleunits.api.RuleUnitData;

import java.util.HashSet;

public class LipidScoreUnit implements RuleUnitData {

    //esta clase sirve para guardar todas las anotaciones y los lípidos
    //para luego analizarlos con reglas y algoritmos

    // !TODO insert here the code to store the data structures containing the facts where the rules will be applied HECHOO

    private final DataStore<Annotation> annotations;
    private final DataStore<Lipid> lipids;

    public LipidScoreUnit() {
        this(DataSource.createStore(), DataSource.createStore());
    }

    public LipidScoreUnit(DataStore<Annotation> annotations, DataStore<Lipid> lipids) {
        this.annotations = annotations;
        this.lipids = lipids;

    }

    public DataStore<Annotation> getAnnotations() {
        return annotations;
    }

    public DataStore<Lipid> getLipids() {
        return lipids;
    }

}