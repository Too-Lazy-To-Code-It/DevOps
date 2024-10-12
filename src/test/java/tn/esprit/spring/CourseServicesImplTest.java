package tn.esprit.spring;import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.services.CourseServicesImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CourseServicesImplTest {

    @InjectMocks
    private CourseServicesImpl courseService;  // Service to be tested

    @Mock
    private ICourseRepository courseRepository;  // Mock repository

    private Course course;  // Single course object for testing
    private List<Course> courseList;  // List of courses for testing

    @BeforeEach
    public void setUp() {
        // Initialize a Course object
        course = new Course(1L, 101, TypeCourse.COLLECTIVE_ADULT, null, 49.99f, 10, null);

        // Initialize list with mock data
        courseList = new ArrayList<Course>() {{
            add(new Course(2L, 102, TypeCourse.COLLECTIVE_ADULT, null, 59.99f, 12, null));
            add(new Course(3L, 103, TypeCourse.INDIVIDUAL, null, 69.99f, 15, null));
        }};
    }

    @Test
    public void testRetrieveAllCourses() {
        // Mock the repository call to return the list of courses
        when(courseRepository.findAll()).thenReturn(courseList);

        // Call the service method
        List<Course> retrievedCourses = courseService.retrieveAllCourses();

        // Assert that the result is not null and has the correct number of elements
        assertNotNull(retrievedCourses);
        assertEquals(2, retrievedCourses.size());

        // Verify that the repository's findAll method was called
        verify(courseRepository).findAll();
    }

    @Test
    public void testAddCourse() {
        // Mock the repository call to save the course
        when(courseRepository.save(course)).thenReturn(course);

        // Call the service method
        Course savedCourse = courseService.addCourse(course);

        // Assert that the course was saved correctly
        assertNotNull(savedCourse);
        assertEquals(course.getNumCourse(), savedCourse.getNumCourse());

        // Verify that the repository's save method was called
        verify(courseRepository).save(course);
    }

    @Test
    public void testUpdateCourse() {
        // Mock the repository call to update the course
        when(courseRepository.save(course)).thenReturn(course);

        // Call the service method
        Course updatedCourse = courseService.updateCourse(course);

        // Assert that the course was updated correctly
        assertNotNull(updatedCourse);
        assertEquals(course.getNumCourse(), updatedCourse.getNumCourse());

        // Verify that the repository's save method was called
        verify(courseRepository).save(course);
    }

    @Test
    public void testRetrieveCourse() {
        // Mock the repository call to find a course by its ID
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        // Call the service method
        Course retrievedCourse = courseService.retrieveCourse(1L);

        // Assert that the course was retrieved correctly
        assertNotNull(retrievedCourse);
        assertEquals(course.getNumCourse(), retrievedCourse.getNumCourse());

        // Verify that the repository's findById method was called
        verify(courseRepository).findById(1L);
    }
}
