package my.rpc.name;

import io.grpc.examples.nameserver.Ip;
import io.grpc.examples.nameserver.Name;
import io.grpc.examples.nameserver.NameServiceGrpc;
import io.grpc.stub.StreamObserver;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class NameServiceImplBaseImpl extends NameServiceGrpc.NameServiceImplBase {

    private Map<String, String> map = new HashMap<String, String>();

    private Logger logger = Logger.getLogger(NameServiceImplBaseImpl.class.getName());

    public NameServiceImplBaseImpl() {
        map.put("Sunny", "125.216.242.51");
        map.put("David", "117.226.178.139");
    }

    @Override
    public void getIpByName(Name request, StreamObserver<Ip> responseObserver) {
        logger.log(Level.INFO, "requst is coming. args=" + request.getName());
        //
        long startTime = System.currentTimeMillis();
        Ip ip = Ip.newBuilder().setIp(getName(request.getName())).build();
        sleep(1000);
        responseObserver.onNext(ip);
        responseObserver.onCompleted();
        sleep(1000);
        long cost = System.currentTimeMillis() - startTime;
        System.out.println("###server cost=" + cost);
    }

    public String getName(String name) {
        String ip = map.get(name);
        if (ip == null) {
            return "0.0.0.0";
        }
        return ip;
    }

    public static void sleep(long mills) {
        try {
            Thread.sleep(mills);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
