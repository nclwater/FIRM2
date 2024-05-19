package uk.ac.ncl.nclwater.firm2.firm2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Defences {
    @SerializedName("defences")
    @Expose
    private List<Defence> defences = new ArrayList<>();

    public List<Defence> getDefences() {
        return defences;
    }

    public void setDefences(List<Defence> defences) {
        this.defences = defences;
    }

    public void add(Defence defence) {
        defences.add(defence);
    }
}
