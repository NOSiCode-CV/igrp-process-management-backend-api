package cv.igrp.platform.process.management.shared.domain.models;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.text.Normalizer;
import java.util.regex.Pattern;


@Getter
@EqualsAndHashCode(of = "normalizedName")
public final class Name {

    private final String value;
    private final String normalizedName;

    @Builder
    private Name(String value) {
        if (value == null || value.trim().isEmpty())
            throw new IllegalArgumentException("The value of the name is required.");
        this.value = value.trim();
        this.normalizedName = normalizeName(this.value);
    }

    private String normalizeName(String input) {
        var normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        var pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        var withoutAccents = pattern.matcher(normalized).replaceAll("");
        return withoutAccents.toUpperCase();
    }

    public static Name create(String value) {
      return Name.builder()
          .value(value)
          .build();
    }

}
