package com.example.unittestsample.contract;

import com.example.unittestsample.contract.application.ContractService;
import com.example.unittestsample.contract.domain.Contract;
import com.example.unittestsample.contract.port.in.ApplyNewContractCommand;
import com.example.unittestsample.contract.port.in.ApplyNewContractUseCase;
import com.example.unittestsample.contract.port.out.ContractSavePort;
import com.example.unittestsample.phone.port.in.PhoneCheckPort;
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
import static org.mockito.Mockito.*;

//@SpringBootTest
@ExtendWith({ MockitoExtension.class })
class UnitTestSampleApplicationTests {
    private ApplyNewContractUseCase contractService;
    private ApplyNewContractCommand command;

    @Mock
    private PhoneCheckPort phoneCheckService;
    @Mock
    private ContractSavePort contractSavePort;

    @Captor
    private ArgumentCaptor<Contract> contractCaptor;

    private String unusedPhone = "0912345678";
    private String usedPhone = "0987654321";

    @BeforeEach
    void setUp() {
        contractService = new ContractService(contractSavePort, phoneCheckService);
        command = new ApplyNewContractCommand();

        givenCustomerAge(18);
        defaultPhoneIsNotUsed();
        defaultSaveCanCallSuccess();
    }

    @ParameterizedTest
    @ValueSource(ints = {18, 19, 29, 30, 31, 80, 81, 100, 1999})
    void Given年齡大於18_When新申請_Then申請成功(int age) {
        // Arrange
        givenCustomerAge(age);

        // Act
        var rs = contractService.applyNewContract(command);

        // Assert
        assertTrue(rs, "年齡滿 18 應申請成功");
    }

    @Test
    void Given年齡小於18_When新申請_Then申請失敗() {
        // Arrange
        givenCustomerAge(17);

        // Act
        var rs = contractService.applyNewContract(command);

        // Assert
        assertFalse(rs, "年齡未滿 18 應申請失敗");
    }

    @Test
    void Given介接回傳門號尚未使用_When新申請_Then儲存成功() {
        // Arrange
        givenPhoneIsNotUsed();

        // Act
        var rs = contractService.applyNewContract(command);

        // Assert
        verify(phoneCheckService).isPhoneUsed(any());
        verify(contractSavePort).save(any());
        assertTrue(rs, "門號尚未使用應申請成功");
    }

    @Test
    void Given介接回傳門號已經使用_When新申請_Then儲存失敗() {
        // Arrange
        givenPhoneIsUsed();

        // Act
        var rs = contractService.applyNewContract(command);

        // Assert
        verify(phoneCheckService).isPhoneUsed(any());
        verify(contractSavePort, never()).save(any());
        assertFalse(rs, "門號已經使用應申請失敗");
    }

    @Test
    void Given年齡大於等於18_When新申請_Then儲存使用者等級L0() {
        // Arrange
        givenCustomerAge(18);

        // Act
        var rs = contractService.applyNewContract(command);

        // Assert
        verify(contractSavePort).save(contractCaptor.capture());
        var param = contractCaptor.getValue();
        assertEquals("L0", param.getUserLevel());
        assertTrue(rs, "年齡滿 18 應申請成功");
    }

    @Test
    void Given年齡大於等於30_When新申請_Then儲存使用者等級LMagician() {
        // Arrange
        givenCustomerAge(30);

        // Act
        var rs = contractService.applyNewContract(command);

        // Assert
        verify(contractSavePort).save(contractCaptor.capture());
        var param = contractCaptor.getValue();
        assertEquals("LMagician", param.getUserLevel());
        assertTrue(rs, "年齡滿 18 應申請成功");
    }

    @Test
    void Given年齡大於等於40_When新申請_Then儲存使用者等級LMaster() {
        // Arrange
        givenCustomerAge(40);

        // Act
        var rs = contractService.applyNewContract(command);

        // Assert
        verify(contractSavePort).save(contractCaptor.capture());
        var param = contractCaptor.getValue();
        assertEquals("LMaster", param.getUserLevel());
        assertTrue(rs, "年齡滿 18 應申請成功");
    }

    @Test
    void Given年齡大於等於60_When新申請_Then儲存使用者等級LSage() {
        // Arrange
        givenCustomerAge(60);

        // Act
        var rs = contractService.applyNewContract(command);

        // Assert
        verify(contractSavePort).save(contractCaptor.capture());
        var param = contractCaptor.getValue();
        assertEquals("LSage", param.getUserLevel());
        assertTrue(rs, "年齡滿 18 應申請成功");
    }

    @Test
    void Given儲存銷售資訊服務_進行申辦_應有正確呼叫(){
        // Arrange

        // Act
        contractService.applyNewContract(command);

        // Assert
        verify(contractSavePort).save(any());
    }

    @Test
    void Given儲存銷售資訊服務且回傳儲存成功_進行申辦_應能成功申辦門號(){
        // Arrange

        // Act
        var rs = contractService.applyNewContract(command);

        // Assert
        verify(contractSavePort).save(any());
        assertTrue(rs, "應能成功申辦門號");
    }

    private void givenPhoneNumber(String phone) {
        command.setPhone(phone);
    }

    private void defaultSaveCanCallSuccess() {
        Contract t = new Contract();
        t.setContractId("default-contract-seqence");
        lenient(). // 可以不發生
                when(contractSavePort.save(any())).thenReturn(t);
    }

    private void givenSaveCanCallSuccess() {
        when(contractSavePort.save(any())).thenReturn(new Contract());
    }

    private void givenCustomerAge(int customerAge) {
        command.setCustomerAge(customerAge);
    }


    private void givenPhoneIsUsed() {
        command.setPhone(usedPhone);
        when(phoneCheckService.isPhoneUsed(usedPhone)).thenReturn(true);
    }

    private void defaultPhoneIsNotUsed() {
        command.setPhone(unusedPhone);
        lenient().when(phoneCheckService.isPhoneUsed(unusedPhone)).thenReturn(false);
    }

    private void givenPhoneIsNotUsed() {
        command.setPhone(unusedPhone);
        when(phoneCheckService.isPhoneUsed(unusedPhone)).thenReturn(false);
    }
}
