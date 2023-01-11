package server.thn.Item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import server.thn.Item.entity.itemClassification.*;

public interface ItemClassificationRepository extends JpaRepository<ItemClassification , Long> {
    ItemClassification
    findByItemClassification1AndItemClassification2AndItemClassification3AndItemClassification4(
            ItemClassification1 classification1,
            ItemClassification2 classification2,
            ItemClassification3 classification3,
            ItemClassification4 classification4
    );
}
