package uk.ac.ncl.nclwater.firm2;

public class Test {
//    static Model model = new Model();
static Thread modelthread;

    static ModelParameters modelParameters = new ModelParameters();
    static Model model;

    public static void main(String[] args) {
        modelParameters.setWidth(100);
        modelParameters.setHeight(100);
        modelParameters.setToroidal(true);
        modelParameters.setTicks(30);
        modelParameters.setVisualise(true);
        modelParameters.setCell_size(5);
        model = new Model(modelParameters);
        modelthread = new Thread(model);
        model.setRun(false);
        model.getVisualisation().setModelThread(modelthread);
        modelthread.start();
    }
}
