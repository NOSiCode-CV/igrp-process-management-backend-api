package cv.igrp.platform.process.management.shared.domain.models;

import lombok.Builder;
import lombok.Getter;

import java.text.Normalizer;
import java.util.regex.Pattern;


@Getter
public final class Name {

    private final String valor;
    private final String nomeNormalizado;

    @Builder
    public Name(String valor) {
        if (valor == null || valor.trim().isEmpty())
            throw new IllegalArgumentException("Nome não pode ser vazio.");
        this.valor = valor.trim();
        this.nomeNormalizado = normalizeName(this.valor);
    }

    private String normalizeName(String input) {
        var normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        var pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        var withoutAccents = pattern.matcher(normalized).replaceAll("");
        return withoutAccents.toUpperCase();
    }

}
