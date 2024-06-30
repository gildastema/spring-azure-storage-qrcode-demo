package com.gildastema.demoqrserver.Clients;

import java.io.IOException;

public interface CodeGeneratorService {

    String generateQrCode(String content) throws IOException;
}
