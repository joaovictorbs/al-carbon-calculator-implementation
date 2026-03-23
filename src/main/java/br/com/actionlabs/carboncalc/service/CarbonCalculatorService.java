package br.com.actionlabs.carboncalc.service;

import br.com.actionlabs.carboncalc.dto.*;
import br.com.actionlabs.carboncalc.model.Calculation;
import br.com.actionlabs.carboncalc.model.EnergyEmissionFactor;
import br.com.actionlabs.carboncalc.model.SolidWasteEmissionFactor;
import br.com.actionlabs.carboncalc.model.TransportationEmissionFactor;
import br.com.actionlabs.carboncalc.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CarbonCalculatorService {

    private final CalculationRepository calculationRepository;
    private final EnergyEmissionFactorRepository energyRepository;
    private final TransportationEmissionFactorRepository transportRepository;
    private final SolidWasteEmissionFactorRepository wasteRepository;

    //POST - /open/start-calc
    public StartCalcResponseDTO startCalculation(StartCalcRequestDTO request) {

        Calculation calc = new Calculation();
        calc.setName(request.getName());
        calc.setEmail(request.getEmail());
        calc.setPhoneNumber(request.getPhoneNumber());
        calc.setUf(request.getUf());

        Calculation saveCalc = calculationRepository.save(calc);

        StartCalcResponseDTO calcResponse = new StartCalcResponseDTO();
        calcResponse.setId(saveCalc.getId());

        return calcResponse;
    }

    //PUT - /open/info
    public UpdateCalcInfoResponseDTO updateInformation(UpdateCalcInfoRequestDTO request) {

        Calculation calc = calculationRepository.findById(request.getId()).orElseThrow(() -> new RuntimeException("Calculation not found - ID: " + request.getId()));

        EnergyEmissionFactor energyFactor = energyRepository.findById(calc.getUf()).orElseThrow(() -> new RuntimeException("Energy factor not found - UF: " + calc.getUf()));

        //calc energy emission
        double energyEmission = request.getEnergyConsumption() * energyFactor.getFactor();


        //calc transport emission
        double transportEmission = 0.0;
        if (request.getTransportation() != null) {
            for (TransportationDTO transport : request.getTransportation()) {
                TransportationEmissionFactor transportFactor = transportRepository.findById(transport.getType()).orElseThrow(() -> new RuntimeException("Tansport factor not found - transport: " + transport.getType()));

                transportEmission += transport.getMonthlyDistance() * transportFactor.getFactor();
            }
        }


        //calc solid waste
        SolidWasteEmissionFactor wasteFactor = wasteRepository.findById(calc.getUf()).orElseThrow(() -> new RuntimeException("Waste factor not found - UF: " + calc.getUf()));

        double recyclableWaste = request.getSolidWasteTotal() * request.getRecyclePercentage();
        double nonRecyclableWaste = request.getSolidWasteTotal() * (1.0 - request.getRecyclePercentage());

        double wasteEmission = (recyclableWaste * wasteFactor.getRecyclableFactor()) + (nonRecyclableWaste * wasteFactor.getNonRecyclableFactor());


        calc.setEnergyResult(energyEmission);
        calc.setTransportationResult(transportEmission);
        calc.setSolidWasteResult(wasteEmission);
        calc.setTotalResult(energyEmission + transportEmission + wasteEmission);

        calculationRepository.save(calc);

        UpdateCalcInfoResponseDTO infoResponse = new UpdateCalcInfoResponseDTO();
        infoResponse.setSuccess(true);

        return infoResponse;
    }

    //GET - /open/result/{id}
    public CarbonCalculationResultDTO getResult(String id) {

        Calculation calc = calculationRepository.findById(id).orElseThrow(() -> new RuntimeException("Calculation not found - ID: " + id));

        CarbonCalculationResultDTO calcResult = new CarbonCalculationResultDTO();
        calcResult.setEnergy(calc.getEnergyResult());
        calcResult.setTransportation(calc.getTransportationResult());
        calcResult.setSolidWaste(calc.getSolidWasteResult());
        calcResult.setTotal(calc.getTotalResult());

        return calcResult;
    }
}