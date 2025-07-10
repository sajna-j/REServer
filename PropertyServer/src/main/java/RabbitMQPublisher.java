import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RabbitMQPublisher {
  private static final String QUEUE_NAME = "analytics_queue";
  private final Channel channel;

  public RabbitMQPublisher() throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    this.channel = connection.createChannel();
    channel.queueDeclare(QUEUE_NAME, true, false, false, null);
  }

  public void publishMessage(String message) throws IOException {
    channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
  }

  public void close() throws Exception {
    channel.close();
    channel.getConnection().close();
  }
}