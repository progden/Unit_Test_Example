package com.example.unittestsample.contract.port.in;

import lombok.Data;

@Data
public class ApplyNewContractCommand {
    private int customerAge;
    private String phone;
}
