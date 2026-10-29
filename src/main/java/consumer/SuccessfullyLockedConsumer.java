package consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.orderService.DTO.SuccessfullyLockedEvent;
import org.example.orderService.Service.OrderService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SuccessfullyLockedConsumer {

    private final OrderService orderService;

    @KafkaListener( topics = "successfully_locked")
    public void handleSuccessfullyLockedEvent(String productString) throws JsonProcessingException  {
        ObjectMapper objectMapper = new ObjectMapper();
        SuccessfullyLockedEvent productEvent = objectMapper.readValue(productString, SuccessfullyLockedEvent.class);
        log.info("Receive product message {}", productEvent);

        orderService.statusChanging(productEvent.getOrderId());

    }
}
