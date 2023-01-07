package com.example.unittestsample.contract;

import com.example.unittestsample.contract.application.ContractService;
import com.example.unittestsample.contract.domain.Contract;
import com.example.unittestsample.contract.port.in.ApplyNewContractCommand;
import com.example.unittestsample.contract.port.in.ApplyNewContractUseCase;
import com.example.unittestsample.contract.port.out.ContractSavePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//@SpringBootTest
@ExtendWith({ MockitoExtension.class })
class UnitTestSampleApplicationTests {
    private ApplyNewContractUseCase contractService;
    private ApplyNewContractCommand command;

    @Mock
    private ContractSavePort contractSavePort;

    @Captor
    private ArgumentCaptor<Contract> contractCaptor;

    @BeforeEach
    void setUp() {
        contractService = new ContractService(contractSavePort); //Mockito.mock(ApplyNewContractUseCase.class);
        command = new ApplyNewContractCommand();
    }

    @ParameterizedTest
    @ValueSource(ints = {18, 19, 29, 30, 31, 80, 81, 100, 1999})
    void Given_年齡大於18_When_新申請_Then_申請成功(int age) {
        // Arrange
        command.setCustomerAge(age);

        // Act
        var rs = contractService.applyNewContract(command);

        // Assert
        assertTrue(rs, "年齡滿 18 應申請成功");
    }

    @Test
    void Given_年齡小於18_When_新申請_Then_申請失敗() {
        // Arrange
        command.setCustomerAge(17);

        // Act
        var rs = contractService.applyNewContract(command);

        // Assert
        assertFalse(rs, "年齡未滿 18 應申請失敗");
    }

    @Test
    void Given_年齡大於18_When_新申請_Then_儲存成功() {
        // Arrange
        command.setCustomerAge(18);
        when(contractSavePort.save(any())).thenReturn(new Contract());

        // Act
        var rs = contractService.applyNewContract(command);

        // Assert
        verify(contractSavePort).save(contractCaptor.capture());
        assertTrue(rs, "年齡滿 18 應申請成功");
    }

    @Test
    void Given_年齡大於等於18_When_新申請_Then_儲存使用者等級_L0() {
        // Arrange
        command.setCustomerAge(18);
        when(contractSavePort.save(any())).thenReturn(new Contract());

        // Act
        var rs = contractService.applyNewContract(command);

        // Assert
        verify(contractSavePort).save(contractCaptor.capture());
        var param = contractCaptor.getValue();
        assertEquals("L0", param.getUserLevel());
        assertTrue(rs, "年齡滿 18 應申請成功");
    }

    @Test
    void Given_年齡大於等於30_When_新申請_Then_儲存使用者等級_LMagician() {
        // Arrange
        command.setCustomerAge(30);
        when(contractSavePort.save(any())).thenReturn(new Contract());

        // Act
        var rs = contractService.applyNewContract(command);

        // Assert
        verify(contractSavePort).save(contractCaptor.capture());
        var param = contractCaptor.getValue();
        assertEquals("LMagician", param.getUserLevel());
        assertTrue(rs, "年齡滿 18 應申請成功");
    }
//
//    @Test
//    void Given_年齡大於等於18_When_新申請_Then_SaveSuccess() {
//        // Arrange
//        command.setCustomerAge(18);
//        when(contractSavePort.save(any())).thenReturn(new Contract());
//
//        // Act
//        var rs = contractService.applyNewContract(command);
//
//        // Assert
//        verify(contractSavePort).save(any());
//        assertTrue(rs, "年齡滿 18 應申請成功");
//    }
}
