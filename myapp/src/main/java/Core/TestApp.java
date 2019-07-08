package Core;
import Core.CommodityService;
import Entity.Commodity;
public class TestApp {
    public static void main(String[] args){
        CommodityService.clearCommodity();
        System.out.println("TestApp over");
    }
}
