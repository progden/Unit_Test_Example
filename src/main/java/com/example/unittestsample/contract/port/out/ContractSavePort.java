package com.example.unittestsample.contract.port.out;

import com.example.unittestsample.contract.domain.Contract;

public interface ContractSavePort {
    Contract save(Contract contract);
}
