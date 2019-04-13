package com.bsep.PKI.service.keystore;

import java.io.File;
import java.security.PrivateKey;
import java.security.cert.Certificate;

public interface KeyStoreWriterService {

    void loadKeyStore(Object fileOrFileName, char[] password);

    void saveKeyStore(Object fileOrFileName, char[] password);

    void write(String alias, PrivateKey privateKey, char[] password, Certificate certificate);

    void writeCertificate(String alias, Certificate certificate);
}
