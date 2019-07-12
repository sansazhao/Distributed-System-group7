package Current;

import Core.Current;
import org.apache.zookeeper.ZooKeeper;

public class CurrentChange extends Thread {
    String initiator;
    Double initValue;
    CurrentChange(String i,Double value){
        initiator = i;
        initValue = value;
    }

    @Override
    public void run(){
        while(true){
            Double currentValue,afterValue;
            try {
                currentValue = Current.getCurrentValue(initiator);
                afterValue = currentValue > initValue?currentValue * (Math.random() * 0.5 + 0.5):currentValue * (Math.random() * 0.5 + 1);
                Current.setCurrentValue(initiator, afterValue);
                System.out.println(String.format("Thread %s set %s from %f to %f",Thread.currentThread().getName(),initiator,currentValue,afterValue ));
                Thread.sleep( 60 * 1000);
            }catch(Exception e){
                e.printStackTrace();
                System.out.println("Thread "+ Thread.currentThread().getName() + " set " + initiator + " fails");
            }

        }
    }
}
