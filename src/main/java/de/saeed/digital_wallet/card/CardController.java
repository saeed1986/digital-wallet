
package de.saeed.digital_wallet.card;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    public List<CardResponseDto> getAllCards() {
        List<Card> cards = cardService.getAllCards();
        List<CardResponseDto> result = cards.stream().map(CardResponseDto::from).toList();
        return result;
    }

    @GetMapping("/{id}")
    public CardResponseDto getCardById(@PathVariable UUID id) {
        Card card = cardService.getCardById(id);
        return CardResponseDto.from(card);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CardResponseDto createCard(@Valid @RequestBody CreateCardRequest request) {
        Card card = cardService.createCard(request.displayName(),
                request.lastFour());
        return CardResponseDto.from(card);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteCard(@PathVariable UUID id) {
        cardService.deleteCard(id);
    }

    @PatchMapping("/{id}")
    public CardResponseDto updateCard(@PathVariable UUID id, @Valid @RequestBody UpdateCardRequest update) {
        Card card = cardService.updateCard(id, update.displayName());
        return CardResponseDto.from(card);
    }

    public record UpdateCardRequest(
            @NotBlank(message = "Display name is required")
            @Size(max = 100, message = "Display name must not exceed 100 characters")
            String displayName
    ) {
    }

    public record CreateCardRequest(

            @NotBlank(message = "Display name is required")
            @Size(max = 100, message = "Display name must not exceed 100 characters")
            String displayName,

            @NotBlank(message = "Last four digits are required")
            @Pattern(regexp = "\\d{4}", message = "Last four must contain exactly four digits")
            String lastFour

    ) {
    }

    public record CardResponseDto(
            UUID id,
            String displayName,
            String lastFour,
            CardStatus status,
            Instant createdAt) {
        public static CardResponseDto from(Card card) {
            return new CardResponseDto(card.getId(), card.getDisplayName(), card.getLastFour(), card.getStatus(), card.getCreatedAt());
        }
    }
}
