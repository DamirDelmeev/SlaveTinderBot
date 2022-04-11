package com.liga.shippingbot.services;

import com.liga.shippingbot.state.UserState;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

/**
 * Класс реализующий этот интерфейс будет производить обработку изменений в анкету.
 */
public interface ChangeService {


    /**
     * Метод предоставляет пользователю кнопки на выбор для изменений.
     *
     * @param longId -идентификатор пользователя.
     */
    BotApiMethod<?> getChangeFormFirstStage(Long longId, UserState userState);

    /**
     * Метод предоставляет пользователю кнопки на выбор для изменений поля:gender.
     *
     * @param longId -идентификатор пользователя.
     */
    BotApiMethod<?> getChangeGenderSecondStage(Long longId, UserState userState);

    /**
     * Метод предоставляет пользователю возможность внести изменения в поле:name.
     *
     * @param longId -идентификатор пользователя.
     */
    BotApiMethod<?> getChangeNameSecondStage(Long longId, UserState userState);

    /**
     * Метод предоставляет пользователю возможность внести изменения в поле:description.
     *
     * @param longId -идентификатор пользователя.
     */
    BotApiMethod<?> getChangeDescriptionSecondStage(Long longId, UserState userState);

    /**
     * Метод предоставляет пользователю кнопки на выбор для изменений поля:preference.
     *
     * @param longId -идентификатор пользователя.
     */
    BotApiMethod<?> getChangePreferenceSecondStage(Long longId, UserState userState);

    /**
     * Метод вносит изменения в поле:gender.
     *
     * @param longId,message -идентификатор пользователя, сообщение от пользователя.
     */
    BotApiMethod<?> addChangeGender(Message message, Long longId, UserState userState);

    /**
     * Метод вносит изменения в поле:name.
     *
     * @param longId,message -идентификатор пользователя, сообщение от пользователя.
     */
    BotApiMethod<?> addChangeName(Message message, Long longId, UserState userState);

    /**
     * Метод вносит изменения в поле:description.
     *
     * @param longId,message -идентификатор пользователя, сообщение от пользователя.
     */
    BotApiMethod<?> addChangeDescription(Message message, Long longId, UserState userState);

    /**
     * Метод вносит изменения в поле:preference.
     *
     * @param longId,message -идентификатор пользователя, сообщение от пользователя.
     */
    BotApiMethod<?> addChangePreference(Message message, Long longId, UserState userState);
}


