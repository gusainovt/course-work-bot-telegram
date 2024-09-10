package pro.sky.telegrambot.service;

import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;

import static pro.sky.telegrambot.service.constants.Constants.*;

@Service
public class NotificationTaskService {

    private final NotificationTaskRepository notificationTaskRepository;

    public NotificationTaskService(NotificationTaskRepository notificationTaskRepository) {
        this.notificationTaskRepository = notificationTaskRepository;
    }


    public void saveNotificationTask(Matcher matcher, Long chatId) {
        LocalDateTime date = LocalDateTime.parse(
                matcher.group(1),
                DateTimeFormatter.ofPattern(FORMAT_DATE));
        String notificationText = matcher.group(3);
        NotificationTask notificationTask = new NotificationTask();
        notificationTask.setChatId(chatId);
        notificationTask.setTextNotification(notificationText);
        notificationTask.setDataTime(date);

        notificationTaskRepository.save(notificationTask);
    }
}
