package uk.ac.ncl.nclwater.firm2;

public class Test {
//    static Model model = new Model();
static Thread modelthread;

    static Model model = new Model();

    public static void main(String[] args) {
        modelthread = new Thread(model);
        model.setRun(true);
        modelthread.start();
    }
}
