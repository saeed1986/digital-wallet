
package de.saeed.digital_wallet.card;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public List<Card> getAllCards() {
        return cardService.getAllCards();
    }

    @GetMapping("/{id}")
    public Card getCardById(@PathVariable UUID id) {
        return cardService.getCardById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Card createCard(@Valid @RequestBody CreateCardRequest request) {
        return cardService.createCard(
                request.displayName(),
                request.lastFour()
        );
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
}
