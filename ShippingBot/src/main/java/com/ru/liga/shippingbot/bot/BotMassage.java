package com.ru.liga.shippingbot.bot;

import com.ru.liga.shippingbot.configuration.TelegramConfig;
import com.ru.liga.shippingbot.handlers.MessageHandler;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;

@Slf4j
@Component
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BotMassage extends SpringWebhookBot {
    String botPath;
    String botUsername;
    String botToken;

    MessageHandler messageHandler;

    public BotMassage(SetWebhook setWebhook, MessageHandler messageHandler,
                      TelegramConfig telegramConfig) {
        super(setWebhook);
        this.messageHandler = messageHandler;

        this.setBotPath(telegramConfig.getWebhookPath());
        this.setBotUsername(telegramConfig.getBotName());
        this.setBotToken(telegramConfig.getBotToken());
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            return handleUpdate(update);
        } catch (IllegalArgumentException exception) {
            log.debug("log message: {}", exception.getMessage());
            return new SendMessage(update.getMessage().getChatId().toString(), "Ошибка ввода.");


        } catch (Exception exception) {
            exception.printStackTrace();
            log.debug("log message: {}", exception.getMessage());
            return new SendMessage(update.getMessage().getChatId().toString(),
                    "Ошибка ввода.");
        }
    }

    private BotApiMethod<?> handleUpdate(Update update) {
        BotApiMethod<?> form = createForm(update);
        if (form != null) {
            return form;
        } else {
            BotApiMethod<?> formAfterChoose = getChoose(update);
            BotApiMethod<?> prepareToChange = getChangeInForm(update);

            if (formAfterChoose != null) {
                return formAfterChoose;
            }
            if (prepareToChange != null) {
                return prepareToChange;
            }
            if (update.getMessage().getText().equals("Влево")) {
                BotApiMethod<?> left = messageHandler.getLeft(update.getMessage(), update.getMessage().getFrom().getId());
                getPhoto(update);
                return left;
            }
            if (update.getMessage().getText().equals("Вправо")) {
                BotApiMethod<?> right = messageHandler.getRight(update.getMessage(), update.getMessage().getFrom().getId());
                getPhoto(update);
                return right;
            }
            if (update.getMessage().getText().equals("Меню")) {
                return messageHandler.getMenu(update.getMessage().getFrom().getId());
            }
            return addChanges(update);
        }

    }

    private BotApiMethod<?> addChanges(Update update) {
        BotState botState = messageHandler.getMap().get(update.getMessage().getFrom().getId()).getBotState();
        if (botState.equals(BotState.CHANGE_GENDER)) {
            BotApiMethod<?> formWithChanges = messageHandler.addChangeGender(update.getMessage(), update.getMessage().getFrom().getId());
            getPhoto(update);
            return formWithChanges;
        }
        if (botState.equals(BotState.CHANGE_NAME)) {
            BotApiMethod<?> formWithChanges = messageHandler.addChangeName(update.getMessage(), update.getMessage().getFrom().getId());
            getPhoto(update);
            return formWithChanges;
        }
        if (botState.equals(BotState.CHANGE_DESCRIPTION)) {
            BotApiMethod<?> formWithChanges = messageHandler.addChangeDescription(update.getMessage(), update.getMessage().getFrom().getId());
            getPhoto(update);
            return formWithChanges;
        }
        if (botState.equals(BotState.CHANGE_PREFERENCE)) {
            BotApiMethod<?> formWithChanges = messageHandler.addChangePreference(update.getMessage(), update.getMessage().getFrom().getId());
            getPhoto(update);
            return formWithChanges;
        }
        return null;
    }

    private BotApiMethod<?> getChangeInForm(Update update) {
        BotState botState = messageHandler.getMap().get(update.getMessage().getFrom().getId()).getBotState();
        if (botState.equals(BotState.SHOW_CHANGES) & update.getMessage().getText().equals("Пол")) {
            BotApiMethod<?> chooseToChanges = messageHandler.getChangeGenderSecondStage(update.getMessage().getFrom().getId());
            getPhoto(update);
            return chooseToChanges;
        }
        if (botState.equals(BotState.SHOW_CHANGES) & update.getMessage().getText().equals("Имя")) {
            BotApiMethod<?> chooseToChanges = messageHandler.getChangeNameSecondStage(update.getMessage().getFrom().getId());
            getPhoto(update);
            return chooseToChanges;
        }
        if (botState.equals(BotState.SHOW_CHANGES) & update.getMessage().getText().equals("Описание")) {
            BotApiMethod<?> chooseToChanges = messageHandler.getChangeDescriptionSecondStage(update.getMessage().getFrom().getId());
            getPhoto(update);
            return chooseToChanges;
        }
        if (botState.equals(BotState.SHOW_CHANGES) & update.getMessage().getText().equals("Приоритет поиска")) {
            BotApiMethod<?> chooseToChanges = messageHandler.getChangePreferenceSecondStage(update.getMessage().getFrom().getId());
            getPhoto(update);
            return chooseToChanges;
        }
        return null;
    }

    public BotApiMethod<?> createForm(Update update) {
        if (update.getMessage() == null) {
            return new SendMessage();
        }
        Long idUser = update.getMessage().getFrom().getId();
        if (update.getMessage().getText().equals("/continue")) {
            BotApiMethod<?> form = messageHandler.getContinue(update.getMessage().getFrom().getId());
            getPhoto(update);
            return form;
        }
        if (update.getMessage().getText().equals("/start")) {
            return messageHandler.startMessage(update.getMessage().getFrom().getId());
        }
        if (messageHandler.getMap().isEmpty()) {
            BotApiMethod<?> form = messageHandler.getForm(update.getMessage().getFrom().getId());
            getPhoto(update);
            return form;
        }
        if (messageHandler.getMap().get(idUser).getGender() == null) {
            return messageHandler.getUserGender(update.getMessage(), update.getMessage().getFrom().getId());
        }
        if (messageHandler.getMap().get(idUser).getName() == null) {
            return messageHandler.getUserName(update.getMessage(), update.getMessage().getFrom().getId());
        }
        if (messageHandler.getMap().get(idUser).getDescription() == null) {
            return messageHandler.getUserDescription(update.getMessage(), update.getMessage().getFrom().getId());
        }
        if (messageHandler.getMap().get(idUser).getPreference() == null) {
            return messageHandler.getUserPreference(update.getMessage(), update.getMessage().getFrom().getId());
        }

        return null;
    }

    public BotApiMethod<?> getChoose(Update update) {
        BotState botState = messageHandler.getMap().get(update.getMessage().getFrom().getId()).getBotState();
        if (botState != null & (update.getMessage().getText()).equals("Изменить анкету")) {
            BotApiMethod<?> favorite = messageHandler.getChangeFormFirstStage(update.getMessage().getFrom().getId());
            getPhoto(update);
            return favorite;
        }
        if (botState != null & (update.getMessage().getText()).equals("Любимцы")) {
            BotApiMethod<?> favorite = messageHandler.getFavorite(update.getMessage(), update.getMessage().getFrom().getId());
            getPhoto(update);
            return favorite;
        }
        if (botState != null & (update.getMessage().getText()).equals("Поиск")) {
            BotApiMethod<?> search = messageHandler.getSearch(update.getMessage().getFrom().getId());
            getPhoto(update);
            return search;
        }
        if (botState != null & (update.getMessage().getText()).equals("Анкета")) {
            BotApiMethod<?> form = messageHandler.getForm(update.getMessage().getFrom().getId());
            getPhoto(update);
            return form;
        }
        return null;
    }

    private void getPhoto(Update update) {
        try {
            execute(messageHandler.sendPhoto(update.getMessage().getFrom().getId()));
        } catch (TelegramApiException e) {
            throw new RuntimeException("Ошибка: невозможно отправить файл.");
        }
    }

}
