package clausEnterprises.crm.scheduler;

import clausEnterprises.crm.service.ClientService;
import clausEnterprises.crm.service.WarehouseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SchedulerService {
    private final WarehouseService warehouseService;
    private final ClientService clientService;

    @Scheduled(fixedDelay = 3000000)
    public void runSupplierJob() {
        warehouseService.resupplyGifts();
    }

    @Scheduled(fixedDelay = 3000000)
    public void runDateCheckerJob() {
        int amount = clientService.deleteClientIfOutdatedRequest();
        log.info("{} outdated requests were deleted", amount);
    }
}
