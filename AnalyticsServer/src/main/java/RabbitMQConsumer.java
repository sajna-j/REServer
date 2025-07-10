import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;

public class RabbitMQConsumer {

  private final String queue;
  private final AnalyticsDAO analyticsDAO;

  public RabbitMQConsumer(String queue, AnalyticsDAO analyticsDAO) {
    this.queue = queue;
    this.analyticsDAO = analyticsDAO;
  }

  public void start() throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("localhost");
    Connection connection = factory.newConnection();
    Channel channel = connection.createChannel();
    channel.queueDeclare(queue, true, false, false, null);

    System.out.println("Listening on " + queue);

    DeliverCallback callback = (tag, delivery) -> {
      String id = new String(delivery.getBody(), StandardCharsets.UTF_8);
      System.out.println(" [x] Received ID: " + id);
      if (queue.equals("property_id_queue"))
      {
        analyticsDAO.incrementPropertyAnalytics(id);
      }
      else
      {
        analyticsDAO.incrementPostcodeAnalytics(id);
      }
    };

    channel.basicConsume(queue, true, callback, consumerTag -> {});
  }
}