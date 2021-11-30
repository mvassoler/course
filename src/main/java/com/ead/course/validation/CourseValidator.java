package com.ead.course.validation;

import com.ead.course.dtos.CourseDto;
import com.ead.course.enums.UserType;
import com.ead.course.models.UserModel;
import com.ead.course.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;
import java.util.UUID;

@Component
public class CourseValidator implements Validator {

    @Autowired
    //Anotação qualifier evita problema de conflito com a controle ja existente para o Validator
    @Qualifier("defaultValidator")
    private Validator validator;

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        CourseDto courseDto = (CourseDto) target;
        //Abaixo ocorre a mesma validação da anotação @Valid na assinatura dos recursos do controller
        validator.validate(courseDto, errors);
        if(!errors.hasErrors()){
            this.validateUserInstructor(courseDto.getUserInstructor(), errors);
        }
    }

    private void validateUserInstructor(UUID userInstructor, Errors errors){
        Optional<UserModel> userModelOptional = this.userService.findById(userInstructor);
        if(!userModelOptional.isPresent()){
            errors.rejectValue("userInstructor", "UserInstructorError", "Instructor Not Found." );
        }
        if(userModelOptional.get().getUserType().equals(UserType.STUDENT)){
            errors.rejectValue("userInstructor", "UserInstructorError", "User must be INSTRUCTOR or ADMIN." );
        }
    }
}
