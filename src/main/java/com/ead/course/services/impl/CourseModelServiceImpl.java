package com.ead.course.services.impl;

import com.ead.course.repositories.CourseModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CourseModelServiceImpl {

    @Autowired
    private CourseModelRepository courseModelRepository;

}
