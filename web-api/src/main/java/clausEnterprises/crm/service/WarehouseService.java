package clausEnterprises.crm.service;

import clausEnterprises.crm.model.Warehouse;
import clausEnterprises.crm.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class WarehouseService {
    private final WarehouseRepository warehouseRepository;

    public void resupplyGifts() {
        saveGift(Warehouse.builder()
                .description("Gift №" + new Random().ints(1, Integer.MAX_VALUE)
                        .findFirst()
                        .getAsInt())
                .is_available(true)
                .build());
    }

    @Transactional(readOnly = true)
    public List<Long> getAllGiftIds() {
        return warehouseRepository.getAllGiftsId();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveGift(Warehouse warehouse) {
        warehouseRepository.save(warehouse);
    }

    @Transactional(readOnly = true)
    public Warehouse getGiftById(Long id) {
        return warehouseRepository.getGiftById(id);
    }

    @Transactional(readOnly = true)
    public List<Warehouse> findAllEntities() {
        return warehouseRepository.findAll();
    }
}
