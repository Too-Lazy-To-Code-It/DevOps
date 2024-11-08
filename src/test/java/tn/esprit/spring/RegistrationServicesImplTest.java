package tn.esprit.spring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Registration;
import tn.esprit.spring.entities.Skier;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IRegistrationRepository;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.services.RegistrationServicesImpl;

public class RegistrationServicesImplTest {

    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private ICourseRepository courseRepository;

    @InjectMocks
    private RegistrationServicesImpl registrationServices;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddRegistrationAndAssignToSkier() {
        Long skierId = 1L;
        Registration registration = new Registration();
        Skier skier = new Skier();
        skier.setNumSkier(skierId);

        when(skierRepository.findById(skierId)).thenReturn(Optional.of(skier));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        Registration result = registrationServices.addRegistrationAndAssignToSkier(registration, skierId);

        assertNotNull(result);
        assertEquals(skier, result.getSkier());
        verify(registrationRepository, times(1)).save(registration);
    }

    @Test
    void testAssignRegistrationToCourse() {
        Long registrationId = 1L;
        Long courseId = 2L;
        Registration registration = new Registration();
        Course course = new Course();
        course.setNumCourse(courseId);

        when(registrationRepository.findById(registrationId)).thenReturn(Optional.of(registration));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        Registration result = registrationServices.assignRegistrationToCourse(registrationId, courseId);

        assertNotNull(result);
        assertEquals(course, result.getCourse());
        verify(registrationRepository, times(1)).save(registration);
    }

    @Test
    void testAddRegistrationAndAssignToSkierAndCourse_SuccessfulAdultCourse() {
        Long skierId = 1L;
        Long courseId = 2L;
        int week = 1;

        Skier skier = new Skier();
        skier.setNumSkier(skierId);
        skier.setDateOfBirth(LocalDate.now().minusYears(20));  // Skier is 20 years old

        Course course = new Course();
        course.setNumCourse(courseId);
        course.setTypeCourse(TypeCourse.COLLECTIVE_ADULT);

        Registration registration = new Registration();
        registration.setNumWeek(week);

        when(skierRepository.findById(skierId)).thenReturn(Optional.of(skier));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(week, skierId, courseId)).thenReturn((long) 0);
        when(registrationRepository.countByCourseAndNumWeek(course, week)).thenReturn((long) 5); // Slot available in course
        when(registrationRepository.save(any(Registration.class))).thenReturn(registration);

        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, skierId, courseId);

        assertNotNull(result);
        assertEquals(skier, result.getSkier());
        assertEquals(course, result.getCourse());
        verify(registrationRepository, times(1)).save(registration);
    }

    @Test
    void testAddRegistrationAndAssignToSkierAndCourse_FullCourse() {
        Long skierId = 1L;
        Long courseId = 2L;
        int week = 1;

        Skier skier = new Skier();
        skier.setNumSkier(skierId);
        skier.setDateOfBirth(LocalDate.now().minusYears(10)); // Skier is 10 years old

        Course course = new Course();
        course.setNumCourse(courseId);
        course.setTypeCourse(TypeCourse.COLLECTIVE_CHILDREN);

        Registration registration = new Registration();
        registration.setNumWeek(week);

        when(skierRepository.findById(skierId)).thenReturn(Optional.of(skier));
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(registrationRepository.countDistinctByNumWeekAndSkier_NumSkierAndCourse_NumCourse(week, skierId, courseId)).thenReturn((long) 0);
        when(registrationRepository.countByCourseAndNumWeek(course, week)).thenReturn((long) 6); // Course is full

        Registration result = registrationServices.addRegistrationAndAssignToSkierAndCourse(registration, skierId, courseId);

        assertNull(result);  // Expect null because the course is full
        verify(registrationRepository, never()).save(registration);
    }
    
}
