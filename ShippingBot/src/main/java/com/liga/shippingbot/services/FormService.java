package com.liga.shippingbot.services;

import com.liga.shippingbot.state.UserState;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Класс реализующий этот интерфейс будет производить инициализацию анкеты.
 */
public interface FormService {

    /**
     * Метод предоставляет пользователю кнопки на выбор для выбора поля gender.
     *
     * @param longId -идентификатор пользователя.
     */
    BotApiMethod<?> startMessage(Message message, Long longId, UserState userState);

    /**
     * Метод предоставляет пользователю возможность внести изменение поля name.
     *
     * @param message -идентификатор пользователя, сообщение от пользователя.
     */
    // BotApiMethod<?> getUserGender(Message message, PersonRequest personRequest);

    /**
     * Метод предоставляет пользователю возможность внести изменение поля description.
     *
     * @param message -идентификатор пользователя, сообщение от пользователя.
     */
    //  BotApiMethod<?> getUserName(Message message, PersonRequest personRequest);

    /**
     * Метод предоставляет пользователю кнопки на выбор для выбора поля preference.
     *
     * @param message -идентификатор пользователя, сообщение от пользователя.
     */
    //  public BotApiMethod<?> getUserDescription(Message message, PersonRequest personRequest);

    /**
     * Метод производит инициализацию пользователя и вносит его в базу данных.
     *
     * @param message -идентификатор пользователя, сообщение от пользователя.
     */
    //  public BotApiMethod<?> getUserPreference(Message message, PersonRequest personRequest, UserState userState);
}


