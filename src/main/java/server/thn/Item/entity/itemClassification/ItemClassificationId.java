package server.thn.Item.entity.itemClassification;

import lombok.*;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ItemClassificationId implements Serializable {

    private ItemClassification1 classification1;

    private ItemClassification2 classification2;

    private ItemClassification3 classification3;

    private ItemClassification4 classification4;
}
