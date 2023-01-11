package server.thn.PCB.dto;

import server.thn.Item.entity.itemClassification.ItemClassification;
import server.thn.Item.entity.itemClassification.ItemClassification1;
import server.thn.Member.entity.Member;
import server.thn.PCB.entity.ItemPcbRelatedAttachment;
import server.thn.PCB.entity.Pcb;
import server.thn.Project.entity.producer.ProduceOrganization;

import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.util.List;

public class PcbCreateRequest {

    private Long classification1Id;
    private Long classification2Id;
    private Long classification3Id;
    private Long classification4Id;
    private String partNumber;
    private String name;
    private String revision;
    private String masterNumber;
    private String material;
    private String thickness;
    private String innerOz;
    private String outerOz;
    private String platingTreatment;
    private String layer;
    private String width;
    private String height;
    private ProduceOrganization produceOrganization;
    private LocalDate pcbCompleteReqDate;
    private String specialSpecification;
    private Integer arrayNumber;
    private String arrayWidth;
    private String arrayHeight;
    private List<ItemPcbRelatedAttachment> itemAttachments;
    private boolean tempsave;
    private boolean readonly;
    @Null
    private Member member;

}
