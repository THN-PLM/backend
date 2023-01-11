package server.thn.Item.entity.itemOrgClassification;

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
    @JoinColumn(name = "teamClassification1_id")
    private ItemClassification1 classification1;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamClassification2_id")
    private ItemClassification2 classification2;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamClassification3_id")
    private ItemClassification3 classification3;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamClassification4_id")
    private ItemClassification4 classification4;

    public ItemClassification(ItemClassification1 classification1, ItemClassification2 classification2, ItemClassification3 classification3, ItemClassification4 classification4) {
        this.classification1 = classification1;
        this.classification2 = classification2;
        this.classification3 = classification3;
        this.classification4 = classification4;
    }

}
