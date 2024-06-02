package pro.sky.telegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pro.sky.telegrambot.model.NotificationTask;
import pro.sky.telegrambot.repository.NotificationTaskRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {

    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private NotificationTaskRepository notificationTaskRepository;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            logger.info("Processing update: {}", update);
            String messageText = update.message().text();
            Long chatId = update.message().chat().id();
            Pattern pattern = Pattern.compile("([0-9\\.\\:\\s]{16})(\\s)([\\W+]+)");
            Matcher matcher = pattern.matcher(messageText);

            try {
                if(matcher.matches()){
                    LocalDateTime date = LocalDateTime.parse(
                            matcher.group(1),
                            DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"));
                    String notificationText = matcher.group(3);
                    NotificationTask notificationTask = new NotificationTask();
                    notificationTask.setChatId(chatId);
                    notificationTask.setTextNotification(notificationText);
                    notificationTask.setDataTime(date);
                    notificationTaskRepository.save(notificationTask);
                }
                if (messageText.equals("/start")) {
                    SendMessage message = new SendMessage(chatId,"Hello!");
                    SendResponse response = telegramBot.execute(message);
                }

            } catch (Exception e) {
                logger.error("Error has occurred" + e);
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }

}
