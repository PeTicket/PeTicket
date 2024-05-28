package tqs.peticket.client.serviceTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import tqs.peticket.client.service.QrCodeService;

import java.io.IOException;
import java.util.UUID;
import com.google.zxing.WriterException;

class QrCodeServiceTests {

    @Mock
    private QrCodeService qrCodeService;

    @BeforeEach
    void setUp() {
        qrCodeService = mock(QrCodeService.class);
    }

    @Test
    void testGenerateCompressedQRCodeImage_Success() throws WriterException, IOException {
        UUID userId = UUID.randomUUID();
        UUID petId = UUID.randomUUID();
        UUID appId = UUID.randomUUID();
        byte[] expectedQrCodeBytes = new byte[]{1, 2, 3};

        when(qrCodeService.generateCompressedQRCodeImage(userId, petId, appId)).thenReturn(expectedQrCodeBytes);

        byte[] qrCodeBytes = qrCodeService.generateCompressedQRCodeImage(userId, petId, appId);

        assertNotNull(qrCodeBytes);
        assertArrayEquals(expectedQrCodeBytes, qrCodeBytes);
    }


    @Test
    void testReadQRCodeImage_ValidImageFile() throws IOException{
        String expectedQrCodeData = "QR_CODE_DATA";
        MultipartFile imageFile = new MockMultipartFile("qr-code.png", new byte[]{});

        when(qrCodeService.readQRCodeImage(imageFile)).thenReturn(expectedQrCodeData);

        String qrCodeData = qrCodeService.readQRCodeImage(imageFile);

        assertNotNull(qrCodeData);
        assertEquals(expectedQrCodeData, qrCodeData);
    }

    @Test
    void testReadQRCodeImage_InvalidImageFile() throws IOException {
        QrCodeService qrCodeService = new QrCodeService();
        MultipartFile imageFile = new MockMultipartFile("invalid-image.png", getClass().getResourceAsStream("/invalid-image.png"));
        String qrCodeData = qrCodeService.readQRCodeImage(imageFile);

        assertNull(qrCodeData);
    }
}
