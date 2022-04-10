package com.liga.shippingbot.services;

import com.liga.shippingbot.state.UserState;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Класс реализующий этот интерфейс будет производить основную часть обращений в базу данных.
 */
public interface MessageService {

    /**
     * Метод реализует работу бота при нажатии кнопки Поиск.
     *
     * @param longId -идентификатор пользователя.
     */
    BotApiMethod<?> getSearch(Long longId, UserState userState);

    /**
     * Метод реализует работу бота при нажатии кнопки Любимцы.
     *
     * @param longId,message -идентификатор пользователя, сообщение от пользователя.
     */

    BotApiMethod<?> getFavorite(Message message, Long longId, UserState userState);

    /**
     * Метод реализует работу бота при нажатии кнопки Анкета.
     *
     * @param longId -идентификатор пользователя.
     */
    BotApiMethod<?> getForm(Long longId, UserState userState);

    /**
     * Метод реализует работу бота при нажатии кнопки Влево.
     *
     * @param longId,message -идентификатор пользователя, сообщение от пользователя.
     */
    BotApiMethod<?> getLeft(Message message, Long longId);

    /**
     * Метод реализует работу бота при нажатии кнопки dislike.
     *
     * @param longId -идентификатор пользователя, сообщение от пользователя.
     */

    BotApiMethod<?> getDislike(Long longId);

    /**
     * Метод реализует работу бота при нажатии кнопки Вправо.
     *
     * @param longId,message -идентификатор пользователя, сообщение от пользователя.
     */
    BotApiMethod<?> getRight(Message message, Long longId);

    /**
     * Метод реализует работу бота при нажатии кнопки like.
     *
     * @param longId,message -идентификатор пользователя, сообщение от пользователя.
     */
    BotApiMethod<?> getLike(Long longId, UserState userState);

    /**
     * Метод реализует работу бота при нажатии кнопки Меню.
     *
     * @param longId -идентификатор пользователя.
     */
    BotApiMethod<?> getMenu(Long longId, UserState userState);

    /**
     * Метод реализует работу бота при вводе команды "/continue".
     *
     * @param longId -идентификатор пользователя.
     */

    BotApiMethod<?> getContinue(Long longId, UserState userState);

    /**
     * Метод реализует отправление картинки по запросу.
     *
     * @param longId -идентификатор пользователя.
     */

    SendPhoto sendPhoto(Long longId);

    /**
     * Метод реализует получение статуса пользователя по запросу.
     *
     * @param longId -идентификатор пользователя.
     */
    SendMessage sendStatus(Long longId, UserState userState);
}

