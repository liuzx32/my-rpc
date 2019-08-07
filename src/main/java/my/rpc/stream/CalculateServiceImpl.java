package my.rpc.stream;

import io.grpc.examples.calculate.CalculateProto;
import io.grpc.examples.calculate.CalculateServiceGrpc.CalculateServiceImplBase;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalculateServiceImpl extends CalculateServiceImplBase {

    private Logger log = LoggerFactory.getLogger(CalculateServiceImpl.class);

    @Override
    public StreamObserver<CalculateProto.Value> getResult(StreamObserver<CalculateProto.Result> responseObserver) {
        return new StreamObserver<CalculateProto.Value>() {

            private int sum = 0;
            private int cnt = 0;
            private double avg;

            public void onNext(CalculateProto.Value value) {
                log.info("接收到消息为:{}", value.getValue());
                sum += value.getValue();
                cnt++;
                avg = 1.0 * sum / cnt;
                // 返回当前统计结果
                CalculateProto.Result response = CalculateProto.Result.newBuilder().setSum(sum).setCnt(cnt).setAvg(avg).build();
                log.info("返回消息为:{}", response);
                responseObserver.onNext(response);
            }

            public void onError(Throwable throwable) {
                log.warn("调用出错:{}", throwable.getMessage());
            }

            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }
}
