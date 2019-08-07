package my.rpc.name;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.examples.nameserver.Ip;
import io.grpc.examples.nameserver.Name;
import io.grpc.examples.nameserver.NameServiceGrpc;

import java.util.concurrent.TimeUnit;

public class NameClient {

    private static final String DEFAULT_HOST = "localhost";

    private static final int DEFAULT_PORT = 8088;
    // channel通道, 主要用于通信
    private ManagedChannel managedChannel;
    // stub存根, 调用stub的相应服务
    private NameServiceGrpc.NameServiceBlockingStub nameServiceBlockingStub;

    public NameClient(String host, int port) {
        // 需要注意的是，这里channel要设置成明文传输，即usePlainText设置为true
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build());
    }

    public NameClient(ManagedChannel managedChannel) {
        this.managedChannel = managedChannel;
        this.nameServiceBlockingStub = NameServiceGrpc.newBlockingStub(managedChannel);
    }

    public void shutdown() throws InterruptedException {
        managedChannel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public String getIpByName(String n) {
        Name name = Name.newBuilder().setName(n).build();
        Ip ip = nameServiceBlockingStub.getIpByName(name);
        return ip.getIp();
    }

    public static void main(String[] args) {
        args = new String[] {"Sunny", "David", "Tom", "Sunny", "David", "Tom"};
        NameClient nameClient = new NameClient(DEFAULT_HOST, DEFAULT_PORT);
        for (String arg : args) {
            long startTime = System.currentTimeMillis();
            System.out.println(startTime);
            String res = nameClient.getIpByName(arg);
            long cost = System.currentTimeMillis() - startTime;
            System.out.println("client cost=" + cost + ", get result from server: " + res + " as param is " + arg);
        }
    }
}
