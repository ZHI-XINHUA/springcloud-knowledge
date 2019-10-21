package cloud.listener;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

/**
 * @ClassName SinkReceiver
 * @Description 接收rabbitmq消息
 * @Author xh.zhi
 * @Date 2019-10-21 17:16
 * @Version 1.0
 **/
@EnableBinding(Sink.class)
public class SinkReceiver {

    @StreamListener(Sink.INPUT)
    public void receive(Object payload){
        System.out.println(" 接收rabbit信息："+payload);
    }
}
