import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

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
@SpringBootTest
 class CourseServicesImplTest {


    @InjectMocks
    private CourseServicesImpl courseService;

    @Mock
    private ICourseRepository courseRepository;
    ICourseRepository cr = Mockito.mock(ICourseRepository.class);

    Course course = new Course(1L, 101, TypeCourse.COLLECTIVE_ADULT, Support.SKI, 49.99f, 10, null);

    List<Course> courseList= new ArrayList<Course>() {{
        add(new Course(2L, 102, TypeCourse.COLLECTIVE_ADULT, Support.SNOWBOARD, 59.99f, 12, null));
        add(new Course(3L, 103, TypeCourse.INDIVIDUAL, Support.SKI, 69.99f, 15, null));
    }};


    @Test
    void testRetrieveCourse() {
        // Mock the repository call to find a course by its ID
        Mockito.when(courseRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(course));
        // Call the service method
        Course retrievedCourse = courseService.retrieveCourse(1L);
        // Assert that the course was retrieved correctly
        assertNotNull(retrievedCourse);
        assertEquals(course.getNumCourse(), retrievedCourse.getNumCourse());
        // Verify that the repository's findById method was called
        verify(courseRepository).findById(Mockito.anyLong());
    }
}