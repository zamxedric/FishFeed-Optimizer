import view.MainFrame;
import controller.AppController;

public class Main {
    public static void main(String[] args) {
        MainFrame frame = new MainFrame();
        new AppController(frame);
    }
}




