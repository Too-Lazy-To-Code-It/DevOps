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
		Piste piste1 = new Piste(1L, "Green Trail", Color.GREEN, 200, 15, null);
		Piste piste2 = new Piste(2L, "Blue Trail", Color.BLUE, 300, 20, null);
		when(pisteRepository.findAll()).thenReturn(Arrays.asList(piste1, piste2));

		// Act
		List<Piste> pistes = pisteServices.retrieveAllPistes();

		// Assert
		assertNotNull(pistes);
		assertEquals(2, pistes.size());
		verify(pisteRepository, times(1)).findAll();
	}

	@Test
	void testAddPiste() {
		// Arrange
		Piste piste = new Piste(1L, "Red Trail", Color.RED, 250, 30, null);
		when(pisteRepository.save(piste)).thenReturn(piste);

		// Act
		Piste savedPiste = pisteServices.addPiste(piste);

		// Assert
		assertNotNull(savedPiste);
		assertEquals(piste.getNumPiste(), savedPiste.getNumPiste());
		verify(pisteRepository, times(1)).save(piste);
	}

	@Test
	void testRemovePiste() {
		// Act
		pisteServices.removePiste(1L);

		// Assert
		verify(pisteRepository, times(1)).deleteById(1L);
	}

	@Test
	void testRetrievePiste() {
		// Arrange
		Piste piste = new Piste(1L, "Black Trail", Color.BLACK, 400, 35, null);
		when(pisteRepository.findById(1L)).thenReturn(Optional.of(piste));

		// Act
		Piste retrievedPiste = pisteServices.retrievePiste(1L);

		// Assert
		assertNotNull(retrievedPiste);
		assertEquals("Black Trail", retrievedPiste.getNamePiste());
		verify(pisteRepository, times(1)).findById(1L);
	}
}
