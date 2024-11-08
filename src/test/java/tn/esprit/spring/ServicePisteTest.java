package tn.esprit.spring;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.entities.Piste;
import tn.esprit.spring.entities.Color;
import tn.esprit.spring.repositories.IPisteRepository;
import tn.esprit.spring.services.PisteServicesImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class PisteServicesImplTest {

	@Mock
	private IPisteRepository pisteRepository;

	@InjectMocks
	private PisteServicesImpl pisteServices;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	void testRetrieveAllPistes() {
		// Arrange
		List<Piste> mockPistes = Arrays.asList(
				new Piste(1L, "Green Trail", Color.GREEN, 200, 15, null),
				new Piste(2L, "Blue Trail", Color.BLUE, 300, 20, null)
		);
		when(pisteRepository.findAll()).thenReturn(mockPistes);

		// Act
		List<Piste> pistes = pisteServices.retrieveAllPistes();

		// Assert
		assertEquals(mockPistes, pistes);
		verify(pisteRepository).findAll();
	}

	@Test
	void testAddPiste() {
		// Arrange
		Piste piste = new Piste(1L, "Red Trail", Color.RED, 250, 30, null);
		when(pisteRepository.save(piste)).thenReturn(piste);

		// Act
		Piste savedPiste = pisteServices.addPiste(piste);

		// Assert
		assertEquals(piste, savedPiste);
		verify(pisteRepository).save(piste);
	}

	@Test
	void testRemovePiste() {
		// Act
		pisteServices.removePiste(1L);

		// Assert
		verify(pisteRepository).deleteById(1L);
	}

	@Test
	void testRetrievePiste() {
		// Arrange
		Piste mockPiste = new Piste(1L, "Black Trail", Color.BLACK, 400, 35, null);
		when(pisteRepository.findById(1L)).thenReturn(Optional.of(mockPiste));

		// Act
		Piste retrievedPiste = pisteServices.retrievePiste(1L);

		// Assert
		assertEquals(mockPiste, retrievedPiste);
		verify(pisteRepository).findById(1L);
	}
}
