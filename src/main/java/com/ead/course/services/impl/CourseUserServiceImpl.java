package com.ead.course.services.impl;

import com.ead.course.clients.AuthUserClient;
import com.ead.course.models.CourseModel;
import com.ead.course.models.CourseUserModel;
import com.ead.course.repositories.CourseModelRepository;
import com.ead.course.services.CourseUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CourseUserServiceImpl implements CourseUserService {

    @Autowired
    private CourseModelRepository courseModelRepository;

    @Autowired
    private AuthUserClient authUserClient;

    @Override
    public boolean existsByCourseAndUserId(CourseModel courseModel, UUID userId) {
        return courseModelRepository.existsByCourseAndUserId(courseModel, userId);
    }

    @Override
    public CourseUserModel save(CourseUserModel courseUserModel) {
        return this.courseModelRepository.save(courseUserModel);
    }

    @Transactional
    @Override
    public CourseUserModel saveAndSendSubscritpionUserInCourse(CourseUserModel courseUserModel) {
        courseUserModel = courseModelRepository.save(courseUserModel);
        this.authUserClient.postSubscriptionUserInCourse(courseUserModel.getCourse().getCourseId(), courseUserModel.getUserId());
        return courseUserModel;
    }
}
