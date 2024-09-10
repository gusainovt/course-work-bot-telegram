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
import pro.sky.telegrambot.service.NotificationTaskService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static pro.sky.telegrambot.service.constants.Constants.REGEX_NOTIFICATION_TEXT;
import static pro.sky.telegrambot.service.constants.Constants.START_COMMAND;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private NotificationTaskService notificationTaskService;

    @Autowired
    private TelegramBot telegramBot;

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
            Pattern pattern = Pattern.compile(REGEX_NOTIFICATION_TEXT);
            Matcher matcher = pattern.matcher(messageText);
            try {
                if (matcher.matches()) {
                    notificationTaskService.saveNotificationTask(matcher, chatId);
                }
                if (messageText.equals(START_COMMAND)) {
                    SendMessage message = new SendMessage(chatId, "Hello!");
                    SendResponse response = telegramBot.execute(message);
                }
            } catch (Exception e) {
                logger.error("Error has occurred" + e);
            }
        });
        return UpdatesListener.CONFIRMED_UPDATES_ALL;
    }


}




