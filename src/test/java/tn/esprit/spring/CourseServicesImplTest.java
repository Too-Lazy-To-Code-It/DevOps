package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.services.CourseServicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CourseServicesImplTest {

    @InjectMocks
    private CourseServicesImpl courseService;

    @Mock
    private ICourseRepository courseRepository;

    private Course course = new Course(1L, 101, TypeCourse.COLLECTIVE_ADULT, Support.SKI, 49.99f, 10, null);

    private List<Course> courseList = new ArrayList<Course>() {{
        add(new Course(2L, 102, TypeCourse.COLLECTIVE_ADULT, Support.SNOWBOARD, 59.99f, 12, null));
        add(new Course(3L, 103, TypeCourse.INDIVIDUAL, Support.SKI, 69.99f, 15, null));
    }};

    @Test
    void testRetrieveCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        Course retrievedCourse = courseService.retrieveCourse(1L);
        assertNotNull(retrievedCourse);
        assertEquals(course.getNumCourse(), retrievedCourse.getNumCourse());
        verify(courseRepository).findById(1L);
    }

    @Test
    void testRetrieveAllCourses() {
        when(courseRepository.findAll()).thenReturn(courseList);
        List<Course> retrievedCourses = courseService.retrieveAllCourses();
        assertNotNull(retrievedCourses);
        assertEquals(2, retrievedCourses.size());
        verify(courseRepository).findAll();
    }
}
