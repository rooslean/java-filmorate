package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.AlreadyFriendsException;
import ru.yandex.practicum.filmorate.exception.FilmBadReleaseDateException;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.FriendshipAcceptionException;
import ru.yandex.practicum.filmorate.exception.FriendshipRequestAlreadyExist;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.ErrorResponse;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ExceptionHandler({FilmNotFoundException.class, UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleObjectNotFound(final RuntimeException e) {
        log.info(e.getMessage());
        return new ErrorResponse("Объект не найден", e.getMessage());
    }

    @ExceptionHandler({AlreadyFriendsException.class, FriendshipAcceptionException.class,
            FriendshipRequestAlreadyExist.class, DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFriendshipError(final RuntimeException e) {
        log.info(e.getMessage());
        return new ErrorResponse("Ошибка добавления в друзья", e.getMessage());
    }

    @ExceptionHandler({FilmBadReleaseDateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleFilmBadReleaseDateError(final RuntimeException e) {
        log.warn(e.getMessage());
        return new ErrorResponse("Неверная дата", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationError(final MethodArgumentNotValidException e) {
        log.warn(e.getMessage());
        return new ErrorResponse("Ошибка валидации", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleException(final Exception e) {
        return new ErrorResponse("Ошибка сервера", e.getMessage());
    }
}
