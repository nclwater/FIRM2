package uk.ac.ncl.nclwater.firm2;

public class Test {
//    static Model model = new Model();
static Thread modelthread;

    static Visualisation visualisation;
    static Model model = new Model(visualisation);

    public static void main(String[] args) {
        modelthread = new Thread(model);
        model.setRun(true);
        modelthread.start();
        Visualisation visualisation = new Visualisation(modelthread, model, model.getCell_size());
        model.setVisualisation(visualisation);
    }
}
