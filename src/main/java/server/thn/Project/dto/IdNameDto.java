package server.thn.Project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import server.thn.Project.entity.BuyerOrganization;
import server.thn.Project.entity.CarType;
import server.thn.Project.entity.ProduceOrganization;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdNameDto {

    private String code1;
    private String code2;
    private String name;

    public static CarType toCarTypeEntity(
            IdNameDto req
    ){
        return new CarType(
                req.getName()
        );
    }

    public static BuyerOrganization toBuyerOrganizationEntity(
            IdNameDto req
    ){
        return new BuyerOrganization(
                req.getCode1(),
                req.getCode2()
        );
    }



}