package bj.orientation.model;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Une note saisie par le candidat pour une matière de son relevé.
 * La note est bornée à l'intervalle [0, 20] (barème du baccalauréat béninois).
 */
public record NoteSaisie(
        @NotBlank @Size(max = 100) String libelle,
        @DecimalMin("0") @DecimalMax("20") double note,
        @DecimalMin("0") @DecimalMax("20") double coefficient) {
}
