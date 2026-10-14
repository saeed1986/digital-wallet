
package de.saeed.digital_wallet.card;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CardService {

    private final CardRepository cardRepository;

    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public List<Card> getAllCards() {
        return cardRepository.findAll();
    }

    public Card getCardById(UUID id) {
        return cardRepository.findById(id)
                .orElseThrow(() ->
                        new CardNotFoundException(id));
    }

    public Card createCard(String displayName, String lastFour) {
        Card card = new Card(displayName, lastFour);
        return cardRepository.save(card);
    }
}
