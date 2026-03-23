package br.com.actionlabs.carboncalc.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "calculations")
public class Calculation {

    @Id
    private String id;

    private String name;
    private String email;
    private String uf;
    private String phoneNumber;

    private double energyResult;
    private double transportationResult;
    private double solidWasteResult;
    private double totalResult;
}