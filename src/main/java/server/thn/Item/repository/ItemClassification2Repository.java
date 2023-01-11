package server.thn.Item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import server.thn.Item.entity.itemClassification.ItemClassification2;

import java.util.List;

public interface ItemClassification2Repository extends JpaRepository<ItemClassification2, Long> {
    @Query("select c from ItemClassification2 c join fetch c.itemClassification3List ")
    List<ItemClassification2> findAllByItemClassification2();
}
