package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.Course;
import tn.esprit.spring.entities.Instructor;
import tn.esprit.spring.entities.Support;
import tn.esprit.spring.entities.TypeCourse;
import tn.esprit.spring.repositories.ICourseRepository;
import tn.esprit.spring.repositories.IInstructorRepository;
import tn.esprit.spring.services.InstructorServicesImpl;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InstructorServicesMockTest {

	@InjectMocks
	private InstructorServicesImpl instructorService;

	@Mock
	private IInstructorRepository instructorRepository;

	@Mock
	private ICourseRepository courseRepository;

	private Instructor instructor = new Instructor(1L, "John", "Doe", LocalDate.of(2020, 1, 1), new HashSet<>());

	private Course course = new Course(2L, 101, TypeCourse.INDIVIDUAL, Support.SKI, 49.99f, 10, null);

	private List<Instructor> instructorList = new ArrayList<Instructor>() {{
		add(new Instructor(2L, "Jane", "Smith", LocalDate.of(2021, 5, 10), new HashSet<>()));
		add(new Instructor(3L, "Jim", "Beam", LocalDate.of(2019, 3, 15), new HashSet<>()));
	}};

	@Test
	void testAddInstructor() {
		when(instructorRepository.save(instructor)).thenReturn(instructor);
		Instructor savedInstructor = instructorService.addInstructor(instructor);
		assertNotNull(savedInstructor);
		assertEquals(instructor.getFirstName(), savedInstructor.getFirstName());
		verify(instructorRepository).save(instructor);
	}

	@Test
	void testRetrieveAllInstructors() {
		when(instructorRepository.findAll()).thenReturn(instructorList);
		List<Instructor> retrievedInstructors = instructorService.retrieveAllInstructors();
		assertNotNull(retrievedInstructors);
		assertEquals(2, retrievedInstructors.size());
		verify(instructorRepository).findAll();
	}

	@Test
	void testRetrieveInstructor() {
		when(instructorRepository.findById(1L)).thenReturn(Optional.of(instructor));
		Instructor retrievedInstructor = instructorService.retrieveInstructor(1L);
		assertNotNull(retrievedInstructor);
		assertEquals(instructor.getFirstName(), retrievedInstructor.getFirstName());
		verify(instructorRepository).findById(1L);
	}

	@Test
	void testUpdateInstructor() {
		when(instructorRepository.save(instructor)).thenReturn(instructor);
		Instructor updatedInstructor = instructorService.updateInstructor(instructor);
		assertNotNull(updatedInstructor);
		assertEquals(instructor.getFirstName(), updatedInstructor.getFirstName());
		verify(instructorRepository).save(instructor);
	}

	@Test
	void testAddInstructorAndAssignToCourse() {
		when(courseRepository.findById(2L)).thenReturn(Optional.of(course));
		when(instructorRepository.save(instructor)).thenReturn(instructor);

		Instructor savedInstructor = instructorService.addInstructorAndAssignToCourse(instructor, 2L);

		assertNotNull(savedInstructor);
		assertTrue(savedInstructor.getCourses().contains(course));
		verify(courseRepository).findById(2L);
		verify(instructorRepository).save(instructor);
	}

	@Test
	void testAddInstructorAndAssignToCourseCourseNotFound() {
		when(courseRepository.findById(2L)).thenReturn(Optional.empty());

		Instructor savedInstructor = instructorService.addInstructorAndAssignToCourse(instructor, 2L);

		assertNotNull(savedInstructor);
		assertTrue(savedInstructor.getCourses().isEmpty());  // No course should be assigned
		verify(courseRepository).findById(2L);
		verify(instructorRepository).save(instructor);
	}
}
