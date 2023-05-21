package com.example.core.facade;

import com.example.core.exceptions.CoreException;
import com.example.core.exceptions.ErrorCode;
import com.example.core.model.entity.AccountDeactivation;
import com.example.core.model.entity.User;
import com.example.core.model.request.UserRegistrationRequest;
import com.example.core.service.AccountDeactivationService;
import com.example.core.service.AuthService;
import com.example.core.service.MinioService;
import com.example.core.service.UserService;
import com.example.core.util.ImageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;
    private final MinioService minioService;

    private final AuthService authService;
    private final AccountDeactivationService deactivationService;

    public void createUser(UserRegistrationRequest request) {
        User user = new User(request);
        userService.save(user);
    }

    public void updateUserImage(String base64Image){
        User loggedInUser = userService.findLoggedInUser();

        String imageLink = minioService.uploadAndGetPath(base64Image);
        loggedInUser.setPictureUrl(imageLink);
        userService.save(loggedInUser);
    }
    @Transactional
    public void deactivateAccount(){
        User loggedInUser = userService.findLoggedInUser();
        Optional<AccountDeactivation> deactivationOptional = deactivationService.findByUserId(loggedInUser.getId());
        deactivationOptional.ifPresent(x->{throw new CoreException(ErrorCode.ILLEGAL_ARGUMENT_PROVIDED,"User is already deactivated!");
        });


        deactivationService.save(new AccountDeactivation(loggedInUser));
        authService.changeStatus(loggedInUser.getKeyCloakId(), false);
    }

    @Transactional
    public void activateAccount() {
        User loggedInUser = userService.findLoggedInUser();
        AccountDeactivation deactivation = deactivationService.findByUserId(loggedInUser.getId())
                .orElseThrow(()->{throw new CoreException(ErrorCode.ILLEGAL_ARGUMENT_PROVIDED, "User is already deactivated!");});
        if (deactivation.getActionTaken().plusDays(30).isBefore(LocalDate.now())){
            throw new CoreException(ErrorCode.CONFLICT,"30 day period for account activation has been passed");
        }
        authService.setEmailVerifyAction(loggedInUser.getKeyCloakId());
        authService.changeStatus(loggedInUser.getKeyCloakId(), true);
    }

}
