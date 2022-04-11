package com.liga.shippingbot.bot;

import com.liga.shippingbot.configuration.TelegramConfig;
import com.liga.shippingbot.constants.Commands;
import com.liga.shippingbot.constants.Gender;
import com.liga.shippingbot.constants.MenuButton;
import com.liga.shippingbot.services.ChangeServiceImpl;
import com.liga.shippingbot.services.FormServiceImpl;
import com.liga.shippingbot.services.MessageServiceImpl;
import com.liga.shippingbot.state.UserState;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.starter.SpringWebhookBot;

/**
 * Класс реализует бота.
 *
 * @version 1.0
 */
@Slf4j
@Component
@Getter
@Setter

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
    private final MessageServiceImpl messageServiceImpl;
    /**
     * Объект класса обработчика инициализации анкет.
     */
    private final FormServiceImpl formServiceImpl;
    /**
     * Объект класса обработчика изменений анкет.
     */
    private final ChangeServiceImpl changeServiceImpl;
    /**
     * Мапа с полями текущего юзера.
     */

    private UserState userState;


    public BotMassage(SetWebhook setWebhook, MessageServiceImpl messageServiceImpl, ChangeServiceImpl changeServiceImpl,
                      FormServiceImpl formServiceImpl, TelegramConfig telegramConfig, UserState userState) {
        super(setWebhook);
        this.messageServiceImpl = messageServiceImpl;
        this.formServiceImpl = formServiceImpl;
        this.changeServiceImpl = changeServiceImpl;
        this.botPath = telegramConfig.getWebhookPath();
        this.botUsername = telegramConfig.getBotName();
        this.botToken = telegramConfig.getBotToken();
        this.userState = userState;
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
                BotApiMethod<?> left = messageServiceImpl.getLeft(update.getMessage(), update.getMessage().getFrom().getId());
                getPhoto(update);
                return left;
            }
            if (update.getMessage().getText().equals("Вправо")) {
                BotApiMethod<?> right = messageServiceImpl.getRight(update.getMessage(), update.getMessage().getFrom().getId());
                getPhoto(update);
                return right;
            }
            if (update.getMessage().getText().equals(MenuButton.LIKE_BUTTON.getName()) &
                    userState.getBotState().equals(BotState.SHOW_SEARCH)) {

                BotApiMethod<?> likeForm = messageServiceImpl.getLike(update.getMessage().getFrom().getId(), userState);
                if (userState.getStatus() != null
                        && !userState.getStatus().isEmpty()) {
                    writeStatus(update);
                }
                getPhoto(update);
                return likeForm;
            }
            if (update.getMessage().getText().equals(MenuButton.DISLIKE_BUTTON.getName())
                    & userState.getBotState().equals(BotState.SHOW_SEARCH)) {
                BotApiMethod<?> disLikeForm = messageServiceImpl.getDislike
                        (update.getMessage().getFrom().getId());
                getPhoto(update);
                return disLikeForm;
            }
            if (update.getMessage().getText().equals("Меню")) {
                return messageServiceImpl.getMenu(update.getMessage().getFrom().getId(), userState);
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
        String text = update.getMessage().getText();
        BotApiMethod<?> form = getUserFormAfterReload(update, text);
        if (form != null) return form;
        if (update.getMessage().getText().equals(Commands.CONTINUE.getName())) {
            form = messageServiceImpl.getContinue(update.getMessage().getFrom().getId(), userState);
            getPhoto(update);
            return form;
        } else if (update.getMessage().getText().equals(Commands.START.getName()) ||
                userState.getBotState().equals(BotState.CREATING_STATE)) {
            userState.setId(idUser);
            return formServiceImpl.startMessage(update.getMessage(), idUser, userState);
        }
        return null;
    }

    private BotApiMethod<?> getUserFormAfterReload(Update update, String text) {
        if (!text.equals(Commands.START.getName()) && !text.equals(Commands.CONTINUE.getName())
                && userState.getBotState() == null & userState.getId() == null) {
            BotApiMethod<?> form = messageServiceImpl.getCommands
                    (update.getMessage().getFrom().getId(), userState, update.getMessage());
            if (((SendMessage) form).getText().contains(Gender.MALE.getName()) || ((SendMessage) form).getText().contains(Gender.FEMALE.getName())) {
                getPhoto(update);
            }
            return form;
        }
        return null;
    }

    /**
     * Метод реализует работу основного меню.
     *
     * @param update сообщение любого типа.
     */
    private BotApiMethod<?> getChoose(Update update) {
        BotState botState = userState.getBotState();
        if (botState != null & (update.getMessage().getText()).equals(MenuButton.CHANGE_BUTTON.getName())) {
            userState.setBotState(BotState.CHANGES);
            return changeServiceImpl.getChangeFormFirstStage(update.getMessage().getFrom().getId(), userState);
        }
        if (botState != null & (update.getMessage().getText()).equals(MenuButton.FAVORITE_BUTTON.getName())) {
            BotApiMethod<?> favorite =
                    messageServiceImpl.getFavorite(update.getMessage(), update.getMessage().getFrom().getId(), userState);
            userState.setBotState(BotState.SHOW_FAVORITE);
            getPhoto(update);
            return favorite;
        }
        if (botState != null & (update.getMessage().getText()).equals(MenuButton.SEARCH_BUTTON.getName())) {
            BotApiMethod<?> search = messageServiceImpl.getSearch(update.getMessage().getFrom().getId(), userState);
            userState.setBotState(BotState.SHOW_SEARCH);
            getPhoto(update);
            return search;
        }
        if (botState != null & (update.getMessage().getText()).equals(MenuButton.FORM_BUTTON.getName())) {
            userState.setBotState(BotState.SHOW_MAIN_MENU);
            BotApiMethod<?> form = messageServiceImpl.getForm(update.getMessage().getFrom().getId(), userState);
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
        BotState botState = userState.getBotState();
        if (botState.equals(BotState.CHANGES) & update.getMessage().getText().equals(MenuButton.CHANGE_GENDER_BUTTON.getName())) {
            userState.setBotState(BotState.CHANGE_GENDER);
            return changeServiceImpl.getChangeGenderSecondStage(update.getMessage().getFrom().getId(), userState);
        }
        if (botState.equals(BotState.CHANGES) & update.getMessage().getText().equals(MenuButton.CHANGE_NAME_BUTTON.getName())) {
            userState.setBotState(BotState.CHANGE_NAME);
            return changeServiceImpl.getChangeNameSecondStage(update.getMessage().getFrom().getId(), userState);
        }
        if (botState.equals(BotState.CHANGES) & update.getMessage().getText().equals(MenuButton.CHANGE_DESCRIPTION_BUTTON.getName())) {
            userState.setBotState(BotState.CHANGE_DESCRIPTION);
            return changeServiceImpl.getChangeDescriptionSecondStage(update.getMessage().getFrom().getId(), userState);
        }
        if (botState.equals(BotState.CHANGES) & update.getMessage().getText().equals(MenuButton.CHANGE_PREFERENCE_BUTTON.getName())) {
            userState.setBotState(BotState.CHANGE_PREFERENCE);
            return changeServiceImpl.getChangePreferenceSecondStage(update.getMessage().getFrom().getId(), userState);
        }
        return null;
    }

    /**
     * Метод реализует внесение изменений в анкету.
     *
     * @param update сообщение любого типа.
     */
    private BotApiMethod<?> addChanges(Update update) {
        BotState botState = userState.getBotState();
        if (botState.equals(BotState.CHANGE_GENDER)) {
            userState.setBotState(BotState.SHOW_MAIN_MENU);
            return changeServiceImpl.addChangeGender(update.getMessage(), update.getMessage().getFrom().getId(), userState);
        }
        if (botState.equals(BotState.CHANGE_NAME)) {
            userState.setBotState(BotState.SHOW_MAIN_MENU);
            return changeServiceImpl.addChangeName(update.getMessage(), update.getMessage().getFrom().getId(), userState);
        }
        if (botState.equals(BotState.CHANGE_DESCRIPTION)) {
            userState.setBotState(BotState.SHOW_MAIN_MENU);
            return changeServiceImpl.addChangeDescription(update.getMessage(), update.getMessage().getFrom().getId(), userState);
        }
        if (botState.equals(BotState.CHANGE_PREFERENCE)) {
            userState.setBotState(BotState.SHOW_MAIN_MENU);
            return changeServiceImpl.addChangePreference(update.getMessage(), update.getMessage().getFrom().getId(), userState);
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
            execute(messageServiceImpl.sendPhoto(update.getMessage().getFrom().getId()));
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
            execute(messageServiceImpl.sendStatus(update.getMessage().getFrom().getId(), userState));
        } catch (TelegramApiException e) {
            throw new RuntimeException("Ошибка: невозможно получить статус.");
        }
    }
}