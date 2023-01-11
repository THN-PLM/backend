package server.thn.Item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import server.thn.Item.entity.itemClassification.ItemClassification1;

import java.util.List;

public interface ItemClassification1Repository extends JpaRepository<ItemClassification1, Long> {
    @Query("select c from ItemClassification1 c join fetch c.itemClassification2List ")
    List<ItemClassification1> findAllByItemClassification1();
}
