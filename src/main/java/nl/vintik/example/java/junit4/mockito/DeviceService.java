package nl.vintik.example.java.junit4.mockito;

import nl.vintik.example.java.junit4.DeviceType;

public interface DeviceService {

    DeviceType getDeviceTypeByUserAgent(String userAgent);

}
