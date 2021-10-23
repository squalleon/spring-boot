package hello.hellospring.util;


import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.util.Store;
import org.bouncycastle.util.encoders.Base64;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;

public class StringUtil {

    public static String getUUID() {
        UUID uuid = UUID.randomUUID();
//        return uuid.toString().replaceAll("-", "").substring(0, 20);
        return toUnsignedString(uuid.getMostSignificantBits(), 6) +
                toUnsignedString(uuid.getLeastSignificantBits(), 6);
    }

    public static String toUnsignedString(long i, int shift) {

        char[] buf = new char[64];
        int charPos = 64;
        int radix = 1 << shift;
        long mask = radix - 1;
        long number = i;

        do {
            buf[--charPos] = digits[(int) (number & mask)];
            number >>>= shift;
        } while (number != 0);

        return new String(buf, charPos, (64 - charPos));
    }

    final static char[] digits = {
            '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9', 'a', 'b', 'c', 'd', 'e', 'f',
            'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
            'o', 'p', 'q', 'r', 's', 't', 'u', 'v',
            'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D',
            'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
            'U', 'V', 'W', 'X', 'Y', 'Z'//, '_', '*' // '.', '-'
    };

    public static String getRandomString() {
        RandomString r = new RandomString(20);
        return String.valueOf(r);
    }

    public static String testSHA256(String pwd) {
        try{

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(pwd.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            //출력
            return hexString.toString();

        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    /**
     * SHA-256으로 해싱하는 메소드
     * SHA256은 단방향으로 복호화가 불가능하고 사용 예는 비밀번호
     * 양방향은 Cipher AES, RSA 은 복호화 가능하다
     */
    public static String sha256(String msg) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(msg.getBytes());

        return bytesToHex(md.digest());
    }

    /**
     * 바이트를 헥스값으로 변환한다
     *
     */
    public static String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b: bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }

    public static void main(String[] args) {
        chainCheck();
    }

    public static void chainCheck() {

        try {
            String signData;
            signData = "MIAGCSqGSIb3DQEHAqCAMIACAQExDzANBglghkgBZQMEAgEFADCABgkqhkiG9w0BBw" +
                    "GggCSABA97ImRhdGEiOiJoZWxsIn0AAAAAAACggDCCAn0wggIioAMCAQICAgTSMAoGCCqGSM49BAMCMDcxEjAQBgNVBAMMCU5hdmVyU2lnbjEUMBIGA1UECgwLTkFWRVIgQ29ycC4xCzAJBgNVBAYTAktSMB4XDTIwMDQxNDAzNDEyNVoXDTIyMDQxNDAzNDEyNV"
                    + "owcTEmMCQGCgmSJomT8ixkAQEMFjlDQTk2dEdaelFnckpzV0ljX2ZFV2cxCzAJBgNVBAYTAktSMRYwFAYDVQQLDA1OYXZlciBBY2NvdW50MRIwEAYDVQQDDAnsoJXsoJXqt6AxDjAMBg"
                    + "NVBAoMBU5hdmVyMFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEp+mdmpS27coS6huZVNd3V3JBmSOyoAvjQ1nMAxmwcX7BpB0EQYxmMIU6Ox97QyrDevW2fCXJhGfAxkVveO5VsqOB4zC"
                    + "B4DAJBgNVHRMEAjAAMF8GA1UdIwRYMFaAFIL05Bk2J3f/TamoGG/7euFNDKyEoTukOTA3MRIwEAYDVQQDDAlOYXZlclNpZ24xFDASBgNVBAoMC05BVkVSIENvcnAuMQsw"
                    + "CQYDVQQGEwJLUoIBCjAdBgNVHQ4EFgQUFp5f+BaoPoKIVAOFnNrVIs3YjKgwDgYDVR0PAQH/BAQDAgbAMEMGCCsGAQUFBwEBBDcwNTAzBggrBgEFBQcwAYYnaHR0cHM6Ly"
                    + "9kZXYubnNpZ24tZ3cubmF2ZXIuY29tL3BraS9vY3NwMAoGCCqGSM49BAMCA0kAMEYCIQCatz4XkLkcIcx4qZhdfprIpdDizJs1f4kLdGlMrIw68wIhAM2CSm3zqPcG4iDezAMPWeiN//sVBU"
                    + "2aUF7Gnh1DzXnJMIIBWzCCAQGgAwIBAgIBCjAKBggqhkjOPQQDAjA3MRIwEAYDVQQDDAlOYXZlclNpZ24xFDASBgNVBAoMC05BVkVSIENvcnAuMQswCQYDVQQGEwJLUjAeFw0xOTEyMzExNTA"
                    + "wMDBaFw0zOTEyMzExNTAwMDBaMDcxEjAQBgNVBAMMCU5hdmVyU2lnbjEUMBIGA1UECgwLTkFWRVIgQ29ycC4xCzAJBgNVBAYTAktSMFkwEwYHKoZIzj0CAQYIK"
                    + "oZIzj0DAQcDQgAEptUHO5MibbGdxRxTtEBnN/RPeuq20ygm+5nx8/thEhtPObTgUxTN6FmXKCdWW8CBJrjtOcvjpXly1ekPi0i1ejAKBggqhkjOPQQDAgNIADBF"
                    + "AiA3AiO8y4lAH20vpH7+2PHdtRarV41JCbYwTsPLDSXhiAIhAM9ZsH3Y+LEbxyo62OGOzXNisXeIBvSBMLWRwUVxEOqrAAAxgakwgaYCAQEwPTA3MRIwEAYDVQQDDAlOYXZl"
                    + "clNpZ24xFDASBgNVBAoMC05BVkVSIENvcnAuMQswCQYDVQQGEwJLUgICBNIwDQYJYIZIAWUDBAIBBQAwCgYIKoZIzj0EAwIERzBFAiBqkZVdvPMhpPIBnSeaOKF8XKVOfQQkW"
                    + "eP2ia9BrSlleAIhANeLXz7giw8Z1XGqVIpGppskrpt6MSfd4J1fdokdSawnAAAAAAAA";

            // PEM 인증서 -> X509Certificate 형태의 인증서로 변경 후
            // 다시 X509Certificate 인증서를 String형태의 PEM 인증서로 변환
            byte[] derData = Base64.decode(signData);
            CMSSignedData cmsData = new CMSSignedData(derData);
            Store<X509CertificateHolder> certStore = cmsData.getCertificates();
            Collection<X509CertificateHolder> allCollection = certStore.getMatches(null);
            Iterator<X509CertificateHolder> allIt = allCollection.iterator();
            X509CertificateHolder firstCertificateHolder = allIt.next();

            X509Certificate issuedCertificate = new JcaX509CertificateConverter().getCertificate(firstCertificateHolder);
            String pemCert = new String(Base64.encode(issuedCertificate.getEncoded()));

            System.out.println("PEM : " + pemCert);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cmsSignedData() {

/*        String derData = "MIAGCSqGSIb3DQEHAqCAMIACAQExDzANBglghkgBZQMEAgEFADCABgkqhkiG9w0BBwGggCSABA97ImRhdGEiOiJoZWxsIn0AAAAAAACggDCCAn0wggIioAMCAQICAgTSMAoGC"
                + "CqGSM49BAMCMDcxEjAQBgNVBAMMCU5hdmVyU2lnbjEUMBIGA1UECgwLTkFWRVIgQ29ycC4xCzAJBgNVBAYTAktSMB4XDTIwMDQxNDAzNDEyNVoXDTIyMDQxNDAzNDEyNV"
                + "owcTEmMCQGCgmSJomT8ixkAQEMFjlDQTk2dEdaelFnckpzV0ljX2ZFV2cxCzAJBgNVBAYTAktSMRYwFAYDVQQLDA1OYXZlciBBY2NvdW50MRIwEAYDVQQDDAnsoJXsoJXqt6AxDjAMBg"
                + "NVBAoMBU5hdmVyMFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEp+mdmpS27coS6huZVNd3V3JBmSOyoAvjQ1nMAxmwcX7BpB0EQYxmMIU6Ox97QyrDevW2fCXJhGfAxkVveO5VsqOB4zC"
                + "B4DAJBgNVHRMEAjAAMF8GA1UdIwRYMFaAFIL05Bk2J3f/TamoGG/7euFNDKyEoTukOTA3MRIwEAYDVQQDDAlOYXZlclNpZ24xFDASBgNVBAoMC05BVkVSIENvcnAuMQsw"
                + "CQYDVQQGEwJLUoIBCjAdBgNVHQ4EFgQUFp5f+BaoPoKIVAOFnNrVIs3YjKgwDgYDVR0PAQH/BAQDAgbAMEMGCCsGAQUFBwEBBDcwNTAzBggrBgEFBQcwAYYnaHR0cHM6Ly"
                + "9kZXYubnNpZ24tZ3cubmF2ZXIuY29tL3BraS9vY3NwMAoGCCqGSM49BAMCA0kAMEYCIQCatz4XkLkcIcx4qZhdfprIpdDizJs1f4kLdGlMrIw68wIhAM2CSm3zqPcG4iDezAMPWeiN//sVBU"
                + "2aUF7Gnh1DzXnJMIIBWzCCAQGgAwIBAgIBCjAKBggqhkjOPQQDAjA3MRIwEAYDVQQDDAlOYXZlclNpZ24xFDASBgNVBAoMC05BVkVSIENvcnAuMQswCQYDVQQGEwJLUjAeFw0xOTEyMzExNTA"
                + "wMDBaFw0zOTEyMzExNTAwMDBaMDcxEjAQBgNVBAMMCU5hdmVyU2lnbjEUMBIGA1UECgwLTkFWRVIgQ29ycC4xCzAJBgNVBAYTAktSMFkwEwYHKoZIzj0CAQYIK"
                + "oZIzj0DAQcDQgAEptUHO5MibbGdxRxTtEBnN/RPeuq20ygm+5nx8/thEhtPObTgUxTN6FmXKCdWW8CBJrjtOcvjpXly1ekPi0i1ejAKBggqhkjOPQQDAgNIADBF"
                + "AiA3AiO8y4lAH20vpH7+2PHdtRarV41JCbYwTsPLDSXhiAIhAM9ZsH3Y+LEbxyo62OGOzXNisXeIBvSBMLWRwUVxEOqrAAAxgakwgaYCAQEwPTA3MRIwEAYDVQQDDAlOYXZl"
                + "clNpZ24xFDASBgNVBAoMC05BVkVSIENvcnAuMQswCQYDVQQGEwJLUgICBNIwDQYJYIZIAWUDBAIBBQAwCgYIKoZIzj0EAwIERzBFAiBqkZVdvPMhpPIBnSeaOKF8XKVOfQQkW"
                + "eP2ia9BrSlleAIhANeLXz7giw8Z1XGqVIpGppskrpt6MSfd4J1fdokdSawnAAAAAAAA";

        CMSSignedData cmsData = new CMSSignedData(derData);
        Store<X509CertificateHolder> certStore = cmsData.getCertificates();
        Iterator<SignerInformation> iter = cmsData.getSignerInfos().getSigners().iterator();
        byte[] content = (byte[]) cmsData.getSignedContent().getContent();
        String signContentStr = new String(content, "UTF-8");
        System.out.println("원문: " + signContentStr);
        SignerInformation signer = iter.next();
        // certificate chain
        Collection<X509CertificateHolder> allCollection = certStore.getMatches(null);
        Iterator<X509CertificateHolder> allIt = allCollection.iterator();
        X509CertificateHolder firstCertificateHolder = allIt.next();
        X509CertificateHolder secondCertificateHolder = allIt.next();

        X509Certificate issuedCertificate = new JcaX509CertificateConverter().getCertificate(firstCertificateHolder);
        X509Certificate rootCertificate = new JcaX509CertificateConverter().getCertificate(secondCertificateHolder);
        // singer certificate
        Collection<X509CertificateHolder> certCollection = certStore.getMatches(signer.getSID());
        Iterator<X509CertificateHolder> certIt = certCollection.iterator();
        X509CertificateHolder certificateHolder = certIt.next();
        // signer verify
        boolean signerVerifyResult = signer.verify(new JcaSimpleSignerInfoVerifierBuilder().setProvider(derData).build(certificateHolder));
        System.out.println("verifier 검증 결과 : " + signerVerifyResult);
        X509CertImpl certfiResult = null;
        try {
            certfiResult = (X509CertImpl) new JcaX509CertificateConverter().getCertificate(certificateHolder);
            certfiResult.verify(rootCertificate.getPublicKey());
        } catch (CertificateException error) {
            System.out.println("certificate chain 검증 실패");
        }
        Signature sig = Signature.getInstance(certfiResult.getSigAlgName());
        sig.initVerify(certfiResult.getPublicKey());
        sig.update(signContentStr.getBytes());
        System.out.println("서명 검증 결과: " + sig.verify(signer.getSignature()));*/

    }
}
