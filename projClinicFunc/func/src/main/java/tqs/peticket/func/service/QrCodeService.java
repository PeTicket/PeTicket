package tqs.peticket.func.service;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.Result;
import com.google.zxing.NotFoundException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.UUID;
import java.util.Map;
import java.awt.image.BufferedImage;


import javax.imageio.ImageIO;

@Service
public class QrCodeService {

    public Byte[] generateQRCodeImage(UUID userId, UUID petId, UUID appId) throws WriterException, IOException {
        // Aqui você pode criar o texto que será codificado no QR code
        String qrText = "UserID:" + userId + ";PetID:" + petId + ";AppointmentID:" + appId;

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrText, BarcodeFormat.QR_CODE, 200, 200);

        // Escreve a matriz do QR code em um array de bytes
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
        byte[] qrCodeBytes = outputStream.toByteArray();

        // Convert o array de bytes para Byte[]
        Byte[] qrCodeByteObjects = new Byte[qrCodeBytes.length];
        for (int i = 0; i < qrCodeBytes.length; i++) {
            qrCodeByteObjects[i] = qrCodeBytes[i];
        }

        return qrCodeByteObjects;
    }
    
    public String readQRCodeImage(MultipartFile imageFile) {
        try {
            byte[] bytes = imageFile.getBytes();
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            BufferedImage image = ImageIO.read(bis);

            Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
            hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE); // Tente mais para encontrar e decodificar códigos de barras na imagem
            hints.put(DecodeHintType.PURE_BARCODE, Boolean.FALSE); // Se for verdadeiro, otimiza a decodificação para códigos de barras puros, ignorando outros tipos de códigos de barras potencialmente presentes
            hints.put(DecodeHintType.POSSIBLE_FORMATS, EnumSet.of(BarcodeFormat.QR_CODE)); // Especifique que estamos esperando um QR code

            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(image)));
            MultiFormatReader reader = new MultiFormatReader();
            Result result = reader.decode(bitmap, hints);
            return result.getText();
        } catch (NotFoundException | IOException e) {
            e.printStackTrace();
            return null; // ou uma mensagem de erro adequada
        }
    }
}

