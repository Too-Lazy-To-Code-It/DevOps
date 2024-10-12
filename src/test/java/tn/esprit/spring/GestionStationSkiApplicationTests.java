package tn.esprit.spring;

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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
class GestionStationSkiApplicationTests {

    @Mock
    ICourseRepository courseRepository;
    // same
    //ICourseRepository m = Mockito.mock(ICourseRepository.class);

    @InjectMocks
    CourseServicesImpl courseServices;
    Course c = new Course(1L, 1, TypeCourse.COLLECTIVE_CHILDREN, Support.SKI, 120 , 1);

    List<Course> lc = new ArrayList<Course>()
    {{
       add(new Course(2L, 1, TypeCourse.COLLECTIVE_ADULT, Support.SNOWBOARD, 120 , 1));
       add(new Course(3L, 1, TypeCourse.INDIVIDUAL, Support.SKI, 120 , 1));
    }};


    @Test
    public  void retreviMagazinTest()
    {
        Mockito.when(courseRepository.save(c)).thenReturn(c);
        // courseServices.addCourse(c);

        // verfy not null
        assertNotNull(c);

        // verfy that the same object is added
        assertEquals(c , courseServices.addCourse(c));

        verify(courseServices).addCourse(c);

    }

    /*
     Mockito.when(courseRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(c));
        //Optional<Course> cc = courseRepository.findById(c.getNumCourse());
        Course cc = courseServices.addCourse(c);
        assertNotNull(cc);
     */

    @Test
    void contextLoads() {
    }

}