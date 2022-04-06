package com.ru.liga.shippingbot.bot;

import com.ru.liga.shippingbot.configuration.TelegramConfig;
import com.ru.liga.shippingbot.entity.Person;
import com.ru.liga.shippingbot.handlers.ChangeHandler;
import com.ru.liga.shippingbot.handlers.FormHandler;
import com.ru.liga.shippingbot.handlers.MessageHandler;
import com.vdurmont.emoji.EmojiParser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;

import java.util.Map;

/**
 * Класс реализует бота.
 *
 * @version 1.0
 */
@Slf4j
@Component
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BotMassage extends SpringWebhookBot {
    /**
     * ("${telegram.webhook-path}")
     */
    private final String botPath;
    /**
     * ("${telegram.bot-name}")
     */
    private final String botUsername;
    /**
     * ("${telegram.bot-token}")
     */
    private final String botToken;
    /**
     * Объект класса обработчика основного меню.
     */
    MessageHandler messageHandler;
    /**
     * Объект класса обработчика инициализации анкет.
     */
    FormHandler formHandler;
    /**
     * Объект класса обработчика изменений анкет.
     */
    ChangeHandler changeHandler;
    /**
     * Мапа с полями текущего юзера.
     */
    Map<Long, Person> map;

    @Autowired
    public void setMap(Map<Long, Person> map) {
        this.map = map;
    }

    public BotMassage(SetWebhook setWebhook, MessageHandler messageHandler, ChangeHandler changeHandler,
                      FormHandler formHandler, TelegramConfig telegramConfig) {
        super(setWebhook);
        this.messageHandler = messageHandler;
        this.formHandler = formHandler;
        this.changeHandler = changeHandler;
        this.botPath = telegramConfig.getWebhookPath();
        this.botUsername = telegramConfig.getBotName();
        this.botToken = telegramConfig.getBotToken();
    }

    /**
     * Метод реализует работу бота и взаимодействие с пользователем.
     *
     * @param update сообщение любого типа.
     */
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

    /**
     * Метод реализует основную обработку сообщений от пользователя.
     *
     * @param update сообщение любого типа.
     */
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
            String like = EmojiParser.parseToUnicode(":hearts:");
            if (update.getMessage().getText().equals(like) & map.get(update.getMessage().getFrom().getId()).getBotState().equals(BotState.SHOW_SEARCH)) {
                BotApiMethod<?> likeForm = messageHandler.getLike(update.getMessage(),
                        update.getMessage().getFrom().getId());
                if (map.get(update.getMessage().getFrom().getId()).getStatus() != null
                        && !map.get(update.getMessage().getFrom().getId()).getStatus().isEmpty()) {
                    writeStatus(update);
                }
                getPhoto(update);
                return likeForm;
            }
            String disLike = EmojiParser.parseToUnicode(":heavy_multiplication_x:");
            if (update.getMessage().getText().equals(disLike)
                    & map.get(update.getMessage().getFrom().getId()).getBotState().equals(BotState.SHOW_SEARCH)) {
                BotApiMethod<?> disLikeForm = messageHandler.getDislike
                        (update.getMessage(), update.getMessage().getFrom().getId());
                getPhoto(update);
                return disLikeForm;
            }
            if (update.getMessage().getText().equals("Меню")) {
                return messageHandler.getMenu(update.getMessage().getFrom().getId());
            }
            return addChanges(update);
        }
    }

    /**
     * Метод реализует инициализацию пользователя.
     * Если мапа с пользователем, пуста производит запрос в бд.
     *
     * @param update сообщение любого типа.
     */
    private BotApiMethod<?> createForm(Update update) {
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
            return formHandler.startMessage(update.getMessage().getFrom().getId());
        }
        if (map.isEmpty()) {
            BotApiMethod<?> form = messageHandler.getForm(update.getMessage().getFrom().getId());
            getPhoto(update);
            return form;
        }
        if (map.get(idUser).getGender() == null) {
            return formHandler.getUserGender(update.getMessage(), update.getMessage().getFrom().getId());
        }
        if (map.get(idUser).getName() == null) {
            return formHandler.getUserName(update.getMessage(), update.getMessage().getFrom().getId());
        }
        if (map.get(idUser).getDescription() == null) {
            return formHandler.getUserDescription(update.getMessage(), update.getMessage().getFrom().getId());
        }
        if (map.get(idUser).getPreference() == null) {
            return formHandler.getUserPreference(update.getMessage(), update.getMessage().getFrom().getId());
        }
        return null;
    }

    /**
     * Метод реализует работу основного меню.
     *
     * @param update сообщение любого типа.
     */
    private BotApiMethod<?> getChoose(Update update) {
        BotState botState = messageHandler.getMap().get(update.getMessage().getFrom().getId()).getBotState();
        if (botState != null & (update.getMessage().getText()).equals("Изменить анкету")) {
            return changeHandler.getChangeFormFirstStage(update.getMessage().getFrom().getId());
        }
        if (botState != null & (update.getMessage().getText()).equals("Любимцы")) {
            BotApiMethod<?> favorite =
                    messageHandler.getFavorite(update.getMessage(), update.getMessage().getFrom().getId());
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

    /**
     * Метод реализует работу меню изменений в анкету.
     *
     * @param update сообщение любого типа.
     */
    private BotApiMethod<?> getChangeInForm(Update update) {
        BotState botState = messageHandler.getMap().get(update.getMessage().getFrom().getId()).getBotState();
        if (botState.equals(BotState.SHOW_CHANGES) & update.getMessage().getText().equals("Пол")) {
            return changeHandler.getChangeGenderSecondStage(update.getMessage().getFrom().getId());
        }
        if (botState.equals(BotState.SHOW_CHANGES) & update.getMessage().getText().equals("Имя")) {
            return changeHandler.getChangeNameSecondStage(update.getMessage().getFrom().getId());
        }
        if (botState.equals(BotState.SHOW_CHANGES) & update.getMessage().getText().equals("Описание")) {
            return changeHandler.getChangeDescriptionSecondStage(update.getMessage().getFrom().getId());
        }
        if (botState.equals(BotState.SHOW_CHANGES) & update.getMessage().getText().equals("Приоритет поиска")) {
            return changeHandler.getChangePreferenceSecondStage(update.getMessage().getFrom().getId());
        }
        return null;
    }

    /**
     * Метод реализует внесение изменений в анкету.
     *
     * @param update сообщение любого типа.
     */
    private BotApiMethod<?> addChanges(Update update) {
        BotState botState = messageHandler.getMap().get(update.getMessage().getFrom().getId()).getBotState();
        if (botState.equals(BotState.CHANGE_GENDER)) {
            return changeHandler.addChangeGender(update.getMessage(), update.getMessage().getFrom().getId());
        }
        if (botState.equals(BotState.CHANGE_NAME)) {
            return changeHandler.addChangeName(update.getMessage(), update.getMessage().getFrom().getId());
        }
        if (botState.equals(BotState.CHANGE_DESCRIPTION)) {
            return changeHandler.addChangeDescription(update.getMessage(), update.getMessage().getFrom().getId());
        }
        if (botState.equals(BotState.CHANGE_PREFERENCE)) {
            return changeHandler.addChangePreference(update.getMessage(), update.getMessage().getFrom().getId());
        }
        return null;
    }

    /**
     * Метод реализует отправление картинки по запросу.
     *
     * @param update сообщение любого типа.
     */
    private void getPhoto(Update update) {
        try {
            execute(messageHandler.sendPhoto(update.getMessage().getFrom().getId()));
        } catch (TelegramApiException e) {
            throw new RuntimeException("Ошибка: невозможно отправить файл.");
        }
    }

    /**
     * Метод реализует отправление статуса пользователей.
     *
     * @param update сообщение любого типа.
     */
    private void writeStatus(Update update) {
        try {
            execute(messageHandler.sendStatus(update.getMessage().getFrom().getId()));
        } catch (TelegramApiException e) {
            throw new RuntimeException("Ошибка: невозможно получить статус.");
        }
    }
}