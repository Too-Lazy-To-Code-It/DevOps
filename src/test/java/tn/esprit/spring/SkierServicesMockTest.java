package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.*;
import tn.esprit.spring.services.SkierServicesImpl;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkierServicesMockTest {

    @InjectMocks
    private SkierServicesImpl skierService;

    @Mock
    private ISkierRepository skierRepository;

    @Mock
    private IPisteRepository pisteRepository;

    @Mock
    private ICourseRepository courseRepository;

    @Mock
    private IRegistrationRepository registrationRepository;

    @Mock
    private ISubscriptionRepository subscriptionRepository;

    // Creating mock entities with relationships

    private Subscription subscription = new Subscription(
            1L,
            LocalDate.now(),
            LocalDate.now().plusMonths(1),
            100f,
            TypeSubscription.MONTHLY
    );

    private Piste piste = new Piste(1L, "Piste A", Color.RED, 1500, 25, new HashSet<>());

    private Course course = new Course(
            1L,
            5,
            TypeCourse.COLLECTIVE_ADULT,
            Support.SKI,
            50.0f,
            3,
            new HashSet<>()
    );

    private Skier skier = new Skier(
            1L,
            "John",
            "Doe",
            LocalDate.of(1995, 5, 15),
            "Aspen",
            subscription,
            new HashSet<>(Collections.singletonList(piste)),
            new HashSet<>()
    );

    private Registration registration = new Registration(1L, 12, skier, course);

    @Test
    void testRetrieveAllSkiers() {
        List<Skier> skiers = Arrays.asList(skier, new Skier(2L, "Jane", "Doe", null, "Denver", null, new HashSet<>(), new HashSet<>()));
        when(skierRepository.findAll()).thenReturn(skiers);

        List<Skier> retrievedSkiers = skierService.retrieveAllSkiers();

        assertNotNull(retrievedSkiers);
        assertEquals(2, retrievedSkiers.size());
        verify(skierRepository).findAll();
    }

    @Test
    void testAddSkier() {
        when(skierRepository.save(skier)).thenReturn(skier);

        Skier savedSkier = skierService.addSkier(skier);

        assertNotNull(savedSkier);
        assertEquals(TypeSubscription.MONTHLY, savedSkier.getSubscription().getTypeSub());
        assertEquals(LocalDate.now().plusMonths(1), savedSkier.getSubscription().getEndDate());
        verify(skierRepository).save(skier);
    }

    @Test
    void testAssignSkierToSubscription() {
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(subscriptionRepository.findById(1L)).thenReturn(Optional.of(subscription));
        when(skierRepository.save(skier)).thenReturn(skier);

        Skier updatedSkier = skierService.assignSkierToSubscription(1L, 1L);

        assertNotNull(updatedSkier);
        assertEquals(subscription, updatedSkier.getSubscription());
        verify(skierRepository).save(skier);
    }

    @Test
    void testAddSkierAndAssignToCourse() {
        when(courseRepository.getById(1L)).thenReturn(course);
        when(skierRepository.save(skier)).thenReturn(skier);

        skier.getRegistrations().add(registration);
        Skier savedSkier = skierService.addSkierAndAssignToCourse(skier, 1L);

        assertNotNull(savedSkier);
        savedSkier.getRegistrations().forEach(reg -> {
            assertEquals(savedSkier, reg.getSkier());
            assertEquals(course, reg.getCourse());
            verify(registrationRepository).save(reg);
        });
        verify(skierRepository).save(skier);
    }

    @Test
    void testRemoveSkier() {
        doNothing().when(skierRepository).deleteById(1L);

        skierService.removeSkier(1L);

        verify(skierRepository).deleteById(1L);
    }

    @Test
    void testRetrieveSkier() {
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));

        Skier retrievedSkier = skierService.retrieveSkier(1L);

        assertNotNull(retrievedSkier);
        assertEquals(skier.getNumSkier(), retrievedSkier.getNumSkier());
        verify(skierRepository).findById(1L);
    }

    @Test
    void testAssignSkierToPiste() {
        when(skierRepository.findById(1L)).thenReturn(Optional.of(skier));
        when(pisteRepository.findById(1L)).thenReturn(Optional.of(piste));
        when(skierRepository.save(skier)).thenReturn(skier);

        Skier updatedSkier = skierService.assignSkierToPiste(1L, 1L);

        assertNotNull(updatedSkier);
        assertTrue(updatedSkier.getPistes().contains(piste));
        verify(skierRepository).save(skier);
    }

    @Test
    void testRetrieveSkiersBySubscriptionType() {
        List<Skier> skiers = Arrays.asList(skier, new Skier(2L, "Jane", "Doe", null, "Denver", null, new HashSet<>(), new HashSet<>()));
        when(skierRepository.findBySubscription_TypeSub(TypeSubscription.MONTHLY)).thenReturn(skiers);

        List<Skier> retrievedSkiers = skierService.retrieveSkiersBySubscriptionType(TypeSubscription.MONTHLY);

        assertNotNull(retrievedSkiers);
        assertEquals(2, retrievedSkiers.size());
        verify(skierRepository).findBySubscription_TypeSub(TypeSubscription.MONTHLY);
    }
}

