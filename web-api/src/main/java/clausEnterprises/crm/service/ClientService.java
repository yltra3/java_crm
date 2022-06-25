package clausEnterprises.crm.service;

import clausEnterprises.crm.model.Client;
import clausEnterprises.crm.repository.ClientRepository;
import clausEnterprises.crm.repository.ParentResponseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {
    private final ClientRepository clientRepository;
    private final ParentResponseRepository parentResponseRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveClient(Client client) {
        clientRepository.save(client);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteClient(Client client) {
        clientRepository.delete(client);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void deleteClientByEmails(String email, String parentEmail) {
        clientRepository.deleteClientByBothEmails(email, parentEmail);
    }

    @Transactional(readOnly = true)
    public Client getAlreadyExistingClient(String email) {
        return clientRepository.getAlreadyExistingClient(email);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int deleteClientIfOutdatedRequest() {
        AtomicInteger count = new AtomicInteger(0);
        parentResponseRepository.findAll().stream()
                .filter(response -> response.getDateCreated().isBefore(LocalDateTime.now().minusDays(14)))
                .forEach(record ->
                {
                    clientRepository.delete(record.getChildren());
                    count.incrementAndGet();
                });
        return count.getAndSet(0);
    }
}
