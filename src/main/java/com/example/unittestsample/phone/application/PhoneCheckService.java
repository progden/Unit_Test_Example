package com.example.unittestsample.phone.application;

import com.example.unittestsample.phone.port.in.PhoneCheckPort;

public class PhoneCheckService implements PhoneCheckPort {
    @Override
    public boolean isPhoneUsed(String phone) {
        return false;
    }
}
