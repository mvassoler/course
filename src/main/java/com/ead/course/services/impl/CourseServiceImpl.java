package com.ead.course.services.impl;

import com.ead.course.dtos.NotificationCommandDto;
import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.models.UserModel;
import com.ead.course.publishers.NotificationCommandPublisher;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.repositories.UserRepository;
import com.ead.course.services.CourseService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private UserRepository courseUserRepository;

    @Autowired
    private NotificationCommandPublisher notificationCommandPublisher;

    @Transactional
    @Override
    public void delete(CourseModel courseModel) {
        List<ModuleModel> moduleModels = this.moduleRepository.findAllModulesIntoCourse(courseModel.getCourseId());
        if(!moduleModels.isEmpty()){
            for(ModuleModel moduleModel : moduleModels){
                List<LessonModel> lessonModels = this.lessonRepository.findAllLessonsIntoModule(moduleModel.getModuleId());
                if(!lessonModels.isEmpty()){
                    this.lessonRepository.deleteAll(lessonModels);
                }
            }
            this.moduleRepository.deleteAll(moduleModels);
        }
        courseRepository.deleteCourseUserByCourse(courseModel.getCourseId());
        this.courseRepository.delete(courseModel);
    }

    @Override
    public CourseModel save(CourseModel courseModel) {
        return this.courseRepository.save(courseModel);
    }

    @Override
    public Optional<CourseModel> findById(UUID courseId) {
        return this.courseRepository.findById(courseId);
    }

    @Override
    public Page<CourseModel> findAll(Specification<CourseModel> spec, Pageable pageable) {
        return this.courseRepository.findAll(spec, pageable);
    }

    @Override
    public boolean existsByCourseAndUser(UUID courseId, UUID userId) {
        return this.courseRepository.existsByCourseAndUser(courseId, userId);
    }

    @Transactional
    @Override
    public void saveSubscriptionUserInCourse(UUID courseId, UUID userId) {
        this.courseRepository.saveCourseUser(courseId, userId);
    }

    @Transactional
    @Override
    public void saveSubscriptionUserInCourseAndSendNotification(CourseModel courseModel, UserModel userModel) {
        this.courseRepository.saveCourseUser(courseModel.getCourseId(), userModel.getUserId());
        try {
            var notificationsCommandDto = new NotificationCommandDto();
            notificationsCommandDto.setTitle("Bem-Vindo(a) ao Curso?" + courseModel.getName());
            notificationsCommandDto.setMessage(userModel.getFullName() + ", a sua inscrição foi realizada com sucesso.");
            notificationsCommandDto.setUserId(userModel.getUserId());
            this.notificationCommandPublisher.publishNotificationCommand(notificationsCommandDto);
        }catch (Exception e){
            log.warn("Error sending notification");
        }
    }

}
