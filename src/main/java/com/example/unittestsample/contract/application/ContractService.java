package com.example.unittestsample.contract.application;

import com.example.unittestsample.contract.domain.Contract;
import com.example.unittestsample.contract.port.in.ApplyNewContractUseCase;
import com.example.unittestsample.contract.port.out.ContractSavePort;
import com.example.unittestsample.contract.port.in.ApplyNewContractCommand;
import com.example.unittestsample.phone.application.PhoneCheckService;
import com.example.unittestsample.phone.port.in.PhoneCheckPort;

public class ContractService implements ApplyNewContractUseCase {
    private final ContractSavePort contractRepo;
    private final PhoneCheckPort phoneCheckPort;

    public ContractService(ContractSavePort contractRepo, PhoneCheckPort phoneCheckService) {
        this.contractRepo = contractRepo;
        this.phoneCheckPort = phoneCheckService;
    }

    @Override
    public boolean applyNewContract(ApplyNewContractCommand command) {
        // validation
        if (command.getCustomerAge() < 18) {
            return false;
        }

        if (phoneCheckPort.isPhoneUsed(command.getPhone())) {
            return false;
        }

        // execute
        Contract contract = new Contract();
        if(command.getCustomerAge() >= 60) {
            contract.setUserLevel("LSage");
        }else if(command.getCustomerAge() >= 40) {
            contract.setUserLevel("LMaster");
        }else if(command.getCustomerAge() >= 30) {
            contract.setUserLevel("LMagician");
        }else if(command.getCustomerAge()>=18){
            contract.setUserLevel("L0");
        }

        contract = contractRepo.save(contract);
        if(contract.getContractId() == null)
        {
            return false;
        }
        return true;
    }
}
