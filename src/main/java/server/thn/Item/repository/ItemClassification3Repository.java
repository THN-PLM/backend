package server.thn.Item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import server.thn.Item.entity.itemClassification.ItemClassification3;

import java.util.List;

public interface ItemClassification3Repository extends JpaRepository<ItemClassification3, Long> {
    @Query("select c from ItemClassification3 c join fetch c.itemClassification4List ")
    List<ItemClassification3> findAllByItemClassification3();
}