package com.ps.cinema.server.util;

import com.ps.cinema.server.BasicResponce.BasicMessageResponse;
import com.ps.cinema.server.dto.CinemaUserDtoG;
import com.ps.cinema.server.exception.GeneralException;
import com.ps.cinema.server.mapper.EntityDtoConverter;
import com.ps.cinema.server.model.*;
import com.ps.cinema.server.repository.FilmRatingRepository;
import com.ps.cinema.server.repository.ProducerRatingRepository;
import com.ps.cinema.server.repository.UserRepository;
import com.ps.cinema.server.repository.UserScheduleRepository;
import com.ps.cinema.server.service.CinemaAppUserService;
import com.ps.cinema.server.service.ViewerService;
import com.ps.cinema.server.validator.ObjectsValidator;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.mutable.MutableObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
@Slf4j
public class UserUtil {
    @Autowired
    CinemaAppUserService cinemaAppUserService;
    @Autowired
    EntityDtoConverter converter;
    @Autowired
    ObjectsValidator<User> cinemaAppUserObjectsValidator;
    @Autowired
    ViewerService viewerService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ProducerRatingRepository producerRatingRepository;
    @Autowired
    FilmRatingRepository filmRatingRepository;
    @Autowired
    UserScheduleRepository userScheduleRepository;

    private void deleteViewerData(int viewerId){
        producerRatingRepository.deleteAllByViewerId(viewerId);
        filmRatingRepository.deleteAllByViewerId(viewerId);
        userScheduleRepository.deleteAllByViewerId(viewerId);
    }

    private void validateUser(String id, SecurityUser securityUser, String idType) {
        User cinemaAppUser;
        boolean isAdmin = userRepository.getAdminByUsername(securityUser.getUsername()).isPresent();;
        cinemaAppUser = getCinemaUser(id, idType);
        boolean isSameUser = cinemaAppUser.getUsername().equals(securityUser.getUsername());


        if(!(isSameUser || isAdmin))
            throw new GeneralException("Unauthorized user." + "isSameUser: " + isSameUser + " isAdmin: " + isAdmin);
    }

    private User getCinemaUser(String id, String idType) {
        return switch (idType) {
            case ("id") -> cinemaAppUserService.getCinemaUserById(Integer.parseInt(id));
            case ("username") -> cinemaAppUserService.getCinemaUserByUsername(id);
            case ("email") -> cinemaAppUserService.getCinemaUserByEmail(id);
            default -> throw new GeneralException("invalid id type", HttpStatus.BAD_REQUEST);

        };
    }

    Role xxx;

    //hon you only delete viewer's data not the viewer himself!
   @Transactional
   protected void updateViewerRole(Viewer user, String role, SecurityUser securityUser) {
        userRepository.getAdminByUsername(securityUser.getUsername()).orElseThrow(() -> new GeneralException("User unauthorized", HttpStatus.UNAUTHORIZED));
        deleteViewerData(user.getId());
        if (Objects.equals(role, "ROLE_PRODUCER")) {
         //   user.setRole(Role.ROLE_PRODUCER);
            userRepository.setProducerType(user.getId());
            userRepository.save(user);
        } else if (Objects.equals(role, "ROLE_ADMIN")) {
        //    user.setRole(Role.ROLE_ADMIN);
            userRepository.setAdminType(user.getId());
            userRepository.save(user);
        }
        else
            throw new GeneralException("Invalid role", HttpStatus.BAD_REQUEST);
    }

  @Transactional
    public ResponseEntity<BasicMessageResponse> updateCinemaUser(String id, Map<Object, Object> fields, String identifierType, SecurityUser securityUser) {
        try {
            //security validation.
            validateUser(id, securityUser, identifierType);
            MutableObject<User> userWrapper = new MutableObject<>(getCinemaUser(id, identifierType));
            fields.forEach((key, value) -> {
                User user = userWrapper.getValue(); // Get the user from the wrapper
                if (key.equals("role")) {
                    User viewer = userRepository.getViewerById(user.getId()).orElseThrow(()-> new GeneralException("Viewer is not in the database", HttpStatus.NOT_FOUND));
                /*    int newId =*/ updateViewerRole(converter.convertToEntity(viewer, Viewer.class), (String) value, securityUser);
                  //  userWrapper.setValue(getCinemaUser(String.valueOf(newId), "id"));

                }
                else {
                    Field field = ReflectionUtils.findRequiredField(User.class, (String) key);
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, user, value);
                }
            });

            User user = userWrapper.getValue();
            cinemaAppUserObjectsValidator.validate(user);
            userRepository.save(user);
            return new ResponseEntity<>(new BasicMessageResponse("updated user", converter.convertToDto(user, CinemaUserDtoG.class), 200), HttpStatus.OK);
        } catch (Exception e) {
            log.error("User is not updated due to a logical error(Maybe a unique constraint)" + e.getMessage());
            throw new GeneralException("Could not update user. Bad user information.", HttpStatus.BAD_REQUEST);
        }
    }
}
