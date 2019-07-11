package Current;
import Core.Current;
import Current.CurrentChange;
public class App {
    static public void main(String[] args) {
        String[] currencies = {"RMB","USD","JPY","EUR"};
        Double[] initValues = {2.0, 12.0, 0.15, 9.0};
        CurrentChange[] threads = new CurrentChange[currencies.length];

        for (int i = 0; i < threads.length; i++) {
            threads[i] = new CurrentChange(currencies[i],initValues[i]);
        }

        for (CurrentChange thread : threads) {
            thread.start();
        }
    }
}
