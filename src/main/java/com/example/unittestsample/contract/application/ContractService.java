package com.example.unittestsample.contract.application;

import com.example.unittestsample.contract.domain.Contract;
import com.example.unittestsample.contract.port.in.ApplyNewContractUseCase;
import com.example.unittestsample.contract.port.out.ContractSavePort;
import com.example.unittestsample.contract.port.in.ApplyNewContractCommand;

public class ContractService implements ApplyNewContractUseCase {
    private final ContractSavePort contractRepo;

    public ContractService(ContractSavePort contractRepo) {
        this.contractRepo = contractRepo;
    }

    @Override
    public boolean applyNewContract(ApplyNewContractCommand command) {
        // validation
        if (command.getCustomerAge() < 18) {
            return false;
        }

        // execute

        Contract contract = new Contract();
        if(command.getCustomerAge() >= 30) {
            contract.setUserLevel("LMagician");
        }else if(command.getCustomerAge()>=18){
            contract.setUserLevel("L0");
        }
        contractRepo.save(contract);

        return true;
    }
}
