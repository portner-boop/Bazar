package aiagents.bazar.api.bot;

import aiagents.bazar.api.service.TelegramUserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

@Slf4j
@Component
@RequiredArgsConstructor
public class MyBot implements LongPollingSingleThreadUpdateConsumer {

    private final OkHttpTelegramClient client;
    private final TelegramUserService telegramUserService;

    @SneakyThrows
    @Override
    @Async
    public void consume(Update update) {
        try {
            if (update.hasMessage() && update.getMessage().hasText()) {
                var message = update.getMessage();
                String chatId = message.getChatId().toString();
                String text = message.getText();
                SendMessage sendMessage;
                switch (text) {
                    case "/register" -> {
                        User user = message.getFrom();
                        if (telegramUserService.createUserIfNotExist(user)) {
                            sendMessage = SendMessage.builder()
                                    .chatId(chatId)
                                    .text("Вы успешно зарегистрировались")
                                    .build();
                        } else {
                            sendMessage = SendMessage.builder()
                                    .chatId(chatId)
                                    .text("Вы уже зарегистрированы")
                                    .build();
                        }
                    }
                    default -> sendMessage = SendMessage.builder()
                            .chatId(chatId)
                            .text("Извините, команда не распознана")
                            .build();
                }
                client.execute(sendMessage);
            }
            else if (update.hasChatMember() && update.getChatMember() != null) {
                var chatMemberUpdate = update.getChatMember();
                if (chatMemberUpdate.getChat() != null) {
                    String chatId = chatMemberUpdate.getChat().getId().toString();
                    log.info("Обновление участников чата: {}", chatId);
                }
            }

        } catch (Exception e) {
            log.error("Ошибка при обработке апдейта", e);
        }
    }
}
