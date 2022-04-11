package com.example.shippingbotserver.service;

import com.example.shippingbotserver.dto.UserDto;
import com.example.shippingbotserver.entity.Attitude;
import com.example.shippingbotserver.entity.Counter;
import com.example.shippingbotserver.entity.User;
import com.example.shippingbotserver.exceptions.EmptyListUsersException;
import com.example.shippingbotserver.exceptions.UserNotFoundException;
import com.example.shippingbotserver.repository.AttitudeRepository;
import com.example.shippingbotserver.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final AttitudeRepository attitudeRepository;
    private final TranslateService translateService;
    private final DrawerService drawerService;
    private final DtoService dtoService;

    @SneakyThrows
    public UserDto findUser(Long id) throws IOException {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        if (user.getName() != null && user.getPreference() != null && user.getDescription() != null) {
            translateService.translate(user);
            return dtoService.convert(drawerService.draw(user), user, "");
        }
        return dtoService.convert(null, user, "");
    }

    public UserDto search(Long id) throws IOException {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        try {
            User res = searchNext(user);
            translateService.translate(searchNext(user));
            return dtoService.convert(drawerService.draw(res), res, statusInit(user, res));
        } catch (EmptyListUsersException e) {
            return dtoService.convertEmpty(drawerService.draw(null));
        }
    }

    public UserDto getFavorite(Long id, String action) throws IOException {
        User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        try {
            User res = getUserInFavorites(user, action.equals("Вправо"));
            translateService.translate(user);
            return dtoService.convert(drawerService.draw(res), res, statusInit(user, res));
        } catch (EmptyListUsersException e) {
            return dtoService.convertEmpty(drawerService.draw(null));
        }
    }

    public UserDto attitudePush(User id, String attitude) throws IOException {
        try {
            User user = userRepository.findById(id.getId()).orElseThrow(UserNotFoundException::new);
            User userSearch = pushAndGetNext(user, attitude);
            translateService.translate(userSearch);
            return dtoService.convert(drawerService.draw(userSearch), userSearch, statusInit(user, userSearch));
        } catch (EmptyListUsersException e) {
            return dtoService.convertEmpty(drawerService.draw(null));
        }
    }

    private User pushAndGetNext(User user, String attitude) throws EmptyListUsersException {
        User userSearch = searchNext(user);
        attitudeRepository.save(new Attitude(user, userSearch, attitude));
        return searchNext(user);
    }


    public void saveLover(User user) {
        translateGenderAndPreference(user);
        userRepository.save(user);
    }

    public void deleteUser(Long id) {
        try {
            User user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
            deleteAttitudes(user);
        } catch (UserNotFoundException ignore) {
        }
    }

    private void deleteAttitudes(User user) {
        attitudeRepository.deleteAll(
                Stream.concat(
                                attitudeRepository.findAllByTargetId(user)
                                        .stream(),
                                attitudeRepository.findAllByInitId(user)
                                        .stream())
                        .collect(Collectors.toList())
        );
    }

    public void update(User user) {
        try {
            User original = userRepository.findById(user.getId()).orElseThrow(UserNotFoundException::new);
            user.setCounter(original.getCounter());
        } catch (UserNotFoundException ignore) {
        }
        translateGenderAndPreference(user);
        userRepository.save(user);
        updateAttitudes(user);
    }

    private void updateAttitudes(User user) {
        if (user.getPreference() == null || user.getGender() == null) {
            return;
        }
        List<Attitude> attitudes = Stream.concat(
                attitudeRepository.findAllByInitId(user)
                        .stream()
                        .filter(a -> !isPreference(user, a.getTargetId())),
                attitudeRepository.findAllByTargetId(user)
                        .stream()
                        .filter(a -> !isPreference(a.getInitId(), user))).collect(Collectors.toList());
        attitudeRepository.deleteAll(attitudes);
    }

    private void translateGenderAndPreference(User user) {
        if (user.getGender() != null) {
            if (user.getGender().equals("Сударь") || user.getGender().equals("boy")) {
                user.setGender("boy");
            } else {
                user.setGender("girl");
            }
        }
        if (user.getPreference() != null) {
            if (user.getPreference().equals("Сударя") || user.getPreference().equals("boy")) {
                user.setPreference("boy");
            } else if (user.getPreference().equals("Всех")) {
                user.setPreference("all");
            } else {
                user.setPreference("girl");
            }
        }
    }

    private String statusInit(User user, User showing) {
        Attitude like = attitudeRepository.findByInitIdAndTargetIdAndNameOfAction(user, showing, "like");
        Attitude userLike = attitudeRepository.findByInitIdAndTargetIdAndNameOfAction(showing, user, "like");
        if (like != null && userLike != null) {
            return "Взаимность";
        } else if (userLike == null && like != null) {
            return "Любим Вами";
        } else if (userLike != null) {
            return "Вы любимы";
        }
        return "";
    }

    private User searchNext(User user) throws EmptyListUsersException {
        List<User> userAttitudes = attitudeRepository.findAllByInitId(user)
                .stream().map(Attitude::getTargetId).collect(Collectors.toList());
        List<User> userList = userRepository.findAll();
        return userList.stream()
                .filter(u -> !u.getId().equals(user.getId()))
                .filter(u -> !userAttitudes.contains(u))
                .filter(u -> isPreference(user, u))
                .findFirst().orElseThrow(EmptyListUsersException::new);
    }


    private User getUserInFavorites(User user, boolean increment) throws EmptyListUsersException {
        List<User> allAttitudes = Stream.concat(
                        attitudeRepository.findAllByInitIdAndNameOfAction(user, "like")
                                .stream()
                                .map(Attitude::getTargetId)
                                .filter(u -> !u.getId().equals(user.getId())),
                        attitudeRepository.findAllByTargetIdAndNameOfAction(user, "like")
                                .stream()
                                .map(Attitude::getInitId)
                                .filter(u -> !u.getId().equals(user.getId())))
                .distinct()
                .collect(Collectors.toList());
        if (allAttitudes.isEmpty()) {
            throw new EmptyListUsersException();
        }
        if (user.getCounter() == null) {
            user.setCounter(new Counter(user.getId(), -1L));
        }
        if (increment) {
            user.getCounter().increment(allAttitudes.size() - 1);
        } else {
            user.getCounter().decrement(allAttitudes.size() - 1);
        }
        userRepository.save(user);
        return allAttitudes.get((int) user.getCounter().getCounter());
    }

    private boolean isPreference(User main, User candidate) {
        return (main.getPreference().equals("all") || main.getPreference().equals(candidate.getGender())) &&
                (candidate.getPreference().equals("all") || candidate.getPreference().equals(main.getGender()));
    }

}
