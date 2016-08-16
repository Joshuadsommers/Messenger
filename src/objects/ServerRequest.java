package objects;

/**
 * Created by Adam on 8/16/2016.
 */
public class ServerRequest {

    private int requestType;

    public ServerRequest(int requestType){
        this.requestType = requestType;
    }

    public int getRequestType() {
        return requestType;
    }

}
