package com.ead.course.services.impl;

import com.ead.course.models.LessonModel;
import com.ead.course.models.ModuleModel;
import com.ead.course.repositories.LessonRepository;
import com.ead.course.repositories.ModuleRepository;
import com.ead.course.services.ModuleService;
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
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Transactional
    @Override
    public void delete(ModuleModel moduleModel) {
        List<LessonModel> lessonModels = this.lessonRepository.findAllLessonsIntoModule(moduleModel.getModuleId());
        if(!lessonModels.isEmpty()){
            this.lessonRepository.deleteAll(lessonModels);
        }
        this.moduleRepository.delete(moduleModel);

    }

    @Override
    public ModuleModel save(ModuleModel moduleModel) {
        return this.moduleRepository.save(moduleModel);
    }

    @Override
    public Optional<ModuleModel> findModuleIntoCourse(UUID courseId, UUID moduleId) {
        return moduleRepository.findModuleIntoCourse(courseId, moduleId);
    }

    @Override
    public List<ModuleModel> findAll() {
        return this.moduleRepository.findAll();
    }

    @Override
    public List<ModuleModel> findAllByCourse(UUID courseId) {
        return this.moduleRepository.findAllModulesIntoCourse(courseId);
    }

    @Override
    public Optional<ModuleModel> findById(UUID moduleId) {
        return this.moduleRepository.findById(moduleId);
    }

    @Override
    public Page<ModuleModel> findAllByCourse(Specification<ModuleModel> spec, Pageable pageable) {
        return moduleRepository.findAll(spec, pageable);
    }
}
