package server.thn.PCB.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import server.thn.Common.entity.EntityDate;
import server.thn.Item.entity.itemClassification.ItemClassification;
import server.thn.Member.entity.Member;
import server.thn.Project.entity.producer.ProduceOrganization;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Pcb extends EntityDate {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQUENCE2")
    @SequenceGenerator(name="SEQUENCE2", sequenceName="SEQUENCE2", allocationSize=1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "c1_id", nullable = false)
    @JoinColumn(name = "c2_id", nullable = false)
    @JoinColumn(name = "c3_id", nullable = false)
    @JoinColumn(name = "c4_id", nullable = false)
    private ItemClassification classification;

    @Column
    private String partNumber;

    @Column
    private String name;

    @Column
    private String revision;

    @Column
    private String masterNumber;

    @Column
    private String material;

    @Column
    private String thickness;

    @Column
    private String innerOz;

    @Column
    private String outerOz;

    @Column
    private String platingTreatment;

    @Column
    private String layer;

    @Column
    private String width;

    @Column
    private String height;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produceOrganization_id")
    private ProduceOrganization produceOrganization;

    @Column
    private LocalDate pcbCompleteReqDate;

    @Column
    private String specialSpecification;

    @Column
    private Integer arrayNumber;

    @Column
    private String arrayWidth;

    @Column
    private String arrayHeight;

    @OneToMany(
            mappedBy = "pcb",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<ItemPcbRelatedAttachment> itemAttachments;

    @OneToMany(
            mappedBy = "pcb",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<DesignPcbRelatedAttachment> designAttachments;

    @OneToMany(
            mappedBy = "pcb",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<SchematicDesignPcbRelatedAttachment> schematicDesignAttachments;

    @OneToMany(
            mappedBy = "pcb",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<PbaDesignPcbRelatedAttachment> pbaDesignAttachments;

    @OneToMany(
            mappedBy = "pcb",
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<PcbDesignPcbRelatedAttachment> pcbDesignAttachments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "member_id",
            nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "modifier_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member modifier;

    @Column(nullable = false)
    private boolean tempsave;

    @Column(nullable = false)
    private boolean readonly;

    public Pcb(
            ItemClassification classification,
            String partNumber, String name,
            String revision,
            String masterNumber,
            String material,
            String thickness,
            String innerOz,
            String outerOz,
            String platingTreatment,
            String layer,
            String width,
            String height,
            ProduceOrganization produceOrganization,
            LocalDate pcbCompleteReqDate,
            String specialSpecification,
            Integer arrayNumber,
            String arrayWidth,
            String arrayHeight,
            List<ItemPcbRelatedAttachment> itemAttachments,
            List<DesignPcbRelatedAttachment> designAttachments,
            List<SchematicDesignPcbRelatedAttachment> schematicDesignAttachments,
            List<PbaDesignPcbRelatedAttachment> pbaDesignAttachments,
            List<PcbDesignPcbRelatedAttachment> pcbDesignAttachments,
            Member member,
            boolean tempsave,
            boolean readonly
    ) {
        this.classification = classification;
        this.partNumber = partNumber;
        this.name = name;
        this.revision = revision;
        this.masterNumber = masterNumber;
        this.material = material;
        this.thickness = thickness;
        this.innerOz = innerOz;
        this.outerOz = outerOz;
        this.platingTreatment = platingTreatment;
        this.layer = layer;
        this.width = width;
        this.height = height;
        this.produceOrganization = produceOrganization;

        this.pcbCompleteReqDate = pcbCompleteReqDate;
        this.specialSpecification = specialSpecification;
        this.arrayNumber = arrayNumber;
        this.arrayWidth = arrayWidth;
        this.arrayHeight = arrayHeight;

        this.itemAttachments = itemAttachments;
        if(itemAttachments!=null){
            addItemAttachment(itemAttachments);
        }
        this.designAttachments = designAttachments;
        if(designAttachments!=null){
            addDesignAttachment(designAttachments);
        }
        this.schematicDesignAttachments = schematicDesignAttachments;
        if(schematicDesignAttachments!=null){
            addSchematicAttachment(schematicDesignAttachments);
        }
        this.pbaDesignAttachments = pbaDesignAttachments;
        if(pbaDesignAttachments!=null){
            addPbaAttachment(pbaDesignAttachments);
        }
        this.pcbDesignAttachments = pcbDesignAttachments;
        if(pcbDesignAttachments!=null){
            addPcbAttachments(pcbDesignAttachments);
        }
        this.member = member;
        this.modifier = member;
        this.tempsave = tempsave;
        this.readonly = readonly;
    }

    /**
     * 추가할 item attachments
     */
    private void addItemAttachment(
            List<ItemPcbRelatedAttachment> added) {
        added.forEach(i -> {
            itemAttachments.add(i);
            i.initPcb(this);
        });
    }

    /**
     * 추가할 Design attachments
     */
    private void addDesignAttachment(
            List<DesignPcbRelatedAttachment> added) {
        added.forEach(i -> {
            designAttachments.add(i);
            i.initPcb(this);
        });
    }

    /**
     * 추가할 Schematic attachments
     */
    private void addSchematicAttachment(
            List<SchematicDesignPcbRelatedAttachment> added) {
        added.forEach(i -> {
            schematicDesignAttachments.add(i);
            i.initPcb(this);
        });
    }

    /**
     * 추가할 Pba attachments
     */
    private void addPbaAttachment(
            List<PbaDesignPcbRelatedAttachment> added) {
        added.forEach(i -> {
            pbaDesignAttachments.add(i);
            i.initPcb(this);
        });
    }

    /**
     * 추가할 Pcb attachments
     */
    private void addPcbAttachments(
            List<PcbDesignPcbRelatedAttachment> added) {
        added.forEach(i -> {
            pcbDesignAttachments.add(i);
            i.initPcb(this);
        });
    }


}
