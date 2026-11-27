package bj.orientation.model;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

/**
 * Requête de recommandation : série du candidat, notes saisies (matières fortes + éventuelles
 * matières complétées), libellés des 3 matières fortes déclarées (filtre de pertinence).
 * Les tailles sont bornées pour refuser les requêtes démesurées (un relevé compte une quinzaine
 * de matières au plus).
 */
public record RecommandationRequest(
        @NotBlank @Size(max = 10) String serie,
        @NotEmpty @Size(max = 30) @Valid List<NoteSaisie> notes,
        @NotEmpty @Size(max = 10) List<@NotBlank @Size(max = 100) String> matieresFortes,
        @Size(max = 100) String domaine) {
}
