package Web;


import Entity.Commodity;
import fi.iki.elonen.NanoHTTPD;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import Core.CommodityService;
import Core.ResultService;
import sun.security.action.GetBooleanAction;

public class WebApp extends NanoHTTPD{
    public WebApp(int port) throws IOException{
        super(port);
    }
    static public void main(String[] args){
        try {
            new WebApp(30441).start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public NanoHTTPD.Response serve(NanoHTTPD.IHTTPSession session) {
        String uri = session.getUri();
        if(uri.startsWith("/clear/database")){
            //CommodityService.getCommodity(1);

            CommodityService.clearCommodity();
            ResultService.clearResult();


        }else if(uri.startsWith("/post/order")){
            Map<String, String> json = new HashMap<String, String>();
            try {
                session.parseBody(json);
            }catch(Exception e){
                e.printStackTrace();
            }
            PostOrder(json);

        }else if(uri.startsWith("/get/result")){
            Map<String, String> parms = session.getParms();
            return newFixedLengthResponse(GetResult());
        }
        return super.serve(session);
    }

    void PostOrder(Map<String, String> json){
        return;
    }
    String GetResult(){
        return "result";
    }
}
