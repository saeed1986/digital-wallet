# Digital Wallet

> Ein persönliches Lern- und Entwicklungsprojekt rund um moderne Softwareentwicklung – Schritt für Schritt, mit Fokus auf Verständnis statt Copy & Paste.

**Projektstatus:** In Entwicklung · Backend zuerst · Frontend später

---

## Warum dieses Projekt?

Vor etwa eineinhalb Jahren wollten meine Frau und ich auf Mallorca zwei Kaffees und zwei Croissants bestellen. Ich versuchte zweimal, kontaktlos mit meiner digitalen Karte zu bezahlen. Beide Male zeigte das Terminal die spanische Meldung „Operación denegada“ mit einem roten Kreuz. Der Cafébesitzer bestätigte, dass die Zahlung fehlgeschlagen sei. Da unser Bargeld nicht für die gesamte Bestellung reichte, nahmen wir nur zwei Kaffees und bezahlten bar.

Zurück in Deutschland stellte ich fest, dass beide Kartenzahlungen trotzdem abgebucht worden waren.

Diese Erfahrung hinterließ eine technische Frage: *Was passiert hinter den Kulissen einer digitalen Zahlung – und wie lassen sich solche Probleme zuverlässig behandeln?*

Dieses Projekt ist mein Weg, mich mit diesen Fragen auseinanderzusetzen und mein Wissen in der Softwareentwicklung systematisch zu vertiefen.

## Ziele

- Java und Spring Boot anhand eines zusammenhängenden Projekts besser verstehen.
- Sauberen, nachvollziehbaren und wartbaren Code schreiben.
- Technische Entscheidungen bewusst treffen und begründen können.
- Tests, Fehlerbehandlung, Datenpersistenz und Transaktionskonzepte praktisch kennenlernen.
- Schritt für Schritt ein Gefühl dafür entwickeln, wie zuverlässige Softwaresysteme aufgebaut werden.
- Erkenntnisse und Themen teilen, die ich auf diesem Lernweg für wichtig halte.

> **Wichtig:** Das Projekt ist in erster Linie ein Lernprojekt. Es ist keine echte Zahlungsplattform und verarbeitet keine realen Zahlungen. Es geht um die **persönliche Finanzübersicht**: Karten verwalten, Ausgaben und Einnahmen nachvollziehen.

---

## Aktueller Stand

Bereits umgesetzt:

- Karten anlegen, auflisten und einzeln abrufen (REST-API)
- Eingabevalidierung (Name Pflicht, genau vier Ziffern)
- Einheitliches Fehlerformat (`400` bei ungültigen Eingaben, `404` bei unbekannter Karte)
- Datenbankschema mit Flyway-Migrationen
- Unit-Tests für die Service-Schicht
- Lokale Datenbank per Docker Compose

Als Nächstes geplant: Antwort-DTOs, Karten ändern/sperren/löschen, Benutzer und Authentifizierung, Transaktionen.

## Technologie-Stack

**Backend (zuerst)**

| Bereich | Technologie |
|---|---|
| Sprache | Java 21 |
| Framework | Spring Boot (Web MVC, Data JPA, Validation, Actuator) |
| Build | Maven (Wrapper `mvnw` enthalten) |
| Datenbank | PostgreSQL 17 (lokal im Docker-Container) |
| Migrationen | Flyway |
| Tests | JUnit 5, Mockito |

**Frontend (später):** React, TypeScript

Die Umsetzung erfolgt schrittweise. Der genaue Funktionsumfang und Zeitplan entwickeln sich mit dem Lernfortschritt.

---

## Schnellstart

### Voraussetzungen

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) (muss laufen)
- Java 21 (Maven ist nicht nötig, der Maven Wrapper `mvnw` lädt es selbst)

### Projekt starten

1. Datenbank starten (PostgreSQL 17 im Container):

   ```bash
   docker compose up -d
   ```

   Mit `docker compose ps` prüfen, ob der Status `healthy` ist.

2. Anwendung starten:

   ```bash
   ./mvnw spring-boot:run
   ```

   Unter Windows (CMD/PowerShell): `mvnw.cmd spring-boot:run`

Beim ersten Start legt Flyway die Tabellen automatisch an. Die API läuft danach unter `http://localhost:8080`.

### Konfiguration

Die Datenbank läuft lokal auf Port **5433** (nicht 5432), damit sie nicht mit einer bereits installierten PostgreSQL-Instanz kollidiert. Die Standardwerte passen zu `docker-compose.yml`. Bei Bedarf lassen sie sich über Umgebungsvariablen überschreiben:

| Variable | Standardwert |
|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5433/digital_wallet` |
| `DB_USERNAME` | `wallet_user` |
| `DB_PASSWORD` | `local_dev_password` |

Diese Werte gelten nur für die lokale Entwicklung.

### Nützliche Befehle

| Befehl | Wirkung |
|---|---|
| `docker compose stop` | Datenbank anhalten (Daten bleiben erhalten) |
| `docker compose start` | Angehaltene Datenbank wieder starten |
| `docker compose down -v` | Container **und** Daten löschen (Neustart bei null) |
| `./mvnw test` | Tests ausführen (die Datenbank muss laufen) |

---

## API-Überblick

| Methode | Pfad | Beschreibung | Erfolg |
|---|---|---|---|
| `POST` | `/api/cards` | Karte anlegen | `201 Created` |
| `GET` | `/api/cards` | Alle Karten auflisten | `200 OK` |
| `GET` | `/api/cards/{id}` | Eine Karte abrufen | `200 OK` |

**Beispiel: Karte anlegen**

```bash
curl -X POST http://localhost:8080/api/cards \
  -H "Content-Type: application/json" \
  -d '{"displayName": "Gehaltskarte", "lastFour": "1234"}'
```

Antwort:

```json
{
  "id": "3f0c0f4e-8c6b-4a6e-9a43-2d1f0b7d9c11",
  "displayName": "Gehaltskarte",
  "lastFour": "1234",
  "status": "ACTIVE",
  "createdAt": "2026-10-29T21:30:00Z"
}
```

**Beispiel: Fehlerantwort (`400 Bad Request`)**

```json
{
  "message": "Validation failed",
  "errors": [
    "lastFour: Last four must contain exactly four digits"
  ]
}
```

---

## Projektstruktur

Der Code ist nach **Fachbereichen** gegliedert (nicht nach technischen Schichten):

```text
src/main/java/de/saeed/digital_wallet/
└── card/
    ├── Card.java                  # Entität (Tabelle "cards")
    ├── CardStatus.java            # ACTIVE / BLOCKED
    ├── CardController.java        # REST-Endpunkte
    ├── CardService.java           # Geschäftslogik
    ├── CardRepository.java        # Datenzugriff (Spring Data JPA)
    ├── CardNotFoundException.java
    └── CardExceptionHandler.java  # Fehler → HTTP-Antworten

src/main/resources/
├── application.properties
└── db/migration/                  # Flyway-Migrationen (V1__...)
```

## Designentscheidungen

- **Keine vollständigen Kartennummern, kein CVV.** Gespeichert werden nur ein frei wählbarer Name und die letzten vier Ziffern. Die Speicherung vollständiger Kartendaten würde den PCI-DSS-Anforderungen unterliegen und ist für ein Lernprojekt weder nötig noch sinnvoll.
- **Schema per Flyway, Prüfung per Hibernate.** Das Datenbankschema wird ausschließlich über Migrationen verändert (`ddl-auto=validate`).
- **Konfiguration über Umgebungsvariablen** mit lokalen Standardwerten, damit keine Zugangsdaten im Repository festgeschrieben werden müssen.
- **Eigene ID-Vergabe per UUID**, damit IDs nicht erratbar oder fortlaufend sind.

---

## Geplanter Lern- und Entwicklungsweg

Die folgenden Punkte sind eine Orientierung. Ein Haken bedeutet: umgesetzt. Die Reihenfolge kann sich ändern, wenn sich beim Lernen neue Fragen oder bessere Ansätze ergeben.

- [x] Projektstruktur und Grundlagen des Backends aufbauen
- [x] Daten mit PostgreSQL persistieren
- [ ] Java- und Spring-Boot-Konzepte praktisch anwenden *(laufend)*
- [ ] REST-API und sinnvolle Fehlerbehandlung entwickeln *(Grundlagen vorhanden, wird erweitert)*
- [ ] Unit- und Integrationstests ergänzen *(Unit-Tests vorhanden, Integrationstests folgen)*
- [ ] Benutzer, Registrierung und Authentifizierung
- [ ] Transaktionen erfassen und den Umgang mit wiederholten Anfragen untersuchen (Idempotenz)
- [ ] Monatsübersicht für Einnahmen und Ausgaben
- [ ] Relevante Sicherheitsaspekte kennenlernen
- [ ] Später ein Frontend mit React und TypeScript ergänzen
- [ ] Architektur und Entscheidungen fortlaufend dokumentieren

---

## Mein Ansatz: verstehen, selbst schreiben, kritisch prüfen

Ich möchte den Code selbst schreiben und die Konzepte dahinter verstehen, statt Lösungen einfach zu übernehmen.

LLMs nutze ich als Lernbegleiter: zum Erklären von Konzepten, zum Hinterfragen technischer Entscheidungen und als Unterstützung beim Lernen. Ihre Vorschläge prüfe ich kritisch. Denn hilfreiche Antworten ersetzen nicht das eigene Wissen und die Fähigkeit, die Korrektheit einer Lösung zu beurteilen.

Mein Ziel ist nicht, möglichst schnell möglichst viel Code zu produzieren, sondern eine solide Grundlage aufzubauen.

## Fortschritt und Austausch

Ich teile regelmäßig Fortschritte und Erkenntnisse auf LinkedIn:

- **Updates:** jeden Sonntag um 18:00 Uhr (deutsche Zeit)
- **Themen:** Java, Spring Boot, Software Engineering, Tests, Datenbanken und weitere Erkenntnisse aus dem Projekt

Ich möchte nicht nur dokumentieren, was ich umsetze, sondern auch erklären, was ich dabei lerne und welche Fragen sich daraus ergeben.

Fragen, konstruktive Kritik und fachlicher Austausch sind herzlich willkommen.

## Mitmachen

Du beschäftigst dich ebenfalls mit Java, Spring Boot oder Softwarearchitektur? Dann freue ich mich über Fragen, Ideen und einen respektvollen fachlichen Austausch.

> **Hinweis:** Dieses Repository dokumentiert ein persönliches Lernprojekt. Es ist nicht für den Einsatz in produktiven Zahlungsprozessen vorgesehen.
