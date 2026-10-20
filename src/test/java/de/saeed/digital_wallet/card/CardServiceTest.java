package de.saeed.digital_wallet.card;

import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CardServiceTest {

    UUID testUuid = UUID.randomUUID();

    @Test
    void shouldReturnCardWhenCardExists() {
        // Arrange
        Card myTestCard = new Card("My Visa Card", "1234");
        UUID id = myTestCard.getId();

        CardRepository repository = mock(CardRepository.class);
        when(repository.findById(id))
                .thenReturn(Optional.of(myTestCard));

        CardService service = new CardService(repository);

        // Act
        Card actualCard = service.getCardById(id);

        // Assert
        assertEquals(myTestCard, actualCard);
        assertEquals("My Visa Card", actualCard.getDisplayName());
    }

    @Test
    void shouldThrowExceptionWhenCardDoesNotExist() {
        CardRepository repository =
                mock(CardRepository.class);

        when(repository.findById(testUuid))
                .thenReturn(Optional.empty());

        CardService service =
                new CardService(repository);

        assertThrows(
                CardNotFoundException.class,
                () -> service.getCardById(testUuid));
    }
}