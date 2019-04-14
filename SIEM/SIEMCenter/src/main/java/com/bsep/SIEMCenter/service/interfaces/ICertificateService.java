package com.bsep.SIEMCenter.service.interfaces;


import com.bsep.SIEMCenter.controller.dto.CertificateSigningRequest;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

public interface ICertificateService {

    KeyPair generateKeyPair();

    CertificateSigningRequest prepareCSR(PublicKey publicKey);

    void saveCertificate(PrivateKey privateKey, X509Certificate certificate);

    void saveCertificateInTrustStore(X509Certificate certificate);

    void loadCertificate(String certificatePath);
}
