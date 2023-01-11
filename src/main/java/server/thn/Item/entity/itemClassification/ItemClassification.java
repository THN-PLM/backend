package server.thn.Item.entity.itemClassification;

import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@IdClass(ItemClassificationId.class)
public class ItemClassification {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemClassification1_id")
    private ItemClassification1 itemClassification1;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemClassification2_id")
    private ItemClassification2 itemClassification2;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemClassification3_id")
    private ItemClassification3 itemClassification3;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "itemClassification4_id")
    private ItemClassification4 itemClassification4;

    public ItemClassification(ItemClassification1 classification1, ItemClassification2 classification2, ItemClassification3 classification3, ItemClassification4 classification4) {
        this.itemClassification1 = classification1;
        this.itemClassification2 = classification2;
        this.itemClassification3 = classification3;
        this.itemClassification4 = classification4;
    }

}
