package de.saeed.digital_wallet.card;

import org.junit.jupiter.api.Test;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CardServiceTest {

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
        // Arrange
        CardRepository repository = mock(CardRepository.class);
        UUID id = UUID.randomUUID();

        when(repository.findById(id))
                .thenReturn(Optional.empty());

        CardService service = new CardService(repository);

        // Act
        CardNotFoundException exception = assertThrows(
                CardNotFoundException.class,
                () -> service.getCardById(id)
        );

        // Assert
        assertEquals(
                "Card not found with id: " + id,
                exception.getMessage()
        );
    }
}