package com.gildastema.demoqrserver.Clients;

import io.nayuki.qrcodegen.QrCode;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Service
public class CodeGeneratorServiceImpl implements CodeGeneratorService{
    @Override
    public String generateQrCode(String content) throws IOException {
        QrCode qr0 = QrCode.encodeText(content, QrCode.Ecc.MEDIUM);
        return qr0.toSvgString(4);
    }

}
