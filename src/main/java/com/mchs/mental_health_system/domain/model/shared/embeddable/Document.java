package com.mchs.mental_health_system.domain.model.shared.embeddable;
import com.mchs.mental_health_system.domain.model.enums.others.Country;
import com.mchs.mental_health_system.domain.model.enums.others.DocumentType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Document {

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "document_type")
    private DocumentType type;

    @NotBlank
    @Column(name = "document_number")
    private String number;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "document_issuing_country")
    private Country issuingCountry;
}