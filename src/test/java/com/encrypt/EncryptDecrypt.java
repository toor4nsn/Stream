package com.encrypt;

import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.AsymmetricAlgorithm;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import cn.hutool.crypto.symmetric.AES;
import org.junit.jupiter.api.Test;

import java.security.*;
import java.util.Base64;

public class EncryptDecrypt {
    @Test
    public void AESTest() throws Exception {
        final String AES_KEY = "0CoJUm6Qyw8W8jud";
        // AES初始化向量为16位
        final String AES_IV = "3vR63quCNIQXfhXh";
        AES aes = new AES(Mode.CBC, Padding.PKCS5Padding, AES_KEY.getBytes(), AES_IV.getBytes());

        String content = "18673130825";
        String encryptBase64 = aes.encryptBase64(content);
        System.out.println(encryptBase64);
        String decryptStr = aes.decryptStr(encryptBase64);
        System.out.println(decryptStr);

        String decrypt2 = com.encrypt.AESUtil.decrypt(encryptBase64);
        System.out.println(decrypt2);
    }

    @Test
    public void RSATest(){
        try {
            // Generate public and private keys
            KeyPair keyPair = RSAUtil.generateKeyPair();

            // Original text
            String originalText = "18699999999";

            // Encrypt the text
            String encryptedText = RSAUtil.encrypt(originalText, keyPair.getPublic());
            System.out.println("Encrypted Text: " + encryptedText);
            System.out.println(encryptedText.length());
            // Decrypt the text
            String decryptedText = RSAUtil.decrypt(encryptedText, keyPair.getPrivate());
            System.out.println("Decrypted Text: " + decryptedText);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void RSATest2222(){
        try {
            // Generate public and private keys
            KeyPair keyPair = RSAUtil.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // Convert PublicKey to Base64 encoded string
            String base64PublicKey = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            System.out.println("Base64 Encoded Public Key: " + base64PublicKey);


            // Convert PrivateKey to Base64 encoded string
            String base64PrivateKey = Base64.getEncoder().encodeToString(privateKey.getEncoded());
            System.out.println("Base64 Encoded Private Key: " + base64PrivateKey);


            PublicKey publicKeyFromBase64 = RSAUtil.getPublicKey(base64PublicKey);

            PrivateKey privateKeyFromBase64 = RSAUtil.getPrivateKey(base64PrivateKey);

            // Original text
            String originalText = "Hello, World!";

            // Encrypt the text
            String encryptedText = RSAUtil.encrypt(originalText, publicKeyFromBase64);
            System.out.println("Encrypted Text: " + encryptedText);

            // Decrypt the text
            String decryptedText = RSAUtil.decrypt(encryptedText, privateKeyFromBase64);
            System.out.println("Decrypted Text: " + decryptedText);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void RSATest3333333(){
        String privateKEYBase64 = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCQgUtpeZCpK8CLKRy1PE/zhelCmM8cAny1VazHVci8d+r8qDK79t15o7S0UP30V1KEEzb+Siii+y/kn87NPHPr5tdWxj0iUMtKznDVjtZvZ96aBPPYLdTnhIYRKkX18H0HhOxSKRheCuCy03XV2o/aROMmO/EsdUQUyH9k15S1XgtSBq4rfur8xRk1OuzrEuqYEwRkiJRwjd7GBiQAPW0c46ruNd6iUTgnQvAIORDsmsWSN8tdxsVcYE5soWQ+SlBuv/byLNNoM7wntF8x41G2s1T/NYnrta66B63ZLSxIHvfo4s7VGg8o67zBR6Ep/bCLXbI02rd+TCNDBYqBBLIRAgMBAAECggEAefJCu631IG5lvIpjSaauKepWIPdnwFFxAG5espF4J2Rjj/g5aFo6KQ63znuS9IJEhmTYwdu/U1r63crm8FgV03wpOXdt2+mmxWhV3EMAP1++gOexCDwVMZOquA2eoJ/kQ1YvQF6hy6sOWJsPE/uv7hi4QGnRax1C6O8SYv1y52I78dJjtuqmlW+jjXNmsGFp+VFEu83tU/Mh2m8C4QwykS4GnS1fUcZNrH5wlhVKWxuPOf3riWZjXIgDxncz+p9K+HjVc74KpE7zaqFDA6oh4+xGnP3BF2I99OSNpnBoQCcV8Vi3QeAmY+wT0OzSQd1jPg8qEeDO4azc6jv+xHE+CQKBgQDtk/pfFQyLpol2vv7RagNvQT5IU5tmfibwJcCtutf5RVHs0XgeCIZ2SNtbw6cqaKEQlXcwa1md9G2EoJ+e+ErbYlXY5xjhNejGb1oDg2480KjKF4k3d3OTbKFeX7yThZsq+2DltG/Rsyss3PUmtsyQXXHEttxDs6gG/A76yvGz0wKBgQCbtceOFFpVMKyEXENKadBvnZ41DGya2osMAJxr8tdn6XPEfDSbsdhQHfw0ZzP/rQFjpJsH6XJmQsN4qUSW/H77ep4i1gZo4ubSYsguuOwggeHyEyw+NjeL+vx8Bssrc/LkHbMMEB/bQVMHDQZbYzllmGaEG7YcKpG/+7aYmLMoCwKBgQDo27B7/1qTikCT5fJGeX0yT6VnKKNZGu65VJd88HR9OwdocYslDMAWMaTnVWaEd5bedVEkYSOuYQPzCahKHRzp2IFCHohRPRCRZlS4utqSuwWlRtpURj0+0yt7H6Tqhvqmb0ry+h7bT5xS1aNnNaZdHWnEIGLV7MIVTnq0L7QlxwKBgAnoVRw7O0w0XqXFYRq5BOQ1OT3S3o4z3akT2OAdgYtBFXapGIfXlxXx8nw7HNnl3jLC5IJqXjcXhGyHCLA6vLGvqaQ5zgqadWlpk6c1aQDggsnuYmBK/tnPTIVvwTmwlQHiyeO4mu5Oh8PpFkB2Xc6TFeLOCJZeNwvqLkIjZTLlAoGAXBL5c5GvB//aCn2UA0WZhMVfsPeKKwip1XSYXW8kGQDnSKv5m78DB2TJa//gqGBU/sqRkdWsNRR4L27RoDINMK+D8LMKbhjfQMcFo/Ku0jLWiCCFkpHyJUpD3xZdsc+0pqetSF7VOvIofvXixx7AijyxqQGRCSK9kUpXmgaqr8I=";
        RSA rsa = new RSA(privateKEYBase64, null);
        String encryptText = "RCaMm0f9/98OG3o5B25oxssiv8QmWniuWOrLsheAIm42werPkdIHATqT78lXkE8GvDcKgdjH73t/5ja0PvMeBTsBa0gt1+G9Mf0YDnRrQNfAOfxbbqczdUiTO9e40sXM2+o+O3CKVyuACq6Bg8miRYnt7F9xG7LjrX5Pqtn9d9VQTgdHpmwrwWWA4w8mAlc79EKklFGFbJMImi8l2vaTHixU2tZwWhZ8PE/L16cxYUYj6e9QW/jPoNL8D2NivKzjOhiLVqtmQmMgN+QtcQsTkJR16+HoXL44FmuACCdTGfZ18Ukavx4H/T9gzTw1dd4LPdVC/D907s5W701PKO3Yng==";
        //byte[] decrypt = rsa.decrypt(aByte, KeyType.PrivateKey);
        String decryptStr = rsa.decryptStr(encryptText, KeyType.PrivateKey);
        System.out.println(decryptStr);
    }

    @Test
    public void RSATest4444444(){
        KeyPair rsaKeyPair = SecureUtil.generateKeyPair("RSA");
        PrivateKey privateKey = rsaKeyPair.getPrivate();
        PublicKey publicKey = rsaKeyPair.getPublic();
        String privateKeyBase64 = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        System.out.println("privateKeyBase64:"+privateKeyBase64);
        System.out.println("publicKeyBase64:"+publicKeyBase64);

        // Original text
        String originalText = "Hello, World!";

        //公钥加密
        RSA rsa = new RSA(null,publicKeyBase64);
        String encryptedBase64 = rsa.encryptBase64(originalText, KeyType.PublicKey);
        System.out.println("encryptedBase64:"+encryptedBase64);


    }

    @Test
    public void RSATest5555555(){
        String privateKeyBase64 = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAIZdOp9v0vLNGt0MT8w16laFt7EOMt84lqAlr3Es6k2sirIQ0FFk49Jkco9lKQXkd7Oh5nywRJLvsLfY83kIGy/Os5WhxLPf3cgZN+3Ny3+d2fSpWRVQQdGqMM/UFKQFkG1cfbfdCtq+DuSOkVRQmNorVXiADqlNp9+QGPLHLYiLAgMBAAECgYBaGSX4R5niqlp7I4hW3Uh4IE5e8bB3MpD+bobIk1/CGhMe+FtYWRjTuft/4Euz4dHKqKPPtLsm9IdBoL8Sx7bCTVHxujtFfkYzc2KamoLjDTX3O0bvwQ8ByW0v4eFE+C/oFnmavR/bioeO7BaKleCyyvCjqRUo/P7SPwds1dY4kQJBALrOx6jRUDpUrjMKdA+T/qIw27SN4AQI0PAV8o9BmplbmdTI3L5R1kLvocCkDElu9ffbKAZbBU12/cR3KpuWEt0CQQC4IbhEKV5NNrKYWdRm6ONBfRodMoI0K9glKznTv3tnfo/NdfVmUASSZ8ciepab+FZVRJzqwUWAVnK7BSC+L46HAkEAlkyytbAbJaNmOOjLMpF3SRWYX6/askRJnzF5ZCt7cLITlfE6MeJ0Soy1DuKuhBLD0fTBQkPPg2Eekgjy/hWD/QJBAIbjXOPCEm0qJsdo9yq0bNbq2JiPCcdCX0NLWT9/xN1aX+o6AZ4D/HVsRPtDJtX3pPcuHm3zTADqU34iEYyBtj0CQQCkyyUA2ejmCOw32AMNviRNjRx98TxiEk96dhSZ5nlBiQBdHVMrC3xA1RxJGUyyPfbm3BXGqaI9RWWf5V30pWWa";
        //base64密文
        String encryptedBase64 = "Xr5tQirzx3JYc1uGTTc0pF4+ymdWiY+mQqVmONSjR6E2OQQpRjLvlRHWHuBxyEQA8dhosBlI3dNw7ICjzuaU4/XSWmLXK6Z2maVb15qXU73CH+cb7gBC4KRdsktl+4pGVSOQ8wiUKEI2stxxmcEdodIRvClWffN3QXtE67ywtDE=";

        RSA rsa = new RSA(privateKeyBase64,null);

        //私钥解密
        String decryptStr = rsa.decryptStr(encryptedBase64, KeyType.PrivateKey);

        //decrypt String
        System.out.println("decryptStr:"+decryptStr);

    }
    @Test
    public void RSATest6666666() throws Exception {
        String privateKeyBase64 = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAIZdOp9v0vLNGt0MT8w16laFt7EOMt84lqAlr3Es6k2sirIQ0FFk49Jkco9lKQXkd7Oh5nywRJLvsLfY83kIGy/Os5WhxLPf3cgZN+3Ny3+d2fSpWRVQQdGqMM/UFKQFkG1cfbfdCtq+DuSOkVRQmNorVXiADqlNp9+QGPLHLYiLAgMBAAECgYBaGSX4R5niqlp7I4hW3Uh4IE5e8bB3MpD+bobIk1/CGhMe+FtYWRjTuft/4Euz4dHKqKPPtLsm9IdBoL8Sx7bCTVHxujtFfkYzc2KamoLjDTX3O0bvwQ8ByW0v4eFE+C/oFnmavR/bioeO7BaKleCyyvCjqRUo/P7SPwds1dY4kQJBALrOx6jRUDpUrjMKdA+T/qIw27SN4AQI0PAV8o9BmplbmdTI3L5R1kLvocCkDElu9ffbKAZbBU12/cR3KpuWEt0CQQC4IbhEKV5NNrKYWdRm6ONBfRodMoI0K9glKznTv3tnfo/NdfVmUASSZ8ciepab+FZVRJzqwUWAVnK7BSC+L46HAkEAlkyytbAbJaNmOOjLMpF3SRWYX6/askRJnzF5ZCt7cLITlfE6MeJ0Soy1DuKuhBLD0fTBQkPPg2Eekgjy/hWD/QJBAIbjXOPCEm0qJsdo9yq0bNbq2JiPCcdCX0NLWT9/xN1aX+o6AZ4D/HVsRPtDJtX3pPcuHm3zTADqU34iEYyBtj0CQQCkyyUA2ejmCOw32AMNviRNjRx98TxiEk96dhSZ5nlBiQBdHVMrC3xA1RxJGUyyPfbm3BXGqaI9RWWf5V30pWWa";
        //base64密文
        String encryptedBase64 = "Xr5tQirzx3JYc1uGTTc0pF4+ymdWiY+mQqVmONSjR6E2OQQpRjLvlRHWHuBxyEQA8dhosBlI3dNw7ICjzuaU4/XSWmLXK6Z2maVb15qXU73CH+cb7gBC4KRdsktl+4pGVSOQ8wiUKEI2stxxmcEdodIRvClWffN3QXtE67ywtDE=";


        String decryptStr = RSAUtil.decrypt(encryptedBase64, RSAUtil.getPrivateKey(privateKeyBase64));

        //decrypt String
        System.out.println("decryptStr:"+decryptStr);

    }
    @Test
    public void RSATest77777() throws Exception {
        String privateKeyBase64 = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAIZdOp9v0vLNGt0MT8w16laFt7EOMt84lqAlr3Es6k2sirIQ0FFk49Jkco9lKQXkd7Oh5nywRJLvsLfY83kIGy/Os5WhxLPf3cgZN+3Ny3+d2fSpWRVQQdGqMM/UFKQFkG1cfbfdCtq+DuSOkVRQmNorVXiADqlNp9+QGPLHLYiLAgMBAAECgYBaGSX4R5niqlp7I4hW3Uh4IE5e8bB3MpD+bobIk1/CGhMe+FtYWRjTuft/4Euz4dHKqKPPtLsm9IdBoL8Sx7bCTVHxujtFfkYzc2KamoLjDTX3O0bvwQ8ByW0v4eFE+C/oFnmavR/bioeO7BaKleCyyvCjqRUo/P7SPwds1dY4kQJBALrOx6jRUDpUrjMKdA+T/qIw27SN4AQI0PAV8o9BmplbmdTI3L5R1kLvocCkDElu9ffbKAZbBU12/cR3KpuWEt0CQQC4IbhEKV5NNrKYWdRm6ONBfRodMoI0K9glKznTv3tnfo/NdfVmUASSZ8ciepab+FZVRJzqwUWAVnK7BSC+L46HAkEAlkyytbAbJaNmOOjLMpF3SRWYX6/askRJnzF5ZCt7cLITlfE6MeJ0Soy1DuKuhBLD0fTBQkPPg2Eekgjy/hWD/QJBAIbjXOPCEm0qJsdo9yq0bNbq2JiPCcdCX0NLWT9/xN1aX+o6AZ4D/HVsRPtDJtX3pPcuHm3zTADqU34iEYyBtj0CQQCkyyUA2ejmCOw32AMNviRNjRx98TxiEk96dhSZ5nlBiQBdHVMrC3xA1RxJGUyyPfbm3BXGqaI9RWWf5V30pWWa";
        //base64密文
        String encryptedBase64 = "Xr5tQirzx3JYc1uGTTc0pF4+ymdWiY+mQqVmONSjR6E2OQQpRjLvlRHWHuBxyEQA8dhosBlI3dNw7ICjzuaU4/XSWmLXK6Z2maVb15qXU73CH+cb7gBC4KRdsktl+4pGVSOQ8wiUKEI2stxxmcEdodIRvClWffN3QXtE67ywtDE=";

        /** 默认的RSA算法 */
        final AsymmetricAlgorithm ALGORITHM_RSA = AsymmetricAlgorithm.RSA_ECB_PKCS1;

        PrivateKey privateKey = KeyUtil.generatePrivateKey(ALGORITHM_RSA.getValue(), Base64.getDecoder().decode(privateKeyBase64));

        String decryptStr = RSAUtil.decrypt(encryptedBase64, privateKey);

        //decrypt String
        System.out.println("decryptStr:"+decryptStr);

    }

    @Test
    public void RSA8888() throws Exception {
        KeyPair rsaKeyPair = RSAUtil.generateKeyPair();
        PrivateKey privateKey = rsaKeyPair.getPrivate();
        PublicKey publicKey = rsaKeyPair.getPublic();
        String privateKeyBase64 = Base64.getEncoder().encodeToString(privateKey.getEncoded());
        String publicKeyBase64 = Base64.getEncoder().encodeToString(publicKey.getEncoded());

        System.out.println("privateKeyBase64:"+privateKeyBase64);
        System.out.println("publicKeyBase64:"+publicKeyBase64);

        // Original text
        String originalText = "Hello, World!";
        String encryptText = RSAUtil.encrypt(originalText, RSAUtil.getPublicKey(publicKeyBase64));

        System.out.println("encryptText:"+encryptText);

        String decrypted = RSAUtil.decrypt(encryptText, RSAUtil.getPrivateKey(privateKeyBase64));
        System.out.println(decrypted);


    }
}
