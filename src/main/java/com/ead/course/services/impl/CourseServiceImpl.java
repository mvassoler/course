package com.ead.course.services.impl;

import com.ead.course.models.CourseModel;
import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.CourseRepository;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private LessonRepository lessonRepository;

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
    public List<CourseModel> findAll() {
        return this.courseRepository.findAll();
    }
}
