package tqs.peticket.vet.middleware;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tqs.peticket.vet.middleware.SenderConfig;

@Component
public class StartupInitializer {

    @Autowired
    private SenderConfig senderConfig;

    @PostConstruct
    public void afterStartup() {
        // Código que você deseja executar após o início da aplicação
        String message = "Mensagem de teste após o início";
        senderConfig.send(message);
    }
}
