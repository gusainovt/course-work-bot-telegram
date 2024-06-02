package pro.sky.telegrambot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Configuration
public class ScheduledMethods {


    @Autowired
    private NotificationTaskRepository notificationTaskRepository;
    @Autowired
    private TelegramBot telegramBot;


    @Scheduled(cron = "0 0/1 * * * *")

    public void sendingNotification() {
        List<NotificationTask> notificationTasks = notificationTaskRepository.findAll();
        LocalDateTime dateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

        notificationTasks.forEach(
                task -> {
                    if (task.getDataTime().isBefore(dateTime)) {
                        SendMessage message = new SendMessage(
                                task.getChatId(),
                                task.getTextNotification());
                        SendResponse response = telegramBot.execute(message);
                        notificationTaskRepository.delete(task);
                    }
                }
        );
    }
}
