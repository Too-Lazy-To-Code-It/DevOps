package tn.esprit.spring;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.spring.entities.*;
import tn.esprit.spring.repositories.ISkierRepository;
import tn.esprit.spring.repositories.ISubscriptionRepository;
import tn.esprit.spring.services.SubscriptionServicesImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionClassTest {

    @Mock
    ISubscriptionRepository subRepository;

    @Mock
    ISkierRepository skierRepository;

    @InjectMocks
    SubscriptionServicesImpl subServices;

    Subscription s = new Subscription(1L, LocalDate.now(), LocalDate.now().plusMonths(6), 150.0f, TypeSubscription.ANNUAL);

    List<Subscription> lc = new ArrayList<Subscription>() {{
        add(new Subscription(1L, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), 250.0f, TypeSubscription.ANNUAL));
        add(new Subscription(2L, LocalDate.of(2024, 6, 1), LocalDate.of(2024, 11, 30), 120.0f, TypeSubscription.SEMESTRIEL));
        add(new Subscription(3L, LocalDate.of(2024, 10, 1), LocalDate.of(2024, 10, 31), 30.0f, TypeSubscription.MONTHLY));
    }};

    @Test
    void testRetrieveSubscription() {
        when(subRepository.findById(1L)).thenReturn(Optional.of(s));
        Subscription retrievedSub = subServices.retrieveSubscriptionById(1L);
        assertNotNull(retrievedSub);
        assertEquals(s.getNumSub(), retrievedSub.getNumSub());
        verify(subRepository).findById(1L);
    }

    @Test
    void testAddSubscription() {
        Subscription newSub = new Subscription(null, LocalDate.now(), null, 150.0f, TypeSubscription.MONTHLY);
        when(subRepository.save(any(Subscription.class))).thenReturn(new Subscription(4L, newSub.getStartDate(), newSub.getStartDate().plusMonths(1), 150.0f, TypeSubscription.MONTHLY));

        Subscription savedSub = subServices.addSubscription(newSub);

        assertNotNull(savedSub);
        assertEquals(4L, savedSub.getNumSub());
        assertEquals(newSub.getStartDate().plusMonths(1), savedSub.getEndDate());
        verify(subRepository).save(newSub);
    }

    @Test
    void testUpdateSubscription() {
        when(subRepository.save(s)).thenReturn(s);
        Subscription updatedSub = subServices.updateSubscription(s);

        assertNotNull(updatedSub);
        assertEquals(s.getNumSub(), updatedSub.getNumSub());
        verify(subRepository).save(s);
    }


    @Test
    void testRetrieveSubscriptionsByDates() {
        LocalDate startDate = LocalDate.of(2024, 1, 1);
        LocalDate endDate = LocalDate.of(2024, 12, 31);
        when(subRepository.getSubscriptionsByStartDateBetween(startDate, endDate)).thenReturn(lc);

        List<Subscription> subsInDateRange = subServices.retrieveSubscriptionsByDates(startDate, endDate);

        assertNotNull(subsInDateRange);
        assertEquals(3, subsInDateRange.size());
        verify(subRepository).getSubscriptionsByStartDateBetween(startDate, endDate);
    }
}
