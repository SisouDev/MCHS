package com.mchs.mental_health_system.domain.model.utils.embeddable;

import com.mchs.mental_health_system.domain.model.enums.others.Country;
import com.mchs.mental_health_system.domain.model.enums.others.DocumentType;

public class Document {
    private DocumentType type;
    private String number;
    private Country issuingCountry;
}
